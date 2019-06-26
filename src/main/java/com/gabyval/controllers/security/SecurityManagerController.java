/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.security;

import com.gabyval.Exceptions.GBException;
import com.gabyval.UI.utils.ADModuleConfigurationCons;
import com.gabyval.UI.utils.UIMessageManagement;
import com.gabyval.controllers.system.ADModuleConfigurationContoller;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.users.GbPwdHistoryPK;
import com.gabyval.referencesbo.security.users.GbUsers;
import com.gabyval.services.security.users.GBPwdHistoryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author OvalleGA
 */
@Controller
public class SecurityManagerController {
    private final Logger log = Logger.getLogger(SecurityManagerController.class);
    private static SecurityManagerController instance;
    @Autowired
    private GBPwdHistoryService pwd_his_service;
    
    public SecurityManagerController(){
        log.debug("Creando instancia del controlador SecurityManagerController");
        instance=this;
    }
    
    public static SecurityManagerController getInstance(){
        if(instance == null){
            instance=new SecurityManagerController();
        }
        return instance;
    }
    
    public void saveOldPwd(String gbPassword, GbUsers gbUsers) throws GBPersistenceException {
        System.out.println("Salvando la contraseña anterior.");
//        GbPwdHistory pwd_hist = new GbPwdHistory();
//        for(Object r:pwd_his_service.runSQL("SELECT COUNT(a.id.gbUsername) FROM GbPwdHistory where a.id.gbUsername='"+gbUsers.getGbUsername()+"'")){
//            System.out.println("Guardadas: "+(Integer)r);
//        }
//        pwd_hist.setGbUsers(gbUsers);
//        pwd_hist.setGbPwdHistoryPK(new GbPwdHistoryPK(gbUsers.getGbUsername(), gbPassword));
//        pwd_hist.setCreateDt(Calendar.getInstance().getTime());
//        pwd_hist.setGbPwdInsDt(Calendar.getInstance().getTime());
//        pwd_hist.setRowversion(0);
//        pwd_his_service.save(pwd_hist);
    }
    
    public boolean isPwdUsed(String user, String newPwd) throws GBPersistenceException{
        return pwd_his_service.load(new GbPwdHistoryPK(user, newPwd))!=null;
    }
    
    public boolean isAccomplishSecPol(String pwd) throws GBException{
        String regex = "^";
        if(ADModuleConfigurationContoller.getInstance().getBooleanConfValue(ADModuleConfigurationCons.PWD_ONE_NUM)){
            regex=regex+"(?=\\w*\\d)";
        }
        if(ADModuleConfigurationContoller.getInstance().getBooleanConfValue(ADModuleConfigurationCons.PWD_ONE_MAY)){
            regex=regex+"(?=\\w*[A-Z])";
        }
        if(ADModuleConfigurationContoller.getInstance().getBooleanConfValue(ADModuleConfigurationCons.PWD_ONE_MIN)){
            regex=regex+"(?=\\w*[a-z])";
        }
        regex=regex+"\\S{"+ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_MIN_SIZE)
                   +","+ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_MAX_SIZE)+"}$";
        return pwd.matches(regex);
    }
    
    private boolean isValidPwd(String repwd) throws GBPersistenceException {
        try {
            return !SecurityManagerController.getInstance().isPwdUsed("", repwd) &&
                    SecurityManagerController.getInstance().isAccomplishSecPol(repwd);
        } catch (GBException ex) {
            UIMessageManagement.putException(ex);
            log.fatal(ex);
            return false;
        }
    }
}
