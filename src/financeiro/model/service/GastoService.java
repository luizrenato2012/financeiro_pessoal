package financeiro.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.Query;

import financeiro.model.bean.Gasto;
import financeiro.model.bean.Pagamento;

@Stateless
public class GastoService extends ServiceGeneric<Gasto, Integer>{
	
	public List<Gasto> listaPorOrcamento(Integer idOrcamento) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idOrcamento", idOrcamento);
		return this.lista("Gasto.listByOrcamento", params);
	}
	
	public List<Pagamento> listaPagamento(Integer idGasto) {
		Query query = entityManager.createNamedQuery("Gasto.listPagamento");
		query.setParameter("idGasto", idGasto);
		return query.getResultList();
	}

}
