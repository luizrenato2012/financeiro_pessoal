package financeiro.controller;

import javax.ejb.EJB;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import financeiro.model.bean.Orcamento;
import financeiro.model.service.OrcamentoService;

@WebListener
public class OrcamentoContextListener implements HttpSessionListener {

	@EJB
	private OrcamentoService orcamentoService;
	
	@Override
	public void sessionCreated(HttpSessionEvent evt) {
		evt.getSession().setAttribute(ConfiguracaoWeb.ORCAMENTO_ATIVO.getDescricao(), 
				orcamentoService.getOrcamentoAtivo());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent evt) {
		evt.getSession().removeAttribute(ConfiguracaoWeb.ORCAMENTO_ATIVO.getDescricao());
	}


}
