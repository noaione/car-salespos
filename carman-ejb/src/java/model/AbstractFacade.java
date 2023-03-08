/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Copied directly from the original AbstractFacade created in class.
 * @author N4O
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    protected abstract EntityManager getEntityManager();
    
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }
    
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }
    
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }
    
    public List<T> findAll() {
        CriteriaQuery eq = getEntityManager().getCriteriaBuilder().createQuery();
        eq.select(eq.from(entityClass));
        return getEntityManager().createQuery(eq).getResultList();
    }
    
    public List<T> findRange(int[] range) {
        CriteriaQuery eq = getEntityManager().getCriteriaBuilder().createQuery();
        eq.select(eq.from(entityClass));
        Query q = getEntityManager().createQuery(eq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public int count() {
        CriteriaQuery eq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> st = eq.from(entityClass);
        eq.select(getEntityManager().getCriteriaBuilder().count(st));
        Query q = getEntityManager().createQuery(eq);
        return ((Long)q.getSingleResult()).intValue();
    }
}
