package br.com.lrsantos.financeiro_pessoal.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;

@Named
@RequestScoped
public class OrcamentoNewBean implements Serializable{
	
	private static Logger log = Logger.getLogger(OrcamentoNewBean.class);
	
	@PostConstruct
	private void psotInit(){
		//log.info(">>> criando OrcamentoNewBean");
	}

	@Inject @SessaoQualifier
	private SessaoBean sessaoBean;
	
	public Orcamento getOrcamentoAtual() {
		return sessaoBean.getOrcamentoAtual();
	}
	
	public String logoff (){
		this.sessaoBean.logoff();
		return "login.xhtml";
	}
	
	
}
