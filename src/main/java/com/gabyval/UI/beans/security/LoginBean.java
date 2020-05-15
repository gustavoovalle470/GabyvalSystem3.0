/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.UI.beans.security;

import com.gabyval.Exceptions.GBException;
import com.gabyval.UI.utils.ADNavigationActions;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import com.gabyval.UI.utils.UIMessageManagement;
import com.gabyval.controllers.security.LoginUsersController;
import com.gabyval.persistence.exception.GBPersistenceException;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;
/**
 * Clase encargada del bean que controla el inicio de sesion en el sistema.
 * NOMBRE DEL BEAN: LoginBean, bean de request.
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
 * |   1.0   |  14/05/2020  |      GAOQ      | Creacion y certificacion de la clase.                                                                   |  
 * |---------|--------------|----------------|---------------------------------------------------------------------------------------------------------|
 */
@RequestScoped
@ManagedBean(name = "LoginBean")
public class LoginBean {
    private final Logger log = Logger.getLogger(LoginBean.class);//Log de esta clase
    private String username;//Nombre de usuario a iniciar.
    private String password;//Contraseña de usuario a iniciar.
    
    /**
     * Regresa el valor del nombre de usuario.
     * @return String el nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Cambia el valor del nombre de usuario.
     * @param username String el nuevo nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Regresa el valor actual de la contraseña.
     * @return String el valor de la contraseña.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Modifica el valor de la contraseña.
     * @param password String la nueva contraseña.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Realiza el ingreso de un usuario a la aplicacion.
     * @return String la direccion de navegacion.
     */
    public String login(){
        username = username.toUpperCase();
        try {
            log.info("Iniciando sesion para el usuario: "+username);
            if(LoginUsersController.getInstance().ValidateCredentials(username, password, (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true))){
                UIMessageManagement.putGbMessage(18, username);
                log.info("El inicio de sesion para el usuario "+username+" fue exitoso enviando a pagina de inicio.");
                return ADNavigationActions.LOGIN;
            }else{
                log.info("El inicio de sesion para el usuario"+username+" fallo.");
                return ADNavigationActions.FAILED_LOGIN;
            }
        } catch (GBException ex) {
            log.error("Error al intentar iniciar la sesion de usuario: "+ex.getMessage());
            UIMessageManagement.putGbMessage(ex);
            return ADNavigationActions.FAILED_LOGIN;
        }catch(GBPersistenceException ex){
            log.error("Error al intentar iniciar la sesion de usuario: "+ex.getMessage());
            UIMessageManagement.putErrorMessage("Ha ocurrido un problema de comunicaciones, contacte con su administrador.");
            return ADNavigationActions.FAILED_LOGIN;
        }catch(NoSuchAlgorithmException ex){
            log.error("Error al intentar iniciar la sesion de usuario: "+ex.getMessage());
            UIMessageManagement.putErrorMessage("Ha ocurrido un error al tratar de descifrar su cuenta, contacte con su administrador.");
            return ADNavigationActions.FAILED_LOGIN;
        }
    }
}
