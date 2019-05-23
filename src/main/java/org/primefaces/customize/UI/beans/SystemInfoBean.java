/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.primefaces.customize.UI.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author OvalleGA
 */
@SessionScoped
@ManagedBean(name = "SystemInfoBean")
public class SystemInfoBean implements Serializable{
    private String app_name="APP_NAME";
    private String header_view="APP_NAME";

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getHeader_view() {
        return header_view;
    }

    public void setHeader_view(String header_view) {
        this.header_view = header_view;
    }
}
