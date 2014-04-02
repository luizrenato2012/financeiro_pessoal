package financeiro.util;

public enum TipoSituacao {
	
	ABERTO("A"),FECHADO("F");
	
	private String sigla;

	private TipoSituacao(String name) {
		sigla=name;
	}

	public String getSigla() {
		return sigla;
	}
	
	
	
}
