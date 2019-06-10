/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.UI.utils;

import com.gabyval.Exceptions.GBException;
import com.gabyval.controllers.security.GBMessageContoller;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.system.AdMessages;
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

    public static void putGbMessage(GBException message){
        putCustomMessage(getSeverity(message.getLevel()), message.getTitle(), message.getMessage());
    }
    
    public static void putGbMessage(int message_id, String replace_values) throws GBPersistenceException{
        AdMessages message = GBMessageContoller.getInstance().getMessage(message_id, replace_values);
        putCustomMessage(getSeverity(message.getMessageLevel()), message.getMessageTitle(), message.getMessageDesc());
    }
    
    private static FacesMessage.Severity getSeverity(String severity){
        switch(severity){
            case "FATAL":
                return FacesMessage.SEVERITY_FATAL;
            case "ERROR":
                return FacesMessage.SEVERITY_ERROR;
            case "WARN":
                return FacesMessage.SEVERITY_WARN;
            case "INFO":
                return FacesMessage.SEVERITY_INFO;
            default:
                return FacesMessage.SEVERITY_FATAL;
        }
    }
    
    private static void putIntoContext(FacesMessage message){
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.addMessage(null, message);
    }
}
