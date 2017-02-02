package br.com.lrsantos.financeiro_pessoal.model.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.DiscriminatorOptions;

import br.com.lrsantos.financeiro_pessoal.util.DataUtils;

@Entity
@Table(name="conta",schema="financ")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo_conta")
@SequenceGenerator(name="SEQ_ID_CONTA", sequenceName="financ.seq_id_conta",allocationSize=1)
@NamedQueries( {
	@NamedQuery(name="Conta.listByOrcamento",query="select c from Conta c inner join c.orcamento o " +
			" where o.id = :idOrcamento and TYPE (c) = Conta"),
	@NamedQuery(name="Conta.listByOrcamentoPendente",query="select c from Conta c inner join c.orcamento o " +
			" where o.id = :idOrcamento and TYPE (c) = Conta and c.situacao = br.com.lrsantos.financeiro_pessoal.model.bean.SituacaoDespesa.PENDENTE")		
})
@DiscriminatorValue("Conta")
@DiscriminatorOptions(force=true)
public class Conta implements Serializable {
	
	private static final long serialVersionUID = 8884725799844326520L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_ID_CONTA")
	private Integer id;
	
	protected String descricao;
	
	@Column(precision=2)
	protected Double valor;
	
	@Column(name="valor_pago")
	protected Double valorPago;
	
	@Column(name="valor_pendente",precision=2)
	/** usado pra calcular total pendente do orcamento , atributo valor indica valor original da conta que nao muda */
	protected Double valorPendente;
	
	@Column(name="data_vencimento")
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
	
	@Column(name="data_pagamento")
	@Temporal(TemporalType.DATE)
	private Date dataPagamento;
	
	@Enumerated(EnumType.STRING)
	protected SituacaoDespesa situacao;
	
	@ManyToOne(fetch=FetchType.LAZY,targetEntity=Orcamento.class)
	@JoinColumn(name="id_orcamento")
	private Orcamento orcamento;
	
	@Transient
	private Boolean isPendente=Boolean.TRUE;
	
	@Transient
	private Boolean isVencida;
	
	public Conta(String descricao, double valor, Date data) {
		this.descricao=descricao;
		this.valor=valor;
		this.valorPendente=valor;
		this.dataVencimento = data;
		this.valorPago=0d;
		this.situacao = SituacaoDespesa.PENDENTE;
	}
	
	public Conta() {
		valor=0d;
		valorPago=0d;
		valorPendente=0d;
		this.situacao = SituacaoDespesa.PENDENTE;
	}
	
	// ---------------metodos de negocio -------------------//
	public void paga (double valor,Date dataPagamento) {
		//conta tem apenas um valor, n�o cumulativo
		this.valorPago=valor;
		this.valorPendente-=valor;
		this.dataPagamento=dataPagamento;
		this.situacao=SituacaoDespesa.PAGA;
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
	
	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}
	
	

	public Boolean getIsPendente() {
		return situacao.getName().equals("PENDENTE");
	}

	public void setIsPendente(Boolean isPendente) {
		this.isPendente = isPendente;
	}
	
	public Boolean getIsVencida() {
		return DataUtils.isAnterior(this.dataVencimento ,new Date());
	}
	
	public void setIsVencida(Boolean isVencida) {
		this.isVencida = isVencida;
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
