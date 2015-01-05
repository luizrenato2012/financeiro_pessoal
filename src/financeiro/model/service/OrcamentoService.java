package financeiro.model.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.jboss.logging.Logger;

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

	private Logger log = Logger.getLogger(this.getClass());

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

	/** 
	 * necessario carregamento dos gastos do orçamento 
	 * em virtudo do cascade.all e pelo fato do relacionamento ser bidirecional
	 * sem o carregamento, só o gastoService.remove(gasto) nao apaga o gasto
	 */
	public void cancelaConta(Integer idConta, Orcamento orcamento) {
		try {
			Conta conta = contaService.encontra(idConta, Conta.class);
			orcamento = this.carregaContas(orcamento.getId());
			
			if (orcamento.getContas()!=null && orcamento.getContas().size() > 0 ) {
				orcamento.getContas().remove(conta);
			} else {
				throw new FinanceiroException("Orçamento " + orcamento.getId() + 
						" nao possui contas relacionadas");
			}
			
			orcamento.cancelaConta(conta);
			contaService.remove(conta);
			this.atualiza(orcamento);
			log.info(">>> Cancelada conta " + idConta);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}

	private Orcamento carregaContas(Integer id) {
		Query query = this.entityManager.createNamedQuery("Orcamento.loadContas");
		query.setParameter("id", id);
		return (Orcamento) query.getSingleResult();
	}

	/** 
	 * necessario carregamento dos gastos do orçamento 
	 * em virtudo do cascade.all e pelo fato do relacionamento ser bidirecional
	 * sem o carregamento, só o gastoService.remove(gasto) nao apaga o gasto
	 */
	public void cancelaGasto(Integer idGasto, Orcamento orcamento) {
		try {
			Gasto gasto = gastoService.encontra(idGasto, Gasto.class);
			orcamento.cancelaGasto(gasto);
			//alterada forma de exclusao
			this.removeGasto(idGasto);
			this.atualiza(orcamento);
			log.info(">>> Cancelado gasto " + idGasto);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}
	
	/**
	 * bulk update delete - exclusao de muitos registros @OneToMany(Cascade.REMOVE)
	 * nao funciona
	 * @param idGasto
	 */
	private void removeGasto(Integer idGasto){
		String qryUpdate = "update financ.pagamento set id_gasto=null where id_gasto=?1";
		Query query = this.entityManager.createNativeQuery(qryUpdate);
		query.setParameter(1, idGasto);
		query.executeUpdate();
		gastoService.removeBydId("Gasto.deleteById", idGasto);
	}


	public void adicionaGasto(Gasto gasto, Orcamento orcamento) {
		try {
			orcamento.adicionaGasto(gasto);
			gasto.setOrcamento(orcamento);
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
			orcamento.pagaGasto(gasto, valor);
		  //gastoService.atualiza(gasto);
			this.atualiza(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}

	/** 
	 * na exclusao do recebimento não é necessária a carga da lista de recebimentos 
	 * pois o fetch do relacionamento é EAGER
	 */
	public void cancelaRecebimento(Integer idRecebimento,Orcamento orcamento) {
		try {
			Recebimento recebimento = recebimentoService.encontra(idRecebimento, Recebimento.class);
			recebimento.setOrcamento(null);
			
			if (orcamento.getRecebimentos()!=null && orcamento.getRecebimentos().size() > 0 ) {
				orcamento.getRecebimentos().remove(recebimento);
			} else {
				throw new FinanceiroException("Orcamento " + orcamento.getId() + 
						" nao possui recebimentos associados");
			}
			
			orcamento.cancelaRecebimento(recebimento.getValor());
			recebimentoService.remove(recebimento);
			this.atualiza(orcamento);
			log.info(">>> Recebimento " + idRecebimento + " cancelado com sucesso");
		} catch(Exception e) {
			throw new FinanceiroException(e);
		}
	}
	
	public double getValorTotalGastos(Integer idOrcamento) {
		StringBuilder strb = new StringBuilder();
		strb.append("select sum(valor) from financ.conta ")
			.append (" where id_orcamento=?1 and tipo_conta=\'Gasto\'");
		Query query = this.entityManager.createNativeQuery(strb.toString());
		query.setParameter(1, idOrcamento);
		Double res = (Double) query.getSingleResult();
		return res!=null ? res.doubleValue() : 0.0;
	}
	
	public double getValorTotalContas(Integer idOrcamento) {
		StringBuilder strb = new StringBuilder();
		strb.append("select sum(valor) from financ.conta ")
			.append (" where id_orcamento=?1 and tipo_conta=\'Conta\'");
		Query query = this.entityManager.createNativeQuery(strb.toString());
		query.setParameter(1, idOrcamento);
		Double res = (Double) query.getSingleResult();
		return res!=null ? res.doubleValue() : 0.0;
	}


}
