/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.system;

import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.system.AdMessages;
import com.gabyval.referencesbo.system.AdModuleConfiguration;
import com.gabyval.services.system.ADMessageService;
import com.gabyval.services.system.ADModuleConfigurationService;
import javax.persistence.Query;
import org.hibernate.jpa.internal.QueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author OvalleGA
 */
@Controller
public class ADModuleConfigurationContoller {
    @Autowired
    private ADModuleConfigurationService module_service;
    private static ADModuleConfigurationContoller instance;
    
    public ADModuleConfigurationContoller(){
        instance=this;
    }
    
    public static ADModuleConfigurationContoller getInstance(){
        if(instance == null){
            instance=new ADModuleConfigurationContoller();
        }
        return instance;
    }
    
    public AdModuleConfiguration getConfiguration(int configuration_id) throws GBPersistenceException{
        AdModuleConfiguration s = module_service.load(configuration_id);
        s.getModuleConfigName();
        return null;        
    }
    
    public AdModuleConfiguration getConfiguration(String configuration_name){
        module_service.getConfigurationByName(configuration_name);
        return null;        
    }
}
