/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package model;

/**
 *
 * @author N4O
 */
public enum CarSalesStatus {
    // Approved, and ready for rental
    AVAILABLE,
    // Boooked by someone
    BOOKED,
    // Paid, and currently being loaned.
    PAID,
    // Added, wait for approval by admin
    PENDING_APPROVAL,
}
