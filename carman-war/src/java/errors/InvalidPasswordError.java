/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package errors;

/**
 *
 * @author N4O
 */
public class InvalidPasswordError extends RuntimeException {
    private String accountId;

    public InvalidPasswordError(String accountId) {
        super("Invalid password provided for account: " + accountId);
        this.accountId = accountId;
    }
    
    public String getAccountId() {
        return accountId;
    }
}
