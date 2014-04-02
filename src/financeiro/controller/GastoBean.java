package financeiro.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import financeiro.model.bean.Gasto;
import financeiro.model.bean.Orcamento;
import financeiro.model.bean.Pagamento;
import financeiro.model.service.GastoService;
import financeiro.model.service.OrcamentoService;

@ManagedBean
@ViewScoped
public class GastoBean implements Serializable {
	
	private static final long serialVersionUID = -8450332101863770172L;
	
	private Gasto gasto;
	private Integer idExclusao;
	private Gasto gastoSelecionado;
	private List<Gasto> gastos;
	private Orcamento orcamentoAtual;
	private Logger log = Logger.getLogger(GastoBean.class);

	@EJB
	private OrcamentoService orcamentoService;
	
	@EJB
	private GastoService gastoService;
	
	@Inject
	private SessaoBean sessaoBean;

	@PostConstruct
	private void init() {
		gasto = new Gasto();
		gastoSelecionado = new Gasto();
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
		if (gasto==null) {
			context.addMessage("frm_tab_gasto:msg_gasto", new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Erro", "Gasto inválido"));
			return;
		}

		if (gasto.getDataInicial()==null) {
			context.addMessage("frm_tab_gasto:msg_gasto", new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Erro",	"Data inicial inválida"));
			return;
		}

		if (gasto.getDataFinal()== null) {
			context.addMessage("frm_tab_gasto:msg_gosta", new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Erro", "Data final inválida"));
			return;
		}

		try {
			log.info("inserindo gssto " + gasto);
			orcamentoService.adicionaGasto(gasto, orcamentoAtual);
			atualizaGastos();
			gasto = new Gasto();
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
				log.info("removendo gasto ");
				orcamentoService.cancelaGasto(idExclusao, orcamentoAtual);
				atualizaGastos();
				severity = FacesMessage.SEVERITY_INFO;
				mensagem="Gasto excluído com sucesso!";
			} catch (Exception e ) {
				severity = FacesMessage.SEVERITY_ERROR;
				mensagem="Erro ao excluir gasto";
			}
		}
		context.addMessage("frm_tab_gasto:msg_gasto", new FacesMessage(severity, 
				"Exclusão", mensagem));
	}
	
	public void paga() {
		
	}

	public String voltaOrcamento() {
		return "orcamento_new";
	}

	public Gasto getGasto() {
		return gasto;
	}

	public void setGasto(Gasto gasto) {
		this.gasto = gasto;
	}

	public Integer getIdExclusao() {
		return idExclusao;
	}

	public void setIdExclusao(Integer idExclusao) {
		this.idExclusao = idExclusao;
	}

	public Gasto getGastoSelecionado() {
		return gastoSelecionado;
	}

	public void setGastoSelecionado(Gasto gastoSelecionado) {
		this.gastoSelecionado = gastoSelecionado;
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




}
