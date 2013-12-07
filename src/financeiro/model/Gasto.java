/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package financeiro.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe que representa um tipo de conta onde sao feito vários pagamento
 * p. ex refeição, passagens ,etc
 * @author Luiz Renato
 *
 */
public class Gasto extends Conta {
	
	
	private Integer id;
//	private  String descricao;
	/** valor do gasto pode ser diferente do valor pago quando o pago ultrapassa o previsto*/
//	private double valor;
//	private double valorPendente;
//	private double valorPago;
	private Date dataInicial;
	private Date dataFinal;
	private List<Pagamento> pagamentos;
//	private SituacaoDespesa situacao;
	
	public Gasto() {
		pagamentos = new ArrayList<Pagamento>();
		id=null;
	}
	
	public void paga(Date data,double valor,String observacao) {
		Pagamento pagamento = new Pagamento();
		pagamento.setData(data);
		pagamento.setObservacao(observacao);
		pagamento.setValor(valor);
		
		this.valorPendente-= valor;
		this.valorPago+=valor;
	}
	
	public void cancelaPagamento(Pagamento pagamento) {
		this.valorPago-=pagamento.getValor();
		this.valorPendente+=pagamento.getValor();
		this.pagamentos.remove(pagamento);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValorPendente() {
		return valorPendente;
	}

	public void setValorPendente(double valorPendente) {
		this.valorPendente = valorPendente;
	}

	public double getValorPago() {
		return valorPago;
	}

	public void setValorPago(double valorPago) {
		this.valorPago = valorPago;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}


	

	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public SituacaoDespesa getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoDespesa situacao) {
		this.situacao = situacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataFinal == null) ? 0 : dataFinal.hashCode());
		result = prime * result
				+ ((dataInicial == null) ? 0 : dataInicial.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		long temp;
		temp = Double.doubleToLongBits(valor);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gasto other = (Gasto) obj;
		if (dataFinal == null) {
			if (other.dataFinal != null)
				return false;
		} else if (!dataFinal.equals(other.dataFinal))
			return false;
		if (dataInicial == null) {
			if (other.dataInicial != null)
				return false;
		} else if (!dataInicial.equals(other.dataInicial))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Double.doubleToLongBits(valor) != Double
				.doubleToLongBits(other.valor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Gasto [descricao=" + descricao + ", valor=" + valor
				+ ", valorPago=" + valorPago + ", valorPendente="
				+ valorPendente + "]";
	}
	
	
	
	
}
