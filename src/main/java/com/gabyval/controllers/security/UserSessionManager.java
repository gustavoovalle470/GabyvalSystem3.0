/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.security;

import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.users.GbUsers;
import com.gabyval.services.security.users.GBUserService;
import java.util.Calendar;
import java.util.HashMap;
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
        GbUsers gbuser=user_service.load(users_online.get(session));
        gbuser.getGbLastPwdXgeDt();
        return false;
    }
    
    public String getUser(HttpSession session){
        return users_online.get(session);
    }
}
