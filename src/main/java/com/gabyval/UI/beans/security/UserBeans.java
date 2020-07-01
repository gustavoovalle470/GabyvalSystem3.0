/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.UI.beans.security;

import com.gabyval.Exceptions.GBException;
import com.gabyval.UI.utils.ADModuleConfigurationCons;
import com.gabyval.UI.utils.UIMessageManagement;
import com.gabyval.controllers.security.UserSessionManager;
import com.gabyval.controllers.system.ADCatalogController;
import com.gabyval.controllers.system.ADModuleConfigurationContoller;
import com.gabyval.controllers.user.GBStaffController;
import com.gabyval.controllers.user.GBUserController;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.users.GbStaff;
import com.gabyval.referencesbo.security.users.GbUsers;
import com.gabyval.services.security.utils.SecurityUtils;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author OvalleGA
 */
@ViewScoped
@ManagedBean(name = "UsersBean")
public class UserBeans {
    private final HttpSession session=(HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    private final Logger log = Logger.getLogger(UserBeans.class);
    private List<GbUsers> users;
    private List<SelectItem> loginStatuses;
    private List<SelectItem> operacionalStatuses;
    private int loginStatus;
    private int operacionalStatus;
    private boolean expirePwd=false;
    private int daysToExpire=0;
    private GbUsers newUser;
    private GbStaff newStaff;
    private List<SelectItem> idType;//Lista de posibles tipos de identificacion.
    private List<SelectItem> gender;//Lista de posibles generos.
    private int idTypeId;//Id del tipo de identificacion seleccionado.
    private int genderId;//Id del tipo de genero seleccionado.
    
    public UserBeans(){
        try {
            log.debug("GabyvalSystem - Obteniendo catalogo de documentos.");
            idType=ADCatalogController.getInstance().geCatalogList("DOCUMENT_TYPE");
            log.debug("GabyvalSystem - Obteniendo catalogo de generos.");
            gender=ADCatalogController.getInstance().geCatalogList("GENDER");
            loginStatuses=ADCatalogController.getInstance().geCatalogList("LOGIN_STATUS");
            operacionalStatuses=ADCatalogController.getInstance().geCatalogList("OPERATIVE_STAT");
            users=GBUserController.getInstance().getAllUsers();
            daysToExpire=ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_EXPIRE_TIME);
            assemblyNewUser();
            assemblyStaff();
        } catch (GBPersistenceException | GBException ex) {
            log.error(ex);
        }
    }

    public List<GbUsers> getUsers() {
        return users;
    }

    public void setUsers(List<GbUsers> users) {
        this.users = users;
    }

    public List<SelectItem> getLoginStatuses() {
        return loginStatuses;
    }

    public void setLoginStatuses(List<SelectItem> loginStatuses) {
        this.loginStatuses = loginStatuses;
    }

    public List<SelectItem> getOperacionalStatuses() {
        return operacionalStatuses;
    }

    public void setOperacionalStatuses(List<SelectItem> operacionalStatuses) {
        this.operacionalStatuses = operacionalStatuses;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public int getOperacionalStatus() {
        return operacionalStatus;
    }

    public void setOperacionalStatus(int operacionalStatus) {
        this.operacionalStatus = operacionalStatus;
    }

    public boolean isExpirePwd() {
        return expirePwd;
    }

    public void setExpirePwd(boolean expirePwd) {
        this.expirePwd = expirePwd;
    }

    public int getDaysToExpire() {
        return daysToExpire;
    }

    public void setDaysToExpire(int daysToExpire) {
        this.daysToExpire = daysToExpire;
    }

    public GbUsers getNewUser() {
        return newUser;
    }

    public void setNewUser(GbUsers newUser) {
        this.newUser = newUser;
    }

    public GbStaff getNewStaff() {
        return newStaff;
    }

    public void setNewStaff(GbStaff newStaff) {
        this.newStaff = newStaff;
    }

    public List<SelectItem> getIdType() {
        return idType;
    }

    public void setIdType(List<SelectItem> idType) {
        this.idType = idType;
    }

    public List<SelectItem> getGender() {
        return gender;
    }

    public void setGender(List<SelectItem> gender) {
        this.gender = gender;
    }

    public int getIdTypeId() {
        return idTypeId;
    }

    public void setIdTypeId(int idTypeId) {
        this.idTypeId = idTypeId;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }
    
    public void updateUser(RowEditEvent event){
        GbUsers userUpdate=(GbUsers)event.getObject();
        try {
            System.out.println("USER: "+userUpdate.getDaysPassedLastXgePwd());
            userUpdate.setLoginStatus(ADCatalogController.getInstance().
                    getCatalog(userUpdate.getLoginStatus().getCatalogId()));
            userUpdate.setOperativeStatus(ADCatalogController.getInstance().
                    getCatalog(userUpdate.getOperativeStatus().getCatalogId()));
            if(expirePwd){
                userUpdate=GBUserController.getInstance().expirePwd(userUpdate);
            }
            GBUserController.getInstance().save(userUpdate);
            UIMessageManagement.putInfoMessage("Se actualizo correctamente el usuario.");
        } catch (GBPersistenceException | GBException ex) {
            log.error(ex);
            UIMessageManagement.putErrorMessage("No se pudo actualizar el usuario.");
        }
    }

    private void assemblyNewUser() throws GBPersistenceException {
        newUser = new GbUsers();
        newUser.setCreateDt(Calendar.getInstance().getTime());
        newUser.setGbLastLogginDt(Calendar.getInstance().getTime());
        newUser.setGbLastPwdXgeDt(Calendar.getInstance().getTime());
        newUser.setGbLastUserXge(UserSessionManager.getInstance().getUser(session));
        newUser.setGbLastXgeDt(Calendar.getInstance().getTime());
        newUser.setRowversion(0);
    }

    private void assemblyStaff() {
        newStaff=new GbStaff();
        newStaff.setCreateDt(Calendar.getInstance().getTime());
        newStaff.setRowversion(0);
    }
    
    public void createUser(){
        try {
            newUser.setGbUsername(GBUserController.getInstance().getNewUserName(newStaff.getGbStaffName(), newStaff.getGbStaffSurname()));
            newUser.setGbPassword(SecurityUtils.encryptPwd(newUser.getGbUsername()));
            newUser.setLoginStatus(ADCatalogController.getInstance().getCatalog(0, "LOGIN_STATUS"));
            newUser.setOperativeStatus(ADCatalogController.getInstance().getCatalog(1, "OPERATIVE_STAT"));
            newUser=GBUserController.getInstance().expirePwd(newUser);
            newStaff.setGender(ADCatalogController.getInstance().getCatalog(genderId));
            newStaff.setIdType(ADCatalogController.getInstance().getCatalog(idTypeId));
            newStaff.setGbUsername(newUser.getGbUsername());
            newStaff.setGbUsers(newUser);
            newStaff.setGbPhoto(new byte[1]);
            if(!GBStaffController.getInstance().existProfile(newStaff)){
                GBUserController.getInstance().save(newUser);
                GBStaffController.getInstance().saveProfile(newStaff);
                UIMessageManagement.putInfoMessage("Se a creado un nuevo perfil con nombre de usuario: "+newUser.getGbUsername());
            }else{
                UIMessageManagement.putErrorMessage("El perfil de usuario ya se encuentra registrado. Verifique e intente nuevamente");
            }
        } catch (GBPersistenceException | GBException | NoSuchAlgorithmException ex) {
            log.error(ex);
        }
    }
}
