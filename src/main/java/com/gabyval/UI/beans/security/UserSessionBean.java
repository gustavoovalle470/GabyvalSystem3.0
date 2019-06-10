/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.UI.beans.security;

import com.gabyval.Exceptions.GBException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import com.gabyval.UI.security.menu.MenuFactory;
import com.gabyval.UI.utils.UIMessageManagement;
import com.gabyval.controllers.security.GBMessageContoller;
import com.gabyval.controllers.security.UserSessionManager;
import com.gabyval.persistence.exception.GBPersistenceException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.primefaces.model.menu.DefaultMenuModel;

/**
 *
 * @author OvalleGA
 */
@SessionScoped
@ManagedBean(name = "UserSessionBean")
public class UserSessionBean implements Serializable{
    private final Logger log = Logger.getLogger(UserSessionBean.class);
    private String username;
    private final HttpSession session;
    private DefaultMenuModel user_sec_menu;

    public UserSessionBean(){
        log.debug("Obteniendo datos de sesion.");
        session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        log.debug("Obteniendo usuario conectado.");
        username = UserSessionManager.getInstance().getUser(session);
        log.debug("Recuperando esquema de seguridad.");
        user_sec_menu = MenuFactory.getInstance().getSecMenuUser(username);
    }
    
    public boolean isPwdExpire(){
        try {
            return UserSessionManager.getInstance().isExpirePwd(session);
        } catch (GBPersistenceException ex) {
            log.error("El sistema no pudo verificar el estado de la contraseña: "+ex.getMessage());
            UIMessageManagement.putException(ex);
            return false;
        }
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public boolean isValidHttpSession(){
        return session.equals((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true));
    }

    public DefaultMenuModel getUser_sec_menu() {
        return user_sec_menu;
    }
    
    public void setUser_sec_menu(DefaultMenuModel user_sec_menu) {
        this.user_sec_menu = user_sec_menu;
    }
    
    public String logout(){
        try {
            log.debug("Cerrando sesion del usuario: "+username);
            if (UserSessionManager.getInstance().disconectUser(session)) {
                UIMessageManagement.putInfoMessage("La sesion del usuario "+username+" finalizó correctamente.");
                log.debug("Cierre de sesion finalizado rediriguiendo a paginal principal.");
                return "logout";
            } else {
                log.debug("El cierre de sesion no se pudo lograr.");
                return "failed";
            }
        } catch (GBPersistenceException ex) {
            UIMessageManagement.putException(ex);
            log.error("El cierre de sesion fallo por que: "+ex.getMessage());
            return "failed";
        }
    }
}
