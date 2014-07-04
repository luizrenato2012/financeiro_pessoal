package financeiro.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.jboss.logging.Logger;

import financeiro.model.bean.Gasto;
import financeiro.model.bean.Pagamento;
import financeiro.model.service.GastoService;
import financeiro.model.service.OrcamentoService;

@Named
@ViewAccessScoped
public class PagamentoBean implements Serializable {
	
	private static final long serialVersionUID = 4766607768419940500L;
	
	private Pagamento pagamento;
	private List<Pagamento> pagamentos;
	private Gasto gasto;
	private final String COMPONENTE_MENSAGEM="frm_pagamento_gasto:msg_pagamento";
	private Logger log = Logger.getLogger(this.getClass());
	
	@Inject
	private SessaoBean sessaoBean;
			
	@EJB
	private GastoService gastoService;
	@EJB
	private OrcamentoService orcamentoService;
	
	public PagamentoBean() {
		log.info("instanciando pagamento bean");
	}

	@PostConstruct
	private void init() {
		log.info("Criando pagamentoBean Gasto id - " + gasto);
		gasto = new Gasto();
		pagamentos = new ArrayList<Pagamento>();
		pagamento = new Pagamento();
	}
	
	public void exclui(Integer idPagamento) {
		log.info("excluindo pagamento " + idPagamento );
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			gastoService.cancelaPagamento(this.gasto, idPagamento);
			context.addMessage(this.COMPONENTE_MENSAGEM, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"Pagamento", 
							"Pagamento cadastrado com sucesso!"));

		} catch (Exception e ) {
			e.printStackTrace();
			context.addMessage(this.COMPONENTE_MENSAGEM, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
							"Não foi possivel excluir pagamento!"));
		}

	}
	
	public void testa() {
		log.info("Teste de acionamento");
	}
	
	public void paga() {
		log.info("paga " + gasto);
		FacesContext context = FacesContext.getCurrentInstance();
		if (this.pagamento==null) {
			context.addMessage(this.COMPONENTE_MENSAGEM, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
							"Pagamento inválido"));
			return;
		}

		if (this.pagamento.getData()==null) {
			context.addMessage(this.COMPONENTE_MENSAGEM, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
							"Data inválida"));
			return;
		}

		if (this.pagamento.getValor()==0.d) {
			context.addMessage(this.COMPONENTE_MENSAGEM, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
							"Valor inválido"));
			return;
		}

		try{
			this.gasto.setOrcamento(sessaoBean.getOrcamentoAtual());
			this.gastoService.registraPagamento(gasto, pagamento);
			this.orcamentoService.efetuaPagamentoGasto(gasto, pagamento.getValor(), 
					pagamento.getData());
			// atualiza dados orçamento
			//atualizaContas();
			this.pagamentos = gasto.getPagamentos();
			this.pagamento = new Pagamento();
			context.addMessage(this.COMPONENTE_MENSAGEM, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"Pagamento cadastrado com sucesso", "Pagamento cadastrado com sucesso!"));
		} catch (Exception e ) {
			e.printStackTrace();
			context.addMessage(this.COMPONENTE_MENSAGEM, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"Erro ao gravar recebimento!", "Erro ao gravar recebimento!"));
		}
	}
	
	public String atualizaPagamentos() {
		//log.info("pagamentoBean - atualizaPagamentos");
		this.pagamentos = gastoService.carregaPagamentos(gasto.getId()).getPagamentos();
		return "pagamento";
	}
	
	public String voltaParaGastos() {
		return "gasto";
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public Gasto getGasto() {
		return gasto;
	}

	public void setGasto(Gasto gasto) {
		log.info("set gasto " + gasto);
		this.gasto = gasto;
	}

	public void setSessaoBean(SessaoBean sessaoBean) {
		this.sessaoBean = sessaoBean;
	}

}
