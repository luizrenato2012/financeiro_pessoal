package br.com.lrsantos.financeiro_pessoal.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;
import br.com.lrsantos.financeiro_pessoal.model.service.OrcamentoFacade;
import br.com.lrsantos.financeiro_pessoal.model.service.OrcamentoService;

@Named
@SessionScoped
@SessaoQualifier
public class SessaoBeanImpl implements SessaoBean, Serializable{

	private static final long serialVersionUID = -7868357574495386347L;
	
	private static final Logger log = Logger.getLogger(SessaoBeanImpl.class);
	@EJB
	private OrcamentoService orcamentoService;
	 
	@EJB
	private OrcamentoFacade orcamentoFacade;

	private Integer idContaSelecionada;
	
	private HttpSession sessao;
	
	@SuppressWarnings("unused")
	public SessaoBeanImpl() {
		//log.info(">>> Criando sessao");
		 sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true); 

	}

	@PostConstruct
	private void init() {
		//HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false); 
	}

	public Orcamento getOrcamentoAtual() {
		Integer idOrcamento = getIdOrcamentoAtual();
		//log.info(" getOrcamentoAtual id: " + idOrcamento);
		Orcamento orcamento = null;
		//if (idOrcamento!=null && idOrcamento!=0) {
		orcamento=orcamentoService.encontra(idOrcamento, Orcamento.class);
		//}
	//	log.info("orcamento: " + orcamento);
		return orcamento;
	}

	public void setIdOrcamentoAtual(Integer id) {
	//	log.info("setando idOrcamentoAtual " + id);
		sessao.setAttribute("idOrcamentoAtual", id);
	}

	private Integer getIdOrcamentoAtual() {
		Integer id = (Integer) sessao.getAttribute("idOrcamentoAtual");
	//	log.info("get id orcamento " + id);
		return id;
	}

	public Integer getIdContaSelecionada() {
		return (Integer) sessao.getAttribute("idContaSelecionada");
	}

	public void setIdContaSelecionada(Integer idContaSelecionada) {
		this.sessao.setAttribute("idContaSelecionada", idContaSelecionada);
	}
	
	public double getValorTotalContas() {
		return orcamentoService.getValorTotalContas(this.getIdOrcamentoAtual());
	}
	
	public double getValorTotalGastos() {
		return orcamentoService.getValorTotalGastos(this.getIdOrcamentoAtual());
	}
	
	public void logoff() {
		this.sessao.removeAttribute(ConfiguracaoWeb.USUARIO_SESSAO.getDescricao());
		this.sessao.invalidate();
	}
	
	public void registraUsuario(String usuario) {
		this.sessao.setAttribute(ConfiguracaoWeb.USUARIO_SESSAO.getDescricao(), usuario);
	}

	public Orcamento getOrcamentoAtivo() {
		return (Orcamento) this.sessao.getAttribute(ConfiguracaoWeb.ORCAMENTO_ATIVO.getDescricao());
	}

	public void setOrcamentoAtivo() {
		this.sessao.setAttribute(ConfiguracaoWeb.ORCAMENTO_ATIVO.getDescricao(),this.orcamentoService.getOrcamentoAtivo());
	}
	
	public void atualizaResumoOrcamento()  {
	    this.sessao.setAttribute(ConfiguracaoWeb.RESUMO_ORCAMENTO.getDescricao(), 
	    this.orcamentoFacade.getResumoOrcamento());
	}


}
