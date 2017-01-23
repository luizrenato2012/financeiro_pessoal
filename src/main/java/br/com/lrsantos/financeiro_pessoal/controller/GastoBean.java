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

import br.com.lrsantos.financeiro_pessoal.model.bean.Gasto;
import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;
import br.com.lrsantos.financeiro_pessoal.model.bean.Pagamento;
import br.com.lrsantos.financeiro_pessoal.model.service.GastoService;
import br.com.lrsantos.financeiro_pessoal.model.service.OrcamentoService;

@Named
@ViewAccessScoped
public class GastoBean implements Serializable {
	
	private static final long serialVersionUID = -8450332101863770172L;
	
	private GastoDTO gastoDTO;
	private Integer idExclusao;
	private List<Gasto> gastos;
	private Orcamento orcamentoAtual;
	private Logger log = Logger.getLogger(GastoBean.class);

	@EJB
	private OrcamentoService orcamentoService;
	
	@EJB
	private GastoService gastoService;
	
	@Inject @SessaoQualifier
	private SessaoBeanImpl sessaoBean;

	@PostConstruct
	private void init() {
		gastoDTO = new GastoDTO();
		try{
			orcamentoAtual = sessaoBean.getOrcamentoAtual();
			atualizaGastos();
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}
	
	private void atualizaGastos() {
		gastos = gastoService.listaPorOrcamento(sessaoBean.getOrcamentoAtual().getId());
	}

	public void inclui() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (gastoDTO==null) {
			context.addMessage("frm_tab_gasto:msg_gasto", new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Erro", "Gasto inv�lido"));
			return;
		}

		if (gastoDTO.getDataInicial()==null) {
			context.addMessage("frm_tab_gasto:msg_gasto", new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Erro",	"Data inicial inv�lida"));
			return;
		}

		if (gastoDTO.getDataFinal()== null) {
			context.addMessage("frm_tab_gasto:msg_gosta", new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Erro", "Data final inv�lida"));
			return;
		}

		try {
		//	log.info("inserindo gssto " + gasto);
			
			orcamentoService.adicionaGasto(gastoDTO.getGasto(), orcamentoAtual);
			atualizaGastos();
			//TODO verificar a criacao do Gasto quando for variavel
			gastoDTO = new GastoDTO();
			//forca atualizacao do orcamento p/ atualizar painel de resumo
			orcamentoAtual = sessaoBean.getOrcamentoAtual();
			context.addMessage("frm_tab_gasto:msg_gasto", new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"", "Gasto cadastrado com sucesso!"));
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage("frm_tab_gasto:msg_gasto", new FacesMessage(FacesMessage.SEVERITY_FATAL, 
					"Erro", "Erro ao incluir gasto"));
			return;
		}

	}
	
	public void exclui() {
		FacesContext context = FacesContext.getCurrentInstance();
		Severity severity = null;
		String mensagem="";
		if (idExclusao==null || idExclusao==0) {
			log.info("id exclusao invalida");
			severity = FacesMessage.SEVERITY_ERROR;
			mensagem="Id da conta a excluir invalido";
		} else {
			try {
			//	log.info("removendo gasto ");
				orcamentoService.cancelaGasto(idExclusao, orcamentoAtual);
				atualizaGastos();
				severity = FacesMessage.SEVERITY_INFO;
				mensagem="Gasto exclu�do com sucesso!";
			} catch (Exception e ) {
				severity = FacesMessage.SEVERITY_ERROR;
				mensagem="Erro ao excluir gasto";
			}
		}
		context.addMessage("frm_tab_gasto:msg_gasto", new FacesMessage(severity, 
				"Exclus�o", mensagem));
	}
	
	public String voltaOrcamento() {
		return "orcamento_new";
	}

	public Integer getIdExclusao() {
		return idExclusao;
	}

	public void setIdExclusao(Integer idExclusao) {
		this.idExclusao = idExclusao;
	}

	public List<Gasto> getGastos() {
		return gastos;
	}

	public void setGastos(List<Gasto> gastos) {
		this.gastos = gastos;
	}

	public Orcamento getOrcamentoAtual() {
		return orcamentoAtual;
	}

	public void setOrcamentoAtual(Orcamento orcamentoAtual) {
		this.orcamentoAtual = orcamentoAtual;
	}

	public Double getTotalGasto() {
		Double totalGasto=0d;
		if (gastos!=null && gastos.size()!=0){
			for(Gasto gasto: gastos){
				totalGasto+=gasto.getValor();
			}
		}
		return totalGasto;
	}

	public Double getTotalPago() {
		double totalPago=0d;
		List<Pagamento> pagamentos=null;
		if (gastos!=null && gastos.size()!=0) {
			for(Gasto gasto: gastos){
				pagamentos = gastoService.listaPagamento(gasto.getId());
				if (pagamentos!=null && pagamentos.size()!=0){
					for(Pagamento pagamento:pagamentos) {
						totalPago+=pagamento.getValor();
					}
				}
			}
		}
		return totalPago;
	}
	
	public Double getTotalPendente() {
		double totalPendente=0d;
		if (gastos!=null && gastos.size()!=0) {
			for(Gasto gasto: gastos){
				totalPendente+= gasto.getValorPendente();
			}
		}
		return totalPendente;
	}

	public GastoDTO getGastoDTO() {
		return gastoDTO;
	}

	public void setGastoDTO(GastoDTO gastoDTO) {
		this.gastoDTO = gastoDTO;
	}
	
	




}
