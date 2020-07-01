/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.user;

import com.gabyval.Exceptions.GBException;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.GBSentencesRBOs;
import com.gabyval.referencesbo.security.users.GbStaff;
import com.gabyval.services.security.users.GBStaffService;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author OvalleGA
 */
@Controller
public class GBStaffController {
    @Autowired
    private GBStaffService staff_service;
    private static GBStaffController instance;
    private final Logger log = Logger.getLogger(GBStaffController.class);
    
    public GBStaffController(){
        instance=this;
    }
    
    public static GBStaffController getInstance(){
        if(instance==null){
            instance=new GBStaffController();
        }
        return instance;
    }
    
    public GbStaff get_profile_info(String username) throws GBException{
        try {
            HashMap<String, Object> parameters=new HashMap<>();
            parameters.put("gbUsername", username);
            List<Object> gbStaffs=staff_service.runSQL(GBSentencesRBOs.GBSTAFF_FINDBYGBUSERNAME, parameters);
            if(gbStaffs == null || gbStaffs.isEmpty()){
                return null;
            }
            return (GbStaff) gbStaffs.get(0);
        } catch (GBPersistenceException ex) {
            log.error(ex);
            throw new GBException("No se pudo carga la informacion del perfil");
        }
    }
    
    public void saveProfile(GbStaff profile) throws GBException{
        try {
            if(get_profile_info(profile.getGbUsername())!=null){
                staff_service.update(profile);
            }else{
                staff_service.save(profile);
            }
            staff_service.refresh(profile);
        } catch (GBPersistenceException ex) {
            log.fatal(ex);
            throw new GBException("No se pudo salvar el perfil");
        }
    }

    public boolean existProfile(GbStaff newStaff) throws GBPersistenceException {
        HashMap<String, Object>params=new HashMap<>();
        log.debug("DATOS DE LA SENTENCIA: "+newStaff.getGbIdNumber()+", "+newStaff.getGbUsername()+", "+newStaff.getGbStaffName()+", "+newStaff.getGbStaffSurname());
        params.put("gbIdNumber", newStaff.getGbIdNumber());
        params.put("gbStaffName", newStaff.getGbStaffName());
        params.put("gbStaffSurname", newStaff.getGbStaffSurname());
        log.debug("Se encontraron "+staff_service.runSQL(GBSentencesRBOs.GBSTAFF_FINDBYPERSONALDATA, params).size()+" perfiles con el mismo nombre.");
        return !staff_service.runSQL(GBSentencesRBOs.GBSTAFF_FINDBYPERSONALDATA, params).isEmpty();
    }
}
