package financeiro.model.service;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import financeiro.model.bean.Conta;
import financeiro.model.bean.FinanceiroException;
import financeiro.model.bean.Gasto;
import financeiro.model.bean.Orcamento;
import financeiro.model.bean.Recebimento;

@Stateless
public class OrcamentoService extends ServiceGeneric<Orcamento, Integer> {

	@EJB
	private RecebimentoService recebimentoService;
	
	@EJB
	private ContaService contaService;
	
	@EJB
	private GastoService gastoService;

	public void recebe(Recebimento recebimento, Orcamento orcamento) {
		try {
			recebimento.setOrcamento(orcamento);
			orcamento.recebe(recebimento);
			this.recebimentoService.persiste(recebimento);
			this.atualiza(orcamento);
		} catch (Exception e ) {
			throw new FinanceiroException(e);
		}
	}
	
	public void adicionaConta(Conta conta, Orcamento orcamento) {
		try {
			conta.setOrcamento(orcamento);
			orcamento.adicionaConta(conta);
			contaService.persiste(conta);
			this.atualiza(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}
	
	public void cancelaConta(Integer idConta, Orcamento orcamento) {
		try {
			Conta conta = contaService.encontra(idConta, Conta.class);
			orcamento.cancelaConta(conta);
			contaService.remove(conta);
			this.atualiza(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}
	
	
	public void adicionaGasto(Gasto gasto, Orcamento orcamento) {
		try {
			orcamento.adicionaGasto(gasto);
			gastoService.persiste(gasto);
			this.atualiza(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}
	
	public void pagaConta(Conta conta , Orcamento orcamento ) {
		try {
			orcamento.pagaConta(conta, conta.getValorPago(), conta.getDataPagamento());
			contaService.atualiza(conta);
			this.atualiza(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}
	
	public void efetuaPagamentoGasto(Gasto gasto, double valor,Date data) {
		try {
			Orcamento orcamento = gasto.getOrcamento();
			orcamento.pagaGasto(gasto, valor, data);
			gastoService.atualiza(gasto);
			this.atualiza(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}
	
	public void cancelaRecebimento(Integer idRecebimento,Orcamento orcamento) {
		try {
			Recebimento recebimento = recebimentoService.encontra(idRecebimento, Recebimento.class);
			orcamento.cancelaRecebimento(recebimento.getValor());
			recebimentoService.remove(recebimento);
			this.atualiza(orcamento);
		} catch(Exception e ) {
			throw new FinanceiroException(e);
		}
	}
	
}
