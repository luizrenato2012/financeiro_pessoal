package br.com.lrsantos.financeiro_pessoal.model.service;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Stateless
public class OrcamentoFacade implements Serializable {

	private static final long serialVersionUID = -9206629184887192978L;

	@EJB
	private OrcamentoService orcamentoService;

	private NumberFormat numFormat;
	private DateFormat dtFormat;

	@PostConstruct
	private void configFormat() {
		numFormat = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
		numFormat.setMinimumFractionDigits(2);
		dtFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	public JsonObject getResumoOrcamento() 	{
		String mensagem = "";
		String tipo = "";

		JsonObject jsObj = new JsonObject();
		int count = 1;
		java.util.Date d1 = null;
		java.util.Date d2 = null;
		Double valorDisponivel = null;
		Double valorPendente = null;
		Double valorSobrante = null;
		Double contaPendente = null;
		Double gastoPendente = null;
		List<Object[]> lista = null;

		try{
			lista = this.orcamentoService.getResumoOrcamento(); 
			for (Object[] ar : lista) {
				if (count == 1)
				{
					jsObj.add("idOrcamento", new JsonParser().parse(new Gson().toJson(ar[0])));
					d1 = new java.util.Date(((java.sql.Date)ar[1]).getTime());
					d2 = new java.util.Date(((java.sql.Date)ar[2]).getTime());
					jsObj.add("descOrcamento", new JsonParser().parse(new Gson().toJson(ar[0] + 
							" - " + this.dtFormat.format(d1) + " a " + 
							this.dtFormat.format(d2))));

					valorDisponivel = Double.valueOf((Double)ar[3] == null ? 0.0D : ((Double)ar[3]).doubleValue());
					valorPendente = Double.valueOf((Double)ar[4] == null ? 0.0D : ((Double)ar[4]).doubleValue());
					valorSobrante = Double.valueOf((Double)ar[5] == null ? 0.0D : ((Double)ar[5]).doubleValue());
					contaPendente = Double.valueOf((Double)ar[6] == null ? 0.0D : ((Double)ar[6]).doubleValue());
					gastoPendente = Double.valueOf((Double)ar[7] == null ? 0.0D : ((Double)ar[7]).doubleValue());

					jsObj.add("valorDisponivel", new JsonParser().parse(new Gson().toJson(valorDisponivel)));
					jsObj.add("valorPendente", new JsonParser().parse(new Gson().toJson(valorPendente)));
					jsObj.add("valorSobrante", new JsonParser().parse(new Gson().toJson(valorSobrante)));
					jsObj.add("contaPendente", new JsonParser().parse(new Gson().toJson(contaPendente)));
					jsObj.add("gastoPendente", new JsonParser().parse(new Gson().toJson(gastoPendente)));
					count++;
				}
			}
			JsonObject objResumo = new JsonObject();
			objResumo.add("resumo", new JsonParser().parse(new Gson().toJson(jsObj)));
		} catch (Exception e ) {
			e.printStackTrace();
			tipo = "ERRO";
			mensagem = "Erro ao listar: " + e.getMessage();
		}
		if (mensagem != null && !mensagem.trim().equals("")) {
			jsObj.add("tipoMensagem", new JsonParser().parse(new Gson().toJson(tipo)));
			jsObj.add("mensagem", new JsonParser().parse(new Gson().toJson(mensagem)));
		}
		return jsObj;
	}

	public String listaGastosPendentesOrcamentoAtivo () {
		List<Object[]>listaPendencias = this.orcamentoService.listaGastosPendentesOrcamentoAtivo();
		return this.getJsonFromList(listaPendencias);
		
	}
	
	public String listaContasPendentesOrcamentoAtivo() {
		List<Object[]>listaPendencias = this.orcamentoService.listaContasPendentesOrcamentoAtivo();
		return this.getJsonFromList(listaPendencias);
	}

	public String listaGastosContasPendentesOrcamentoAtivo() {
		List<Object[]>listaPendencias = this.orcamentoService.listaGastosContasPendentesOrcamentoAtivo();
		return this.getJsonFromList(listaPendencias);
	}
	
	private String getJsonFromList(List<Object[]> listaPendencias) {
		Gson gson = new Gson();
		ListaPendenciasJSon listaPendencia = new ListaPendenciasJSon();
		Map<String,Object> map = null;
		for (Object[] ar : listaPendencias)    {
			map =new LinkedHashMap<String, Object>();
			map.put("id", (Integer)ar[0]);
			map.put("descricao", (String)ar[1]);
			map.put("tipo", (String)ar[2]);
			map.put("vencimento", JSonUtil.parseDateToString(ar[3]));
			map.put("valor", ar[4]);
			listaPendencia.add(map);
		}
		return gson.toJson(listaPendencia);
	}
	
	/** retorna valores do orcamentos gastos e contas pendentes */
	public JsonObject listaGastoContaResumo () {
		String mensagem = "";
		String tipo = "";

		//Orcamento orcamento = null;
		JsonObject obj = new JsonObject();
		try {
			obj.add("gastos", new JsonParser().parse(this.listaGastosPendentesOrcamentoAtivo()));
			obj.add("contas", new JsonParser().parse(this.listaContasPendentesOrcamentoAtivo()));
			obj.add("resumo", new JsonParser().parse(new Gson().toJson(this.getResumoOrcamento())));
			
		} catch (Exception e ) {
			e.printStackTrace();
			tipo = "ERRO";
			mensagem = "Erro em listaGastoContaResumo: " + e.getMessage();
		}
		if (mensagem != null && !mensagem.trim().equals("")) {
			obj.add("tipoMensagem", new JsonParser().parse(new Gson().toJson(tipo)));
			obj.add("mensagem", new JsonParser().parse(new Gson().toJson(mensagem)));
		}
		return obj;
	}
	
	/** retorna valores do orcamentos e contas pendentes -  */
	public JsonObject listaContaResumo () {
		String mensagem = "";
		String tipo = "";

		//Orcamento orcamento = null;
		JsonObject obj = new JsonObject();
		try {
			obj.add("contas", new JsonParser().parse(this.listaContasPendentesOrcamentoAtivo()));
			obj.add("resumo", new JsonParser().parse(new Gson().toJson(this.getResumoOrcamento())));

		} catch (Exception e ) {
			e.printStackTrace();
			tipo = "ERRO";
			mensagem = "Erro em listaGastoContaResumo: " + e.getMessage();
		}
		if (mensagem != null && !mensagem.trim().equals("")) {
			obj.add("tipoMensagem", new JsonParser().parse(new Gson().toJson(tipo)));
			obj.add("mensagem", new JsonParser().parse(new Gson().toJson(mensagem)));
		}
		return obj;
	}
	
	/** retorna valores do orcamentos e contas pendentes -  */
	public JsonObject listaGastoResumo () {
		String mensagem = "";
		String tipo = "";

		//Orcamento orcamento = null;
		JsonObject obj = new JsonObject();
		try {
			obj.add("resumo", new JsonParser().parse(new Gson().toJson(this.getResumoOrcamento())));
			obj.add("gastos", new JsonParser().parse(this.listaGastosPendentesOrcamentoAtivo()));

		} catch (Exception e ) {
			e.printStackTrace();
			tipo = "ERRO";
			mensagem = "Erro em listaGastoContaResumo: " + e.getMessage();
		}
		if (mensagem != null && !mensagem.trim().equals("")) {
			obj.add("tipoMensagem", new JsonParser().parse(new Gson().toJson(tipo)));
			obj.add("mensagem", new JsonParser().parse(new Gson().toJson(mensagem)));
		}
		return obj;
	}


}
