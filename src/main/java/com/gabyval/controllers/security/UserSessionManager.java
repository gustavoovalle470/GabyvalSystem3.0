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
import com.gabyval.referencesbo.security.users.GbUsers;
import com.gabyval.services.security.users.GBUserService;
import com.gabyval.services.security.utils.SecurityUtils;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import javax.faces.application.FacesMessage;
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
        log.info("Registrando sesion de usuario");
        GbUsers gbuser=user_service.load(user);
        log.info("Cambiando estado de sesion de usuario");
        gbuser.setGbLoginStatus(1);
        log.info("Cambiando fecha de ultimo inicio");
        gbuser.setGbLastLogginDt(Calendar.getInstance().getTime());
        log.info("Guardando cambios sobre el usuario");
        user_service.save(gbuser);
        log.info("Registrando sesion HTTP "+session.getId());
        users_online.put(session, user);
        log.info("Registro finalizado con éxito.");
    }
    
    public boolean disconectUser(HttpSession session) throws GBPersistenceException{        
        log.info("Invalidando sesion.");
        GbUsers gbuser=user_service.load(users_online.get(session));
        log.info("Cerrando sesion de usuario en base de datos.");
        gbuser.setGbLoginStatus(0);
        log.info("Guardando cambios de cambio de estado.");
        user_service.save(gbuser);
        log.info("Removiendo sesión de usuario de la lista de usuarios conectados.");
        users_online.remove(session);
        log.info("Invalidando sesion HTPP de usuario.");
        session.invalidate();
        log.info("Cierre de sesion completado.");
        return true;
    }
  
    public boolean isExpirePwd(HttpSession session) throws GBPersistenceException{
        try {
            log.info("Verificando si la contraseña de usuario aun es valida, obteniendo usuario...");
            GbUsers gbuser=user_service.load(users_online.get(session));
            log.info("Validando, ultimo cambio de contraseña fue el "+gbuser.getGbLastPwdXgeDt().toString());
            int days_last_change=(int) ((Calendar.getInstance().getTime().getTime()-gbuser.getGbLastPwdXgeDt().getTime())/86400000);
            log.info("Consultando politicas de vencimiento del moduleconfiguration, vencimiento "
                    + "cada "+ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_EXPIRE_TIME)+" dias. Han pasado "+days_last_change+" dias desde el ultimo cambio. "
                    + "Se requiere cambio? "+(days_last_change>=
                      ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_EXPIRE_TIME)));
            return (days_last_change>=ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_EXPIRE_TIME));
        } catch (GBException ex) {
            UIMessageManagement.putException(ex);
            log.error(ex.getMessage());
            return false;
        }
    }
    
    public void changePwd(HttpSession session, String newPwd) throws GBPersistenceException, NoSuchAlgorithmException, GBException{
        log.info("Cambiando credenciales de ingreso. Obteniendo usuario.");
            GbUsers gbuser=user_service.load(users_online.get(session));
        if(!gbuser.getGbPassword().equals(SecurityUtils.encryptPwd(newPwd))){
            log.info("inicia el proceso de cambio de contraseña.");
            user_service.save(SecurityManagerController.getInstance().changePwd(gbuser, newPwd));
            log.info("La contraseña se cambio exitosamente.");
            UIMessageManagement.putCustomMessage(FacesMessage.SEVERITY_INFO, "Exito", "La contraseña se cambio exitosamente.");
        }else{
            throw new GBException("La contraseña no puede ser igual a la que ya tiene asignada");
        }
    }
    
    public String getUser(HttpSession session){
        return users_online.get(session);
    }   
}