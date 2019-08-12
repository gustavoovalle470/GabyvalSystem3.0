/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.UI.security.menu;

import com.gabyval.Exceptions.GBException;
import com.gabyval.controllers.security.SecurityMenuController;
import com.gabyval.referencesbo.security.profiling.GbMenuProfiling;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;

/**
 *
 * @author OvalleGA
 */
public class MenuFactory {
    private final Logger log = Logger.getLogger(MenuFactory.class);
    private static MenuFactory instance;
    private final HashMap<String, MenuDescriptor> allMenuSystem;
    
    public MenuFactory() throws GBException{
        log.info("Obteniendo todos los menus del sistema.");
        allMenuSystem = SecurityMenuController.getInstance().getAllMenuSystem();
        log.info("Menus de sistema cargados en memoria.");
    }
    
    public static MenuFactory getInstance() throws GBException{
        if(instance == null){
            instance = new MenuFactory();
        }
        return instance;
    }

    private HashMap<String, MenuDescriptor> organizeMenus(HashMap<String, MenuDescriptor> to_organize) {
        log.info("Organizando los menus por jerarquia.");
        HashMap<String, MenuDescriptor> organizeMenus = new  HashMap<>();
        to_organize.keySet().forEach((menuId) -> {
            MenuDescriptor descriptor=to_organize.get(menuId);
            if (descriptor.isPrincipalNode()) {
                descriptor.setSubMenus(getSubMenus(menuId, to_organize));
                organizeMenus.put(menuId, descriptor);
            }
        });
        log.info("Retornando menus a mostrar.");
        return organizeMenus;
    }

    private ArrayList<MenuDescriptor> getSubMenus(String menuId, HashMap<String, MenuDescriptor> to_organize) {
        log.info("Obteniendo y organizando sub menus en menus nodo.");
        ArrayList<MenuDescriptor> subMenus = new ArrayList<>();
        for(MenuDescriptor descriptor: to_organize.values()){
            if(descriptor.getParentId() != null && descriptor.getParentId().equals(menuId)){
                if(descriptor.isNode()){
                    log.info("El menu "+menuId+" es un nodo obteniendo los sub menus");
                    descriptor.setSubMenus(getSubMenus(descriptor.getId(), to_organize));
                }
                log.info("El adicionando el menu "+menuId+" a la jerarquia");
                subMenus.add(descriptor);
            }
        }
        return subMenus;
    }
    
    private HashMap<String, MenuDescriptor> getSecMenusByUser(String usernamne) throws GBException{
        HashMap<String, MenuDescriptor> user_menus = new HashMap<>();
        log.info("Iniciando consulta al servidor de base de datos para obtener los menus a los que el usuario "+usernamne+" tiene acceso");
        List<Object> user_menu_allow = SecurityMenuController.getInstance().getMenuSec(usernamne);
        log.info("Menus recuperados "+user_menu_allow.size());
        for(Object o : user_menu_allow){
            GbMenuProfiling prof = (GbMenuProfiling) o;
            user_menus.put(prof.getGbMenuProfilingPK().getGbMenuId(), allMenuSystem.get(prof.getGbMenuProfilingPK().getGbMenuId()));
            String parentId = allMenuSystem.get(prof.getGbMenuProfilingPK().getGbMenuId()).getParentId();
            while(parentId != null){
                user_menus.put(parentId, allMenuSystem.get(parentId));
                parentId = allMenuSystem.get(parentId).getParentId();
            }
        }
        log.info("Los menus han para el usuario "+usernamne+" han sido cargados con exito.");
        return organizeMenus(user_menus);
    }
    
    public DefaultMenuModel getSecMenuUser(String username) throws GBException{
        log.info("Obteniendo el arbol de seguridad para el usuario "+username);
        DefaultMenuModel user_menu = new DefaultMenuModel();
        HashMap<String, MenuDescriptor> user_desc = getSecMenusByUser(username);
        for(String id: user_desc.keySet()){
            MenuDescriptor descriptor = user_desc.get(id);
            if(descriptor.isPrincipalNode() || descriptor.isNode()){
                user_menu.addElement(assembleSubmenu(descriptor));
            }
        }
        log.info("Los menus han para el usuario "+username+" han sido cargados con exito.");
        return user_menu;
    }
    
    public DefaultSubMenu assembleSubmenu(MenuDescriptor descriptor){
        log.info("Creando sub menu: "+descriptor.getLabel());
        DefaultSubMenu sub = new DefaultSubMenu(descriptor.getLabel());
        sub.setIcon(descriptor.getIcon());
        sub.setId(descriptor.getId());
        sub.setElements(assembleMenuItems(descriptor.getSubMenus()));
        log.info("Sub menu: "+descriptor.getLabel()+" creado.");
        return sub;
    }

    private List<MenuElement> assembleMenuItems(ArrayList<MenuDescriptor> subMenus) {
        log.info("Creando menu items");
        List<MenuElement> menus_to_return = new ArrayList<>();
        for(MenuDescriptor descriptor: subMenus){
            if(descriptor.isNode() || descriptor.isPrincipalNode()){
                log.info("El item es nodo, creando sub menus");
                menus_to_return.add(assembleSubmenu(descriptor));
            }else{
                log.info("El item no es nodo, creando menu item");
                DefaultMenuItem item = new DefaultMenuItem(descriptor.getLabel());
                item.setId(descriptor.getId());
                item.setIcon(descriptor.getIcon());
                item.setCommand(descriptor.getAction());
                menus_to_return.add(item);
            }
        }
        log.info("Finaliza la construccion del menu.");
        return menus_to_return;
    }
}