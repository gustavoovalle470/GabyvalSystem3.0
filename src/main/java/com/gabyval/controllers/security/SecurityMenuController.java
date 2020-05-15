/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.security;

import com.gabyval.Exceptions.GBException;
import java.util.HashMap;
import com.gabyval.persistence.exception.GBPersistenceException;
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
    
    
    
    public List<Object> getMenuSec(String username) throws GBException, GBPersistenceException{
        log.debug("Recuperando menus para el usuario: "+username);
        List<Object> menus=new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        params.put("gbUsername", username);
        for(Object prof: sec_prof_service.runSQL("GbUserProfiling.findByGbUsername", params)){
            params = new HashMap<>();
            params.put("gbProfile", ((GbUserProfiling)prof).getGbSecurityProfile().getGbProfile());
            menus.addAll(profiling_service.runSQL("GbMenuProfiling.findByGbProfile", params));
        }
        return menus;
    }
}
