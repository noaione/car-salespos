/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package model;

/**
 *
 * @author N4O
 */
public enum UserType {
    // The administrator
    MANAGER,
    // Both the seller/loaner and also someone that can rent a car.
    CUSTOMER;
    
    public String asName() {
        String name = this.name().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
