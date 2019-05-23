/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.primefaces.customize.controllers.security;

import java.util.HashMap;
import javax.servlet.http.HttpSession;

/**
 *
 * @author OvalleGA
 */
public class UserSessionManager {
    private static UserSessionManager instance;
    private final HashMap<HttpSession, String> users_online;
    
    public UserSessionManager(){
        users_online = new HashMap<HttpSession, String>();
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
    
    public void connectUser(String user, HttpSession session){
        users_online.put(session, user);
    }
    
    public boolean disconectUser(HttpSession session){        
        users_online.remove(session);
        session.invalidate();
        return true;
    }
    
 /**   public void putAttribute(String user, String attribute, Object value){
        users_online.get(user).setAttribute(attribute, value);
    }
    
    public Object getAttribute(String user, String attribute){
        return users_online.get(user).getAttribute(attribute);
    }
 */   
    public String getUser(HttpSession session){
        return users_online.get(session);
    }
}
