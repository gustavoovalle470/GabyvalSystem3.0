/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.UI.beans.profile;

import com.gabyval.Exceptions.GBException;
import com.gabyval.UI.utils.UIMessageManagement;
import com.gabyval.controllers.security.UserSessionManager;
import com.gabyval.controllers.user.GBStaffController;
import com.gabyval.referencesbo.security.users.GbStaff;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


/**
 *
 * @author OvalleGA
 */
@SessionScoped
@ManagedBean(name = "ProfileBean")
public class UserProfileBean implements Serializable{
    private final Logger log = Logger.getLogger(UserProfileBean.class);
    private StreamedContent photo_profile;
    private GbStaff profile_info;
    
    public UserProfileBean(){
        try {
            log.info("Obteniendo el perfil de usuario.");
            profile_info=GBStaffController.getInstance().get_profile_info(UserSessionManager.getInstance().getUser((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)));
            photo_profile=new DefaultStreamedContent(new ByteArrayInputStream(profile_info.getGbPhoto()), "image/png");
            log.info("Perfil de usuario cargado.");
        } catch (GBException ex) {
            log.error("El perfil del usuario no pudo ser cargado.");
            UIMessageManagement.putException(ex);
        }
    }

    public StreamedContent getPhoto_profile() {
        return photo_profile;
    }

    public void setPhoto_profile(StreamedContent photo_profile) {
        this.photo_profile = photo_profile;
    }
    
    public GbStaff getProfile_info() {
        return profile_info;
    }

    public void setProfile_info(GbStaff profile_info) {
        this.profile_info = profile_info;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        profile_info.setGbPhoto(event.getFile().getContents());
        photo_profile=new DefaultStreamedContent(new ByteArrayInputStream(profile_info.getGbPhoto()), "image/png");
        try {
            GBStaffController.getInstance().saveProfile(profile_info);
        } catch (GBException ex) {
            UIMessageManagement.putException(ex);
            log.error(ex.getMessage());
        }
        photo_profile=getPhoto_profile();
        UIMessageManagement.putInfoMessage("FOTO GUARDADA CON EXITO");
    }
    
    public void saveProfile(){
        try {
            photo_profile=new DefaultStreamedContent(new ByteArrayInputStream(profile_info.getGbPhoto()), "image/png");
            GBStaffController.getInstance().saveProfile(profile_info);
        } catch (GBException ex) {
            UIMessageManagement.putException(ex);
            log.error(ex.getMessage());
        }
    }
}
