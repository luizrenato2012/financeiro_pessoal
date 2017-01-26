package br.com.lrsantos.financeiro_pessoal.model.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.lrsantos.financeiro_pessoal.controller.LabelValueDTO;
import br.com.lrsantos.financeiro_pessoal.model.bean.Conta;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Stateless
public class ContaService extends ServiceGeneric<Conta, Integer> {
	
	 private static String QRY_CONTA_PENDENTE = "select ct.descricao,ct.data_vencimento, ct.valor, case when data_vencimento <= current_date then 'Vencida' else 'A Vencer' end as status from financ.conta ct  inner join financ.orcamento orc on ct.id_orcamento = orc.id  where ct.situacao = 'PENDENTE' and ct.tipo_conta='Conta' and orc.ativo=true";
	 
	
	public List<Conta> listaPorOrcamento(Integer idOrcamento) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idOrcamento", idOrcamento);
		return this.lista("Conta.listByOrcamento", params);
	}
	
	 public List<Conta> listaContasImportacao()  {
	    StringBuilder strb = new StringBuilder();
	    strb.append("Select c.* from financ.Conta c ")
	      .append(" where c.tipo_conta='Conta'")
	      .append(" and c.favorito=true")
	      .append(" order by c.descricao");
	    Query query = this.entityManager.createNativeQuery(strb.toString(), Conta.class);
	    List<Conta> contas = query.getResultList();
	    return contas;
	  }
	  
	  public JsonObject listaContasPendentes()   {
	    Query query = this.entityManager.createNativeQuery(QRY_CONTA_PENDENTE);
	    
	    List<Object[]> listaPendencias = query.getResultList();
	    List<LabelValueDTO> listaDTO = new ArrayList();
	    
	    JsonArray jsonAr = new JsonArray();
	    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	    for (Object[] ar : listaPendencias)    {
	      JsonObject jsObj = new JsonObject();
	      jsObj.add("descricao", new JsonParser().parse(new Gson().toJson(ar[0])));
	      jsObj.add("vencimento", new JsonParser().parse(new Gson().toJson(
	        fmt.format((Date)ar[1]))));
	      jsObj.add("valor", new JsonParser().parse(new Gson().toJson(ar[2])));
	      jsObj.add("situacao", new JsonParser().parse(new Gson().toJson(ar[3])));
	      jsonAr.add(jsObj);
	    }
	    
	    JsonObject jsObjRet = new JsonObject();
	    jsObjRet.add("listaContasPendentes", jsonAr);
	    return jsObjRet;
	  }

}
