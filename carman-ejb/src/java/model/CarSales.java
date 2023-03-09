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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;

/**
 *
 * @author N4O
 */
@Entity
public class CarSales implements Serializable {

    private static final long serialVersionUID = 5L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    private CarModel carModel;
    // Can be none, since no one buying it yet.
    @ManyToOne(optional = true)
    private User owner;
    @ManyToOne
    private User sales;
    private CarSalesStatus status;

    private String salesReview;

    public CarSales() {
    }

    public CarSales(String id, CarModel carModel, User owner, User sales, CarSalesStatus status, String salesReview) {
        this.id = id;
        this.carModel = carModel;
        this.owner = owner;
        this.sales = sales;
        this.status = status;
        this.salesReview = salesReview;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getSales() {
        return sales;
    }

    public void setSales(User sales) {
        this.sales = sales;
    }

    public CarSalesStatus getStatus() {
        return status;
    }

    public void setStatus(CarSalesStatus status) {
        this.status = status;
    }
    
    public String getSalesReview() {
        return salesReview;
    }

    public void setSalesReview(String salesReview) {
        this.salesReview = salesReview;
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
        if (!(object instanceof CarSales)) {
            return false;
        }
        CarSales other = (CarSales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CarSales[ id=" + id + " ]";
    }
    
}
