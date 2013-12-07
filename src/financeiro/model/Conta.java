package financeiro.model;

import java.io.Serializable;
import java.util.Date;

public class Conta implements Serializable {
	
	private static final long serialVersionUID = 8884725799844326520L;
	
	private Integer id;
	protected String descricao;
	protected Double valor;
	protected Double valorPago;
	/** usado pra calcular total pendente do orcamento , atributo valor indica valor original da conta que nao muda */
	protected Double valorPendente;
	private Date dataVencimento;
	private Date dataPagamento;
	protected SituacaoDespesa situacao;
	
	public Conta(String descricao, double valor, Date data) {
		this.descricao=descricao;
		this.valor=valor;
		this.valorPendente=valor;
		this.dataVencimento = data;
		this.valorPago=0d;
	}
	
	public Conta() {
		valor=0d;
		valorPago=0d;
		valorPendente=0d;
	}
	
	// ---------------metodos de negocio -------------------//
	public void paga (double valor,Date dataPagamento) {
		this.valorPago+=valor;
		this.valorPendente-=valor;
		this.dataPagamento=dataPagamento;
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
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public double getValorPago() {
		return valorPago;
	}
	public void setValorPago(double valorPago) {
		this.valorPago = valorPago;
	}
	
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public boolean isPendente() {
		return this.valor!= valorPago ;
	}
	
	public double getValorPendente() {
		return valorPendente;
	}
	public void setValorPendente(double valorPendente) {
		this.valorPendente = valorPendente;
	}
	public SituacaoDespesa getSituacao() {
		return situacao;
	}
	public void setSituacao(SituacaoDespesa situacao) {
		this.situacao = situacao;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}
	public void setValorPendente(Double valorPendente) {
		this.valorPendente = valorPendente;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataVencimento == null) ? 0 : dataVencimento.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
		Conta other = (Conta) obj;
		if (dataVencimento == null) {
			if (other.dataVencimento != null)
				return false;
		} else if (!dataVencimento.equals(other.dataVencimento))
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
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Conta [descricao=" + descricao + ", valor=" + valor
				+ ", valorPago=" + valorPago + ", valorPendente="
				+ valorPendente + "]";
	}
	
	
	
}
