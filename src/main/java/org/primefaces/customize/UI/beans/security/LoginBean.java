/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.primefaces.customize.UI.beans.security;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.customize.UI.utils.UIMessageManagement;
import org.primefaces.customize.controllers.security.LoginUsers;


/**
 *
 * @author OvalleGA
 */
@RequestScoped
@ManagedBean(name = "LoginBean")
public class LoginBean {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String login(){
        username = username.toUpperCase();
        try {
            if(LoginUsers.getInstance().ValidateCredentials(username, password, (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true))){
                UIMessageManagement.putInfoMessage("Bienvenido "+username);
                return "autowired";
            }else{
                return "denied";
            }
        } catch (Exception ex) {
            UIMessageManagement.putErrorMessage(ex.getMessage());
            return "denied";
        }
    }
}
