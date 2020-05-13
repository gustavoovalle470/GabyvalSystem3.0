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
import com.gabyval.referencesbo.security.profiling.GbMenuProfiling;
import com.gabyval.referencesbo.security.profiling.GbUserProfiling;
import com.gabyval.services.security.menu.GBMenuLinkService;
import com.gabyval.services.security.profiling.GBMenuProfilingServices;
import com.gabyval.services.security.profiling.GBUserProfiilingService;
import java.util.ArrayList;
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
    @Autowired
    private GBMenuProfilingServices profiling_service;
    @Autowired
    private GBUserProfiilingService sec_prof_service;
    private final Logger log = Logger.getLogger(SecurityMenuController.class);
    private static SecurityMenuController instance;
    
    public SecurityMenuController(){
        instance=this;
    }
    
    public static SecurityMenuController getInstance(){
        if(instance == null){
            instance=new SecurityMenuController();
        }
        return instance;
    }
    
    public HashMap<String, MenuDescriptor> getAllMenuSystem() throws GBException{
        HashMap<String, MenuDescriptor> allMenuSystem = new HashMap<>();
        if(menu_service == null){
            throw new GBException("El sistema no pudo detectar menus en el sistema.");
        }
        try {
            for(Object o : menu_service.getAll()){
                GbMenulinks menu = (GbMenulinks) o;
                allMenuSystem.put(menu.getGbMenuId(), new MenuDescriptor(menu.getGbMenuId(), menu.getGbMenuName(), menu.getGbPageView(), menu.getGbIcon(), menu.getGbMenuParId()));
            }
        } catch (GBPersistenceException ex) {
            log.error(ex);
        }
        return allMenuSystem;
    }
    
    public List<Object> getMenuSec(String username) throws GBException{
        log.debug("Ejecutando sentencia.");
        List menus = new ArrayList();
       try {
            HashMap<String, Object> parameters= new HashMap<>();
            parameters.put("gbUsername", username);
            HashMap<String, Object> parameters2= new HashMap<>();
            for (Object o :sec_prof_service.runSQL(GBSentencesRBOs.GBUSERPROFILING_FINDBYGBUSERNAME, parameters)){
                GbUserProfiling prof = (GbUserProfiling) o;
                log.debug("Obteniendo menus para el perfil "+prof.getGbUserProfilingPK().getGbProfile()+" que ha sido asignado al usuario "+username);
                parameters2.put("gbProfile", prof.getGbUserProfilingPK().getGbProfile());
                menus.addAll(profiling_service.runSQL(GBSentencesRBOs.GBMENUPROFILING_FINDBYGBPROFILE, parameters2));
                parameters2.remove("gbProfile", prof.getGbUserProfilingPK().getGbProfile());
            }
            return menus;
        } catch (GBPersistenceException ex) {
            log.error(ex.getMessage());
            throw new GBException("Se ha producido un error al tratar de obtener los menus del usuario "+username);
        }
    }
}
