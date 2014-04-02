package financeiro.util;

import java.util.Date;

import financeiro.model.bean.SituacaoDespesa;

public class Ocorrencia {
	
	public Ocorrencia(String descricao, Date date, SituacaoDespesa situacao) {
		super();
		this.descricao = descricao;
		this.date = date;
		this.situacao = situacao;
	}

	private String descricao;
	
	private Date date;
	
	private SituacaoDespesa situacao;

	public SituacaoDespesa getSituacao() {
		return situacao;
	}

	@Override
	public String toString() {
		return "Ocorrencia [descricao=" + descricao + ", situacao=" + situacao
				+ "]";
	}
	
	

}
