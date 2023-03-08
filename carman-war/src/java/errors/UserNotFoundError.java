/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package errors;

/**
 *
 * @author N4O
 */
public class UserNotFoundError extends RuntimeException {
    private String accountId;

    public UserNotFoundError(String accountId) {
        super("Unable to find account: " + accountId);
        this.accountId = accountId;
    }
    
    public String getAccountId() {
        return accountId;
    }
}
