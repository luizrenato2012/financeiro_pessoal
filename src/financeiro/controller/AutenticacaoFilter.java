package financeiro.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns="/mob/*")
public class AutenticacaoFilter implements Filter {
	
    
	public AutenticacaoFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest requestHttp = (HttpServletRequest) request;
		String uri = requestHttp.getRequestURI();
		System.out.println("URI " + uri);
		
		
		if ( estaLogado((HttpServletRequest) request) || estaLogando(uri)) {
		//	System.out.println("login iniciado ");
			chain.doFilter(request, response);
		} else {
		//	System.out.println("outras paginas");
			request.getRequestDispatcher(ConfiguracaoWeb.URI_LOGIN.getDescricao()).include(request, response);;
		}
		
	}
	
	private boolean estaLogado(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return session.getAttribute(ConfiguracaoWeb.USUARIO_SESSAO_MOB.getDescricao())!=null;
	}
	
	private boolean estaLogando(String uri) {
		return uri.contains(ConfiguracaoWeb.URI_LOGIN.getDescricao());
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
