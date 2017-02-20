package br.com.lrsantos.financeiro_pessoal.model.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.lrsantos.financeiro_pessoal.model.bean.Pagamento;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Stateless
public class PagamentoService extends ServiceGeneric<Pagamento, Integer> {
	public String montaQuery(String tipo)  {
	    StringBuilder strb = new StringBuilder();
	    
	    strb.append("select pag.id_gasto, pag.data,pag.observacao as descricao,pag.valor, 'Gasto' ")
		      .append("from financ.pagamento pag ")
		      .append("inner join financ.conta ct on pag.id_gasto = ct.id ")
		      .append("inner join financ.orcamento orc on orc.id = ct.id_orcamento ")
		      .append("where orc.id = :id_orcamento ")
		      .append("and pag.data between :data_inicial and :data_final ");
	    if (!tipo.equals("")) {
	      strb.append("and ct.tipo_conta like :tipo ");
	    }
	    strb.append(" union ").append("select ct.id, ct.data_pagamento, ct.descricao as descricao,ct.valor_pago, 'Conta' ").append("from financ.conta ct ").append("inner join financ.orcamento orc on ct.id_orcamento = orc.id ").append("where ct.situacao = 'PAGA' ").append(" and orc.id = :id_orcamento ").append(" and ct.data_pagamento between :data_inicial and :data_final ");
	    if (!tipo.equals("")) {
	      strb.append(" and ct.tipo_conta like :tipo");
	    }
	    strb.append(" order by 5,2 ");
	    return strb.toString();
	  }
	  
	  public JsonObject listaPagamentos(Date dataInicial, Date dataFinal, Integer idOrcamento, String tipo)  {
	    Query query = this.entityManager.createNativeQuery(montaQuery(tipo));
	    query.setParameter("id_orcamento", idOrcamento);
	    query.setParameter("data_inicial", dataInicial);
	    query.setParameter("data_final", dataFinal);
	    if (!tipo.equals("")) {
	      query.setParameter("tipo", "%" + tipo + "%");
	    }
	    List<Object[]> lista = query.getResultList();
	    
	    Object[] result = null;
	    JsonObject jsObject = null;
	    JsonArray jsnArray = new JsonArray();
	    if ((lista != null) && (lista.size() > 0)) {
	      for (int i = 0; i < lista.size(); i++)
	      {
	        result = (Object[])lista.get(i);
	        jsObject = new JsonObject();
	        //conversoes devido GSon 2.7 - anterior 2.4
	        jsObject.addProperty("id", (Integer)result[0]);
	        jsObject.addProperty("data", ((java.sql.Date)result[1]).toString()  ) ;
	        jsObject.addProperty("descricao", (String)result[2]);
	        jsObject.addProperty("valor", (Double)result[3]);
	        jsObject.addProperty("tipo", (String)result[4]);
	        jsnArray.add(jsObject);
	      }
	    }
	    JsonObject obj = new JsonObject();
	    obj.add("pagamentos", jsnArray);
	    return obj;
	  }

}
