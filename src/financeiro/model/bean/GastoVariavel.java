package financeiro.model.bean;

import javax.persistence.Entity;

/**
 * Gasto sem valor previsto
 * nao deve aumentar valor pendente ao incluir
 * nao deve diminuir o valor pendente ao excluir ou pagar
 *
 */
@Entity
public class GastoVariavel extends Gasto {
	

	public GastoVariavel() {
		super();
		this.valor=0.0;
		this.valorPendente=0.0;
		this.situacao = SituacaoDespesa.ABERTO;
	}

	@Override
	public void paga(double valor) {
		this.valorPago+=valor;
	//	if (this.valorPendente==0.0d) {
	//		this.situacao=SituacaoDespesa.PAGA;
	//	}
	}

	@Override
	public void cancelaPagamento(Pagamento pagamento) {
		this.valorPago-=pagamento.getValor();
		this.valorPendente+=pagamento.getValor();
		this.getPagamentos().remove(pagamento);
	}
	
	

	@Override
	public void setValorPendente(double valorPendente) {
		throw new FinanceiroException("Valor pendente de gasto variavel nao pode ser alterado");
	}

	@Override
	public void setValor(double valor) {
		this.valor=valor;
	}

	@Override
	public double getValor() {
		return 0.0;
	}

	@Override
	public double getValorPendente() {
		return 0.0;
	}
	
	
}
