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
public class CarSalesFacade extends AbstractFacade<CarSales> {

    @PersistenceContext(unitName = "carman-ejbPU")
    private EntityManager em;

    public CarSalesFacade() {
        super(CarSales.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<CarSales> findAllForUser(String userId) {
        EntityManager em = getEntityManager();
        TypedQuery<CarSales> query = em.createQuery(
                "SELECT cs FROM CarSales cs WHERE cs.user.id = :userId", CarSales.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    
    public List<CarSales> findAllForSeller(String userId) {
        EntityManager em = getEntityManager();
        TypedQuery<CarSales> query = em.createQuery(
                "SELECT cs FROM CarSales cs WHERE cs.sales.id = :userId", CarSales.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    
    public List<CarSales> findAllForUser(User user) {
        return findAllForUser(user.getId());
    }
    
    public List<CarSales> findAllForSeller(User user) {
        return findAllForSeller(user.getId());
    }
}
