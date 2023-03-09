/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author N4O
 */
@Stateless
public class CarModelFacade extends AbstractFacade<CarModel> {

    @PersistenceContext(unitName = "carman-ejbPU")
    private EntityManager em;

    public CarModelFacade() {
        super(CarModel.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Override
    public void create(CarModel carModel) {
        if (carModel.getCarSales() == null) {
            throw new IllegalStateException("Please assign seller before creating the carModel");
        }
        getEntityManager().persist(carModel);
    }

    public void create(CarModel carModel, User seller) {
        try {
            carModel.initSales(seller);
        } catch (IllegalStateException ex) {
            // no op
        }
        create(carModel);
    }
}
