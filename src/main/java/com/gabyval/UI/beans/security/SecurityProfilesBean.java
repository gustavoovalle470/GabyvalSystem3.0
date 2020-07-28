/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.UI.beans.security;

import com.gabyval.UI.utils.UIMessageManagement;
import com.gabyval.controllers.security.GbSecurityProfileController;
import com.gabyval.controllers.security.UserSessionManager;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.profiling.GbSecurityProfile;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author OvalleGA
 */
@ViewScoped
@ManagedBean(name = "SecurityProfilesBean")
public class SecurityProfilesBean {
    private final HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    private final Logger log = Logger.getLogger(UserBeans.class);
    private List<GbSecurityProfile> profiles;
    private boolean isProfileActive=true;
    private GbSecurityProfile newProfile=new GbSecurityProfile();
    
    public SecurityProfilesBean(){
        try {
            profiles=GbSecurityProfileController.getInstance().getAllProfiles();
        } catch (GBPersistenceException ex) {
            UIMessageManagement.putErrorMessage("No se pudieron cargar los perfiles de seguridad. Si el problema persiste contacte con su administrador.");
            log.error(ex);
        }
    }

    public List<GbSecurityProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<GbSecurityProfile> profiles) {
        this.profiles = profiles;
    }

    public boolean isIsProfileActive() {
        return isProfileActive;
    }

    public void setIsProfileActive(boolean isProfileActive) {
        this.isProfileActive = isProfileActive;
    }

    public GbSecurityProfile getNewProfile() {
        return newProfile;
    }

    public void setNewProfile(GbSecurityProfile newProfile) {
        this.newProfile = newProfile;
    }
    
    public void updateProfile(RowEditEvent event){
        GbSecurityProfile profileUpdate=(GbSecurityProfile)event.getObject();
         if(profileUpdate.getGbProfile().equals("ADMINISTRATOR")){
             UIMessageManagement.putErrorMessage("El perfil de seguridad ADMINISTRADOR no puede deshabilitarse. Verifique e intente nuevamente");
         }else{
            try {
                profileUpdate.setGbLastXgeDt(Calendar.getInstance().getTime());
                profileUpdate.setGbLastUserXge(UserSessionManager.getInstance().getUser(session).getGbUsername());
                profileUpdate.setRowversion(profileUpdate.getRowversion()+1);
                profileUpdate.setGbProfileStatus(isProfileActive?1:0);
                GbSecurityProfileController.getInstance().update(profileUpdate);
                UIMessageManagement.putInfoMessage("Se ha actualizado el perfil de seguridad exitosamente.");
            } catch (GBPersistenceException ex) {
                UIMessageManagement.putErrorMessage("No se pudo actualizar el perfil de seguridad "+profileUpdate.getGbProfile()+", si el problema persiste contacte con su administrador.");
                log.error(ex);
            }
         }
    }
     
    public void createSecurityProfile(){
        try {
            newProfile.setGbProfileStatus(isProfileActive?1:0);
            newProfile.setGbLastXgeDt(Calendar.getInstance().getTime());
            newProfile.setGbLastUserXge(UserSessionManager.getInstance().getUser(session).getGbUsername());
            newProfile.setCreateDt(Calendar.getInstance().getTime());
            newProfile.setRowversion(0);
            GbSecurityProfileController.getInstance().save(newProfile);
            UIMessageManagement.putInfoMessage("El perfil de seguridad fue creado correctamente.");
        } catch (Exception ex) {
            UIMessageManagement.putException(ex);
            log.error(ex);
        }
    }
}
