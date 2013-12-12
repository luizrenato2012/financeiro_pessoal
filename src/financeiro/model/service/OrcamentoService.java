package financeiro.model.service;

import javax.ejb.EJB;

import financeiro.model.bean.FinanceiroException;
import financeiro.model.bean.Orcamento;
import financeiro.model.bean.Recebimento;

public class OrcamentoService extends ServiceGeneric<Orcamento, Integer>{

	@EJB
	private ServiceGeneric<Recebimento, Integer> recebimentoService;

	public void recebe(Recebimento recebimento, Orcamento orcamento) {
		try {
			recebimento.setOrcamento(orcamento);
			orcamento.recebe(recebimento);
			this.recebimentoService.persist(recebimento);
			this.merge(orcamento);
		} catch (Exception e ) {
			throw new FinanceiroException(e);
		}
	}
}
