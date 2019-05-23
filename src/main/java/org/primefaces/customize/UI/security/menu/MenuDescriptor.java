/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.primefaces.customize.UI.security.menu;

import java.util.ArrayList;

/**
 *
 * @author OvalleGA
 */
public class MenuDescriptor {
    private String id;
    private String label;
    private String action;
    private String icon;
    private String parentId;
    private ArrayList<MenuDescriptor> subMenus;

    public MenuDescriptor(String id, String label, String action, String icon, String parentId) {
        subMenus = new ArrayList<>();
        this.id = id;
        this.label = label;
        this.action = action;
        this.icon = icon;
        this.parentId=parentId;
    }
    
    public ArrayList<MenuDescriptor> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(ArrayList<MenuDescriptor> subMenus) {
        this.subMenus = subMenus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    public boolean isPrincipalNode(){
        return parentId==null;
    }
    
    public boolean isNode(){
        return (parentId==null || subMenus != null);
    }
}
