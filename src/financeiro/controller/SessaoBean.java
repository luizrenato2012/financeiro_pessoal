package financeiro.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import financeiro.model.bean.Orcamento;
import financeiro.model.service.OrcamentoService;

@ManagedBean
@ViewScoped
public class SessaoBean implements Serializable{
	
	private static final Logger log = Logger.getLogger(SessaoBean.class);
	@EJB
	private OrcamentoService orcamentoService;
	
	private static final long serialVersionUID = -7868357574495386347L;
	
	public SessaoBean() {
		//log.info(">>> Criando sessaoUtil ");
	}
	
	public Orcamento getOrcamentoAtual() {
		Integer idOrcamento = getIdOrcamentoAtual();
		//log.info(" getOrcamentoAtual id: " + idOrcamento);
		Orcamento orcamento = null;
		if (idOrcamento!=null && idOrcamento!=0) {
			orcamento=orcamentoService.encontra(idOrcamento, Orcamento.class);
		}
		//log.info("orcamento: " + orcamento);
		return orcamento;
	}
	
	public void setIdOrcamentoAtual(Integer id) {
		//log.info("setando idOrcamentoAtual " + id);
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true); 
		sessao.setAttribute("idOrcamentoAtual", id);
	}
	
	private Integer getIdOrcamentoAtual() {
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true); 
		Integer id = (Integer) sessao.getAttribute("idOrcamentoAtual");
		//log.info("get id orcamento " + id);
		return id;
	}

}
