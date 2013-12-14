/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package financeiro.model.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Classe que representa um tipo de conta onde sao feito vários pagamento
 * p. ex refeição, passagens ,etc<br>
 * sao despesas que podenm continuar a receber pagamentos mesmo sem ter valores pendentes
 * @author Luiz Renato
 *
 */
@Entity
public class Gasto extends Conta {
	
	/** valor do gasto pode ser diferente do valor pago quando o pago ultrapassa o previsto*/
	@Column(name="data_inicial")
	@Temporal(TemporalType.DATE)
	private Date dataInicial;
	
	@Column(name="data_final")
	@Temporal(TemporalType.DATE)
	private Date dataFinal;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="gasto")
	private List<Pagamento> pagamentos;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_orcamento")
	private Orcamento orcamento;
	
	public Gasto() {
		pagamentos = new ArrayList<Pagamento>();
		this.setId(null);
	}
	
	public void paga(Date data,double valor,String observacao) {
		Pagamento pagamento = new Pagamento();
		pagamento.setData(data);
		pagamento.setObservacao(observacao);
		pagamento.setValor(valor);
		
		this.valorPendente-= valor;
		this.valorPago+=valor;
		if (this.valorPendente==0d) {
			this.situacao=SituacaoDespesa.PAGA;
		}
	}
	
	public void cancelaPagamento(Pagamento pagamento) {
		this.valorPago-=pagamento.getValor();
		this.valorPendente+=pagamento.getValor();
		this.pagamentos.remove(pagamento);
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
		this.valorPendente = valor;
	}

	public SituacaoDespesa getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoDespesa situacao) {
		this.situacao = situacao;
	}
	
	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	@Override
	public String toString() {
		return "Gasto [descricao=" + descricao + ", valor=" + valor
				+ ", valorPago=" + valorPago + ", valorPendente="
				+ valorPendente + "]";
	}
	
	
}
