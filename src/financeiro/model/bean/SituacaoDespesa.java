package financeiro.model.bean;

public enum SituacaoDespesa {
	PENDENTE("PENDENTE"),PAGA("PAGA");
	
	private String name;
	
	private SituacaoDespesa(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
