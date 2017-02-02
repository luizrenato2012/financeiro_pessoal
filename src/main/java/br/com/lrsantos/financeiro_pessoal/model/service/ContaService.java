package br.com.lrsantos.financeiro_pessoal.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import br.com.lrsantos.financeiro_pessoal.model.bean.Conta;

@Stateless
public class ContaService extends ServiceGeneric<Conta, Integer> {
	
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
	 
	 //listByOrcamentoPendente
	 public List<Conta> listaPendentesPorOrcamento(Integer idOrcamento) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("idOrcamento", idOrcamento);
			return this.lista("Conta.listByOrcamentoPendente", params);
		}


}
