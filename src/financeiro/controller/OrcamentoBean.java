package financeiro.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import financeiro.model.bean.Orcamento;
import financeiro.model.service.OrcamentoService;

@ManagedBean
@ViewScoped
public class OrcamentoBean implements Serializable{

	private static final long serialVersionUID = 8560642129797177973L;
	private static final  Logger log = Logger.getLogger(OrcamentoBean.class);

	@EJB
	private OrcamentoService service;

	private Orcamento orcamentoEdicao;

	private List<Orcamento> orcamentos;

	private Integer idExclusao;

	private Integer idEdicao;
	
	@Inject
	private SessaoBean sessaoBean;

	@PostConstruct
	private void init() {
		orcamentoEdicao = new Orcamento();
	//	log.info("orcamento edicao " + orcamentoEdicao);
		orcamentos = service.listaTodos(Orcamento.class);
		log.info("post construct");
	}

	public void inclui() {
		log.info("incluindo orcamento");
		FacesContext context = FacesContext.getCurrentInstance();
	//	log.info("Observacao " + orcamentoEdicao.getObservacao());

		if (orcamentoEdicao.getDataInicial()==null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
					"Data inicial inváliida"));
			return;
		}

		if (orcamentoEdicao.getDataFinal()==null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
					"Data final inváliida"));
			return;
		}

		try {
			service.persiste(orcamentoEdicao);
			log.info("persistindo orcamento");
			orcamentos = service.listaTodos(Orcamento.class);
			log.info("listando orcamento");
			orcamentoEdicao = new Orcamento();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", 
					"Orçamento incluído com sucesso!"));
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage("frm_orcamento", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro ao gravar orcamento", 
					"Erro ao gravar orçamento"));
		}
	}

	public void exclui() {
		FacesContext context = FacesContext.getCurrentInstance();
		log.info("orcamento id " + idExclusao);
		try {
			service.remove(idExclusao,Orcamento.class);
			orcamentos = service.listaTodos(Orcamento.class);
			context.addMessage(":frm_orcamento:msg_orcamento", new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Exclusao", "Orçamento excluido com sucesso!"));
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(":frm_orcamento:msg_orcamento", new FacesMessage(
					FacesMessage.SEVERITY_FATAL, "Erro", "Erro ao excluir"));
			return ;
		}
	}

	/** abre detalhameneto */
	public String abre() {
		sessaoBean.setIdOrcamentoAtual(idEdicao);
		return "orcamento_new";
	}
	
	public String retornaMenuOrcamento() {
		System.out.println("Retornando orcamento");
		sessaoBean.setIdOrcamentoAtual(null);	
		return "menu_orcamento";
	}


	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}

	public Orcamento getOrcamentoEdicao() {
		return orcamentoEdicao;
	}

	public void setOrcamentoEdicao(Orcamento orcamentoEdicao) {
		this.orcamentoEdicao = orcamentoEdicao;
	}

	public Integer getIdExclusao() {
		return idExclusao;
	}

	public void setIdExclusao(Integer idExclusao) {
		this.idExclusao = idExclusao;
	}

	public Integer getIdEdicao() {
		return idEdicao;
	}

	public void setIdEdicao(Integer idEdicao) {
		this.idEdicao = idEdicao;
	}

	public void setSessaoBean(SessaoBean sessaoBean) {
		this.sessaoBean = sessaoBean;
	}

	
	public void testeOrcamento() {
		System.out.println("OrcamentoBean testeOrcamento");
	}

}
