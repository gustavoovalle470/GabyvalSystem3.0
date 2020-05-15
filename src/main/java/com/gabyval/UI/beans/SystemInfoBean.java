package com.gabyval.UI.beans;

import com.gabyval.Exceptions.GBException;
import com.gabyval.UI.utils.ADModuleConfigurationCons;
import com.gabyval.UI.utils.UIMessageManagement;
import com.gabyval.controllers.system.ADModuleConfigurationContoller;
import com.gabyval.persistence.exception.GBPersistenceException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.log4j.Logger;
/** 
 * Clase encargada del bean que carga los parametros de nombre y version del sistema
 * NOMBRE DEL BEAN: SystemInfoBean, bean de sesion.
 * @version 1.0
 * @since 14/05/2020
 * *****************************************************************************************************************************************************
 * *****************************************************************************************************************************************************
 * *******************************                                                                              ****************************************
 * *******************************  ********** ********** ******    **      ** **      ** ********** **         ****************************************
 * *******************************  ********** **********  ******   **      ** **      ** ********** **         ****************************************
 * *******************************  **         **      **  **  **   **      ** **      ** **      ** **         ****************************************
 * *******************************  **         **      **  **  **   **      ** **      ** **      ** **         ****************************************
 * *******************************  **         **      **  ******    **    **  **      ** **      ** **         ****************************************
 * *******************************  **    **** **********  *******    **  **   **      ** ********** **         ****************************************
 * *******************************  **    **** **********  ********    ****    **      ** ********** **         ****************************************
 * *******************************  **      ** **      **  **    **     **     **      ** **      ** **         ****************************************
 * *******************************  **      ** **      **  **    **     **      **    **  **      ** **         ****************************************
 * *******************************  **      ** **      **  **   **      **       **  **   **      ** **         ****************************************
 * *******************************  ********** **      **  **  **       **        ****    **      ** ********** ****************************************
 * *******************************  ********** **      ** ******        **         **     **      ** ********** ****************************************
 * *****************************************************************************************************************************************************
 * *****************************************************************************************************************************************************
 * |---------------------------------------------------------------------------------------------------------------------------------------------------|
 * |                                                        Control de versiones                                                                       |
 * |---------|--------------|----------------|---------------------------------------------------------------------------------------------------------|
 * | Version |    Fecha     |  Responsable   |                                                  Comentarios                                            |
 * |---------|--------------|----------------|---------------------------------------------------------------------------------------------------------|
 * |   1.0   |  14/05/2020  |      GAOQ      | Creacion y certificacion de la clase.                                                                   |  
 * |---------|--------------|----------------|---------------------------------------------------------------------------------------------------------|
 */
@SessionScoped
@ManagedBean(name = "SystemInfoBean")
public class SystemInfoBean implements Serializable{
    private final Logger log = Logger.getLogger(SystemInfoBean.class);//Log de esta clase.
    private String app_name="APP_NAME"; //Nombre de la aplicacion.
    private String header_view="APP_NAME";//Version actual.

    /**
     * Crea una nueva instancia de este bean, obteniendo de base de datos los
     * atributos de esta clase, o seteando por defecto al no encontrarlos.
     */
    public SystemInfoBean(){
        log.debug("GabyvalSystem - nueva instancia del bean "+SystemInfoBean.class);
        try {
            log.debug("GabyvalSystem - Obteniendo configuraciones del modulo: ");
            log.debug("GabyvalSystem - Buscando parametro: "+ADModuleConfigurationCons.APP_VERSION);
            log.debug("GabyvalSystem - Buscando parametro: "+ADModuleConfigurationCons.APP_NAME);
            header_view = ADModuleConfigurationContoller.getInstance().getStrConfValue(ADModuleConfigurationCons.APP_NAME)+" "+ADModuleConfigurationContoller.getInstance().getStrConfValue(ADModuleConfigurationCons.APP_VERSION);
            log.debug("GabyvalSystem - Se encontraron los parametros y se creo el header_view: "+header_view);
            app_name = ADModuleConfigurationContoller.getInstance().getStrConfValue(ADModuleConfigurationCons.APP_NAME);
            log.debug("GabyvalSystem - Se encontraron los parametros y se creo el app_name: "+app_name);
        } catch (GBException | GBPersistenceException ex) {
            log.fatal("Se ha presentado un error al consultar los parametros en la base de datos.");
            log.fatal(ex);
            UIMessageManagement.putErrorMessage("No se ha podido leer la configuracion del modulo.");
        }
    }
    
    /**
     * Retorna el valor del app_name
     * @return String el valor de app_name
     */
    public String getApp_name() {
        return app_name;
    }

    /**
     * Cambia el valor de app_name
     * @param app_name String el nuevo valor.
     */
    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    /**
     * Devuelve el valor de header_view
     * @return String el valor de header_view.
     */
    public String getHeader_view() {
        return header_view;
    }

    /**
     * Cambia el valor del header_view
     * @param header_view String el nuevo valor.
     */
    public void setHeader_view(String header_view) {
        this.header_view = header_view;
    }
}
