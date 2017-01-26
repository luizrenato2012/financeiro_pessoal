package br.com.lrsantos.financeiro_pessoal.controller;

public enum ConfiguracaoWeb {
	
	URI_LOGIN("login.html"),
	USUARIO_SESSAO("usuario"),
	USUARIO_SESSAO_MOB("usuario_mob"),
	ORCAMENTO_ATIVO("orcamento_ativo"),
	ID_ORCAMENTO_ATIVO("id_orcamento_ativo"),  
	RESUMO_ORCAMENTO("resumo_orcamento");

	private ConfiguracaoWeb(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;

	public String getDescricao() {
		return descricao;
	}
	
	
	

}
