package financeiro.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;

import financeiro.model.bean.Conta;
import financeiro.model.bean.Orcamento;
import financeiro.model.service.ContaService;

@ManagedBean
@ViewScoped
public class ContaBean implements Serializable {
	private static final long serialVersionUID = -3940177813948818914L;

	private static final Logger log = Logger.getLogger(ContaBean.class);

	@EJB
	private ContaService contaService;

	private List<Conta> contas;
	private Conta selecao;
	private Integer idExclusao;
	private Integer idPagamento;
	private Orcamento orcamentoAtual;
	private Conta conta;

	@Inject
	private SessaoBean sessaoBean;

	@PostConstruct
	private void init() {
		conta = new Conta();
		try {
			orcamentoAtual = sessaoBean.getOrcamentoAtual();
			contas = contaService.listaPorOrcamento(orcamentoAtual.getId());
		} catch (Exception e ) {
			e.printStackTrace();
		}

	}

	public void inclui() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (conta==null) {
			context.addMessage("msg_conta", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
					"Conta inválida"));
			return;
		}

		if (conta.getDataVencimento()==null) {
			context.addMessage("msg_conta", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
					"Data de vecnimento inválida"));
			return;
		}

		if (conta.getValor()==0d) {
			context.addMessage("msg_conta", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
					"Valor inválido"));
			return;
		}

		try {
			conta.setOrcamento(orcamentoAtual);
			contaService.persiste(conta);
			contas = contaService.listaPorOrcamento(orcamentoAtual.getId());
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage("msg_conta", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro", 
					"Erro ao incluir conta"));
			return;
		}
	}

	public void exibeExclusao() {
		if (conta==null){
			RequestContext.getCurrentInstance().showMessageInDialog(
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exclusão de conta", "Selecione uma conta "));
		} else {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("modal", true);
			RequestContext.getCurrentInstance().openDialog("confirma_exclusao_conta",params,null);
		}
	}

	public void exclui() {
		log.info("Excluindo conta idExclusao: " + this.idExclusao);
	}

	public void paga() {

	}

	public List<Conta> getContas() {
		return contas;
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

	public void setSessaoBean(SessaoBean sessaoBean) {
		this.sessaoBean = sessaoBean;
	}



}
