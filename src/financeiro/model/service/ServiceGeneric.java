package financeiro.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

/**
 * 
 * @author Luiz Renato
 * Service generico (EJB) utilizando EntityManager como DAO 
 *
 * @param <T>
 * @param <PK>
 */
@Stateless
public class ServiceGeneric <T,PK extends Serializable> {

	@PersistenceContext(name="financPU")
	private EntityManager entityManager;
	
	public void persist(T t) {
		entityManager.persist(t);
	}
	
	public void merge(T t) {
		entityManager.merge(t);
	}
	
	public void remove(T t) {
		entityManager.remove(t);
	}
	
	public T find (PK pk, Class<T> classe) {
		return entityManager.find(classe, pk);
	}
	
	public List<T> listAll(Class<T> classe) {
		CriteriaQuery<T> criteria = entityManager.getCriteriaBuilder().createQuery(classe);
		TypedQuery<T> typedQuery = entityManager.createQuery(criteria);
		return typedQuery.getResultList();
	}
	
	public List<T> list (String namedQuery, Map<String,Object> params) {
		Query query = entityManager.createNamedQuery(namedQuery);
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		return query.getResultList();
	}
	
	
}
