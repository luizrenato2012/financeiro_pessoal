package financeiro.model.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import financeiro.model.bean.Usuario;

@Stateless
public class UsuarioService {

	@PersistenceContext(name="financPU")
	private EntityManager entityManager;


	public Usuario valida (String login,String senha) {
		try {
			Usuario usuario=null;
			Query query = entityManager.createNamedQuery("Usuario.findByLoginSenha");
			query.setParameter("login", login);
			query.setParameter("senha", getHash(senha));
			return usuario;
		} catch (Exception e ) {
			throw new RuntimeException(e);
		}
	}
	
	public void insert(String login,String senha) {
		try {
			Usuario usuario = new Usuario();
			usuario.setLogin(login);
			usuario.setSenha(getHash(senha));
			entityManager.persist(usuario);
		} catch (Exception e ) {
			throw new RuntimeException(e);
		}
	}

	private String getHash(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		byte messageDigest[] = algorithm.digest("senha".getBytes("UTF-8"));

		//cria String com valores hex provenientes do byte[]
		StringBuilder strbHex = new StringBuilder();
		for (byte b : messageDigest) {
			strbHex.append(String.format("%02X", 0xFF & b));
		}
		return strbHex.toString();
	}


}
