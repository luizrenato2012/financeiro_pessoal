package br.com.lrsantos.financeiro_pessoal.controller;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.lrsantos.financeiro_pessoal.model.bean.Gasto;
import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;
import br.com.lrsantos.financeiro_pessoal.model.bean.Pagamento;
import br.com.lrsantos.financeiro_pessoal.model.service.ContaService;
import br.com.lrsantos.financeiro_pessoal.model.service.GastoService;
import br.com.lrsantos.financeiro_pessoal.model.service.OrcamentoService;

@WebServlet({ "/orcamento", "/orc" })
public class OrcamentoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private NumberFormat numberFormat;

	@EJB
	private OrcamentoService orcamentoService;
	
	@EJB
	private ContaService contaService;
	
	@EJB
	private GastoService gastoService;
	
	SimpleDateFormat df;
	
	@Override
	public void init() throws ServletException {
		numberFormat = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
		numberFormat.setMinimumFractionDigits(2);
		df = new SimpleDateFormat("dd/MM/yy");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processa(request,response);
	}

	private void processa(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String acao = request.getParameter("acao");
		switch (acao) {
		case "iniciaOrcamento" :
			iniciaOrcamento(response, request);
			break;
		case "listaOrcamento" :
			listaOrcamento(response);
			break;
		case "listaGasto":
			listaGasto(request,response);
			break;
		case "pagaGasto":
			pagaGasto(request,response);
			break;
		case "resumeOrcamento":
			resumeOrcamento(request,response);
		}

	}

	private void listaGasto(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String strIdOrcamento=request.getParameter("id");
		if (strIdOrcamento==null || strIdOrcamento.equals("")){
			throw new RuntimeException("Erro ao pesquisar gasto");
		}
		Integer idOrcamento = Integer.parseInt(strIdOrcamento);
		Orcamento orcamento = orcamentoService.encontra(idOrcamento, Orcamento.class);
		
		if (orcamento==null) {
			throw new RuntimeException("Orcamento nao encontrado "+ idOrcamento);
		}
		
		
		Collection lista = new ArrayList();
		List<Gasto> gastos = gastoService.listaPorOrcamento(idOrcamento);
		for(Gasto gasto: gastos){
			lista.add(new LabelValueDTO(gasto.getId(), gasto.getDescricao()+ ": R$ " + 
					numberFormat.format(gasto.getValor())) );
		}
		
		JsonObject obj = new JsonObject();
		obj.add("gastos", new JsonParser().parse(new Gson().toJson(lista)));
		setaValoresResumo(obj, orcamento);
		//System.out.println(obj);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(obj);
	}

	private void pagaGasto(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String strIdGasto=request.getParameter("id");
		if (strIdGasto==null || strIdGasto.equals("")){
			throw new RuntimeException("Erro ao pagar gasto");
		}
		String strIdOrcamento = request.getParameter("idOrcamento");
		String mensagem="";
		String tipo="";
		Orcamento orcamento = null;
		try {
			Integer idGasto= Integer.parseInt(strIdGasto);

			String strData = request.getParameter("data");
			
			if (strData==null || strData.equals("")){
				throw new RuntimeException("Data obrigatoria");
			}
			Date data = new SimpleDateFormat("ddMMyyyy").parse(strData);
			
			String strValor = request.getParameter("valor");
			if (strValor==null || strValor.equals("")){
				throw new RuntimeException("Valor obrigatorio");
			}
			
			Double valor = numberFormat.parse(strValor).doubleValue();

			Collection lista = new ArrayList();
			Gasto gasto = gastoService.encontra(idGasto, Gasto.class);
			orcamento = orcamentoService.encontra(Integer.parseInt(strIdOrcamento),
					Orcamento.class);
			gasto.setOrcamento(orcamento);

			Pagamento pagamento = new Pagamento();
			pagamento.setData(data);
			pagamento.setValor(valor);
			pagamento.setObservacao(request.getParameter("descricao"));

			gastoService.registraPagamento(gasto, pagamento);
			orcamentoService.efetuaPagamentoGasto(gasto, valor, data);
			
			tipo="OK";
			mensagem="Pagamento registrado com sucesso!";
			
			
		} catch (Exception e) {
			e.printStackTrace();
			tipo="ERRO";
			mensagem="Erro ao pagar: "+ e.getMessage();
		}
		
		JsonObject obj = new JsonObject();
		obj.add("tipoMensagem", new JsonParser().parse(new Gson().toJson(tipo )));
		obj.add("mensagem", new JsonParser().parse(new Gson().toJson(mensagem)));
		setaValoresResumo(obj, orcamento);
		response.getWriter().println(obj);
	}
	
	private void iniciaOrcamento(HttpServletResponse response, HttpServletRequest request) throws IOException {

	//	Orcamento orcamento = (Orcamento) request.getSession().getAttribute(ConfiguracaoWeb.ORCAMENTO_ATIVO.getDescricao());

	//	if (orcamento==null){
	//		throw new RuntimeException("N�o h� or�amento ativo");
	//	}
	//	JsonObject obj = new JsonObject();
	//	String descOrcamento = orcamento.getId()+ " - "+
	//			df.format(orcamento.getDataInicial() ) +
	//			" a " + df.format(orcamento.getDataFinal());
	//	obj.add("descOrcamento", new JsonParser().parse( new Gson().toJson( descOrcamento )));
	//	obj.add("idOrcamento", new JsonParser().parse( new Gson().toJson( orcamento.getId()) ) ); 
	//	List<LabelValueDTO> lista = new ArrayList<LabelValueDTO>();
	//	List<Gasto> gastos = orcamento.getGastos();
	//	System.out.println("Gastos " + gastos);
	//	for(Gasto gasto: gastos){
	//		lista.add(new LabelValueDTO(gasto.getId(), gasto.getDescricao()+ ": R$ " + 
	//				numberFormat.format(gasto.getValor())) );
	//	}
		
	//	obj.add("gastos", new JsonParser().parse(new Gson().toJson(lista)));
	//	setaValoresResumo(obj, orcamento);
		
	//	this.obtemResumoOrcamento(obj, orcamento);
		
		JsonObject obj = (JsonObject) request.getSession().getAttribute(ConfiguracaoWeb.ORCAMENTO_ATIVO.getDescricao());
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(obj);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void listaOrcamento(HttpServletResponse response) throws IOException {
		//System.out.println("Listando orcamentos" );
		List<Orcamento> orcamentos = orcamentoService.listaTodos(Orcamento.class);
		if (orcamentos.size()==0) {
			throw new RuntimeException("Lista de orcamentos vazia");
		}
		
		

		Collection c = new ArrayList();
		for(Orcamento orcamento : orcamentos){
			c.add(new LabelValueDTO(orcamento.getId(),  orcamento.getId()+ " - "+
					df.format(orcamento.getDataInicial() ) +
					" a " + df.format(orcamento.getDataFinal())));
		}

		JsonObject obj  = new JsonObject();
		new JsonParser().parse( new Gson().toJson(c));

		JsonObject obj2 = new JsonObject();
		obj2.add("orcamentos", new JsonParser().parse( new Gson().toJson(c)));
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(obj2);
	}
	
	private void resumeOrcamento(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Orcamento orcamento = null;
		String mensagem="";
		String tipo="";
		
		String strIdOrcamento = request.getParameter("idOrcamento");
		JsonObject obj  = new JsonObject();
		
		try {
			if (strIdOrcamento==null) {
				throw new RuntimeException("Orcamento invalido");
			}
			orcamento = orcamentoService.encontra(Integer.parseInt(strIdOrcamento),
					Orcamento.class);
			
			setaValoresResumo(obj,orcamento);
			
		} catch(Exception e ) {
			e.printStackTrace();
			tipo="ERRO";
			mensagem="Erro ao pagar: "+ e.getMessage();
		}
		
		if (mensagem!=null){
			obj.add("tipoMensagem", new JsonParser().parse( new Gson().toJson(tipo)));
			obj.add("mensagem", new JsonParser().parse( new Gson().toJson(mensagem)));
		}
		response.getWriter().println(obj);
	}
	
	private JsonObject obtemResumoOrcamento(JsonObject obj,
			Orcamento orcamento) throws IOException {
		String mensagem="";
		String tipo="";
		
		try {
			setaValoresResumo(obj,orcamento);
		} catch(Exception e ) {
			e.printStackTrace();
			tipo="ERRO";
			mensagem="Erro ao pagar: "+ e.getMessage();
		}
		
		if (mensagem!=null){
			obj.add("tipoMensagem", new JsonParser().parse( new Gson().toJson(tipo)));
			obj.add("mensagem", new JsonParser().parse( new Gson().toJson(mensagem)));
		}
		return obj;
	}
	
	private void setaValoresResumo(JsonObject obj,Orcamento orcamento) {
		obj.add("valorDisponivel", new JsonParser().parse(new Gson().toJson(
				"Valor disponivel: R$ " + numberFormat.format(orcamento.getValorDisponivel()))));
		obj.add("valorPendente", new JsonParser().parse(new Gson().toJson(
				"Valor Pendente: R$ " + numberFormat.format( orcamento.getValorTotalPendente()))));
		obj.add("valorSobrante", new JsonParser().parse(new Gson().toJson(
				"Sobrara: R$ " + numberFormat.format(orcamento.getValorDisponivel()- 
				orcamento.getValorTotalPendente()))));
	}




}
