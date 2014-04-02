package financeiro.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import financeiro.model.bean.Gasto;
import financeiro.model.bean.Pagamento;

@ManagedBean
@ViewScoped
public class PagamentoBean implements Serializable {
	
	private static final long serialVersionUID = 4766607768419940500L;
	
	private Pagamento pagamento;
	private List<Pagamento> pagamentos;
	private Gasto gasto;
	
	@PostConstruct
	private void init() {
		
	}
	
	public void inclui() {
		
	}
	
	public void exclui(Integer idPagamento) {
		
	}
	
	public void paga() {
		
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
		this.gasto = gasto;
	}
	
	
}
