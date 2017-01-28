package br.com.lrsantos.financeiro_pessoal.controller;

import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;

public interface SessaoBean {
	public Orcamento getOrcamentoAtual() ;

	public void setIdOrcamentoAtual(Integer id) ;

	public Integer getIdContaSelecionada() ;

	public void setIdContaSelecionada(Integer idContaSelecionada) ;
	
	public double getValorTotalContas() ;
	
	public double getValorTotalGastos() ;
	
	public void logoff() ;
	
	public void registraUsuario(String usuario) ;

	public Orcamento getOrcamentoAtivo() ;

	public void setOrcamentoAtivo() ;

}
