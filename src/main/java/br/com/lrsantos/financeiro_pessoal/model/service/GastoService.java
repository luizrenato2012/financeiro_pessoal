package br.com.lrsantos.financeiro_pessoal.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import br.com.lrsantos.financeiro_pessoal.model.bean.FinanceiroException;
import br.com.lrsantos.financeiro_pessoal.model.bean.Gasto;
import br.com.lrsantos.financeiro_pessoal.model.bean.Pagamento;

@Stateless
public class GastoService extends ServiceGeneric<Gasto, Integer>{
	private Logger log = Logger.getLogger(this.getClass());
	
	@EJB
	private PagamentoService pagamentoService;
	
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
	
	public Gasto carregaPagamentos(Integer idGasto) {
		log.info("carregaPagamentos " + idGasto);
		Query query = entityManager.createNamedQuery("Gasto.loadPagamentos");
		query.setParameter("idGasto", idGasto);
		return (Gasto) query.getSingleResult();
	}
	
	public void registraPagamento (Gasto gasto, Pagamento pagamento) {
		pagamento.setGasto(gasto);
		pagamentoService.persiste(pagamento);
		//gasto.paga(pagamento.getValor(), pagamento.getData());
		gasto.paga(pagamento.getValor());
		this.atualiza(gasto);
		List<Pagamento> pagamentos = this.listaPagamento(gasto.getId());
		gasto.setPagamentos( pagamentos!=null && pagamentos.size()> 0 ? pagamentos :
			new ArrayList<Pagamento>() );
	}
	
	
	public void cancelaPagamento(Gasto gasto, Integer idPagamento) {
		Pagamento pagamento = this.encontraPagamento(idPagamento);
		if (pagamento==null) {
			throw new FinanceiroException("Pagamento nao foi encontrado para exclus�o");
		}
		gasto.cancelaPagamento(pagamento);
		this.atualiza(gasto);
		this.pagamentoService.remove(pagamento);
		this.carregaPagamentos(gasto.getId());
	}
	
	private Pagamento encontraPagamento(Integer idPagamento) {
		return this.pagamentoService.encontra(idPagamento, Pagamento.class);
	}

}
