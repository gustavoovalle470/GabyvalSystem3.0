/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.UI.beans.security;

import com.gabyval.Exceptions.GBException;
import com.gabyval.UI.security.menu.MenuFactory;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import com.gabyval.UI.utils.ADNavigationActions;
import com.gabyval.UI.utils.UIMessageManagement;
import com.gabyval.controllers.security.UserSessionManager;
import com.gabyval.controllers.user.GBStaffController;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.users.GbUsers;
import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.menu.DefaultMenuModel;

/**
 *
 * @author OvalleGA
 */
@RequestScoped
@ManagedBean(name = "UserSessionBean")
public class UserSessionBean implements Serializable{
    private final Logger log = Logger.getLogger(UserSessionBean.class);
    private final HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    private final GbUsers user= UserSessionManager.getInstance().getUser(session);
    private StreamedContent photo_profile;//La foto del perfil de usuario.
    private DefaultMenuModel user_sec_menu;
    private String pwd;
    private String repwd;

    public UserSessionBean(){
        log.debug("Recuperando esquema de seguridad.");
        try {
            System.out.println("CREANDO...");
            photo_profile=new DefaultStreamedContent(new ByteArrayInputStream( GBStaffController.getInstance().get_profile_info(user.getGbUsername()).getGbPhoto()), "image");
            user_sec_menu = MenuFactory.getInstance().getSecMenuUser(user.getGbUsername());
        } catch (GBException | GBPersistenceException ex) {
            log.error(ex);
        }
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
        return user.getGbUsername();
    }

    public void setUsername(String username) {
        this.user.setGbUsername(username);
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRepwd() {
        return repwd;
    }

    public void setRepwd(String repwd) {
        this.repwd = repwd;
    }

    public StreamedContent getPhoto_profile() {
        return photo_profile;
    }

    
    public String logout(){
        try {
            log.debug("Cerrando sesion del usuario: "+user.getGbUsername());
            if (UserSessionManager.getInstance().disconectUser(session)) {
                UIMessageManagement.putInfoMessage("La sesion del usuario "+user.getGbUsername()+" finalizó correctamente.");
                log.debug("Cierre de sesion finalizado redirigiendo a paginal principal.");
                return ADNavigationActions.LOGOUT;
            } else {
                log.debug("El cierre de sesion no se pudo lograr.");
                return ADNavigationActions.FAILED_LOGOUT;
            }
        } catch (GBPersistenceException ex) {
            UIMessageManagement.putException(ex);
            log.error("El cierre de sesion fallo por que: "+ex.getMessage());
            return ADNavigationActions.FAILED_LOGOUT;
        }
    }
    
    public String changePassword(){
        try {
            log.debug("Cambiando la contraseña para el usuario "+user.getGbUsername());
            log.debug("Pasword ingresada: "+pwd+" confirmacion: "+repwd);
            if(pwd != null && repwd != null && pwd.equals(repwd)){
                UserSessionManager.getInstance().changePwd(session, repwd);
                log.debug("Cerrando sesion de usuario para confirmar los cambios...");
                return logout();
            }else{
                log.debug("Las contraseñas ingresadas no coinciden.");
                UIMessageManagement.putCustomMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Las contraseñas ingresadas no coinciden.");                
                return ADNavigationActions.FAILED_LOGOUT;
            }
            
        } catch (GBPersistenceException | NoSuchAlgorithmException | GBException ex) {
            UIMessageManagement.putException(ex);
            log.error(ex.getMessage());
            return ADNavigationActions.FAILED_LOGOUT;
        }
    }
}
