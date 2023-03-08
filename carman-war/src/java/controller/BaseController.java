/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import model.ShareableError;
import model.User;

/**
 *
 * @author N4O
 */
public class BaseController {
    public FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }
    
    public HttpSession getSession() {
        return (HttpSession)getContext().getExternalContext().getSession(true);
    }

    public User getUserSession() {
        return (User)getAttribute("userContext");
    }
    
    public void setUserSession(User user) {
        HttpSession sess = getSession();
        sess.setAttribute("userContext", user);
    }
    
    public void destroyUserSession() {
        HttpSession sess = getSession();
        sess.removeAttribute("userContext");
    }
    
    public Object getAttribute(String attr) {
        HttpSession sess = getSession();
        return sess.getAttribute(attr);
    }
    
    public void setErrorRedirectMessage(ShareableError error) {
        HttpSession sess = getSession();
        sess.setAttribute("shareableErrorCtx", error);
    }
    
    public void setErrorRedirectMessage(String message) {
        HttpSession sess = getSession();
        sess.setAttribute("shareableErrorCtx", new ShareableError(message, "index"));
    }
    
    public void setErrorRedirectMessage(String message, String target) {
        HttpSession sess = getSession();
        sess.setAttribute("shareableErrorCtx", new ShareableError(message, target));
    }
    
    public ShareableError getErrorRedirectMessage() {
        HttpSession sess = getSession();
        // After getting the message, immediatly delete it
        ShareableError error = (ShareableError)getAttribute("shareableErrorCtx");
        sess.removeAttribute("shareableErrorCtx");
        return error;
    }
    
    public String createRedirect(String target) {
        if (!target.endsWith(".xhtml")) {
            target += ".xhtml";
        }
        target += "?faces-redirect=true";
        return target;
    }
}
