package com.gabyval.UI.security.menu;

import com.gabyval.Exceptions.GBException;
import com.gabyval.controllers.security.SecurityMenuController;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.menu.GbMenulinks;
import com.gabyval.referencesbo.security.profiling.GbMenuProfiling;
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
    
    public static MenuFactory getInstance() throws GBException{
        if(instance == null){
            instance = new MenuFactory();
        }
        return instance;
    }
    
    public DefaultMenuModel getSecMenuUser(String username) throws GBException, GBPersistenceException{
        log.debug("Obteniendo el arbol de seguridad para el usuario "+username);
        DefaultMenuModel user_menu = new DefaultMenuModel();
        for(Object menu_prof: SecurityMenuController.getInstance().getMenuSec(username)){
            GbMenulinks menu=((GbMenuProfiling)menu_prof).getGbMenulinks();
            if(menu.getGbMenuStatus()==1){
                if(menu.getGbMenuParId()!=null){
                    user_menu.addElement(createSub(menu.getGbMenuParId(), createItem(menu)));
                }else{
                    user_menu.addElement(createItem(menu));
                }
            }
        }
        return user_menu;
    }

    private DefaultSubMenu createSub(GbMenulinks gbMenuParId, MenuElement menu) {
        DefaultSubMenu item = new DefaultSubMenu(gbMenuParId.getGbMenuName());
        item.setId(""+gbMenuParId.getGbMenuId());
        item.setIcon(gbMenuParId.getGbIcon());
        item.addElement(menu);
        if(gbMenuParId.getGbMenuParId()!=null){
            return createSub(gbMenuParId.getGbMenuParId(), item);
        }
        return item;
    }
    
    private DefaultMenuItem createItem(GbMenulinks gbMenulinks) {
        DefaultMenuItem item = new DefaultMenuItem(gbMenulinks.getGbMenuName());
        item.setId(""+gbMenulinks.getGbMenuId());
        item.setIcon(gbMenulinks.getGbIcon());
        item.setCommand(gbMenulinks.getGbPageView());
        return item;
    }
}