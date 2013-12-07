package financeiro.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orcamento {
	
	/** total de todos as conts do tipo gastos */
	private Double valorTotalGastos;
	
	private Double valorTotalContas;
	
	/** soma de todos as contas e gastos */
	private Double valorTotalDevido;
	
	/** total pendentes de contas e gastos*/
	private Double valorTotalPendente;
	
	/** total pago de todas contas e gastos */
	private Double valorTotalPago;
	
	
	private Double valorTotalRecebido;
	
	/** diferent�a entre todos o recebimentos e a soma do que foi pago e o que est� pago */
	private Double valorDisponivel;
	
	private List<Gasto> gastos;
	private List<Conta> contas;
	private List<Recebimento> recebimentos;
	
	/** */
	private double totalPendenteGastos;
	private double totalPendenteContas;

    public Orcamento() {
        this.valorTotalPendente = 0d;
        this.valorTotalPago = 0d;
        this.valorTotalGastos = 0d;
        this.valorTotalContas = 0d;
        this.valorTotalRecebido = 0d;
        this.valorDisponivel = 0d;
        
        gastos = new ArrayList<Gasto>();
        contas = new ArrayList<Conta>();
        recebimentos = new ArrayList<Recebimento>();
    }
        
    // -----------------  metodos de negocio	-------------	//
    
    /** acrescenta recebimento , aumenta valor disponivel */
    public void efetuaRecebimento (Recebimento recebimento) {
    	if (recebimento.getValor()==0) {
    		throw new FinanceiroException("Recebimento valor invalido : " + 
    				recebimento.getValor());
    	}
    	this.valorTotalRecebido+=recebimento.getValor();
    	this.valorDisponivel+=recebimento.getValor();
    	this.recebimentos.add(recebimento);
    	calculaTotalDisponivel();
    }
    
    /** aumenta valor devido,valor pendente */
    public void adicionaConta(Conta conta) {
    	if (conta.getValor()==0) {
    		throw new FinanceiroException("Gasto com valor invalido : " + 
    				conta.getValor());
    	}
    	this.valorTotalContas+=conta.getValor();
    	this.valorTotalDevido+=conta.getValor();
    	this.valorTotalPendente+=conta.getValor();
    	this.contas.add(conta);
    }
    
    /** aumenta valor devido,valor pendente */
    public void adicionaGasto(Gasto gasto) {
    	this.valorTotalGastos+=gasto.getValor();
    	this.valorTotalDevido+=gasto.getValor();
    	this.valorTotalPendente+=gasto.getValor();
    	this.gastos.add(gasto);
    }
    
    /** aumenta total pago, diminui total pendente */
    public void pagaConta(Conta conta , double valor, Date data) {
    	if (valor==0d) {
    		throw new FinanceiroException("Pagamento com valor invalido : " + 
    				conta.getValor());
    	}
    	conta.paga(valor, data);
    	this.valorTotalPago+=valor;
    	this.valorTotalPendente-=valor;
    	this.valorTotalPendente =  this.valorTotalPendente < 0 ? 0 : this.valorTotalPendente ;
    	calculaTotalDisponivel();
    }
    
    /** aumenta total pago, diminui total pendente */
    public void pagaGasto(Gasto gasto,double valor,Date dataPagamento) {
    	if (valor==0d) {
    		throw new FinanceiroException("Pagamento com valor invalido : " + valor);
    	}
    	gasto.paga(valor, dataPagamento);
    	this.valorTotalPendente-=valor;
    	this.valorTotalPendente =  this.valorTotalPendente < 0 ? 0 : this.valorTotalPendente ; 
    	this.valorTotalPago+=valor;
    	calculaTotalDisponivel();
    }
    
    /** 
     * resultado entre tudo que foi recebido e o que foi pago 
     */
    private void calculaTotalDisponivel() {
    	this.valorDisponivel = valorTotalRecebido-valorTotalPago;
    }
    
    /** resultado final do que vai sobrar */
    public Double getValorResultado() {
    	return this.valorDisponivel - this.valorTotalDevido;
    }
    
	// -------------------------------------------------//
	public Double getValorTotalPendente() {
		return valorTotalPendente;
	}
	public void setValorTotalPendente(Double valorTotalPendente) {
		this.valorTotalPendente = valorTotalPendente;
	}
	public Double getValorTotalPago() {
		return valorTotalPago;
	}
	public void setValorTotalPago(Double valorTotalPago) {
		this.valorTotalPago = valorTotalPago;
	}
	public Double getValorTotalGastos() {
		return valorTotalGastos;
	}
	public void setValorTotalGastos(Double valorTotalGastos) {
		this.valorTotalGastos = valorTotalGastos;
	}
	public Double getValorTotalContas() {
		return valorTotalContas;
	}
	public void setValorTotalContas(Double valorTotalContas) {
		this.valorTotalContas = valorTotalContas;
	}
	public Double getValorDisponivel() {
		return valorDisponivel;
	}
	public void setValorDisponivel(Double valorDisponivel) {
		this.valorDisponivel = valorDisponivel;
	}
	public Double getValorTotalRecebido() {
		return valorTotalRecebido;
	}
	public void setValorTotalRecebido(Double valorTotalRecebido) {
		this.valorTotalRecebido = valorTotalRecebido;
	}

	public double getTotalPendenteGastos() {
		return totalPendenteGastos;
	}

	public void setTotalPendenteGastos(double totalPendenteGastos) {
		this.totalPendenteGastos = totalPendenteGastos;
	}

	public double getTotalPendenteContas() {
		return totalPendenteContas;
	}

	public void setTotalPendenteContas(double totalPendenteContas) {
		this.totalPendenteContas = totalPendenteContas;
	}

	public Double getValorTotalDevido() {
		return valorTotalDevido;
	}

	
	
	
}