/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.user;

import com.gabyval.Exceptions.GBException;
import com.gabyval.UI.utils.ADModuleConfigurationCons;
import com.gabyval.controllers.system.ADModuleConfigurationContoller;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.users.GbUsers;
import com.gabyval.services.security.users.GBUserService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author OvalleGA
 */
@Controller
public class GBUserController {
    @Autowired
    private GBUserService userService;
    private static GBUserController instance;
    
    public GBUserController(){
        instance=this;
    }
    
    public static GBUserController getInstance(){
        if(instance==null){
            instance=new  GBUserController();
        }
        return instance;
    }
    
    public List<GbUsers> getAllUsers() throws GBPersistenceException{
        List<GbUsers> users = new  ArrayList<>();
        for(Object o: userService.getAll()){
            users.add((GbUsers)o);
        }
        return users;
    }

    public void save(GbUsers userUpdate) throws GBPersistenceException {
        userService.save(userUpdate);
    }
    
    
    public GbUsers expirePwd(GbUsers user) throws GBException, GBPersistenceException{
        int daysToExpire=(ADModuleConfigurationContoller.getInstance().getIntegerConfValue(ADModuleConfigurationCons.PWD_EXPIRE_TIME));
        if(user.getDaysPassedLastXgePwd()<daysToExpire){
            Calendar c = Calendar.getInstance();
            c.setTime(user.getGbLastPwdXgeDt());
            c.add(Calendar.DATE, daysToExpire*-1);
            user.setGbLastPwdXgeDt(c.getTime());
        }
        return user;
    }

    public String getNewUserName(String gbStaffName, String gbStaffSurname) throws GBPersistenceException {
        int userIterator=1;
        String username,aux;
        String[] dataSurname=gbStaffSurname.split(" ");
        String[] dataName=gbStaffName.split(" ");
        username=dataSurname[0]+dataName[0].toCharArray()[0]+
                (dataName.length>1?dataName[1].toCharArray()[0]:"");
        aux=username;
        while(userService.load(username)!=null){
            username=aux;
            username=username+""+userIterator;
            userIterator++;
        }
        return username.toUpperCase();
    }
}
