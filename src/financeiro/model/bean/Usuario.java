package financeiro.model.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="financ.usuario")
@SequenceGenerator(name="SEQ_ID_USUARIO", sequenceName="financ.seq_id_usuario")
@NamedQuery(name="findByLoginSenha",query="select u from Usuario u where u.login=:login " +
				" and u.senha= :senha")	
public class Usuario implements Serializable {

	private static final long serialVersionUID = 3973928551156386641L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_ID_USUARIO")
	private Integer id;
	
	@Column(length=30)
	private String login;
	
	@Column(length=60)
	private String senha;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
