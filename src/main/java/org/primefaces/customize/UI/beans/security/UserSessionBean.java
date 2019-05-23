/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.primefaces.customize.UI.beans.security;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.customize.UI.security.menu.MenuFactory;
import org.primefaces.customize.UI.utils.UIMessageManagement;
import org.primefaces.customize.controllers.security.UserSessionManager;
import org.primefaces.model.menu.DefaultMenuModel;

/**
 *
 * @author OvalleGA
 */
@SessionScoped
@ManagedBean(name = "UserSessionBean")
public class UserSessionBean implements Serializable{
    private String username;
    private final HttpSession session;
    private DefaultMenuModel user_sec_menu;

    public UserSessionBean(){
        session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        username = UserSessionManager.getInstance().getUser(session);
        System.out.println("Pre cargando la sesion");
        user_sec_menu = MenuFactory.getInstance().getSecMenuUser(username);
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
        System.out.println("Cerrar la sesion para el usuario: "+username);
        if (UserSessionManager.getInstance().disconectUser(session)) {
            UIMessageManagement.putInfoMessage("La sesion del usuario "+username+" finalizó correctamente.");
            return "logout";
        } else {
            return "failed";
        }
    }
}
