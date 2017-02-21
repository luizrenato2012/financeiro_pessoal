package br.com.lrsantos.financeiro_pessoal.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.jboss.logging.Logger;

import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;
import br.com.lrsantos.financeiro_pessoal.model.service.OrcamentoService;

@Named
@ViewAccessScoped
public class OrcamentoBean implements Serializable{

	private static final long serialVersionUID = 8560642129797177973L;
	private static final  Logger log = Logger.getLogger(OrcamentoBean.class);

	@EJB
	private OrcamentoService service;

	private Orcamento orcamentoEdicao;

	private List<Orcamento> orcamentos;

	private Integer idExclusao;

	private Integer idEdicao;
	
	private Integer idAtivacao;
	
	@Inject @SessaoQualifier
	private SessaoBean sessaoBean;
	
	private static final String MENU_ORCAMENTO = "orcamento_new";
	private final String MSG_ORCAMENTO = ":frm_orcamento:msg_orcamento";
	

	@PostConstruct
	private void init() {
		orcamentoEdicao = new Orcamento();
		orcamentos = service.listaTodos(Orcamento.class);
	//	log.info("post construct");
	}

	public void inclui() {
		String mensagem="";
		Severity severity = null; 

		if (orcamentoEdicao.getDataInicial()==null) {
			mensagem = "Data inicial invalida";
			severity = FacesMessage.SEVERITY_ERROR;
		} else if (orcamentoEdicao.getDataFinal()==null) {
			mensagem = "Data final invaliida";
			severity = FacesMessage.SEVERITY_ERROR;
		} else {

			try {
				service.persiste(orcamentoEdicao);
				orcamentos = service.listaTodos(Orcamento.class);
				orcamentoEdicao = new Orcamento();
				
				severity = FacesMessage.SEVERITY_INFO;
				mensagem = "Orçamento incluido com sucesso!";
			} catch (Exception e) {
				e.printStackTrace();
				severity = FacesMessage.SEVERITY_ERROR;
				mensagem = "Erro ao incluir orcamento!";

			}
			FacesContext.getCurrentInstance().addMessage(MSG_ORCAMENTO ,  
					new FacesMessage(severity, "",	mensagem));
		}
	}

	public void exclui() {
		String mensagem = "";
		Severity severity = null;
		try {
			service.remove(idExclusao,Orcamento.class);
			orcamentos = service.listaTodos(Orcamento.class);
			mensagem = "Orçamento excluido com sucesso!";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (Exception e) {
			e.printStackTrace();
			mensagem = "Erro ao excluir!";
			severity = FacesMessage.SEVERITY_ERROR;
		}
		FacesContext.getCurrentInstance().addMessage(MSG_ORCAMENTO, 
				new FacesMessage(severity, "", mensagem));
	}

	/** abre detalhameneto */
	public String detalhaOrcamento() {
		sessaoBean.setIdOrcamentoAtual(idEdicao);
		return MENU_ORCAMENTO;
	}
	
	public String retornaMenuOrcamento() {
		sessaoBean.setIdOrcamentoAtual(null);	
		return "menu_orcamento";
	}
	
	public void ativaOrcamento() {
		try {
			this.service.ativaOrcamento(this.idAtivacao);
			this.orcamentos = service.listaTodos(Orcamento.class);
			this.sessaoBean.setOrcamentoAtivo();
		} catch (Exception e ) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(MSG_ORCAMENTO, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao ativar orcamento"));
		}
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

	public void setSessaoBean(SessaoBeanImpl sessaoBean) {
		this.sessaoBean = sessaoBean;
	}

	public Integer getIdAtivacao() {
		return idAtivacao;
	}

	public void setIdAtivacao(Integer idAtivacao) {
		this.idAtivacao = idAtivacao;
	}

}
