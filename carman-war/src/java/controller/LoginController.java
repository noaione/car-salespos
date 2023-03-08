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
        User found = userFacade.find(user.getUsername());
        if (found != null) {
            if (found.getPassword() == user.getPassword()) {
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
