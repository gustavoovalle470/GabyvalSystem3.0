/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.primefaces.customize.controllers.security;

import java.util.ArrayList;
import java.util.HashMap;
import org.primefaces.customize.UI.security.menu.MenuDescriptor;

/**
 *
 * @author gusta
 */
public class SecurityMenuManager {
    private static SecurityMenuManager instance;
    
    public static SecurityMenuManager getInstance(){
        if(instance == null){
            instance=new SecurityMenuManager();
        }
        return instance;
    }
    
    public HashMap<String, MenuDescriptor> getAllMenuSystem(){
        HashMap<String, MenuDescriptor> allMenuSystem = new HashMap<>();
        allMenuSystem.put("1", new MenuDescriptor("1", "Prueba 1", "Prueba 1", "Prueba 1", null));
        allMenuSystem.put("1.1", new MenuDescriptor("1.1", "Prueba 1.1", "Prueba 1.1", "Prueba 1.1", "1"));
        allMenuSystem.put("1.1.1", new MenuDescriptor("1.1.1", "Prueba 1.1.1", "Prueba 1.1.1", "Prueba 1.1.1", "1.1"));
        allMenuSystem.put("1.1.2", new MenuDescriptor("1.1.2", "Prueba 1.1.2", "Prueba 1.1.2", "Prueba 1.1.2", "1.1"));
        allMenuSystem.put("1.2", new MenuDescriptor("1.2", "Prueba 1.2", "Prueba 1.2", "Prueba 1.2", "1"));
        allMenuSystem.put("1.3", new MenuDescriptor("1.3", "Prueba 1.3", "Prueba 1.3", "Prueba 1.3", "1"));
        allMenuSystem.put("1.3.1", new MenuDescriptor("1.3.1", "Prueba 1.3.1", "Prueba 1.3.1", "Prueba 1.3.1", "1.3"));
        allMenuSystem.put("1.3.2", new MenuDescriptor("1.3.2", "Prueba 1.3.2", "Prueba 1.3.2", "Prueba 1.3.2", "1.3"));
        allMenuSystem.put("2", new MenuDescriptor("2", "Prueba 2", "Prueba 2", "Prueba 2", null));
        return allMenuSystem;
    }
    
    public ArrayList<String> getMenuSec(String username){
        ArrayList<String> menusId = new ArrayList<>();
        menusId.add("1.1.1");
        menusId.add("1.1.2");
        menusId.add("2");
        return menusId;
    }
}
