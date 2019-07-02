/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.security;

import com.gabyval.Exceptions.GBException;
import com.gabyval.UI.utils.ADModuleConfigurationCons;
import com.gabyval.controllers.system.ADModuleConfigurationContoller;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.GBSentencesRBOs;
import com.gabyval.referencesbo.security.users.GbPwdHistory;
import com.gabyval.referencesbo.security.users.GbPwdHistoryPK;
import com.gabyval.referencesbo.security.users.GbUsers;
import com.gabyval.services.security.users.GBPwdHistoryService;
import com.gabyval.services.security.utils.SecurityUtils;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.hibernate.Query;
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
        log.info("Creando instancia del controlador SecurityManagerController");
        instance=this;
    }
    
    public static SecurityManagerController getInstance(){
        if(instance == null){
            instance=new SecurityManagerController();
        }
        return instance;
    }
    
    public GbUsers changePwd(GbUsers user, String newPwd) throws GBPersistenceException, GBException, NoSuchAlgorithmException{
        log.info("Validando politicas de seguridad para la nueva contraseña.");
        if(!isPwdUsed(user.getGbUsername(), newPwd) && isAccomplishSecPol(newPwd)){
            log.info("La nueva contraseña cumple con las politicas de seguridad configuradas. Guardando la contraseña utilizada previamente.");
            saveOldPwd(user.getGbPassword(), user);
            log.info("Cifrando y aplicando cambios a las credenciales de usuario.");
            user.setGbPassword(SecurityUtils.encryptPwd(newPwd));
            user.setGbLastPwdXgeDt(Calendar.getInstance().getTime());
            log.info("Cambios sobre las credenciales de usuario fueron aplicados exitosamente.");
        }else{
            log.error("La contraseña no cumple con las politicas de seguridad.");
            throw new GBException("La contraseña no cumple con las politicas de seguridad.");
        }
        return user;
    }
    
    public void saveOldPwd(String gbPassword, GbUsers gbUsers) throws GBPersistenceException, GBException {
        log.info("Salvando la contraseña anterior.");
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("gbUsername", "admin");
        if(pwd_his_service.runSQL(GBSentencesRBOs.GBPWDHISTORY_FINDBYGBUSERNAME, parameters).size()>
           ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_CANT_SAVE)){
            // FALTA AQUI!!!!!!!!!!!!!
        }else{
            GbPwdHistory pwd_hist = new GbPwdHistory();
            pwd_hist.setGbUsers(gbUsers);
            pwd_hist.setGbPwdHistoryPK(new GbPwdHistoryPK(gbUsers.getGbUsername(), gbPassword));
            pwd_hist.setCreateDt(Calendar.getInstance().getTime());
            pwd_hist.setGbPwdInsDt(Calendar.getInstance().getTime());
            pwd_hist.setRowversion(0);
            pwd_his_service.save(pwd_hist);
        }
    }
    
    public boolean isPwdUsed(String user, String newPwd) throws GBPersistenceException, NoSuchAlgorithmException{
        log.info("La contraseña esta en el historico de contraseñas para el usuario? "+pwd_his_service.load(new GbPwdHistoryPK(user, newPwd))!=null);
        return pwd_his_service.load(new GbPwdHistoryPK(user, SecurityUtils.encryptPwd(newPwd)))!=null;
    }
    
    public boolean isAccomplishSecPol(String pwd) throws GBException, GBPersistenceException{
        log.info("Buscando las politicas de seguridad, para validar la contraseña.");
        String regex = "^";
        if(ADModuleConfigurationContoller.getInstance().getBooleanConfValue(ADModuleConfigurationCons.PWD_ONE_NUM)){
            log.info("La contraseña debe contener al menos un digito.");
            regex=regex+"(?=\\w*\\d)";
        }
        if(ADModuleConfigurationContoller.getInstance().getBooleanConfValue(ADModuleConfigurationCons.PWD_ONE_MAY)){
            log.info("La contraseña debe contener al una mayuscula.");
            regex=regex+"(?=\\w*[A-Z])";
        }
        if(ADModuleConfigurationContoller.getInstance().getBooleanConfValue(ADModuleConfigurationCons.PWD_ONE_MIN)){
            log.info("La contraseña debe contener al una minuscula.");
            regex=regex+"(?=\\w*[a-z])";
        }
        log.info("La contraseña debe tener una longitud minima "
                  +ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_MIN_SIZE)
                  +" y maxima "
                  +ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_MAX_SIZE)+".");
        regex=regex+"\\S{"+ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_MIN_SIZE)
                   +","+ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_MAX_SIZE)+"}$";
        log.info("La contraseña cumple con las politicas anteriormente listadas? "+pwd.matches(regex));
        return pwd.matches(regex);
    }
}
