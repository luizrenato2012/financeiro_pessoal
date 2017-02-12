package br.com.lrsantos.financeiro_pessoal.model.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jboss.logging.Logger;

import br.com.lrsantos.financeiro_pessoal.controller.ConfiguracaoWeb;
import br.com.lrsantos.financeiro_pessoal.controller.LabelValueDTO;
import br.com.lrsantos.financeiro_pessoal.model.bean.Conta;
import br.com.lrsantos.financeiro_pessoal.model.bean.FinanceiroException;
import br.com.lrsantos.financeiro_pessoal.model.bean.Gasto;
import br.com.lrsantos.financeiro_pessoal.model.bean.Orcamento;
import br.com.lrsantos.financeiro_pessoal.model.bean.Recebimento;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

	
	private static String QRY_DESPESAS_PENDENTES_TIPO;
	private static String QRY_DESPESAS_PENDENTES_TODOS;
	private static String QRY_CONTA_PENDENTE;
	
	@PostConstruct
	private void init() {
		this.initSQL();
	}
	
	private void initSQL() {
		StringBuilder strb = new StringBuilder();
		strb.append("select gt.id, ")
			.append("gt.descricao, ")
			.append("gt.tipo_conta, ")
			.append("gt.data_vencimento, ")
			.append("case when gt.tipo_conta ='Conta' then gt.valor else gt.valor_pendente end as valor ")
			.append("from financ.conta gt ")
			.append("inner join financ.orcamento orc on gt.id_orcamento = orc.id  ")
			.append("where gt.situacao in ('ABERTO','PENDENTE') and ")
			.append("gt.tipo_conta = ?  and ")
			.append("orc.ativo=true " )
			.append("order by gt.tipo_conta, gt.data_vencimento desc");
		QRY_DESPESAS_PENDENTES_TIPO = strb.toString();
		
		strb = new StringBuilder();
		strb.append("select gt.id, ")
			.append("gt.descricao, ")
			.append("gt.tipo_conta, ")
			.append("gt.data_vencimento, ")
			.append("case when gt.tipo_conta ='Conta' then gt.valor else gt.valor_pendente end as valor ")
			.append("from financ.conta gt ")
			.append("inner join financ.orcamento orc on gt.id_orcamento = orc.id  ")
			.append("where gt.situacao in ('ABERTO','PENDENTE') and ")
			.append("gt.tipo_conta in ('Conta','Gasto') and ")
			.append("orc.ativo=true " )
			.append("order by gt.tipo_conta, gt.data_vencimento desc");
		QRY_DESPESAS_PENDENTES_TODOS = strb.toString();
		
		strb = new StringBuilder();
		strb.append("select new br.com.lrsantos.financeiro_pessoal.model.service.ContaDTO(ct.id,ct.descricao, ct.valor,ct.dataVencimento) from Conta ct ")
			.append("where ct.situacao = br.com.lrsantos.financeiro_pessoal.model.bean.SituacaoDespesa.PENDENTE and ")
			.append("ct.orcamento.ativo=true");
		QRY_CONTA_PENDENTE = strb.toString();
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
	 * necessario carregamento dos gastos do orçamento 
	 * em virtudo do cascade.all e pelo fato do relacionamento ser bidirecional
	 * sem o carregamento, so o gastoService.remove(gasto) nao apaga o gasto
	 */
	public void cancelaConta(Integer idConta, Orcamento orcamento) {
		try {
			Conta conta = contaService.encontra(idConta, Conta.class);
			orcamento = this.carregaContas(orcamento.getId());

			if (orcamento.getContas()!=null && orcamento.getContas().size() > 0 ) {
				orcamento.getContas().remove(conta);
			} else {
				throw new FinanceiroException("Orçamento " + orcamento.getId() + 
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
	 * necessario carregamento dos gastos do orcamento 
	 * em virtudo do cascade.all e pelo fato do relacionamento ser bidirecional
	 * sem o carregamento, so o gastoService.remove(gasto) nao apaga o gasto
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

			this.atualiza(orcamento);
		} catch (Exception e) {
			throw new FinanceiroException(e);
		}
	}

	/** 
	 * na exclusao do recebimento nao é necessaria a carga da lista de recebimentos 
	 * pois o fetch do relacionamento é EAGER
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

	public Orcamento getOrcamentoAtivo() {
		Query query = this.entityManager.createNamedQuery("Orcamento.findOrcamentoAtivo");
		List<Orcamento> orcamentos = query.getResultList();
		//		this.log.info("Total de orcamentos "+ orcamentos.size());
		if (orcamentos==null || orcamentos.size()==0) {
			throw new RuntimeException("Nao foi encontrado oramento ativo");
		}
		//		this.log.info("Orcamento ativo "+ orcamentos.get(0));
		return orcamentos.get(0);
	}

	public Integer getIdOrcamentoAtivo(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		if (session.getAttribute(ConfiguracaoWeb.ID_ORCAMENTO_ATIVO.getDescricao()) == null)
		{
			String str = "select id from financ.orcamento where ativo = true";
			Query query = this.entityManager.createNativeQuery(str);
			List lista = query.getResultList();
			if ((lista == null) || (lista.size() == 0)) {
				return Integer.valueOf(0);
			}
			Integer id = (Integer)lista.get(0);
			session.setAttribute(ConfiguracaoWeb.ID_ORCAMENTO_ATIVO.getDescricao(), id);
			return id;
		}
		return (Integer)session.getAttribute(ConfiguracaoWeb.ID_ORCAMENTO_ATIVO.getDescricao());
	}

	public void reiniciaIdOrcamentoAtual(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String str = "select id from financ.orcamento where ativo = true";
		Query query = this.entityManager.createNativeQuery(str);
		List lista = query.getResultList();

		Integer id = (Integer)lista.get(0);
		session.setAttribute(ConfiguracaoWeb.ID_ORCAMENTO_ATIVO.getDescricao(), id);
	}


	public List<Object[]>  getResumoOrcamento() 	{
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

		return query.getResultList();
		
	}
	
	
	/** usado pra retornar DTO de conta pendentes*/
	public String listaContasDTOPendentesOrcamentoAtivo()   {
		Query query = this.entityManager.createQuery(QRY_CONTA_PENDENTE);
		
		List<ContaDTO> listaPendencias = query.getResultList();

		Gson gson = new Gson();
		return gson.toJson(listaPendencias);
	}
	
	/** TODO mudar implementacao similar gasto */
	public  List<Object[]> listaContasPendentesOrcamentoAtivo()   {
		return this.listaPendenciasOrcamentoAtivo("Conta");
	}
	
	public List<Object[]> listaGastosPendentesOrcamentoAtivo()   {
		return this.listaPendenciasOrcamentoAtivo("Gasto");
	}
	
	public List<Object[]> listaGastosContasPendentesOrcamentoAtivo() {
		return this.listaPendenciasOrcamentoAtivo("todos");
	}

	public List<Object[]> listaPendenciasOrcamentoAtivo(String tipo) {
		Query query = this.entityManager.createNativeQuery(tipo.equals("todos")? QRY_DESPESAS_PENDENTES_TODOS  : QRY_DESPESAS_PENDENTES_TIPO);
		if (!tipo.equals("todos")) {
			query.setParameter(1, tipo);
		}
		List<Object[]> listaPendencias = query.getResultList();
		return listaPendencias;
	//	List<LabelValueDTO> listaDTO = new ArrayList();

	//	Gson gson = new Gson();
	//	ListaPendenciasJSon lista = new ListaPendenciasJSon();
	//	Map<String,Object> map = null;
	//	for (Object[] ar : listaPendencias)    {
	//		map =new LinkedHashMap<String, Object>();
	//		map.put("descricao", (String)ar[0]);
	//		map.put("tipo", (String)ar[1]);
	//		map.put("vencimento", JSonUtil.parseDateToString(ar[2]));
	//		map.put("valor", ar[3]);
	//		lista.add(map);
	//	}
	//	return gson.toJson(lista);
	}


}
