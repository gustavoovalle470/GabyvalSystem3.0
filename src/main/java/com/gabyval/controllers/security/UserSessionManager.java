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
import com.gabyval.referencesbo.security.users.GbPwdHistory;
import com.gabyval.referencesbo.security.users.GbPwdHistoryPK;
import com.gabyval.referencesbo.security.users.GbUsers;
import com.gabyval.services.security.users.GBPwdHistoryService;
import com.gabyval.services.security.users.GBUserService;
import com.gabyval.services.security.utils.SecurityUtils;
import com.gabyval.services.system.ADModuleConfigurationService;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author OvalleGA
 */
@Controller
@Transactional
public class UserSessionManager {
    private final Logger log = Logger.getLogger(UserSessionManager.class);
    private static UserSessionManager instance;
    private final HashMap<HttpSession, String> users_online;
    @Autowired
    private GBUserService user_service;
    
    public UserSessionManager(){
        instance=this;
        users_online = new HashMap<>();
    }
    
    public static UserSessionManager getInstance(){
        if(instance == null){
            instance = new UserSessionManager();
        }
        return instance;
    }
    
    public boolean isUserConnected(HttpSession session){
        return users_online.containsKey(session);
    }
    
    public void connectUser(String user, HttpSession session) throws GBPersistenceException{
        log.debug("Registrando sesion de usuario");
        GbUsers gbuser=user_service.load(user);
        log.debug("Cambiando estado de sesion de usuario");
        gbuser.setGbLoginStatus(1);
        log.debug("Cambiando fecha de ultimo inicio");
        gbuser.setGbLastLogginDt(Calendar.getInstance().getTime());
        log.debug("Guardando cambios sobre el usuario");
        user_service.save(gbuser);
        log.debug("Registrando sesion HTTP "+session.getId());
        users_online.put(session, user);
        log.debug("Registro finalizado con éxito.");
    }
    
    public boolean disconectUser(HttpSession session) throws GBPersistenceException{        
        log.debug("Invalidando sesion.");
        GbUsers gbuser=user_service.load(users_online.get(session));
        log.debug("Cerrando sesion de usuario en base de datos.");
        gbuser.setGbLoginStatus(0);
        log.debug("Guardando cambios de cambio de estado.");
        user_service.save(gbuser);
        log.debug("Removiendo sesuib de usuario.");
        users_online.remove(session);
        log.debug("Invalidando sesion de usuario.");
        session.invalidate();
        log.debug("Cierre de sesion completado.");
        return true;
    }
  
    public boolean isExpirePwd(HttpSession session) throws GBPersistenceException{
        try {
            log.debug("Verificando si la contraseña de usuario aun es valida, obteniendo usuario...");
            GbUsers gbuser=user_service.load(users_online.get(session));
            log.debug("Validando, ultimo cambio de contraseña fue el "+gbuser.getGbLastPwdXgeDt().toString());
            int days_last_change=(int) ((Calendar.getInstance().getTime().getTime()-gbuser.getGbLastPwdXgeDt().getTime())/86400000);
            log.debug("Consultando politicas de vencimiento del moduleconfiguration, vencimiento "
                    + "cada "+1+" dias. Han pasado "+days_last_change+" dias desde el ultimo cambio. "
                    + "Se requiere cambio? "+(days_last_change>=
                      ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_EXPIRE_TIME)));
            return (days_last_change>=ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_EXPIRE_TIME));
        } catch (GBException ex) {
            UIMessageManagement.putException(ex);
            log.error(ex.getMessage());
            return false;
        }
    }
    
    public void changePwd(HttpSession session, String newPwd){
        try {
            log.debug("Cambiando credenciales de ingreso. Obteniendo usuario...");
            GbUsers gbuser=user_service.load(users_online.get(session));
            String old_pwd = gbuser.getGbPassword();
            gbuser.setGbPassword(SecurityUtils.encryptPwd(newPwd));
            log.debug("Guardando contraseña ...");
            user_service.save(gbuser);
            log.debug("Credenciales actualizadas con exito. Guardando la contraseña anterior, para efectuar politicas de seguridad.");
            SecurityManagerController.getInstacnce().saveOldPwd(old_pwd, gbuser);
            log.debug("Contraseña anterior salvada. Finalizado el proceso.");
        } catch (GBPersistenceException | NoSuchAlgorithmException ex) {
            UIMessageManagement.putException(ex);
            log.error(ex.getMessage());
        }
    }
    
    public String getUser(HttpSession session){
        return users_online.get(session);
    }   
}
