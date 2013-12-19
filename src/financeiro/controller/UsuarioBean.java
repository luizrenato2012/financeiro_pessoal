package financeiro.controller;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import financeiro.model.bean.Usuario;
import financeiro.model.service.UsuarioService;

@Named
@RequestScoped
public class UsuarioBean {
	private String login;
	private String senha;
	
	@EJB
	private UsuarioService usuarioService;
	
	public String autentica() {
		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println(">> autenticando ");
		try {
			if (this.login==null || this.login.equals("")) {
				context.addMessage( null, new FacesMessage("Login inválido"));
				return null;
			}
			
			if (this.senha==null || this.senha.equals("")) {
				context.addMessage( null, new FacesMessage("Senha inválido"));
				return null;
			}
			Usuario usuario = usuarioService.valida(login, senha);
			if (usuario==null) {
				context.addMessage( null, new FacesMessage("Senha/senha incorreto"));
				return null;
			} else {
				return "orcamento_menu.jsf";
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
