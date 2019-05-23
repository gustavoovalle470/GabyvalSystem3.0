/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.primefaces.customize.controllers.security;

import javax.servlet.http.HttpSession;

/**
 *
 * @author OvalleGA
 */
public class LoginUsers {
    private static LoginUsers instance;
    
    public static LoginUsers getInstance(){
        if(instance == null){
            instance = new LoginUsers();
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

    private static boolean isInvalidCrendetial(String user, String password) throws Exception{
        if(!user.equals("govalle") && !password.equals("ovalle")){
            return false;
        }
        return true;
    }
}
