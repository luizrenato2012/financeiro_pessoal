package br.com.lrsantos.financeiro_pessoal.model.service;

public class OrcamentoDTO {
	
	private int id;
	
	private String descricao;
	
	private String dataInicial;
	
	private String dataFinal;
	
	private Double valorDisponivel;
	
	private Double valorTotalPendente;
	
	private boolean ativo;
	

	public OrcamentoDTO(int id, String descricao, String dataInicial, String dataFinal, Double valorDisponivel,
			Double valorTotalPendente, boolean ativo) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.valorDisponivel = valorDisponivel;
		this.valorTotalPendente = valorTotalPendente;
		this.ativo = ativo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Double getValorDisponivel() {
		return valorDisponivel;
	}

	public void setValorDisponivel(Double valorDisponivel) {
		this.valorDisponivel = valorDisponivel;
	}

	public Double getValorTotalPendente() {
		return valorTotalPendente;
	}

	public void setValorTotalPendente(Double valorTotalPendente) {
		this.valorTotalPendente = valorTotalPendente;
	}
	
	
	

}
