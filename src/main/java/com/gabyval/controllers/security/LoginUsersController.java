/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.security;

import com.gabyval.referencesbo.security.users.GbUsers;
import com.gabyval.services.security.users.GBUserService;
import java.io.Serializable;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author OvalleGA
 */
@Controller
public class LoginUsersController {
    private static LoginUsersController instance;
    
    @Autowired
    private GBUserService user_service;
    
    public LoginUsersController(){
        instance=this;
        System.out.println("LoginController");
    }
    
    public static LoginUsersController getInstance(){
        if(instance == null){
            instance = new LoginUsersController();
        }
        return instance;
    }
    
    public boolean ValidateCredentials(String user, String password, HttpSession session) throws Exception{
        String msg ="No se pudo iniciar sesión porque: ";
        msg = msg+(user == null?"\n - El usuario no puede ser nulo":"")
                 +(password == null?"\n - La contraseña no puede ser nula":"")
                 +(UserSessionManager.getInstance().isUserConnected(session)?"\n - El usuario "+user+" ya tiene una sesión abierta.":"")
                 +(!isInvalidCrendetial(user, password)?"\n - El usuario y/o contraseña no son validos.":"");
        System.out.println("El mensaje es: "+msg);
        if(!msg.equals("No se pudo iniciar sesión porque: ")){
            throw new Exception(msg);
        }
        UserSessionManager.getInstance().connectUser(user, session);
        return true;
    }

    private boolean isInvalidCrendetial(String user, String password) throws Exception{
        GbUsers gbuser=user_service.load(user);
        System.out.println("Usuario recuperado: "+gbuser.getGbUsername());
        System.out.println("Contraseña recuperado: "+gbuser.getGbPassword());
        if(gbuser == null || !password.equals(gbuser.getGbPassword())){
            return false;
        }
        return true;
    }
}
