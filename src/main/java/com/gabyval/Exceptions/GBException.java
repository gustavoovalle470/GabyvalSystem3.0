/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.Exceptions;

import com.gabyval.controllers.security.GBMessageContoller;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.system.AdMessages;

/**
 *
 * @author OvalleGA
 */
public class GBException extends Exception{
    private String title;
    private String message;
    private String level;
            
    public GBException(String message){
        this.message=message;
    }
    
    public GBException(int message_id, String replace_values){
        try {
            AdMessages message = GBMessageContoller.getInstance().getMessage(message_id, replace_values);
            if(message!=null){
                this.title=message.getMessageTitle();
                this.message=message.getMessageDesc();
                this.level=message.getMessageLevel();
            }else{
                this.title="Error no controlado";
                this.message="El error generado no se encuentra configurado o controlado.";
                this.level="FATAL";
            }
        } catch (GBPersistenceException ex) {
                this.title="Error no controlado";
                this.message="Ha ocurrido un error tratando de obtener el mensaje de la base de datos.";
                this.level="FATAL";
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    
}
