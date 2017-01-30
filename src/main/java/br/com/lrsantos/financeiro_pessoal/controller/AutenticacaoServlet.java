package br.com.lrsantos.financeiro_pessoal.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.lrsantos.financeiro_pessoal.model.bean.Usuario;
import br.com.lrsantos.financeiro_pessoal.model.service.UsuarioService;

/**
 * 
 * autenticacao mobile 
 */
@WebServlet(urlPatterns="/autentica.do", loadOnStartup=1)
public class AutenticacaoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String PAGINA_LOGIN="mob/login.html";
	private static final String PAGINA_GASTO="mob/gasto.html";
	private static final String PAGINA_PRINCIPAL="mob/main.html";
	
	@EJB
	private UsuarioService usuarioService;
       
    public AutenticacaoServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String acao = request.getParameter("acao");
		switch (acao) {
		case "usuarioLogado":
			processaUsuario(request,response);
			break;
		case "logoff":
			processLogoff(request,response);
		default:
			break;
		}
			
		
	}

	private void processaUsuario(HttpServletRequest request,
			HttpServletResponse response) {
		String usuario = (String) request.getSession().getAttribute("usuario");
		request.setAttribute(ConfiguracaoWeb.USUARIO_SESSAO_MOB.getDescricao(), 
				usuario!=null ? usuario : "Usuario nao logado");
	}
	
	private void processLogoff(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.getSession().removeAttribute("usuario");
		request.getSession().invalidate();
		response.sendRedirect(PAGINA_LOGIN);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String acao = request.getParameter("acao");
		if (acao.equals("login")) {
			processaLogin(request,response);
		}
	}

	private void processaLogin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String login=request.getParameter("login");
		String senha=request.getParameter("senha");
		
		try {
			String validacao = validaParametros(login, senha);
			if ( validacao!=null) {
				throw new Exception (validacao); 
			}
			
			Usuario usuario = usuarioService.valida(login, senha);
			if (usuario==null){
				response.sendRedirect(PAGINA_LOGIN);
				return;
			}
			request.getSession().setAttribute(ConfiguracaoWeb.USUARIO_SESSAO_MOB.getDescricao(),
					usuario.getLogin());
			response.sendRedirect(PAGINA_PRINCIPAL);
		} catch (Exception e){
			e.printStackTrace();
			response.sendRedirect(PAGINA_LOGIN);
		}
		
	}
	
	private String validaParametros(String login,String senha ){
		
		if (login==null || login.equals("")){
			return "Login invalido";
		}
		
		if (senha==null || senha.equals("")){
			return "Senha invalida";
		}
		
		return null;
	}
	

}
