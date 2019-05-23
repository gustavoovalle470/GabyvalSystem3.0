/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.primefaces.customize.UI.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author OvalleGA
 */
public class UIMessageManagement {
    public static void putException(Exception ex){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Se ha presentado un problema", ex.getMessage());
        putIntoContext(message);
    }
    
    public static void putFatalMessage(String IncomingMsg){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal", IncomingMsg);
        putIntoContext(message);
    }
    
    public static void putErrorMessage(String IncomingMsg){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", IncomingMsg);
        putIntoContext(message);
    }
    
    public static void putInfoMessage(String IncomingMsg){
        System.out.println(IncomingMsg);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", IncomingMsg);
        putIntoContext(message);
    }
    
    public static void putWarnMessage(String IncomingMsg){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", IncomingMsg);
        putIntoContext(message);
    }
    
    public static void putCustomMessage(FacesMessage.Severity severity, String title, String IncomingMsg){
        FacesMessage message = new FacesMessage(severity, title, IncomingMsg);
        putIntoContext(message);
    }

    private static void putIntoContext(FacesMessage message){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.addMessage(null, message);
    }
}
