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
import com.gabyval.UI.utils.ADNavigationActions;
import com.gabyval.UI.utils.UIMessageManagement;
import com.gabyval.controllers.security.UserSessionManager;
import com.gabyval.controllers.user.GBStaffController;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.users.GbStaff;
import com.mysql.cj.jdbc.Blob;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.StreamedContent;
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
    private String pwd;
    private String repwd;
    private GbStaff profile_info;

    public UserSessionBean(){
        log.info("Obteniendo datos de sesion.");
        session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        log.info("Obteniendo usuario conectado.");
        username = UserSessionManager.getInstance().getUser(session);
        try {
            profile_info=GBStaffController.getInstance().get_profile_info(username);
        } catch (GBException ex) {
            UIMessageManagement.putException(ex);
        }
        log.info("Recuperando esquema de seguridad.");
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

    public GbStaff getProfile_info() {
        return profile_info;
    }

    public void setProfile_info(GbStaff profile_info) {
        this.profile_info = profile_info;
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
    
    public String logout(){
        try {
            log.info("Cerrando sesion del usuario: "+username);
            if (UserSessionManager.getInstance().disconectUser(session)) {
                UIMessageManagement.putInfoMessage("La sesion del usuario "+username+" finalizó correctamente.");
                log.info("Cierre de sesion finalizado redirigiendo a paginal principal.");
                return ADNavigationActions.LOGOUT;
            } else {
                log.info("El cierre de sesion no se pudo lograr.");
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
            log.info("Cambiando la contraseña para el usuario "+username);
            log.info("Pasword ingresada: "+pwd+" confirmacion: "+repwd);
            if(pwd != null && repwd != null && pwd.equals(repwd)){
                UserSessionManager.getInstance().changePwd(session, repwd);
                log.info("Cerrando sesion de usuario para confirmar los cambios...");
                return logout();
            }else{
                log.info("Las contraseñas ingresadas no coinciden.");
                UIMessageManagement.putCustomMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Las contraseñas ingresadas no coinciden.");                
                return ADNavigationActions.FAILED_LOGOUT;
            }
            
        } catch (GBPersistenceException | NoSuchAlgorithmException | GBException ex) {
            UIMessageManagement.putException(ex);
            log.error(ex.getMessage());
            return ADNavigationActions.FAILED_LOGOUT;
        }
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        profile_info.setGbPhoto(event.getFile().getContents());
        try {
            GBStaffController.getInstance().saveProfile(profile_info);
        } catch (GBException ex) {
            UIMessageManagement.putException(ex);
            log.error(ex.getMessage());
        }
        UIMessageManagement.putInfoMessage("FOTO GUARDADA CON EXITO");
    }
    
    public StreamedContent getProfPhoto(){
        System.out.println("Tratando de cargar la foto");
        System.out.println("Instancia del profile: "+profile_info);
        System.out.println("Streaming de la foto: "+profile_info.getGbPhoto());
        if(profile_info.getGbPhoto() == null){
            System.out.println("Nulo");
            return null;
        }else{
            try {
                Blob blob = new Blob(profile_info.getGbPhoto(), null);
                return new ByteArrayContent(blob.getBytes(1, (int) blob.length()));
                //return new DefaultStreamedContent(new ByteArrayInputStream(profile_info.getGbPhoto()));
            } catch (SQLException ex) {
                return null;
            }
        }
    }
}
