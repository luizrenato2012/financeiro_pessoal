package financeiro.model.bean;

public enum SituacaoDespesa {
	PENDENTE("Pendente"),PAGA("Paga");
	
	private String name;
	
	private SituacaoDespesa(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}
	
}
