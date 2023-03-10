/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author N4O
 */
@Stateless
public class SalesHistoryFacade extends AbstractFacade<SalesHistory> {

    @PersistenceContext(unitName = "carman-ejbPU")
    private EntityManager em;

    public SalesHistoryFacade() {
        super(SalesHistory.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<SalesHistory> findAllForUser(String userId) {
        EntityManager em = getEntityManager();
        TypedQuery<SalesHistory> query = em.createQuery(
                "SELECT sh FROM SalesHistory sh WHERE sh.loaner.id = :userId", SalesHistory.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<SalesHistory> findAllForUser(User user) {
        return findAllForUser(user.getId());
    }
}
