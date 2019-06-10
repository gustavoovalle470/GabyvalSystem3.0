/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.security;

import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.system.AdMessages;
import com.gabyval.services.system.ADMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author OvalleGA
 */
@Controller
public class GBMessageContoller {
    @Autowired
    private ADMessageService msg_service;
    private static GBMessageContoller instance;
    
    public GBMessageContoller(){
        instance=this;
    }
    
    public static GBMessageContoller getInstance(){
        if(instance == null){
            instance=new GBMessageContoller();
        }
        return instance;
    }
    
    public AdMessages getMessage(int error_id, String replace_values) throws GBPersistenceException{
        AdMessages message= msg_service.load(error_id);
        if(replace_values != null){
            String msg_desc = message.getMessageDesc();
            String[] values = replace_values.split(",");
            for(String value: values){
                msg_desc=msg_desc.replaceFirst("&", value);
            }
            message.setMessageDesc(msg_desc);
        }
        return message;
    }
}
