/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import model.ShareableError;

/**
 *
 * @author N4O
 */
@Named(value = "errorController")
@RequestScoped
public class ErrorController extends BaseController {
    private ShareableError error;

    public ErrorController() {
        this.error = this.getErrorRedirectMessage();
    }
    
    public String getMessage() {
        if (error == null) {
            return "An unknown error has occured";
        }
        return error.getErrorMessage();
    }
    
    public String redirectExecute() {
        if (error == null) {
            return "index.xhtml?faces-redirect=true";
        }
        String target = error.getRedirectTarget();
        if (!target.endsWith(".xhtml")) {
            target += ".xhtml";
        }
        target += "?faces-redirect=true";
        return target;
    }
}
