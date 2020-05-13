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
 *
 * @author OvalleGA
 */
@RequestScoped
@ManagedBean(name = "LoginBean")
public class LoginBean {
    private final Logger log = Logger.getLogger(LoginBean.class);
    private String username;
    private String password;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
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
