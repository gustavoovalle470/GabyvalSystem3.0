/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.security;

import com.gabyval.Exceptions.GBException;
import java.util.HashMap;
import com.gabyval.UI.security.menu.MenuDescriptor;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.GBSentencesRBOs;
import com.gabyval.referencesbo.security.menu.GbMenulinks;
import com.gabyval.services.security.menu.GBMenuLinkService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author gusta
 */
@Controller
public class SecurityMenuController {
    @Autowired
    private GBMenuLinkService menu_service;
    private final Logger log = Logger.getLogger(SecurityMenuController.class);
    private static SecurityMenuController instance;
    
    public static SecurityMenuController getInstance(){
        if(instance == null){
            instance=new SecurityMenuController();
        }
        return instance;
    }
    
    public HashMap<String, MenuDescriptor> getAllMenuSystem(){
        HashMap<String, MenuDescriptor> allMenuSystem = new HashMap<>();
        try {
            for(Object o : menu_service.getAll()){
                GbMenulinks menu = (GbMenulinks) o;
                allMenuSystem.put(menu.getGbMenuId(), new MenuDescriptor(menu.getGbMenuId(), menu.getGbMenuName(), menu.getGbPageView(), menu.getGbIcon(), menu.getGbMenuParId()));
            }
        } catch (GBPersistenceException ex) {
            log.error(ex);
        }
//        allMenuSystem.put("1", new MenuDescriptor("1", "Prueba 1", "Prueba 1", "Prueba 1", null));
//        allMenuSystem.put("1.1", new MenuDescriptor("1.1", "Prueba 1.1", "Prueba 1.1", "Prueba 1.1", "1"));
//        allMenuSystem.put("1.1.1", new MenuDescriptor("1.1.1", "Prueba 1.1.1", "Prueba 1.1.1", "Prueba 1.1.1", "1.1"));
//        allMenuSystem.put("1.1.2", new MenuDescriptor("1.1.2", "Prueba 1.1.2", "Prueba 1.1.2", "Prueba 1.1.2", "1.1"));
//        allMenuSystem.put("1.2", new MenuDescriptor("1.2", "Prueba 1.2", "Prueba 1.2", "Prueba 1.2", "1"));
//        allMenuSystem.put("1.3", new MenuDescriptor("1.3", "Prueba 1.3", "Prueba 1.3", "Prueba 1.3", "1"));
//        allMenuSystem.put("1.3.1", new MenuDescriptor("1.3.1", "Prueba 1.3.1", "Prueba 1.3.1", "Prueba 1.3.1", "1.3"));
//        allMenuSystem.put("1.3.2", new MenuDescriptor("1.3.2", "Prueba 1.3.2", "Prueba 1.3.2", "Prueba 1.3.2", "1.3"));
//        allMenuSystem.put("2", new MenuDescriptor("2", "Prueba 2", "Prueba 2", "Prueba 2", null));
        return allMenuSystem;
    }
    
    public List<Object> getMenuSec(String username) throws GBException{
        log.info("Ejecutando sentencia.");
//       try {
            HashMap<String, Object> parameters= new HashMap<>();
            parameters.put("gbUsername", username);
//            return menu_service.runSQL(GBSentencesRBOs.GBMENULINKS_FINDBYUSERNAME, parameters);
//        } catch (GBPersistenceException ex) {
//            log.error(ex.getMessage());
//            throw new GBException("Se ha producido un error al tratar de obtener los menus del usuario "+username);
//        }
//        ArrayList<String> menusId = new ArrayList<>();
//        menusId.add("1.1.1");
//        menusId.add("1.1.2");
//        menusId.add("2");
//        return menusId;
        return null;
    }
}
