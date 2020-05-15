/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.controllers.system;

import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.system.AdCatalogs;
import com.gabyval.services.system.ADCatalogService;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author OvalleGA
 */
@Controller
public class ADCatalogController {
    
    @Autowired
    private ADCatalogService catalogService;
    private static ADCatalogController instance;
    private List<Object> catalogs;
    private final Logger log = Logger.getLogger(ADCatalogController.class);
    
    public static ADCatalogController getInstance(){
        if(instance==null){
            instance=new ADCatalogController();
        }
        return instance;
    }
    
    public ADCatalogController(){
        instance=this;
    }
    
    private List<AdCatalogs> getCatalog(String catalogName) throws GBPersistenceException{
        List<AdCatalogs> catalog=new ArrayList<>();
        for(Object cat:catalogs){
            if(((AdCatalogs) cat).getCatalogName().equals(catalogName)){
                catalog.add((AdCatalogs) cat);
            }            
        }
        return catalog;
    }
    
     public List<SelectItem> geCatalogList(String catalogName) throws GBPersistenceException{
         catalogs=catalogService.getAll();
         List<SelectItem> catalogItems= new ArrayList<>();
         for(AdCatalogs catalog: getCatalog(catalogName)){
             SelectItem item = new SelectItem();
             item.setLabel(catalog.getCatalogItem());
             item.setValue(catalog.getCatalogId());
             item.setDescription(catalog.getCatalogDescription());
             catalogItems.add(item);
         }
         return catalogItems;
     }

    public AdCatalogs getCatalog(int catalogId) throws GBPersistenceException {
        catalogs=catalogService.getAll();
        for(Object cat:catalogs){
            if(((AdCatalogs) cat).getCatalogId().equals(catalogId)){
                return (AdCatalogs) cat;
            }            
        }
        return null;
    }
    
    public AdCatalogs getCatalog(int catalog_item_id, String catalog_name) throws GBPersistenceException{
        catalogs=catalogService.getAll();
        for(Object cat:catalogs){
            if(((AdCatalogs) cat).getCatalogItemId()==catalog_item_id && 
               ((AdCatalogs) cat).getCatalogName().equals(catalog_name)){
                return (AdCatalogs) cat;
            }            
        }
        return null;
    }
}
