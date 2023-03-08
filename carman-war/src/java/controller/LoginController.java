/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import model.User;
import model.UserFacade;

/**
 *
 * @author N4O
 */
@Named(value = "loginController")
@RequestScoped
public class LoginController extends BaseController {
    @EJB
    private UserFacade userFacade;
    
    public LoginController() {
    }
    
    private User user = new User();
    
    public User getUser() {
        return user;
    }
    
    public String executeLogin() {
        String username = user.getUsername();
        String password = user.getPassword();
        if (username == null) {
            this.setErrorRedirectMessage("Please provide a username for login");
            return this.createRedirect("errorpage");
        }
        if (username.isEmpty()) {
            this.setErrorRedirectMessage("Please provide a username for login");
            return this.createRedirect("errorpage");
        }
        if (password == null) {
            this.setErrorRedirectMessage("Please provide a password for login");
            return this.createRedirect("errorpage");
        }
        if (password.isEmpty()) {
            this.setErrorRedirectMessage("Please provide a password for login");
            return this.createRedirect("errorpage");
        }
        User found = userFacade.find(username);
        if (found != null) {
            if (found.getPassword().equals(password)) {
                this.setUserSession(found);
                return this.createRedirect("home");
            } else {
                this.setErrorRedirectMessage("Invalid password provided");
                return this.createRedirect("errorpage");
            }
        }
        this.setErrorRedirectMessage("Unable to find the provided user");
        return this.createRedirect("errorpage");
    }
}
