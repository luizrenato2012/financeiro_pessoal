package br.com.lrsantos.financeiro_pessoal.model.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.lrsantos.financeiro_pessoal.model.bean.Usuario;

@Stateless
public class UsuarioService extends ServiceGeneric<Usuario, Integer> {


	private Map<String,Usuario> mapUsuario ;
	
	public UsuarioService() {
		mapUsuario = new HashMap<String, Usuario>();
	}
	
	public Usuario valida (String login,String senha) {
		try {
			
			Map<String,Object> parametros = new HashMap<String,Object>();
			parametros.put("login", login);
			parametros.put("senha", getHash(senha));
			
			List<Usuario> usuarios = this.lista("Usuario.findByLoginSenha",parametros);
			return usuarios!=null && usuarios.size()!=0 ? usuarios.get(0) : null;
			// retirado cache de usuario p/ permitir insercao do mesmo sem reiniciar a
//			if (mapUsuario.size()==0) {
//				initMapUsuarios();
//			}
//			return existeUsuario(login, getHash(senha));
		} catch (Exception e ) {
			throw new RuntimeException(e);
		}
	}
	
	private void initMapUsuarios(){
		List<Usuario> lista = this.listaTodos(Usuario.class);
		for(Usuario usuario: lista) {
			mapUsuario.put(usuario.getLogin(), usuario);
		}
	}
	
	public Usuario existeUsuario(String login, String senha){
		if (!mapUsuario.containsKey(login)){
			return null;
		}
		
		for(String key : mapUsuario.keySet()){
			Usuario usuario = mapUsuario.get(key);
			if( login.equals(usuario.getLogin()) && senha.equals(usuario.getSenha()) ){
				return usuario;
			}
		}
		return null;
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
	
}
