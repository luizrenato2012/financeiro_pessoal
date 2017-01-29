package br.com.lrsantos.financeiro_pessoal.model.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Luiz Renato A. Santos
 *
 */
public class ListaPendenciasJSon {
	
	private List <Map<String,Object>>listaPendencias;

	public ListaPendenciasJSon() {
		this.listaPendencias = new LinkedList<Map<String,Object>>();
	}

	public void add(Map<String,Object> value) {
		this.listaPendencias.add(value);
	}
	
	public void add(String key ,Object value) {
		Map<String,Object> map = new LinkedHashMap<String, Object>(); 
		map.put(key, value);
		this.listaPendencias.add(map);
	}
	
	public List<Map<String, Object>> getListaGastosPendentes() {
		return listaPendencias;
	}

	public void setListaGastosPendentes(
			List<Map<String, Object>> listaGastosPendentes) {
		this.listaPendencias = listaGastosPendentes;
	}
	
	
	
	

}
;