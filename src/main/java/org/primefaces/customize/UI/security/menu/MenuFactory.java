/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.primefaces.customize.UI.security.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.primefaces.customize.controllers.security.SecurityMenuManager;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;

/**
 *
 * @author OvalleGA
 */
public class MenuFactory {
    private static MenuFactory instance;
    private final HashMap<String, MenuDescriptor> allMenuSystem;
    
    public MenuFactory(){
        allMenuSystem = SecurityMenuManager.getInstance().getAllMenuSystem();
    }
    
    public static MenuFactory getInstance(){
        if(instance == null){
            instance = new MenuFactory();
        }
        return instance;
    }

    private HashMap<String, MenuDescriptor> organizeMenus(HashMap<String, MenuDescriptor> to_organize) {
        HashMap<String, MenuDescriptor> organizeMenus = new  HashMap<>();
        to_organize.keySet().forEach((menuId) -> {
            MenuDescriptor descriptor=to_organize.get(menuId);
            if (descriptor.isPrincipalNode()) {
                descriptor.setSubMenus(getSubMenus(menuId, to_organize));
                organizeMenus.put(menuId, descriptor);
            }
        });
        return organizeMenus;
    }

    private ArrayList<MenuDescriptor> getSubMenus(String menuId, HashMap<String, MenuDescriptor> to_organize) {
        ArrayList<MenuDescriptor> subMenus = new ArrayList<>();
        for(MenuDescriptor descriptor: to_organize.values()){
            if(descriptor.getParentId() != null && descriptor.getParentId().equals(menuId)){
                if(descriptor.isNode()){
                    descriptor.setSubMenus(getSubMenus(descriptor.getId(), to_organize));
                }
                subMenus.add(descriptor);
            }
        }
        return subMenus;
    }
    
    private HashMap<String, MenuDescriptor> getSecMenusByUser(String usernamne){
        HashMap<String, MenuDescriptor> user_menus = new HashMap<>();
        List<String> user_menu_allow = SecurityMenuManager.getInstance().getMenuSec(usernamne);
        System.out.println("Cantidad de menus de usuario: "+user_menu_allow.size());
        for(String id : user_menu_allow){
            System.out.println("Menu: "+allMenuSystem.get(id));
            System.out.println("Obteniendo menu: "+id);
            user_menus.put(id, allMenuSystem.get(id));
            String parentId = allMenuSystem.get(id).getParentId();
            while(parentId != null){
                user_menus.put(parentId, allMenuSystem.get(parentId));
                parentId = allMenuSystem.get(parentId).getParentId();
            }
        }
        return organizeMenus(user_menus);
    }
    
    public DefaultMenuModel getSecMenuUser(String username){
        System.out.println("Cargando menu");
        DefaultMenuModel user_menu = new DefaultMenuModel();
        HashMap<String, MenuDescriptor> user_desc = getSecMenusByUser(username);
        for(String id: user_desc.keySet()){
            MenuDescriptor descriptor = user_desc.get(id);
            if(descriptor.isPrincipalNode() || descriptor.isNode()){
                user_menu.addElement(assembleSubmenu(descriptor));
            }
        }
        return user_menu;
    }
    
    public DefaultSubMenu assembleSubmenu(MenuDescriptor descriptor){
        DefaultSubMenu sub = new DefaultSubMenu(descriptor.getLabel());
        sub.setIcon(descriptor.getIcon());
        sub.setId(descriptor.getId());
        sub.setElements(assembleMenuItems(descriptor.getSubMenus()));
        return sub;
    }

    private List<MenuElement> assembleMenuItems(ArrayList<MenuDescriptor> subMenus) {
        List<MenuElement> menus_to_return = new ArrayList<>();
        for(MenuDescriptor descriptor: subMenus){
            if(descriptor.isNode() || descriptor.isPrincipalNode()){
                menus_to_return.add(assembleSubmenu(descriptor));
            }else{
                DefaultMenuItem item = new DefaultMenuItem(descriptor.getLabel());
                item.setId(descriptor.getId());
                item.setIcon(descriptor.getIcon());
                item.setCommand(descriptor.getAction());
                menus_to_return.add(item);
            }
        }
        return menus_to_return;
    }
}