package br.com.lrsantos.financeiro_pessoal.controller;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.lrsantos.financeiro_pessoal.model.bean.Usuario;
import br.com.lrsantos.financeiro_pessoal.model.service.UsuarioService;

@Named
@RequestScoped
public class UsuarioBean {
	private String login;
	private String senha;
	
	@EJB
	private UsuarioService usuarioService;
	@Inject
	private SessaoBeanImpl sessaoBean;
	
	private static final String ORCAMENTO_MENU = "orcamento_menu.jsf";
	
	public String autentica() {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			if (this.login==null || this.login.equals("")) {
				context.addMessage( null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,"Login inv�lido","" ));
				return null;
			}
			
			if (this.senha==null || this.senha.equals("")) {
				context.addMessage( null, 
						new FacesMessage(FacesMessage.SEVERITY_ERROR,"Senha inv�lido",""));
				return null;
			}
			Usuario usuario = usuarioService.valida(login, senha);
			if (usuario==null) {
				context.addMessage( null, 
						new FacesMessage(FacesMessage.SEVERITY_ERROR,"Senha/senha incorreto",""));
				return null;
			} else {
				sessaoBean.registraUsuario(usuario.getLogin());
				return ORCAMENTO_MENU;
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage( null, new FacesMessage("Erro ao autenticar"));
			return null;
		}
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
}
