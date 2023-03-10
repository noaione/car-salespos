/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author N4O
 */
@Stateless
public class ReviewDataFacade extends AbstractFacade<ReviewData> {

    @PersistenceContext(unitName = "carman-ejbPU")
    private EntityManager em;

    public ReviewDataFacade() {
        super(ReviewData.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<ReviewData> findAllForUser(String userId) {
        EntityManager em = getEntityManager();
        TypedQuery<ReviewData> query = em.createQuery(
                "SELECT rd FROM ReviewData rd WHERE rd.saleHistory.loaner.id = :userId", ReviewData.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<ReviewData> findAllForUser(User user) {
        return findAllForUser(user.getId());
    }
    
    public List<ReviewData> findAllForSeller(String userId) {
        EntityManager em = getEntityManager();
        TypedQuery<ReviewData> query = em.createQuery(
                "SELECT rd FROM ReviewData rd WHERE rd.saleHistory.carSales.sales.id = :userId", ReviewData.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<ReviewData> findAllForSeller(User user) {
        return findAllForSeller(user.getId());
    }
    
    public ReviewData findForHistory(String saleHistoryId) {
        EntityManager em = getEntityManager();
        TypedQuery<ReviewData> query = em.createQuery(
                "SELECT rd FROM ReviewData rd WHERE rd.saleHistory.id = :saleId", ReviewData.class);
        query.setParameter("saleId", saleHistoryId);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public ReviewData findForHistory(SalesHistory saleHistory) {
        return findForHistory(saleHistory.getId());
        
    }
}
