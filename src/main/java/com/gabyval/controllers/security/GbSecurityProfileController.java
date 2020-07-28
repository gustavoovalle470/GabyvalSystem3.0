/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.security;

import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.GBSentencesRBOs;
import com.gabyval.referencesbo.security.profiling.GbSecurityProfile;
import com.gabyval.services.security.profiling.GBSecurityProfileService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author OvalleGA
 */
@Controller
public class GbSecurityProfileController {

    @Autowired
    private GBSecurityProfileService secProfService;
    private static GbSecurityProfileController instance;
    
    public GbSecurityProfileController(){
        instance=this;
    }
    
    public static GbSecurityProfileController getInstance(){
        if(instance==null){
            instance=new GbSecurityProfileController();
        }
        return instance;
    }
    
    public List<GbSecurityProfile> getAllProfiles() throws GBPersistenceException{
         List<GbSecurityProfile> profiles = new  ArrayList<>();
        for(Object o: secProfService.getAll()){
            profiles.add((GbSecurityProfile)o);
        }
        return profiles;
    }
    
    public void update(GbSecurityProfile profileUpdate) throws GBPersistenceException {
        secProfService.update(profileUpdate);
    }

    public void save(GbSecurityProfile newProfile) throws GBPersistenceException, Exception {
        HashMap<String, Object>params=new HashMap<>();
        params.put("gbProfile", newProfile.getGbProfile());
        if(secProfService.runSQL(GBSentencesRBOs.GBSECURITYPROFILE_FINDBYGBPROFILE, params).isEmpty()){
            secProfService.save(newProfile);
        }else{
            throw new Exception("No se pudo crear el perfil de usuario pues ya existe.");
        }
    }
}
