/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.UI.beans.security;

import com.gabyval.Exceptions.GBException;
import com.gabyval.UI.utils.ADModuleConfigurationCons;
import com.gabyval.UI.utils.UIMessageManagement;
import com.gabyval.controllers.system.ADCatalogController;
import com.gabyval.controllers.system.ADModuleConfigurationContoller;
import com.gabyval.controllers.user.GBUserController;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.users.GbUsers;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author OvalleGA
 */
@ViewScoped
@ManagedBean(name = "UsersBean")
public class UserBeans {
    private final Logger log = Logger.getLogger(UserBeans.class);
    private List<GbUsers> users;
    private List<SelectItem> loginStatuses;
    private List<SelectItem> operacionalStatuses;
    private int loginStatus;
    private int operacionalStatus;
    private boolean expirePwd=false;
    private int daysToExpire=0;
    
    public UserBeans(){
        try {
            loginStatuses=ADCatalogController.getInstance().geCatalogList("LOGIN_STATUS");
            operacionalStatuses=ADCatalogController.getInstance().geCatalogList("OPERATIVE_STAT");
            users=GBUserController.getInstance().getAllUsers();
            daysToExpire=ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_EXPIRE_TIME);
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
}
