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
import com.mysql.cj.jdbc.Blob;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.StreamedContent;


/**
 *
 * @author OvalleGA
 */
@RequestScoped
@ManagedBean(name = "ProfileBean")
public class UserProfileBean {
    private final Logger log = Logger.getLogger(UserProfileBean.class);
    private GbStaff profile_info;
    
    public UserProfileBean(){
        try {
            log.info("Obteniendo el perfil de usuario.");
            profile_info=GBStaffController.getInstance().get_profile_info(UserSessionManager.getInstance().getUser((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)));
            log.info("Perfil de usuario cargado.");
        } catch (GBException ex) {
            log.error("El perfil del usuario no pudo ser cargado.");
            UIMessageManagement.putException(ex);
        }
    }

    public GbStaff getProfile_info() {
        return profile_info;
    }

    public void setProfile_info(GbStaff profile_info) {
        this.profile_info = profile_info;
    }
    
    public StreamedContent getProfPhoto(){
        if(profile_info.getGbPhoto() == null){
            return null;
        }else{
            try {
                Blob blob = new Blob(profile_info.getGbPhoto(), null);
                return new ByteArrayContent(blob.getBytes(1, (int) blob.length()));
            } catch (SQLException ex) {
                return null;
            }
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
    
    public void saveProfile(){
        try {
            GBStaffController.getInstance().saveProfile(profile_info);
        } catch (GBException ex) {
            UIMessageManagement.putException(ex);
            log.error(ex.getMessage());
        }
    }
}
