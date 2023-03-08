/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

/**
 * A shareable error between redirection, need to be set into HTTP attribute
 * @author N4O
 */
public class ShareableError implements Serializable {
    private String errorMessage;
    private String redirectTarget;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRedirectTarget() {
        return redirectTarget;
    }

    public void setRedirectTarget(String redirectTarget) {
        this.redirectTarget = redirectTarget;
    }

    public ShareableError(String errorMessage, String redirectTarget) {
        this.errorMessage = errorMessage;
        this.redirectTarget = redirectTarget;
    }
}
