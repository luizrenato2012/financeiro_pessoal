package financeiro.model;

public enum SituacaoDespesa {
	PENDENTE("Pendente"),PAGO("Pago");
	
	private String name;
	
	private SituacaoDespesa(String name) {
		this.name=name;
	}
}
