/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.Exceptions;

import com.gabyval.controllers.security.GBMessageContoller;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.system.AdMessages;
import org.apache.log4j.Logger;
/** 
 * Clase encargada de manejar todas las excepciones del sistema GB
 * @version 1.0
 * @since 14/05/2020
 * *****************************************************************************************************************************************************
 * *****************************************************************************************************************************************************
 * *******************************                                                                              ****************************************
 * *******************************  ********** ********** ******    **      ** **      ** ********** **         ****************************************
 * *******************************  ********** **********  ******   **      ** **      ** ********** **         ****************************************
 * *******************************  **         **      **  **  **   **      ** **      ** **      ** **         ****************************************
 * *******************************  **         **      **  **  **   **      ** **      ** **      ** **         ****************************************
 * *******************************  **         **      **  ******    **    **  **      ** **      ** **         ****************************************
 * *******************************  **    **** **********  *******    **  **   **      ** ********** **         ****************************************
 * *******************************  **    **** **********  ********    ****    **      ** ********** **         ****************************************
 * *******************************  **      ** **      **  **    **     **     **      ** **      ** **         ****************************************
 * *******************************  **      ** **      **  **    **     **      **    **  **      ** **         ****************************************
 * *******************************  **      ** **      **  **   **      **       **  **   **      ** **         ****************************************
 * *******************************  ********** **      **  **  **       **        ****    **      ** ********** ****************************************
 * *******************************  ********** **      ** ******        **         **     **      ** ********** ****************************************
 * *****************************************************************************************************************************************************
 * *****************************************************************************************************************************************************
 * |---------------------------------------------------------------------------------------------------------------------------------------------------|
 * |                                                        Control de versiones                                                                       |
 * |---------|--------------|----------------|---------------------------------------------------------------------------------------------------------|
 * | Version |    Fecha     |  Responsable   |                                                  Comentarios                                            |
 * |---------|--------------|----------------|---------------------------------------------------------------------------------------------------------|
 * |   1.0   |  14/05/2020  |      GAOQ   | Creacion y certificacion de la clase.                                                                      |  
 * |---------|--------------|----------------|---------------------------------------------------------------------------------------------------------|
 */
public class GBException extends Exception{
    private String title; //Titulo del mensaje
    private String message; // Mensaje de excepcion
    private String level; //Nivel de la excepcion.
    private final Logger log = Logger.getLogger(GBException.class);//Log de esta clase.
            
    /**
     * Crea una nueva instancia de esta excepcion.
     * @param message String el mensaje de la excepcion. 
     */
    public GBException(String message){
        log.debug("GabyvalSystem - Creando mensaje de excepcion con mensaje: "+message);
        this.message=message;
    }
    
    /**
     * Crea un mensaje de excepcion dado un id de menasje de la base de datos.
     * @param message_id int el id del mensaje en la base de datos.
     * @param replace_values String los valores con los que se reemplazara los comodines en el mensaje de base de datos, separados por coma.
     */
    public GBException(int message_id, String replace_values){
        try {
            log.debug("GabyvalSystem - Creando mensaje de excepcion de base de datos con id: "+message_id);
            log.debug("GabyvalSystem - Datos a reemplazar: "+replace_values);
            AdMessages gbMessage = GBMessageContoller.getInstance().getMessage(message_id, replace_values);
            if(gbMessage!=null){
                log.debug("GabyvalSystem - El mensaje fue encontrado y sus valores reemplazados");
                this.title=gbMessage.getMessageTitle();
                log.debug("GabyvalSystem - Titulo: "+this.title);
                this.message=gbMessage.getMessageDesc();
                log.debug("GabyvalSystem - Mensaje: "+this.message);
                this.level=gbMessage.getMessageLevel();
                log.debug("GabyvalSystem - Nivel: "+this.level);
            }else{
                log.debug("GabyvalSystem - El mensaje no fue encontrado, estableciendo mensaje por defecto.");
                this.title="Error no controlado";
                this.message="El error generado no se encuentra configurado o controlado.";
                this.level="FATAL";
            }
        } catch (GBPersistenceException ex) {
            log.fatal("GabyvalSystem - Se ha presentado un error de base de datos al momento de recuperar el mensaje solicitado.");
            log.fatal(ex);
            this.title="Error no controlado";
            this.message="Ha ocurrido un error tratando de obtener el mensaje de la base de datos.";
            this.level="FATAL";
        }
    }

    /**
     * Regresa el mensaje de esta excepcion.
     * @return String el mensaje de esta excepcion.
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Modifica el mensaje de esta excepcion.
     * @param message String el nuevo mensaje de esta excepcion.
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Regresa el titulo de este mensaje.
     * @return String el titulo de este mensaje.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Modifica el titulo de este mensaje.
     * @param title String el nuevo titulo.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Regresa el valor del nivel de este mensaje.
     * @return String el nivel del mensaje.
     */
    public String getLevel() {
        return level;
    }

    /**
     * Modifica el valor del nivel de este mensaje.
     * @param level String el nuevo nivel.
     */
    public void setLevel(String level) {
        this.level = level;
    }
    
}
