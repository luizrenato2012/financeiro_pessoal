package br.com.lrsantos.financeiro_pessoal.model.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jboss.logging.Logger;

import br.com.lrsantos.financeiro_pessoal.controller.LabelValueDTO;
import br.com.lrsantos.financeiro_pessoal.model.bean.Conta;
import br.com.lrsantos.financeiro_pessoal.model.bean.FinanceiroException;
import br.com.lrsantos.financeiro_pessoal.model.bean.Gasto;
import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;
import br.com.lrsantos.financeiro_pessoal.model.bean.Recebimento;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Stateless
public class OrcamentoService extends ServiceGeneric<Orcamento, Integer> {

	@EJB
	private RecebimentoService recebimentoService;

	@EJB
	private ContaService contaService;

	@EJB
	private GastoService gastoService;

	private Logger log = Logger.getLogger(this.getClass());
	
	private NumberFormat numFormat;
	private DateFormat dtFormat;
	
	@PostConstruct
	private void configFormat() {
		numFormat = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
		numFormat.setMinimumFractionDigits(2);
		dtFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	public void recebe(Recebimento recebimento, Orcamento orcamento) {
		try {
			recebimento.setOrcamento(orcamento);
			orcamento.recebe(recebimento);
			this.recebimentoService.persiste(recebimento);
			this.atualiza(orcamento);
		} catch (Exception e ) {
			throw new FinanceiroException(e);
		}
	}

	public void adicionaConta(Conta conta, Orcamento orcamento) {
		try {
			conta.setOrcamento(orcamento);
			orcamento.adicionaConta(conta);
			contaService.persiste(conta);
			this.atualiza(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}

	/** 
	 * necessario carregamento dos gastos do or�amento 
	 * em virtudo do cascade.all e pelo fato do relacionamento ser bidirecional
	 * sem o carregamento, s� o gastoService.remove(gasto) nao apaga o gasto
	 */
	public void cancelaConta(Integer idConta, Orcamento orcamento) {
		try {
			Conta conta = contaService.encontra(idConta, Conta.class);
			orcamento = this.carregaContas(orcamento.getId());

			if (orcamento.getContas()!=null && orcamento.getContas().size() > 0 ) {
				orcamento.getContas().remove(conta);
			} else {
				throw new FinanceiroException("Or�amento " + orcamento.getId() + 
						" nao possui contas relacionadas");
			}

			orcamento.cancelaConta(conta);
			contaService.remove(conta);
			this.atualiza(orcamento);
			log.info(">>> Cancelada conta " + idConta);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}

	private Orcamento carregaContas(Integer id) {
		Query query = this.entityManager.createNamedQuery("Orcamento.loadContas");
		query.setParameter("id", id);
		return (Orcamento) query.getSingleResult();
	}

	/** 
	 * necessario carregamento dos gastos do or�amento 
	 * em virtudo do cascade.all e pelo fato do relacionamento ser bidirecional
	 * sem o carregamento, s� o gastoService.remove(gasto) nao apaga o gasto
	 */
	public void cancelaGasto(Integer idGasto, Orcamento orcamento) {
		try {
			Gasto gasto = gastoService.encontra(idGasto, Gasto.class);
			orcamento.cancelaGasto(gasto);
			//alterada forma de exclusao
			this.removeGasto(idGasto);
			this.atualiza(orcamento);
			log.info(">>> Cancelado gasto " + idGasto);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}

	/**
	 * bulk update delete - exclusao de muitos registros @OneToMany(Cascade.REMOVE)
	 * nao funciona
	 * @param idGasto
	 */
	private void removeGasto(Integer idGasto){
		String qryUpdate = "update financ.pagamento set id_gasto=null where id_gasto=?1";
		Query query = this.entityManager.createNativeQuery(qryUpdate);
		query.setParameter(1, idGasto);
		query.executeUpdate();
		gastoService.removeBydId("Gasto.deleteById", idGasto);
	}


	public void adicionaGasto(Gasto gasto, Orcamento orcamento) {
		try {
			orcamento.adicionaGasto(gasto);
			gasto.setOrcamento(orcamento);
			gastoService.persiste(gasto);
			this.atualiza(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}

	public void pagaConta(Conta conta , Orcamento orcamento ) {
		try {
			orcamento.pagaConta(conta, conta.getValorPago(), conta.getDataPagamento());
			contaService.atualiza(conta);
			this.atualiza(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}

	public void efetuaPagamentoGasto(Gasto gasto, double valor,Date data) {
		try {
			Orcamento orcamento = gasto.getOrcamento();
			orcamento.pagaGasto(gasto, valor);
			//gastoService.atualiza(gasto);
			this.atualiza(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}

	/** 
	 * na exclusao do recebimento n�o � necess�ria a carga da lista de recebimentos 
	 * pois o fetch do relacionamento � EAGER
	 */
	public void cancelaRecebimento(Integer idRecebimento,Orcamento orcamento) {
		try {
			Recebimento recebimento = recebimentoService.encontra(idRecebimento, Recebimento.class);
			recebimento.setOrcamento(null);

			if (orcamento.getRecebimentos()!=null && orcamento.getRecebimentos().size() > 0 ) {
				orcamento.getRecebimentos().remove(recebimento);
			} else {
				throw new FinanceiroException("Orcamento " + orcamento.getId() + 
						" nao possui recebimentos associados");
			}

			orcamento.cancelaRecebimento(recebimento.getValor());
			recebimentoService.remove(recebimento);
			this.atualiza(orcamento);
			log.info(">>> Recebimento " + idRecebimento + " cancelado com sucesso");
		} catch(Exception e) {
			throw new FinanceiroException(e);
		}
	}

	public double getValorTotalGastos(Integer idOrcamento) {
		StringBuilder strb = new StringBuilder();
		strb.append("select sum(valor) from financ.conta ")
		.append (" where id_orcamento=?1 and tipo_conta=\'Gasto\'");
		Query query = this.entityManager.createNativeQuery(strb.toString());
		query.setParameter(1, idOrcamento);
		Double res = (Double) query.getSingleResult();
		return res!=null ? res.doubleValue() : 0.0;
	}

	public double getValorTotalContas(Integer idOrcamento) {
		StringBuilder strb = new StringBuilder();
		strb.append("select sum(valor) from financ.conta ")
		.append (" where id_orcamento=?1 and tipo_conta=\'Conta\'");
		Query query = this.entityManager.createNativeQuery(strb.toString());
		query.setParameter(1, idOrcamento);
		Double res = (Double) query.getSingleResult();
		return res!=null ? res.doubleValue() : 0.0;
	}

	public void ativaOrcamento(final Integer idOrcamento) {
		//	StringBuilder strbUpdate = new StringBuilder();
		//	strbUpdate.append ("update financ.orcamento set ativo=not ")
		//			  .append ("( select o.ativo from financ.orcamento o where o.id= :id1) ")
		//			  .append (" where id = :id2"); 
		Session session = ((Session)this.entityManager.getDelegate());
		session.doWork(new Work() {

			@Override
			public void execute(Connection connecion) throws SQLException {
				CallableStatement callable = connecion.prepareCall("{call financ.ativa_orcamento(?)}");
				callable.setInt(1, idOrcamento);
				callable.executeUpdate();

			}
		});
	}

	//	public Orcamento getOrcamentoAtivo() {
	//		Query query = this.entityManager.createNamedQuery("Orcamento.findOrcamentoAtivo");
	//		List<Orcamento> orcamentos = query.getResultList();
	//		if (orcamentos==null || orcamentos.size()==0) {
	//			throw new RuntimeException("N�o foi encontrado or�amento ativo");
	//		}
	//	return orcamentos.get(0);
	//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JsonObject getOrcamentoAtivo() {
//		StringBuilder strb = new StringBuilder();
//		strb.append("select orc.id 			as idOrcamento,")
//		.append("orc.data_inicial 			as dataInicial,")
//		.append("orc.data_final  			as dataFinal,")
//		.append("orc.valor_disponivel	    as valorDisponivel, ")
//		.append("orc.valor_total_pendente 	as valorPendente, ")
//		.append("(orc.valor_disponivel - orc.valor_total_pendente) as valorSobrante, ")
//		.append("ct.id as idGasto, ")
//		.append("ct.descricao as descGasto, ")
//		.append("ct.valor	  as valorGasto ")
//		.append("from financ.orcamento  orc ")
//		.append("left join financ.conta ct on ct.id_orcamento = orc.id and ct.tipo_conta in ('Gasto','GastoVariavel') ")
//		.append("where orc.ativo = true");
//		Query query = this.entityManager.createNativeQuery(strb.toString());
//
//		List<Object[]> lista = query.getResultList();
//		JsonObject jsObj = new JsonObject();
//		List<LabelValueDTO> listaDTO = new ArrayList<LabelValueDTO>();
//		LabelValueDTO labelDTo = null;
//		int count = 1 ;
//		Date d1 = null;
//		Date d2 = null;
//		for (Object [] ar : lista) {
//			if (count==1) {
//				jsObj.add("idOrcamento",     new JsonParser().parse( new Gson().toJson( ar[0])));
//				d1 = new Date(((java.sql.Date) ar[1]).getTime());
//				d2 = new Date(((java.sql.Date) ar[2]).getTime());
//				jsObj.add("descOrcamento",   new JsonParser().parse( new Gson().toJson( ar[0] + 
//						" - " + this.dtFormat.format(d1)+ " a " + 
//							  this.dtFormat.format(d2)     ))   );
//				jsObj.add("valorDisponivel", new JsonParser().parse( new Gson().toJson( "Valor disponivel: " 
//							+ numFormat.format((Double)ar[3])) ));
//				jsObj.add("valorPendente",   new JsonParser().parse( new Gson().toJson( "Valor Pendente:  "+ 
//						numFormat.format((Double)ar[4]))));
//				jsObj.add("valorSobrante",   new JsonParser().parse( new Gson().toJson( "Sobrara: "+ 
//						numFormat.format((Double)ar[5]))));
//				count++;
//			}
//			labelDTo = new LabelValueDTO((Integer)ar[6], ar[7]+ ": " + (numFormat.format((Double) ar[8])) );
//			listaDTO.add(labelDTo);
//		}
//		jsObj.add("gastos", new JsonParser().parse(new Gson().toJson(listaDTO)));
//		return jsObj;
		
		StringBuilder strb = new StringBuilder();
	    strb.append("select orc.id as idOrcamento,")
	      .append("orc.data_inicial \t\t\tas dataInicial,")
	      .append("orc.data_final  \t\t\tas dataFinal,")
	      .append("orc.valor_disponivel\t    as valorDisponivel, ")
	      .append("orc.valor_total_pendente \tas valorPendente, ")
	      .append("(orc.valor_disponivel - orc.valor_total_pendente) as valorSobrante, ")
	      .append("(select sum(ct2.valor_Pendente)  from financ.conta ct2 ")
	      .append("where  ct2.tipo_conta = 'Conta' and ct2.situacao = 'PENDENTE' and ct2.id_orcamento = orc.id ")
	      .append("group by  ct2.tipo_conta ) as conta_pendente, ")
	      .append("(select sum(ct3.valor_Pendente) from financ.conta ct3  ")
	      .append("where  ct3.tipo_conta like 'Gasto%' and ct3.situacao = 'PENDENTE' and ct3.id_orcamento = orc.id  ")
	      .append("group by  ct3.tipo_conta) as gasto_pendente ")
	      .append("from financ.orcamento  orc ")
	      .append("left join financ.conta ct on ct.id_orcamento = orc.id and ct.tipo_conta in ('Gasto','GastoVariavel') ")
	      .append("where orc.ativo = true ")
	      .append("order by ct.descricao");
	    Query query = this.entityManager.createNativeQuery(strb.toString());
	    
	    List<Object[]> lista = query.getResultList();
	    JsonObject jsObj = new JsonObject();
	    List<LabelValueDTO> listaDTO = new ArrayList();
	    LabelValueDTO labelDTo = null;
	    int count = 1;
	    java.util.Date d1 = null;
	    java.util.Date d2 = null;
	    Double valorDisponivel = null;
	    Double valorPendente = null;
	    Double valorSobrante = null;
	    Double contaPendente = null;
	    Double gastoPendente = null;
	    for (Object[] ar : lista) {
	      if (count == 1)
	      {
	        jsObj.add("idOrcamento", new JsonParser().parse(new Gson().toJson(ar[0])));
	        d1 = new java.util.Date(((java.sql.Date)ar[1]).getTime());
	        d2 = new java.util.Date(((java.sql.Date)ar[2]).getTime());
	        jsObj.add("descOrcamento", new JsonParser().parse(new Gson().toJson(ar[0] + 
	          " - " + this.dtFormat.format(d1) + " a " + 
	          this.dtFormat.format(d2))));
	        
	        valorDisponivel = Double.valueOf((Double)ar[3] == null ? 0.0D : ((Double)ar[3]).doubleValue());
	        valorPendente = Double.valueOf((Double)ar[4] == null ? 0.0D : ((Double)ar[4]).doubleValue());
	        valorSobrante = Double.valueOf((Double)ar[5] == null ? 0.0D : ((Double)ar[5]).doubleValue());
	        contaPendente = Double.valueOf((Double)ar[6] == null ? 0.0D : ((Double)ar[6]).doubleValue());
	        gastoPendente = Double.valueOf((Double)ar[7] == null ? 0.0D : ((Double)ar[7]).doubleValue());
	        
	        jsObj.add("valorDisponivel", new JsonParser().parse(new Gson().toJson(valorDisponivel)));
	        jsObj.add("valorPendente", new JsonParser().parse(new Gson().toJson(valorPendente)));
	        jsObj.add("valorSobrante", new JsonParser().parse(new Gson().toJson(valorSobrante)));
	        jsObj.add("contaPendente", new JsonParser().parse(new Gson().toJson(contaPendente)));
	        jsObj.add("gastoPendente", new JsonParser().parse(new Gson().toJson(gastoPendente)));
	        count++;
	      }
	    }
	    jsObj.add("resumo", new JsonParser().parse(new Gson().toJson(listaDTO)));
	    return jsObj;
	}



	/** retorna resuma do orcamento com o valor disponivel, total pendente e o que sobrar� */
	//	public List<Map> getResumo(int idOrcamento) {
	//		StringBuilder strb = new StringBuilder();
	//		strb.append("select  valor_disponivel as em_conta, ")
	//			.append("valor_total_pendente as total_pendente, ")
	//			.append("valor_disponivel-valor_total_pendente as sobrante")
	//			.append("from financ.orcamento where id=?1");
	//		Query query = this.entityManager.createNativeQuery(strb.toString());
	//		query.setParameter(1, idOrcamento);
	//		return query.getResultList();
	//	}


}
