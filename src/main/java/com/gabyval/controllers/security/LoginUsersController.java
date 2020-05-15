/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.security;

import com.gabyval.Exceptions.GBException;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.users.GbUsers;
import com.gabyval.services.security.users.GBUserService;
import com.gabyval.services.security.utils.SecurityUtils;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author OvalleGA
 */
@Controller
public class LoginUsersController {
    private final Logger log = Logger.getLogger(LoginUsersController.class);
    private static LoginUsersController instance;
    
    @Autowired
    private GBUserService user_service;
    
    public LoginUsersController(){
        log.debug("Creando instancia del controlador LoginUsersController");
        instance=this;
    }
    
    public static LoginUsersController getInstance(){
        if(instance == null){
            instance = new LoginUsersController();
        }
        return instance;
    }
    
    public boolean ValidateCredentials(String user, String password, HttpSession session) throws GBException, GBPersistenceException, NoSuchAlgorithmException {
        log.debug("Validando credenciales de usuario. Aplicando seguridad.");
        String pwd_enc = SecurityUtils.encryptPwd(password);
        log.debug("Iniciando validaciones:");
        isInvalidCrendetial(user, pwd_enc);
        log.debug("Registrando sesion de usuario:");
        UserSessionManager.getInstance().connectUser(user, session);
        log.debug("Redireccionando a la pagina principal.");
        return true;
    }

    private boolean isInvalidCrendetial(String user, String password) throws GBException, GBPersistenceException{
        log.debug("Obteniendo usuario de la base de datos: "+user);
        GbUsers gbuser=user_service.load(user);
        if(gbuser == null){
            log.error("El usuario "+user+" no pudo ser encontrado en la base de datos.");
            throw new GBException(10, user);
        }
        if(!password.equals(gbuser.getGbPassword())){
            log.error("Las credenciales proporcionadas no son validas.");
            throw new GBException(13, null);
        }
        if(gbuser.getLoginStatus().getCatalogItemId() == 1){
            log.error("El usuario "+user+" se encuentra conectado.");
            throw new GBException(11, user);
        }
        if(gbuser.getOperativeStatus().getCatalogItemId() != 1){
            log.error("El usuario "+user+" "+gbuser.getOperativeStatus().getCatalogDescription());
            throw new GBException(12, user);
        }
        log.debug("Las credenciales proporcionadas fueron validadas con exito.");
        return true;
    }
}
