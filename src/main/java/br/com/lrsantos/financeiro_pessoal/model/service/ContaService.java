package br.com.lrsantos.financeiro_pessoal.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.lrsantos.financeiro_pessoal.model.bean.Conta;

@Stateless
public class ContaService extends ServiceGeneric<Conta, Integer> {
	
	public List<Conta> listaPorOrcamento(Integer idOrcamento) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idOrcamento", idOrcamento);
		return this.lista("Conta.listByOrcamento", params);
	}

}
