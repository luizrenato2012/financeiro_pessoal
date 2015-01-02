package financeiro.model.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import financeiro.model.bean.Usuario;

@Stateless
public class UsuarioService extends ServiceGeneric<Usuario, Integer> {


	public Usuario valida (String login,String senha) {
		try {
			
			Map<String,Object> parametros = new HashMap<String,Object>();
			System.out.println("Senha " + senha);
			parametros.put("login", login);
			parametros.put("senha", getHash(senha));
			List<Usuario> usuarios = this.lista("Usuario.findByLoginSenha",parametros);
			return usuarios!=null && usuarios.size()!=0 ? usuarios.get(0) : null;
		} catch (Exception e ) {
			throw new RuntimeException(e);
		}
	}
	
	public void insert(String login,String senha) {
		try {
			Usuario usuario = new Usuario();
			usuario.setLogin(login);
			usuario.setSenha(getHash(senha));
			this.persiste(usuario);
		} catch (Exception e ) {
			throw new RuntimeException(e);
		}
	}

	private String getHash(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		byte messageDigest[] = algorithm.digest(str.getBytes("UTF-8"));

		//cria String com valores hex provenientes do byte[]
		StringBuilder strbHex = new StringBuilder();
		for (byte b : messageDigest) {
			strbHex.append(String.format("%02X", 0xFF & b));
		}
		return strbHex.toString();
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(new UsuarioService().getHash("admin123"));
	}


}
