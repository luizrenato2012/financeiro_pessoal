package financeiro.model.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="recebimento", schema="financ")
@SequenceGenerator(name="SEQ_ID_RECEBIMENTO", sequenceName="financ.seq_id_recebimento",
		allocationSize=1)
@NamedQueries({
	@NamedQuery(name="Recebimento.findByOrcamento", query="select r from Recebimento r inner join r.orcamento o "+
			" where o.id= :idOrcamento")
})
public class Recebimento implements Serializable {

	private static final long serialVersionUID = -2052768367354551920L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_ID_RECEBIMENTO")
	private Integer id;
	
	@Column(length=35)
	private String descricao;
	
	@Column(precision=2)
	private Double valor;
	
	@Temporal(TemporalType.DATE)
	private Date data;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_orcamento")
	private Orcamento orcamento;
	
	public Recebimento(String descricao, Double valor, Date data) {
		this.descricao = descricao;
		this.valor = valor;
		this.data = data;
	}

	public Recebimento() {
		id=null;
		data=null;
		valor=0d;
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
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	
	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Recebimento other = (Recebimento) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
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
		return true;
	}

	
	

}
