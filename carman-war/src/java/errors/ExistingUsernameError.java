/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package errors;

/**
 *
 * @author N4O
 */
public class ExistingUsernameError extends RuntimeException {
    private String accountId;

    public ExistingUsernameError(String accountId) {
        super("The provided username has already been registered: " + accountId);
        this.accountId = accountId;
    }
    
    public String getAccountId() {
        return accountId;
    }
}
