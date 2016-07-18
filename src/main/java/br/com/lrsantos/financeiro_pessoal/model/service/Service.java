package br.com.lrsantos.financeiro_pessoal.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface Service<T,PK extends Serializable> {
	
	public void persiste(T t) ;
	
	public void atualiza(T t);
	
	public void remove(T t) ;
	
	public void removeBydId(String hql, Integer id);
	
	public T encontra (PK pk, Class<T> classe) ;
	
	public List<T> listaTodos (Class<T> classe) ;
	
	public List<T> lista (String namedQuery, Map<String,Object> params) ;
}
