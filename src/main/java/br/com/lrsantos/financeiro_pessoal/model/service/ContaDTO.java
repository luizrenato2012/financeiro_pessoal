package br.com.lrsantos.financeiro_pessoal.model.service;

import java.util.Date;

public class ContaDTO {
	
	private Integer id;
	
	private String descricao;
	
	private Double valor;
	
	private Date vencimento;

	public ContaDTO(Integer id, String descricao, Double valor, Date vencimento) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
		this.vencimento = vencimento;
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

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}
	
	

}
