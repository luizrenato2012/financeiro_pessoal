package financeiro.controller;

public enum ConfiguracaoWeb {
	
	URI_LOGIN("login.html"),
	USUARIO_SESSAO("usuario"),
	USUARIO_SESSAO_MOB("usuario_mob"),
	ORCAMENTO_ATIVO("orcamento_ativo");

	private ConfiguracaoWeb(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;

	public String getDescricao() {
		return descricao;
	}
	
	
	

}
