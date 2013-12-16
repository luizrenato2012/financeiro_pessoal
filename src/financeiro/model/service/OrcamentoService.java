package financeiro.model.service;

import java.util.Date;

import javax.ejb.EJB;

import financeiro.model.bean.Conta;
import financeiro.model.bean.FinanceiroException;
import financeiro.model.bean.Gasto;
import financeiro.model.bean.Orcamento;
import financeiro.model.bean.Recebimento;

public class OrcamentoService extends ServiceGeneric<Orcamento, Integer> {

	@EJB
	private ServiceGeneric<Recebimento, Integer> recebimentoService;
	
	@EJB
	private ServiceGeneric<Conta, Integer> contaService;
	
	@EJB
	private ServiceGeneric<Gasto, Integer> gastoService;

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
	
	public void adicionaConta(Conta conta, Orcamento orcamento) {
		try {
			conta.setOrcamento(orcamento);
			orcamento.adicionaConta(conta);
			contaService.persist(conta);
			this.merge(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}
	
	
	public void adicionaGasto(Gasto gasto, Orcamento orcamento) {
		try {
			orcamento.adicionaGasto(gasto);
			gastoService.persist(gasto);
			this.merge(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}
	
	public void pagaConta(Conta conta , double valor,Date data) {
		try {
			Orcamento orcamento = conta.getOrcamento();
			orcamento.pagaConta(conta, valor, data);
			contaService.merge(conta);
			this.merge(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}
	
	public void efetuaPagamentoGasto(Gasto gasto, double valor,Date data) {
		try {
			Orcamento orcamento = gasto.getOrcamento();
			orcamento.pagaGasto(gasto, valor, data);
			gastoService.merge(gasto);
			this.merge(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}
}
