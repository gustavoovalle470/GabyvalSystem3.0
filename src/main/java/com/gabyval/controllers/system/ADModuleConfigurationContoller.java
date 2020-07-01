/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.system;

import com.gabyval.Exceptions.GBException;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.GBSentencesRBOs;
import com.gabyval.referencesbo.system.AdModuleConfiguration;
import com.gabyval.services.system.ADModuleConfigurationService;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        return (AdModuleConfiguration)module_service.load(configuration_id);
    }
    
    public AdModuleConfiguration getConfiguration(String configuration_name) throws GBPersistenceException, GBException{
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("moduleConfigName", configuration_name);
        List modules = module_service.runSQL(GBSentencesRBOs.ADMODULECONFIGURATION_FINDBYMODULECONFIGNAME, parameters);
        if(modules.size() != 1){
            throw new GBException("Al tratar de obtener la configuracion se ha obtenido mas de un resultado.");
        }
        return (AdModuleConfiguration) modules.get(0);
    }
    
    public boolean getBooleanConfValue(String name) throws GBException, GBPersistenceException, GBPersistenceException{
        if(!getConfiguration(name).getModuleConfigType().equals("BOOLEAN")){
            throw new GBException("No se puede obtener el valor booleano de la configuracion con nombre "+name);
        }
        return Boolean.valueOf(getConfiguration(name).getModuleConfigValue());
    }
    
    public int getIntegerConfValue(String name) throws GBException, GBPersistenceException{
        if(!getConfiguration(name).getModuleConfigType().equals("INTEGER")){
            throw new GBException("No se puede obtener el valor entero de la configuracion con nombre "+name);
        }
        return Integer.parseInt(getConfiguration(name).getModuleConfigValue());
    }
    
    public long getLongConfValue(String name) throws GBException, GBPersistenceException{
        if(!getConfiguration(name).getModuleConfigType().equals("LONG")){
            throw new GBException("No se puede obtener el valor Long de la configuracion con nombre "+name);
        }
        return new Long(getConfiguration(name).getModuleConfigValue());
    }
    
    public Double getDoubleConfValue(String name) throws GBException, GBPersistenceException{
        if(!getConfiguration(name).getModuleConfigType().equals("DOUBLE")){
            throw new GBException("No se puede obtener el valor double de la configuracion con nombre "+name);
        }
        return new Double(getConfiguration(name).getModuleConfigValue());
    }
    
    public BigInteger getBigIntegerConfValue(String name) throws GBException, GBPersistenceException{
        if(!getConfiguration(name).getModuleConfigType().equals("BIGINTEGER")){
            throw new GBException("No se puede obtener el valor big integer de la configuracion con nombre "+name);
        }
        return new BigInteger(getConfiguration(name).getModuleConfigValue());
    }
    
    public Date getDateConfValue(String name) throws GBException, GBPersistenceException{
        if(!getConfiguration(name).getModuleConfigType().equals("DATE")){
            throw new GBException("No se puede obtener el valor fecha de la configuracion con nombre "+name);
        }
        return new Date(getConfiguration(name).getModuleConfigValue());
    }
    
    public String getStrConfValue(String name) throws GBException, GBPersistenceException{
        if(!getConfiguration(name).getModuleConfigType().equals("STRING")){
            throw new GBException("No se puede obtener el valor String de la configuracion con nombre "+name);
        }
        return getConfiguration(name).getModuleConfigValue();
    }
}
