/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author N4O
 */
@Entity
public class SalesHistory implements Serializable {

    private static final long serialVersionUID = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    
    @ManyToOne
    private User loaner;
    
    @OneToOne
    private CarSales carSales;
    
    private long timestamp;

    public SalesHistory() {
    }

    public SalesHistory(String id, User loaner, CarSales carSales, long timestamp) {
        this.id = id;
        this.loaner = loaner;
        this.carSales = carSales;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getLoaner() {
        return loaner;
    }

    public void setLoaner(User loaner) {
        this.loaner = loaner;
    }

    public CarSales getCarSales() {
        return carSales;
    }

    public void setCarSales(CarSales carSales) {
        this.carSales = carSales;
    }
    
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setTimestamp() {
        this.timestamp = Instant.now().getEpochSecond();
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
        if (!(object instanceof SalesHistory)) {
            return false;
        }
        SalesHistory other = (SalesHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.SalesHistory[ id=" + id + " ]";
    }
    
}
