package financeiro.controller;

import java.io.Serializable;
import java.util.Date;

import financeiro.model.bean.Gasto;
import financeiro.model.bean.GastoVariavel;

public class GastoDTO implements Serializable {

	private static final long serialVersionUID = -6707562844954356071L;
	
	private String descricao;
	private double valor;
	private Date dataInicial;
	private Date dataFinal;
	private String tipo;
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
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
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Gasto getGasto(){
		Gasto gasto = this.isVariavel() ? new GastoVariavel(): new Gasto();
		gasto.setDataFinal(this.getDataFinal());
		gasto.setDataInicial(this.dataInicial);
		gasto.setDescricao(descricao);
		gasto.setValor(isVariavel() ? 0.0 : this.valor);
		return gasto;
	}
	
	private boolean isVariavel() {
		return this.tipo.equals("VARIAVEL");
	}
	
	
}
