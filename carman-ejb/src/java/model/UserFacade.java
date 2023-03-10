/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author N4O
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "carman-ejbPU")
    private EntityManager em;

    public UserFacade() {
        super(User.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public User findByUsername(String username) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> from = cq.from(User.class);
        cq.select(from);
        cq.where(cb.equal(from.get(User_.username), username));
        TypedQuery<User> typed = em.createQuery(cq);
        try {
            return typed.getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
    
    public User findByType(UserType type) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> from = cq.from(User.class);
        cq.select(from);
        cq.where(cb.equal(from.get(User_.type), type));
        TypedQuery<User> typed = em.createQuery(cq);
        try {
            return typed.getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
