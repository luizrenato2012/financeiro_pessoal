package financeiro.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import financeiro.model.bean.Orcamento;
import financeiro.model.service.OrcamentoService;

@Named
@SessionScoped
public class SessaoBean implements Serializable{

	private static final Logger log = Logger.getLogger(SessaoBean.class);
	@EJB
	private OrcamentoService orcamentoService;

	private Integer idContaSelecionada;

	private static final long serialVersionUID = -7868357574495386347L;
	private HttpSession sessao;

	@SuppressWarnings("unused")
	public SessaoBean() {
	//	log.info("Criando sessao");
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
		log.info("setando idOrcamentoAtual " + id);
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


}
