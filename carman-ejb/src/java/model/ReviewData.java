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
public class ReviewData implements Serializable {

    private static final long serialVersionUID = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    
    @OneToOne
    private SalesHistory saleHistory;
    
    private int userRate;
    private int sellerRate;
    private String userReview;
    private String sellerReview;

    public ReviewData() {
    }

    public ReviewData(String id, SalesHistory saleHistory, int userRate, int sellerRate, String userReview, String sellerReview) {
        this.id = id;
        this.saleHistory = saleHistory;
        this.userRate = userRate;
        this.sellerRate = sellerRate;
        this.userReview = userReview;
        this.sellerReview = sellerReview;
    }

    public int getUserRate() {
        return userRate;
    }

    public void setUserRate(int userRate) {
        this.userRate = userRate;
    }

    public int getSellerRate() {
        return sellerRate;
    }

    public void setSellerRate(int sellerRate) {
        this.sellerRate = sellerRate;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    public String getSellerReview() {
        return sellerReview;
    }

    public void setSellerReview(String sellerReview) {
        this.sellerReview = sellerReview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SalesHistory getSaleHistory() {
        return saleHistory;
    }

    public void setSaleHistory(SalesHistory saleHistory) {
        this.saleHistory = saleHistory;
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
        if (!(object instanceof ReviewData)) {
            return false;
        }
        ReviewData other = (ReviewData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.ReviewData[ id=" + id + " ]";
    }
    
}
