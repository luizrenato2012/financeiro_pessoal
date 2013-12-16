package financeiro.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import financeiro.model.bean.Orcamento;
import financeiro.model.service.OrcamentoService;

@Named
@ViewScoped
public class OrcamentoBean {
	
	@EJB
	private OrcamentoService service;
	
	private Orcamento selecao;
	
	private List<Orcamento> orcamentos;
	
	private Date dataInicial;
	private Date dataFinal;
	
	public void inclui() {
		
	}
	
	public void exclui() {
		
	}
	
	public String abre() {
		return null;
	}

	public Orcamento getSelecao() {
		return selecao;
	}

	public void setSelecao(Orcamento selecao) {
		this.selecao = selecao;
	}

	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	

}
