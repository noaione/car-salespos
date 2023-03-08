/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author N4O
 */
@Named(value = "logoutController")
@RequestScoped
public class LogoutController extends BaseController {

    public LogoutController() {
    }
    
    public String executeExit() {
        this.getContext().getExternalContext().invalidateSession();
        return this.createRedirect("index");
    }
}
