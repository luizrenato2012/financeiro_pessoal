package br.com.lrsantos.financeiro_pessoal.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name="orcamento", schema="financ")
@SequenceGenerator(name="SEQ_ID_ORCAMENTO",sequenceName="financ.seq_id_orcamento",
	allocationSize=1)
@NamedQueries({
	@NamedQuery(name="Orcamento.loadContas",query="select o from Orcamento o join fetch o.contas where o.id= :id"),
	@NamedQuery(name="Orcamento.loadGastos",query="select o from Orcamento o join fetch o.gastos where o.id= :id"),
	@NamedQuery(name="Orcamento.findOrcamentoAtivo",query="select o from Orcamento o join fetch o.gastos where o.ativo = true")
})
@SqlResultSetMapping(name="scalar",columns={ @ColumnResult(name="id")})
public class Orcamento implements Serializable {
	
	private static final long serialVersionUID = 4845673316790597578L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_ID_ORCAMENTO")
	private Integer id;

	/** total de todos as conts do tipo gastos */
	@Transient
	private Double valorTotalGastos=0d;
	
	@Transient
	private Double valorTotalContas=0d;
	
	/** soma de todos as contas e gastos */
	@Column(name="valor_total_devido", precision=2)
	private Double valorTotalDevido=0d;
	
	/** total pendentes de contas e gastos*/
	@Column(name="valor_total_pendente", precision=2)
	private Double valorTotalPendente=0d;
	
	/** total pago de todas contas e gastos */
	@Column(name="valor_total_pago", precision=2)
	private Double valorTotalPago=0d;
	
	@Column(name="valor_total_recebido", precision=2)
	private Double valorTotalRecebido=0d;
	
	/** diferent�a entre todos o recebimentos e a soma do que foi pago e o que est� pago */
	@Column(name="valor_disponivel", precision=2)
	private Double valorDisponivel=0d;
	
	@Column(name="data_inicial")
	@Temporal(TemporalType.DATE)
	private Date dataInicial;
	
	@Column(name="data_final")
	@Temporal(TemporalType.DATE)
	private Date dataFinal;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY, mappedBy="orcamento")
	private List<Gasto> gastos;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="orcamento")
	private List<Conta> contas;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER,mappedBy="orcamento")
	private List<Recebimento> recebimentos;
	
	@Column(length=100)
	private String observacao;
	
	@Column(length=50)
	private String descricao;
	
	@Column
	private boolean ativo;
	
	/** */
	//private double totalPendenteGastos;
	//private double totalPendenteContas;

    public Orcamento() {
        this.valorTotalPendente = 0d;
        this.valorTotalPago = 0d;
        this.valorTotalGastos = 0d;
        this.valorTotalContas = 0d;
        this.valorTotalRecebido = 0d;
        this.valorDisponivel = 0d;
        this.ativo = false;
        
        gastos = new ArrayList<Gasto>();
        contas = new ArrayList<Conta>();
        recebimentos = new ArrayList<Recebimento>();
    }
    
        
    public Orcamento(Date dataInicial, Date dataFinal) {
		this();
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
	}



	// -----------------  metodos de negocio	-------------	//
    
    public Date getDataInicial() {
		return dataInicial;
	}



	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}



	public Date getDataFinal() {
		return dataFinal;
	}



	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}


	public String getObservacao() {
		return observacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	/** acrescenta recebimento , aumenta valor disponivel */
    public void efetuaRecebimento (Recebimento recebimento) {
    	if (recebimento.getValor()==0) {
    		throw new FinanceiroException("Recebimento valor invalido : " + 
    				recebimento.getValor());
    	}
    	this.valorTotalRecebido+=recebimento.getValor();
    	this.valorDisponivel+=recebimento.getValor();
    //	this.recebimentos.add(recebimento);
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
    //	this.contas.add(conta);
    }
    
    /** aumenta valor devido,valor pendente */
    public void adicionaGasto(Gasto gasto) {
    	if (gasto instanceof GastoVariavel) {
    		this.valorTotalGastos+=gasto.getValor();
    	} else if (gasto instanceof Gasto) {
    		this.valorTotalGastos+=gasto.getValor();
    		this.valorTotalDevido+=gasto.getValor();
    		this.valorTotalPendente+=gasto.getValor();
    	}
    }
    
    /** aumenta total pago, diminui total pendente */
    public void pagaConta(Conta conta , double valor, Date data) {
    	if (valor==0d) {
    		throw new FinanceiroException("Pagamento com valor invalido : " + valor);
    	}
    	conta.paga(valor, data);
    	this.valorTotalPago+=valor;
    	this.valorTotalPendente =  this.valorTotalPendente == 0d ? 0 : this.valorTotalPendente-valor;
    	//this.valorTotalDevido =  this.valorTotalDevido== 0d ? 0 : this.valorTotalDevido-valor;
    	calculaTotalDisponivel();
    }
    
    /** aumenta total pago, diminui total pendente */
    public void pagaGasto(Gasto gasto,double valor) {
    	if (valor==0d) {
    		throw new FinanceiroException("Pagamento com valor invalido : " + valor);
    	}
    //	gasto.paga(valor, dataPagamento); 
    	if (gasto instanceof GastoVariavel==false) {
    		this.valorTotalPendente-= valor;
    		this.valorTotalPendente =  this.valorTotalPendente < 0 ? 0 : this.valorTotalPendente ; 
    	}
    	this.valorTotalPago+=valor;
    	calculaTotalDisponivel();
    }
    
    public void cancelaConta(Conta conta) {
    	this.valorTotalDevido-= conta.getValor();
    	
    	if (conta.getValorPago()> 0 ) {
    		this.valorTotalPago-=conta.getValorPago();
    	}
    	
    	if (conta.getValorPendente() > 0 ) {
    		this.valorTotalPendente-=conta.getValor();
    	}
    	calculaTotalDisponivel();
    }
    
    /** deve-se cancelar os pagamentos feitos */
    public void cancelaGasto(Gasto gasto) {
    	if (gasto instanceof GastoVariavel ==false) {
    		this.valorTotalDevido-=gasto.getValor();
    	}
    	
    	if (gasto.getValorPago() > 0) {
    		this.valorTotalPago-=gasto.getValorPago();
    	}
    	
    	if ( gasto instanceof GastoVariavel ==false && gasto.getValorPendente()> 0 ) {
    		this.valorTotalPendente-=gasto.getValorPendente();
    	}
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
    	return this.valorDisponivel - this.valorTotalPendente;
    }
    
    public void recebe (Recebimento recebimento) {
    	//this.recebimentos.add(recebimento);
    	this.valorTotalRecebido+= recebimento.getValor();
    	calculaTotalDisponivel();
    //	this.recebimentos.add(recebimento);
    	
    }
    
    public void  cancelaPagamento(double valorPagamento) {
    	this.valorTotalPago-=valorPagamento;
    	this.valorTotalPendente+=valorPagamento;
    	calculaTotalDisponivel();
    }
    
    public void cancelaRecebimento(double valor) {
    	this.valorTotalRecebido-=valor;
    	calculaTotalDisponivel();
    }
    
	// -------------------------------------------------//
    
    public Double getValorTotalPendente() {
		return valorTotalPendente;
	}
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Gasto> getGastos() {
		return gastos;
	}

	public List<Conta> getContas() {
		return contas;
	}

	public List<Recebimento> getRecebimentos() {
		return recebimentos;
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

	public Double getValorTotalDevido() {
		return valorTotalDevido;
	}
	
	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean getAtivo() {
		return this.ativo;
	}


	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}


	@Override
	public String toString() {
		return "Orcamento [\nvalorTotalDevido=" + valorTotalDevido
				+ ", \nvalorTotalPendente=" + valorTotalPendente
				+ ", \nvalorTotalPago=" + valorTotalPago
				+ ", \nvalorTotalRecebido=" + valorTotalRecebido
				+ ", \nvalorDisponivel=" + valorDisponivel
				+ ", \ngetValorResultado()=" + getValorResultado() + "]";
	}
	
	

	public void setGastos(List<Gasto> gastos) {
		this.gastos = gastos;
	}


	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}


	public static void main(String[] args) {
		Orcamento orcamento = new Orcamento();
		
		Recebimento recebimento = new Recebimento("Descricao "  , 1000d , new Date());
		
		orcamento.recebe(recebimento);
		
		Conta contaCdc = new Conta("CDC" , 250,new Date());
		orcamento.adicionaConta(contaCdc);
		
		orcamento.pagaConta(contaCdc, 250, new Date());
		
		Gasto gastoPassagens = new Gasto();
		gastoPassagens.setDescricao("Passagem");
		gastoPassagens.setValor(50);
		gastoPassagens.setDataInicial(new Date());
		gastoPassagens.setDataFinal(new Date());
		
		orcamento.adicionaGasto(gastoPassagens);
	//	orcamento.pagaGasto(gastoPassagens, 15, new Date());
		
		Gasto gastoAlmoco = new Gasto();
		gastoAlmoco.setDescricao("Almoco");
		gastoAlmoco.setValor(100);
		gastoAlmoco.setDataFinal(new Date());
		gastoAlmoco.setDataInicial(new Date());
		
		orcamento.adicionaGasto(gastoAlmoco);
	//	orcamento.pagaGasto(gastoAlmoco, 50, new Date());
		System.out.println(orcamento);
		System.out.println("\n/---------Cancelando pagamento conta ---------/\n");
		
		
		orcamento.cancelaPagamento(15);
		System.out.println(orcamento);
		gastoPassagens.cancelaPagamento(new Pagamento(new Date(),15d));
		System.out.println(gastoPassagens);
		
	}
	
}
