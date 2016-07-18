package br.com.lrsantos.financeiro_pessoal.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.lrsantos.financeiro_pessoal.model.beanTeste.Produto;
import br.com.lrsantos.financeiro_pessoal.model.service.LojaService;
import br.com.lrsantos.financeiro_pessoal.model.service.ProdutoService;
import br.com.lrsantos.financeiro_pessoal.model.service.UsuarioService;

/**
 * Servlet implementation class TesteServlet
 */
@WebServlet("/teste")
public class TesteServlet extends HttpServlet {
	
	@EJB
	private UsuarioService usuarioService;
	
	@EJB
	private LojaService lojaService;
	
	@EJB
	private ProdutoService produtoService;
	
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter  writer = response.getWriter();
		
		String opcao = request.getParameter("opcao");
		Integer opcaoInt = 0;
		if (opcao==null || opcao.equals("")){
			writer.println("Op��o escolhida invalida ");
		} else {
			opcaoInt = Integer.parseInt(opcao);
			switch (opcaoInt) {
			case 1:
				criaUsuario("admin","123");
				break;

			default:
				break;
			}
		}
		writer.close();
		
	}
	
	private void criaProduto(PrintWriter writer) {
		Produto p = new Produto();
		p.setDescricao("Produto 2");
		p.setPreco(4.0);
		p.setIdLoja(1);
		produtoService.persiste(p);
		writer.println(p);
	}
	
	private void criaUsuario(String login,String senha) {
		//String login="admin";
		//String senha="admin123";
		usuarioService.insert(login, senha);
	}

}
