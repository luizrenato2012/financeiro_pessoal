package br.com.lrsantos.financeiro_pessoal.controller;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.lrsantos.financeiro_pessoal.model.bean.Conta;
import br.com.lrsantos.financeiro_pessoal.model.bean.Gasto;
import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;
import br.com.lrsantos.financeiro_pessoal.model.bean.Pagamento;
import br.com.lrsantos.financeiro_pessoal.model.bean.SituacaoDespesa;
import br.com.lrsantos.financeiro_pessoal.model.service.ContaService;
import br.com.lrsantos.financeiro_pessoal.model.service.GastoService;
import br.com.lrsantos.financeiro_pessoal.model.service.OrcamentoFacade;
import br.com.lrsantos.financeiro_pessoal.model.service.OrcamentoService;
import br.com.lrsantos.financeiro_pessoal.model.service.PagamentoService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

	@EJB
	private PagamentoService pagamentoService;

	@EJB
	private OrcamentoFacade orcamentoFacade;

	private SimpleDateFormat df;

	@Override
	public void init() throws ServletException {
		numberFormat = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
		numberFormat.setMinimumFractionDigits(2);
		df = new SimpleDateFormat("dd/MM/yy");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processaGet(request,response);
	}

	private void processaGet(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String acao = request.getParameter("acao");
		switch (acao) {

		case "listaGasto": // lista usada no comob de pagamento de gastos
			listaGasto(request,response);
			break;
		case "listaContaResumo": // lista usada no comob de pagamento de gastos
			listaContaResumo(request,response);
			break;	
		case "orcamento": //lista usada no combo de pagamento de gastos e contas
			listaGastoContaResumo(request,response);
			break;	
		case "pagaGasto":
			pagaGasto(request,response);
			break;
		case "pagaConta":
			pagaConta(request,response);
			break;	
		case "resumeOrcamento":
			resumeOrcamento(request,response);
			break;
		case "listaPendenciaConta":
			this.listaContasPendentes(request, response);
			break;
		case "listaPendenciaGasto":
			this.listaGastosPendentes(request, response);
			break;	
		case "listaPendenciaContaGasto":
			this.listaContasGastosPendentes(request, response);
			break;	
		case "reinicia":
			this.reinicia(request);
			break;
		case "teste":
			this.testa(request, response);
			break;
		case "listaPagamento":
			this.listaPagamentos(request, response);
			break;
		case "listaOrcamento":
			this.listaOrcamento(request, response);
			break;	
		}	

	}

	/** retorna resumo, lista de gastos e contas pendentes */
	private void listaGastoContaResumo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		JsonObject obj = this.orcamentoFacade.listaGastoContaResumo() ;
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(obj);
	}

	/** retorna resumo, lista de gastos e contas pendentes */
	private void listaContaResumo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		JsonObject obj = this.orcamentoFacade.listaContaResumo() ;
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(obj);
	}


	private void listaGastosPendentes(HttpServletRequest request,
			HttpServletResponse response) {
		try  {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(this.orcamentoFacade.listaGastosPendentesOrcamentoAtivo(request.getParameter("tipoGasto")));
		}  catch (Exception e)   {
			e.printStackTrace();
		}
	}

	private void listaContasGastosPendentes(HttpServletRequest request,
			HttpServletResponse response) {
		try  {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(this.orcamentoFacade.listaGastosContasPendentesOrcamentoAtivo());
		}  catch (Exception e)   {
			e.printStackTrace();
		}
	}

	private void listaGasto(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Orcamento orcamento = (Orcamento)request.getSession().getAttribute(ConfiguracaoWeb.ORCAMENTO_ATIVO.getDescricao());
		if (orcamento == null) {
			throw new RuntimeException("Orcamento nao encontrado ");
		}
		Collection lista = new ArrayList();
		List<Gasto> gastos = this.gastoService.listaPorOrcamento(orcamento.getId());
		for (Gasto gasto : gastos) {
			lista.add(new LabelValueDTO(gasto.getId(), gasto.getDescricao() + ": R$ " + 
					this.numberFormat.format(gasto.getValor())));
		}
		JsonObject obj = new JsonObject();
		obj.add("gastos", new JsonParser().parse(new Gson().toJson(lista)));
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(obj);
	}


	private void resumeOrcamento(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonObject obj = this.orcamentoFacade.getResumoOrcamento();
		response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.getWriter().println(obj);
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

	private void listaContasPendentes(HttpServletRequest request, HttpServletResponse response)   {
		try  {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(this.orcamentoFacade.listaContasPendentesOrcamentoAtivo());
		}  catch (Exception e)   {
			e.printStackTrace();
		}
	}

	private void reinicia(HttpServletRequest request)  {
		this.orcamentoService.reiniciaIdOrcamentoAtual(request);
	}



	private void testa(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		//	Orcamento orcamento = (Orcamento)request.getSession().getAttribute(ConfiguracaoWeb.ORCAMENTO_ATIVO.getDescricao());
		//	List<Gasto> gastos = orcamento.getGastos();
		System.out.println("Receendo teste");
		//	System.out.println("Atributos " + this.imprime(request.getAttributeNames()));
		System.out.println("Parametros "+ this.imprime(request.getParameterNames()));

		JsonObject obj = new JsonObject();
		obj.add("retorno", new JsonParser().parse(new Gson().toJson("retorno ok")));

		response.setContentType("application/json");
		response.getWriter().print(obj);
	}

	private String imprime(Enumeration<String> enumeration) {
		StringBuilder strb = new StringBuilder();
		while(enumeration.hasMoreElements()) {
			strb.append( (String)enumeration.nextElement() );
		}
		return strb.toString();
	}

	private void listaPagamentos(HttpServletRequest request, HttpServletResponse response)  {
		try  {
			String strDataInicial = request.getParameter("dataInicial");
			String strDataFinal = request.getParameter("dataFinal");
			if ((strDataInicial == null) || (strDataInicial.trim().equals(""))) {
				throw new RuntimeException("Data inicial invalida");
			}
			if ((strDataFinal == null) || (strDataFinal.trim().equals(""))) {
				throw new RuntimeException("Data final invalida");
			}
			String tipoPagamento = request.getParameter("tipo");

			Date dataInicial = this.df.parse(strDataInicial);
			Date dataFinal = this.df.parse(strDataFinal);

			JsonObject pagamentos = this.pagamentoService.listaPagamentos(dataInicial, 
					dataFinal, this.orcamentoService.getIdOrcamentoAtivo(request), tipoPagamento);
			//			System.out.println(pagamentos);
			response.setContentType("application/json");

			response.getWriter().println(pagamentos.toString());
		}
		catch (ParseException e)  {
			e.printStackTrace();
		}
		catch (IOException e)  {
			e.printStackTrace();
		}
		catch (Exception e)  {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.processaPut(request, response);
	}
	
	private void processaPut(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try {
			String acao = request.getParameter("acao");
			switch (acao) {

			case "pagaGasto":
				pagaGasto(request,response);
				break;
			case "pagaConta":
				pagaConta(request,response);
				break;	
			case "ativaOrcamento":
				this.ativaOrcamento(request, response);
			}
		} catch (Exception e ) {
			retornaRespostaErro(response,"Erro " + e.getMessage());
		}
	}
	
	private void retornaRespostaErro(HttpServletResponse response, String mensagem) throws IOException {
		JsonObject obj = new JsonObject();
		obj.add("tipoMensagem", new JsonParser().parse(new Gson().toJson("ERRO")));
		obj.add("mensagem", new JsonParser().parse(new Gson().toJson(mensagem)));
		response.getWriter().println(obj);
	}

	private void pagaGasto(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String strIdGasto = request.getParameter("id");
		if ((strIdGasto == null) || (strIdGasto.equals(""))) {
			throw new RuntimeException("Erro ao pagar gasto");
		}
		String strIdOrcamento = request.getParameter("idOrcamento");
		String mensagem = "";
		String tipo = "";
		Orcamento orcamento = null;
		JsonObject obj = new JsonObject();
		try {
			Integer idGasto = Integer.valueOf(Integer.parseInt(strIdGasto));

			String strData = request.getParameter("data");
			if ((strData == null) || (strData.equals(""))) {
				throw new RuntimeException("Data obrigatoria");
			}
			Date data = new SimpleDateFormat("dd/MM/yyyy").parse(strData);

			String strValor = request.getParameter("valor");
			if ((strValor == null) || (strValor.equals(""))) {
				throw new RuntimeException("Valor obrigatorio");
			}
			Double valor = Double.valueOf(this.numberFormat.parse(strValor).doubleValue());

			Collection lista = new ArrayList();
			Gasto gasto = (Gasto)this.gastoService.encontra(idGasto, Gasto.class);
			orcamento = (Orcamento)this.orcamentoService.encontra(Integer.valueOf(Integer.parseInt(strIdOrcamento)), 
					Orcamento.class);
			gasto.setOrcamento(orcamento);

			Pagamento pagamento = new Pagamento();
			pagamento.setData(data);
			pagamento.setValor(valor);
			pagamento.setObservacao(request.getParameter("descricao"));

			this.gastoService.registraPagamento(gasto, pagamento);
			this.orcamentoService.efetuaPagamentoGasto(gasto, valor.doubleValue(), data);
			request.getSession().setAttribute(ConfiguracaoWeb.RESUMO_ORCAMENTO.getDescricao(), 
					this.orcamentoFacade.getResumoOrcamento());
			
			obj.add("orcamento",this.orcamentoFacade.listaGastoResumo());
			JsonObject objResumo = (JsonObject)request.getSession().getAttribute(
					ConfiguracaoWeb.RESUMO_ORCAMENTO.getDescricao());
			tipo = "OK";
			mensagem = "Pagamento registrado com sucesso!";
			obj.add("resumo", objResumo);
		}	catch (Exception e) {
			e.printStackTrace();
			tipo = "ERRO";
			mensagem = "Erro ao pagar: " + e.getMessage();
		}
		
		obj.add("tipoMensagem", new JsonParser().parse(new Gson().toJson(tipo)));
		obj.add("mensagem", new JsonParser().parse(new Gson().toJson(mensagem)));
		
		response.getWriter().println(obj);
	}

	private void pagaConta(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String strIdConta = request.getParameter("id");
		if ((strIdConta == null) || (strIdConta.equals(""))) {
			throw new RuntimeException("Erro ao pagar conta");
		}
		String strIdOrcamento = request.getParameter("idOrcamento");
		String strValor = request.getParameter("valor");
		String mensagem = "";
		String tipo = "";
		Orcamento orcamento = null;

		try  {
			Integer idConta = Integer.valueOf(Integer.parseInt(strIdConta));

			String strData = request.getParameter("data");
			if ((strData == null) || (strData.equals(""))) {
				throw new RuntimeException("Data obrigatoria");
			}

			if (strIdOrcamento == null || strIdOrcamento.trim().equals("")) {
				throw new RuntimeException("Id orcamento obrigatorio");
			}

			if (strValor == null || strValor.trim().equals("")) {
				throw new RuntimeException("Valor obrigatorio");
			}

			Conta conta = (Conta)this.contaService.encontra(idConta, Conta.class);
			if (conta.getSituacao().equals(SituacaoDespesa.PAGA)) {
				throw new RuntimeException("Conta ja paga");
			}
			conta.setDataPagamento(new SimpleDateFormat("dd/MM/yyyy").parse(strData));
			conta.setValorPago(Double.valueOf(strValor));
			orcamento = (Orcamento)this.orcamentoService.encontra(Integer.valueOf(Integer.parseInt(strIdOrcamento)), 
					Orcamento.class);
			conta.setOrcamento(orcamento);

			this.orcamentoService.pagaConta(conta, orcamento);
			request.getSession().setAttribute(ConfiguracaoWeb.RESUMO_ORCAMENTO.getDescricao(), 
					this.orcamentoFacade.getResumoOrcamento());

			tipo = "OK";
			mensagem = "Pagamento registrado com sucesso!";
		}
		catch (Exception e)   {
			e.printStackTrace();
			tipo = "ERRO";
			mensagem = "Erro ao pagar: " + e.getMessage();
		}
		
		JsonObject obj = new JsonObject();
		obj.add("tipoMensagem", new JsonParser().parse(new Gson().toJson(tipo)));
		obj.add("mensagem", new JsonParser().parse(new Gson().toJson(mensagem)));
		obj.add("orcamento",this.orcamentoFacade.listaContaResumo());

		response.getWriter().println(obj);
	}
	
	private void ativaOrcamento(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String mensagem = "";
		String tipo = "";
		try {

			String strIdOrcamento = request.getParameter("idOrcamento");
			if (strIdOrcamento == null || strIdOrcamento.trim().equals("")) {
				throw new RuntimeException("Id orcamento obrigatorio");
			}
			this.orcamentoFacade.ativaOrcamento(Integer.parseInt(strIdOrcamento));
			tipo = "OK";
			mensagem = "Orcamento ativado com sucesso!";
		} catch (Exception e) {
			e.printStackTrace();
			tipo = "ERRO";
			mensagem = "Erro ao ativar orçamento!";
		}
		response.getWriter().println(this.orcamentoFacade.criaMensagemRetorno(tipo, mensagem));
	}
	
	
	
	private void listaOrcamento(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String mensagem = "";
		String tipo = "";
		Map<String,Object> mapJson = new HashMap<String,Object>();
		
		try {
			mapJson.put("orcamentos",this.orcamentoFacade.listaOrcamentos());
			
			tipo = "OK";
			mensagem = "Listagem de orcamentos ok!";
		} catch (Exception e) {
			e.printStackTrace();
			tipo = "ERRO";
			mensagem = "Erro ao listar orcamento ";
		}
		
		mapJson.put("tipo", tipo);
		mapJson.put("mensagem", mensagem);
		
		response.getWriter().println(this.orcamentoFacade.criaMensagemRetorno(mapJson));
	}
	

}
