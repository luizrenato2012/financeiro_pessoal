package br.com.lrsantos.financeiro_pessoal.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;

@Named
@SessionScoped
public class OrcamentoNewBean implements Serializable{

	@Inject @SessaoQualifier
	private SessaoBean sessaoBean;
	
	public Orcamento getOrcamentoAtual() {
		return sessaoBean.getOrcamentoAtual();
	}
	
	public void logoff (){
		this.sessaoBean.logoff();
	}
	
	
}
