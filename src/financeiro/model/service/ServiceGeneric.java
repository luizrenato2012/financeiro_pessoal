package financeiro.model.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 
 * @author Luiz Renato
 * Service generico (EJB) utilizando EntityManager como DAO 
 *
 * @param <T>
 * @param <PK>
 */
@Stateless
public class ServiceGeneric <T,PK> implements Service <T,Integer>{

	@PersistenceContext(name="financPU")
	protected EntityManager entityManager;

	@Override
	public void persiste(T t) {
		entityManager.persist(t);
	}
	
	@Override
	public void atualiza(T t) {
		entityManager.merge(t);
	}
	
	@Override
	public void remove(T t) {
		entityManager.remove(t);
	}
	
	public void remove(Integer pk, Class<T> classe) {
		T t = entityManager.find(classe, pk);
		if (t==null) {
			throw new RuntimeException("Objeto class " + classe.getName() + " #" + pk +
					 " nao encontrado");
		}
		entityManager.remove(t);
	}
	
	@Override
	public T encontra (Integer pk, Class<T> classe) {
		return entityManager.find(classe, pk);
	}
	
	@Override
	public List<T> listaTodos (Class<T> classe) {
		Query query = entityManager.createQuery("select f from "+ classe.getName()+ " f order by f.id");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> lista (String namedQuery, Map<String,Object> params) {
		Query query = entityManager.createNamedQuery(namedQuery);
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		return query.getResultList();
	}

	@Override
	public void removeBydId(String namedQuery, Integer id) {
		Query query = entityManager.createNamedQuery(namedQuery);
		query.setParameter(1, id).executeUpdate();
	}
	
	


	
}
