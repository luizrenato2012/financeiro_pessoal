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
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.com.lrsantos.financeiro_pessoal.model.bean.Conta;
import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;
import br.com.lrsantos.financeiro_pessoal.model.service.ContaService;
import br.com.lrsantos.financeiro_pessoal.model.service.OrcamentoService;

@Named
@ViewAccessScoped
public class ContaBean implements Serializable {
	private static final long serialVersionUID = -3940177813948818914L;

	private static final Logger log = Logger.getLogger(ContaBean.class);

	@EJB
	private ContaService contaService;

	@EJB
	private OrcamentoService orcamentoService;

	private List<Conta> contas;
	private Conta selecao;
	private Integer idExclusao;
	private Integer idPagamento;
	private Orcamento orcamentoAtual;
	private Conta conta;
	private Conta contaSelecionada;
	private Double totalGasto;

	@Inject @SessaoQualifier
	private SessaoBeanImpl sessaoBean;

	@PostConstruct
	private void init() {
		log.info("criando contaBean");
		conta = new Conta();
		try {
			orcamentoAtual = sessaoBean.getOrcamentoAtual();
			atualizaContas();
			contaSelecionada = new Conta();
		} catch (Exception e ) {
			e.printStackTrace();
		}

	}

	private void atualizaContas() {
		contas = contaService.listaPorOrcamento(orcamentoAtual.getId());
	}

	public void inclui() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (conta==null) {
			context.addMessage("frm_tab_conta:msg_conta", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
					"Conta inv�lida"));
			return;
		}

		if (conta.getDataVencimento()==null) {
			context.addMessage("frm_tab_conta:msg_conta", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
					"Data de vecnimento inv�lida"));
			return;
		}

		if (conta.getValor()==0d) {
			context.addMessage("frm_tab_conta:msg_conta", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
					"Valor inv�lido"));
			return;
		}

		try {
			log.info("inserindo conta " + conta );
			conta.setValorPendente(conta.getValor());
			orcamentoService.adicionaConta(conta, orcamentoAtual);
			atualizaContas();
			conta = new Conta();
			//forca atualizacao do orcamento p/ atualizar painel de resumo
			orcamentoAtual = sessaoBean.getOrcamentoAtual();
			context.addMessage("frm_tab_conta:msg_conta", new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"", "Conta cadastrada com sucesso"));
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage("frm_tab_conta:msg_conta", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro", 
					"Erro ao incluir conta"));
			return;
		}
	}

//	public void exibeTelaExclusao() {
//		log.info("exibe exclusao ");
//		if (contaSelecionada==null || contaSelecionada.getId()==0){
//			log.info("exibe exclusao  - conta nula");
//			RequestContext.getCurrentInstance().showMessageInDialog(
//					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exclus�o de conta", "Selecione uma conta "));
//		} else {
//			log.info("exibe exclusao  - abrindo tela");
//			Map<String,Object> params = new HashMap<String,Object>();
//			params.put("modal", true);
//			sessaoBean.setIdContaSelecionada(contaSelecionada.getId());
//			RequestContext.getCurrentInstance().openDialog("dialogs/confirma_exclusao_conta",params,null);
//		}
//	}

//	public void exibeTelaPagamento() {
//		log.info("exibe tela de pagamento");
///		if (contaSelecionada==null){
//			log.info("exibe exclusao  - conta nula");
//			RequestContext.getCurrentInstance().showMessageInDialog(
//	/				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pagamento de conta", 
//							"Selecione uma conta "));
//		} else {
//			log.info("exibe exclusao  - abrindo tela");
//			Map<String,Object> params = new HashMap<String,Object>();
//			params.put("modal", true);

//			RequestContext.getCurrentInstance().openDialog("dialogs/pagamento_conta",params,null);
//		}
//	}

//	public void fechaPagamento() {
//		log.info("fechando pagamento");
//		sessaoBean.setIdContaSelecionada(null);
//		RequestContext.getCurrentInstance().closeDialog("dialogs/pagamento_conta");
//	}

//	public void fechaExclusao() {
//		log.info("fechando exclusao");
//		sessaoBean.setIdContaSelecionada(null);
//		RequestContext.getCurrentInstance().closeDialog("dialogs/confirma_exclusao_conta");
//	}

	public void  atualizaContas(SelectEvent event) {
		log.info("Atualizando contas apos insercao");
		RequestContext.getCurrentInstance().update("tab_view:tbl_conta");
	}

	public void exclui() {
		try {
			log.info("Excluindo conta idExclusao: " + this.idExclusao);
			if (idExclusao==null || idExclusao==0) {
				log.info("id exclusao invalida");
				FacesContext.getCurrentInstance().addMessage("frm_tab_conta:msg_conta", 
						new FacesMessage(FacesMessage.SEVERITY_ERROR, 
								"Erro ao excluir conta", "Id da conta a excluir invalido"));
			} else {
				log.info("removendo conta ");
				orcamentoService.cancelaConta(idExclusao, orcamentoAtual);
				atualizaContas();
				this.orcamentoAtual = this.sessaoBean.getOrcamentoAtual();
			}
		} catch (Exception e ) {
			FacesContext.getCurrentInstance().addMessage("frm_tab_conta:msg_conta", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"Erro ao excluir conta", e.getMessage()));
		}
	}

	public void paga() {
		try{
			log.info("pagamento conta: " + contaSelecionada.getDescricao());
			if(contaSelecionada==null) {
				FacesContext.getCurrentInstance().addMessage("frm_tab_conta:msg_conta", 
						new FacesMessage(FacesMessage.SEVERITY_ERROR, 
								"Erro ao pagar conta", "Conta selecionada invalida"));
			} else {
				orcamentoService.pagaConta(contaSelecionada, orcamentoAtual);
				FacesContext.getCurrentInstance().addMessage("frm_tab_conta:msg_conta", 
						new FacesMessage(FacesMessage.SEVERITY_INFO, 
								"Pagamento", "Conta paga com sucesso"));
			} 
		}catch (Exception e ) { 
			FacesContext.getCurrentInstance().addMessage("frm_tab_conta:msg_conta", 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"Nao foi possivel pagar conta", e.getMessage()));
		}
	}
	
	public void exibeContaSelecionada() {
		log.debug("Conta selecionada: " + contaSelecionada);
	}

	public String voltaOrcamento() {
		return "orcamento_new";
	}

	public List<Conta> getContas() {
		return contas;
	}

	public Double getTotalConta() {
		double total = Double.valueOf(0);
		if (this.contas!=null && this.contas.size()>0) {
			for(Conta conta: contas) {
				total+=conta.getValor();
			//	log.info("situacao " + conta.getSituacao());
			}
		}
		return total;
	}

	public Double getTotalPago() {
		double totalPago=Double.valueOf(0);
		if (this.contas!=null && this.contas.size()>0) {
			for(Conta conta: contas) {
				totalPago+=conta.getValorPago();
			}
		}
		return totalPago;
	}

	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}

	public Conta getSelecao() {
		return selecao;
	}

	public void setSelecao(Conta selecao) {
		this.selecao = selecao;
	}

	public Integer getIdExclusao() {
		return idExclusao;
	}

	public void setIdExclusao(Integer idExclusao) {
		this.idExclusao = idExclusao;
	}

	public Integer getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Integer idPagamento) {
		this.idPagamento = idPagamento;
	}

	public Orcamento getOrcamentoAtual() {
		return orcamentoAtual;
	}

	public void setOrcamentoAtual(Orcamento orcamentoAtual) {
		this.orcamentoAtual = orcamentoAtual;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public void setSessaoBean(SessaoBeanImpl sessaoBean) {
		this.sessaoBean = sessaoBean;
	}

	public Conta getContaSelecionada() {
		return contaSelecionada;
	}

	public void setContaSelecionada(Conta contaSelecionada) {
		this.contaSelecionada = contaSelecionada;
	}

	public Double getTotalGasto() {
		return totalGasto;
	}

	public void setTotalGasto(Double totalGasto) {
		this.totalGasto = totalGasto;
	}
	

}
