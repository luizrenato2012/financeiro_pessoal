package br.com.lrsantos.financeiro_pessoal.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.jboss.logging.Logger;

import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;
import br.com.lrsantos.financeiro_pessoal.model.bean.Recebimento;
import br.com.lrsantos.financeiro_pessoal.model.service.OrcamentoService;
import br.com.lrsantos.financeiro_pessoal.model.service.RecebimentoService;

@Named
@ViewAccessScoped
public class RecebimentoBean implements Serializable{

	private static final long serialVersionUID = 6929361999234674129L;

	private static Logger log = Logger.getLogger(RecebimentoBean.class);

	@EJB
	private OrcamentoService orcamentoService;

	@EJB
	private RecebimentoService recebimentoService;

	private Recebimento recebimento;
	private List<Recebimento> recebimentos;
	private Integer idExclusao;
	private Orcamento orcamentoAtual;

	@Inject @SessaoQualifier
	private SessaoBean sessaoBean;

	@PostConstruct
	private void init() {
		try{
			recebimento = new Recebimento();
			//recebimentos = recebimentoService.listaPorOrcamento(sessaoBean.getIdContaSelecionada());
			//orcamentoAtual = orcamentoService.encontra(sessaoBean.getIdContaSelecionada(), Orcamento.class);
			atualizaContas();
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}

	public void inclui() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (recebimento==null) {
			context.addMessage("frm_tab_recebimento:msg_recebimento", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
							"Recebimento inv�lido"));
			return;
		}

		if (recebimento.getData()==null) {
			context.addMessage("frm_tab_recebimento:msg_recebimento", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
							"Data inv�lida"));
			return;
		}

		if (recebimento.getValor()==0d) {
			context.addMessage("frm_tab_recebimento:msg_recebimento", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
							"Valor inv�lido"));
			return;
		}

		try{ 
			orcamentoService.recebe(recebimento, orcamentoAtual);
			recebimento = new Recebimento();
			// atualiza dados or�amento
			atualizaContas();
			context.addMessage("frm_tab_recebimento:msg_recebimento", 
					new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"", "Recebimento cadastrado com sucesso!"));
		} catch (Exception e ) {
			e.printStackTrace();
			context.addMessage("frm_tab_recebimento:msg_recebimento", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"", "Erro ao gravar recebimento!"));
		}


	}

	public void exclui() {
		try{
			FacesContext context = FacesContext.getCurrentInstance();
			if (idExclusao==null || idExclusao==0) {
				log.info("id exclusao invalida");
				FacesContext.getCurrentInstance().addMessage("frm_tab_recebimento:msg_recebimento", 
						new FacesMessage(FacesMessage.SEVERITY_ERROR, 
								"Erro ao excluir conta", "Id do recebimento a excluir invalido"));
			} else {
				orcamentoService.cancelaRecebimento(idExclusao, this.orcamentoAtual);
				atualizaContas();
			}
		} catch(Exception e ) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("frm_tab_recebimento:msg_recebimento", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"Erro ao excluir conta", "Id do recebimento a excluir invalido"));
		}
	}

	public String voltaOrcamento () {
		return "orcamento_new";
	}


	public Recebimento getRecebimento() {
		return recebimento;
	}


	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}

	public List<Recebimento> getRecebimentos() {
		return recebimentos;
	}

	public void setRecebimentos(List<Recebimento> recebimentos) {
		this.recebimentos = recebimentos;
	}

	public Integer getIdExclusao() {
		return idExclusao;
	}

	private void atualizaContas() {
		try {
			orcamentoAtual = sessaoBean.getOrcamentoAtual();
			recebimentos = recebimentoService.listaPorOrcamento(orcamentoAtual.getId());
		} catch (Exception e ) {
			FacesContext.getCurrentInstance().addMessage("frm_tab_recebimento:msg_recebimento", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"", "Erro ao gravar recebimento!"));
		}
	}

	public void setIdExclusao(Integer idExclusao) {
		this.idExclusao = idExclusao;
	}

	public OrcamentoService getOrcamentoService() {
		return orcamentoService;
	}

	public void setOrcamentoService(OrcamentoService orcamentoService) {
		this.orcamentoService = orcamentoService;
	}

	public void setSessaoBean(SessaoBeanImpl sessaoBean) {
		this.sessaoBean = sessaoBean;
	}

	public Orcamento getOrcamentoAtual() {
		return orcamentoAtual;
	}

	public void setOrcamentoAtual(Orcamento orcamentoAtual) {
		this.orcamentoAtual = orcamentoAtual;
	}





}
