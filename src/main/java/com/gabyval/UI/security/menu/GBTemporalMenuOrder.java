/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.UI.security.menu;

import com.gabyval.referencesbo.security.menu.GbMenulinks;
import java.util.HashMap;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;

/**
 *
 * @author OvalleGA
 */
public class GBTemporalMenuOrder {
    private int menuId;
    private GbMenulinks menu;
    private HashMap<Integer,GBTemporalMenuOrder>subMenus;
    
    public GBTemporalMenuOrder(GbMenulinks menu){
        menuId=menu.getGbMenuId();
        this.menu=menu;
        subMenus=new HashMap<>();
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public GbMenulinks getMenu() {
        return menu;
    }

    public void setMenu(GbMenulinks menu) {
        this.menu = menu;
    }

    public HashMap<Integer,GBTemporalMenuOrder> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(HashMap<Integer,GBTemporalMenuOrder> subMenus) {
        this.subMenus = subMenus;
    }
    
    
    
    public void addItem(Integer menuId, GBTemporalMenuOrder subMenu){
        if(subMenus.containsKey(menuId)){
            subMenus.get(menuId).addItem(menuId, subMenu);
        }else{
            subMenus.put(menuId, subMenu);
        }
    }
    
    public void addItems(HashMap<Integer,GBTemporalMenuOrder> submenusToAdd){
        for(Integer menuId: submenusToAdd.keySet()){
            if(subMenus.containsKey(menuId)){
                subMenus.get(menuId).addItems(submenusToAdd.get(menuId).getSubMenus());
            }else{
                subMenus.put(submenusToAdd.get(menuId).menuId, submenusToAdd.get(menuId));
            }
        }
    }
    
    public MenuElement buildMenuElement(){
        if(!subMenus.isEmpty()){
            DefaultSubMenu subMenu=createSub(menu);
            for(Integer menuId: subMenus.keySet()){
                subMenu.addElement(subMenus.get(menuId).buildMenuElement());
            }
            return subMenu;
        }else{
            return createItem(menu);
        }
        
    }
    
    private DefaultSubMenu createSub(GbMenulinks gbMenuParId) {
        DefaultSubMenu item = new DefaultSubMenu(gbMenuParId.getGbMenuName());
        item.setId(""+gbMenuParId.getGbMenuId());
        item.setIcon(gbMenuParId.getGbIcon());
        return item;
    }
    
    private DefaultMenuItem createItem(GbMenulinks gbMenulinks) {
        DefaultMenuItem item = new DefaultMenuItem(gbMenulinks.getGbMenuName());
        item.setId(""+gbMenulinks.getGbMenuId());
        item.setIcon(gbMenulinks.getGbIcon());
        item.setCommand(gbMenulinks.getGbPageView()+"?faces-redirect=true");
        return item;
    }
}
