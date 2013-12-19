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
public class ServiceGeneric <T,PK> implements Service <T,Integer>{

	@PersistenceContext(name="financPU")
	private EntityManager entityManager;

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
	
	@Override
	public T encontra (Integer pk, Class<T> classe) {
		return entityManager.find(classe, pk);
	}
	
	@Override
	public List<T> listaTodos (Class<T> classe) {
		CriteriaQuery<T> criteria = entityManager.getCriteriaBuilder().createQuery(classe);
		TypedQuery<T> typedQuery = entityManager.createQuery(criteria);
		return typedQuery.getResultList();
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
	
	


	
}
