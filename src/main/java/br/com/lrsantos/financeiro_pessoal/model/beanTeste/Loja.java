package br.com.lrsantos.financeiro_pessoal.model.beanTeste;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema="financ", name="loja")
@SequenceGenerator(name="SEQ_ID_LOJA",sequenceName="seq_id_loja" , schema="financ",allocationSize=1)
public class Loja implements Serializable{
	
	private static final long serialVersionUID = 6248993556995938863L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_ID_LOJA")
	private Integer id;
	
	private String sigla;
	
	public Loja() {
		super();
	}

	public Loja(String sigla) {
		super();
		this.sigla = sigla;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sigla == null) ? 0 : sigla.hashCode());
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
		Loja other = (Loja) obj;
		if (sigla == null) {
			if (other.sigla != null)
				return false;
		} else if (!sigla.equals(other.sigla))
			return false;
		return true;
	}
	

}
