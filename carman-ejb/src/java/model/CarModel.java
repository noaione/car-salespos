/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author N4O
 */
@Entity
public class CarModel implements Serializable {
    
    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;
    private int price;

    @OneToOne(mappedBy = "carModel")
    private CarSales carSales;

    public CarModel() {
    }

    public CarModel(String id, String name, int price, CarSales carSales) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.carSales = carSales;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public CarSales getCarSales() {
        return carSales;
    }

    public void setCarSales(CarSales carSales) {
        this.carSales = carSales;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CarModel)) {
            return false;
        }
        CarModel other = (CarModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CarModel[ id=" + id + " ]";
    }
    
    public CarSales initSales(User seller) {
        if (carSales != null) {
            throw new IllegalStateException("A CarSales object already exists for this CarModel");
        }
        CarSales sales = new CarSales();
        sales.setSales(seller);
        sales.setCarModel(this);
        this.setCarSales(sales);
        return sales;
    }
}
