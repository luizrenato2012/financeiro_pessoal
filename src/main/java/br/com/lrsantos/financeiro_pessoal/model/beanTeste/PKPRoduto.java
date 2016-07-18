package br.com.lrsantos.financeiro_pessoal.model.beanTeste;

import java.io.Serializable;


public class PKPRoduto implements Serializable {
	
	private static final long serialVersionUID = -8316166056378203751L;
	
	private Integer idProduto;
	
	private Integer idLoja;
	

	public PKPRoduto() {
		super();
	}


	public PKPRoduto(Integer idLoja, Integer idProduto) {
		super();
		this.idLoja = idLoja;
		this.idProduto = idProduto;
	}


	public PKPRoduto(Integer idLoja) {
		super();
		this.idLoja = idLoja;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idLoja == null) ? 0 : idLoja.hashCode());
		result = prime * result
				+ ((idProduto == null) ? 0 : idProduto.hashCode());
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
		PKPRoduto other = (PKPRoduto) obj;
		if (idLoja == null) {
			if (other.idLoja != null)
				return false;
		} else if (!idLoja.equals(other.idLoja))
			return false;
		if (idProduto == null) {
			if (other.idProduto != null)
				return false;
		} else if (!idProduto.equals(other.idProduto))
			return false;
		return true;
	}


	public Integer getIdLoja() {
		return idLoja;
	}


	public void setIdLoja(Integer idLoja) {
		this.idLoja = idLoja;
	}


	public Integer getIdProduto() {
		return idProduto;
	}


	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}
	
	
}
