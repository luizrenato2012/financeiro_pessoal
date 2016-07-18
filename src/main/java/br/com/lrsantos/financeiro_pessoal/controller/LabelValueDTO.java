package br.com.lrsantos.financeiro_pessoal.controller;

import java.io.Serializable;

import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;

public class LabelValueDTO implements Serializable {
	
	private static final long serialVersionUID = 5316145858733422724L;
	
	private String value;
	private String label;
	
	public LabelValueDTO(Integer id, String descricao) {
		super();
		this.value = id+"";
		this.label = descricao;
	}
	
	public LabelValueDTO(Orcamento orcamento) {
		this.value=orcamento.getId()+"";
		this.label = orcamento.getDescricao();
	}


	public LabelValueDTO(String label, String value) {
		super();
		this.value = value;
		this.label = label;
	}

	public String getDescricao() {
		return label;
	}
	
	public void setValues(Orcamento orcamento) {
		this.value = orcamento.getId()+"";
		this.label = orcamento.getDescricao();
	}

	public String getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}
	
	
}
