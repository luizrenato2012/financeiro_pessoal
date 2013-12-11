package financeiro.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import financeiro.model.service.UsuarioService;

/**
 * Servlet implementation class TesteServlet
 */
@WebServlet("/teste")
public class TesteServlet extends HttpServlet {
	
	@EJB
	private UsuarioService usuarioService;
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login="admin";
		String senha="admin123";
		usuarioService.insert(login, senha);
	}

}
