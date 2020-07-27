package com.gabyval.UI.security.menu;

import com.gabyval.Exceptions.GBException;
import com.gabyval.controllers.security.SecurityMenuController;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.menu.GbMenulinks;
import com.gabyval.referencesbo.security.profiling.GbMenuProfiling;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.primefaces.model. menu.DefaultMenuModel;

/**
 *
 * @author OvalleGA
 */
public class MenuFactory {
    private final Logger log = Logger.getLogger(MenuFactory.class);
    private static MenuFactory instance;
    
    public static MenuFactory getInstance() throws GBException{
        if(instance == null){
            instance = new MenuFactory();
        }
        return instance;
    }
    
    public DefaultMenuModel getSecMenuUser(String username) throws GBException, GBPersistenceException{
        log.debug("Obteniendo el arbol de seguridad para el usuario "+username);
        DefaultMenuModel user_menu = new DefaultMenuModel();
        HashMap<Integer, GBTemporalMenuOrder> menusToPut=getMenu(SecurityMenuController.getInstance().getMenuSec(username));
        for(Integer id:menusToPut.keySet()){
            user_menu.addElement(menusToPut.get(id).buildMenuElement());
        }
        return user_menu;
    }
    
    private HashMap<Integer, GBTemporalMenuOrder> getMenu(List<Object> menu_profiling){
        HashMap<Integer, GBTemporalMenuOrder> menus=new HashMap<>();
        for(Object object: menu_profiling){
            GBTemporalMenuOrder menuPrincipal;
            if(((GbMenuProfiling)object).getGbMenulinks().getGbMenuParId() != null){
                GbMenulinks par=((GbMenuProfiling)object).getGbMenulinks().getGbMenuParId();
                menuPrincipal=new GBTemporalMenuOrder(par);
                menuPrincipal.addItem(((GbMenuProfiling)object).getGbMenulinks().getGbMenuId(), 
                                     new GBTemporalMenuOrder(((GbMenuProfiling)object).getGbMenulinks()));
                par=par.getGbMenuParId();
                while(par!=null){
                    GBTemporalMenuOrder aux=menuPrincipal;
                    menuPrincipal=new GBTemporalMenuOrder(par);
                    menuPrincipal.addItem(aux.getMenuId(), aux);
                    par=par.getGbMenuParId();
                }
            }else{
                menuPrincipal=new GBTemporalMenuOrder(((GbMenuProfiling)object).getGbMenulinks());
            }
            if(!menus.containsKey(menuPrincipal.getMenuId())){
                menus.put(menuPrincipal.getMenuId(), menuPrincipal);
            }else{
                menus.get(menuPrincipal.getMenuId()).addItems(menuPrincipal.getSubMenus());
            }
        }
        return menus;
    }
}