package br.com.lrsantos.financeiro_pessoal.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.lrsantos.financeiro_pessoal.model.bean.Recebimento;

@Stateless
public class RecebimentoService extends ServiceGeneric<Recebimento, Integer>{
	
	public List<Recebimento> listaPorOrcamento(Integer idOrcamento) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idOrcamento", idOrcamento);
		return this.lista("Recebimento.findByOrcamento", params);
	}
}
