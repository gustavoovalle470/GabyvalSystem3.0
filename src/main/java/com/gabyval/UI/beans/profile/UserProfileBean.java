/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gabyval.UI.beans.profile;

import com.gabyval.Exceptions.GBException;
import com.gabyval.UI.utils.UIMessageManagement;
import com.gabyval.controllers.security.UserSessionManager;
import com.gabyval.controllers.system.ADCatalogController;
import com.gabyval.controllers.user.GBStaffController;
import com.gabyval.persistence.exception.GBPersistenceException;
import com.gabyval.referencesbo.security.users.GbStaff;
import com.gabyval.referencesbo.security.users.GbUsers;
import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
/** 
 * Clase encargada del bean que controla el perfil de usuario.
 * NOMBRE DEL BEAN: ProfileBean, bean de sesion.
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
@ManagedBean(name = "ProfileBean")
public class UserProfileBean{
    private final Logger log = Logger.getLogger(UserProfileBean.class);//El log de esta clase
    private GbUsers user;//El usuario de este perfil
    private StreamedContent photo_profile;//La foto del perfil de usuario.
    private GbStaff profile_info;//La informacion personal del usuario.
    private List<SelectItem> idType;//Lista de posibles tipos de identificacion.
    private List<SelectItem> gender;//Lista de posibles generos.
    private int idTypeId;//Id del tipo de identificacion seleccionado.
    private int genderId;//Id del tipo de genero seleccionado.
    
    /**
     * Crea una nueva instancia de este bean, obteniendo de la base de datos los
     * valores de informacion del perfil de usuario o asignado los valores por 
     * defecto al no econtrarlos.
     */
    public UserProfileBean(){
        try {
            preload();
            if(profile_info != null){
                log.debug("GabyvalSystem - Perfil de usuario detectado.");
                loadProfile();
            }else{
                log.debug("GabyvalSystem - Perfil de usuario no detectado.");
                setDefaulProfile();
            }
        } catch (GBException | GBPersistenceException ex) {
            log.error("Se ha presentado un error al cargar el perfil de usuario.");
            log.error(ex);
            UIMessageManagement.putErrorMessage("El perfil del usuario no pudo ser cargado.");
        }
    }

    /**
     * Pre carga los componentes necesarios para localizar y mostrar un perfil de usuario.
     * @throws GBException
     * @throws GBPersistenceException 
     */
    private void preload() throws GBException, GBPersistenceException {
        log.debug("GabyvalSystem - Obteniendo usuario conectado.");
        user=UserSessionManager.getInstance().getUser((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true));
        log.debug("GabyvalSystem - Obteniendo perfil de usuario.");
        profile_info=GBStaffController.getInstance().get_profile_info(user.getGbUsername());
        log.debug("GabyvalSystem - Obteniendo catalogo de documentos.");
        idType=ADCatalogController.getInstance().geCatalogList("DOCUMENT_TYPE");
        log.debug("GabyvalSystem - Obteniendo catalogo de generos.");
        gender=ADCatalogController.getInstance().geCatalogList("GENDER");
    }
    
    /**
     * Regresa una foto de perfil desde base de datos.
     * @return StreamedContent la foto re creada.
     */
    public StreamedContent getPhoto_profile() {
        return photo_profile;
    }

    /**
     * Modifica el valor de la foto de usuario
     * @param photo_profile la nueva foto de usuario.
     */
    public void setPhoto_profile(StreamedContent photo_profile) {
        this.photo_profile = photo_profile;
    }
    
    /**
     * Regresa el contenedor del perfil de usuario.
     * @return GbStaff el perfil de usuario.
     */
    public GbStaff getProfile_info() {
        return profile_info;
    }

    /**
     * Modifica el contenedor de perfil de usuario.
     * @param profile_info el perfil de usuario.
     */
    public void setProfile_info(GbStaff profile_info) {
        this.profile_info = profile_info;
    }

    /**
     * Regresa el listado de tipo de identificacion.
     * @return List<SelectItem> el listado.
     */
    public List<SelectItem> getIdType() {
        return idType;
    }

    /**
     * Modifica el listado de tipo de identificaion.
     * @param idType el nuevo listado.
     */
    public void setIdType(List<SelectItem> idType) {
        this.idType = idType;
    }

    /**
     * Regresa el listado de generos.
     * @return List<SelectItem> el listado de generos.
     */
    public List<SelectItem> getGender() {
        return gender;
    }

    /**
     * Modifica el listado de generos.
     * @param gender el nuevo listado.
     */
    public void setGender(List<SelectItem> gender) {
        this.gender = gender;
    }

    /**
     * Regresa el valor actual del tipo de identificacion seleccionado.
     * @return int el tipo de identificacion.
     */
    public int getIdTypeId() {
        return idTypeId;
    }

    /**
     * Modifica el tipo de identificaion seleccionado.
     * @param idTypeId el nuevo tipo de identificacion.
     */
    public void setIdTypeId(int idTypeId) {
        this.idTypeId = idTypeId;
    }

    /**
     * Regresa el valor actual del genero seleccionado.
     * @return int el genero seleccionado.
     */
    public int getGenderId() {
        return genderId;
    }

    /**
     * Modifica el genero seleccionado.
     * @param genderId el nuevo genero seleccionado.
     */
    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }
    
    /**
     * Captura el evento de carga de una foto.
     * @param event el evento de carga.
     */
    public void handleFileUpload(FileUploadEvent event) {
        log.debug("GabyvalSystem - Se a cargado una nueva foto, obteniendo el archivo.");
        profile_info.setGbPhoto(event.getFile().getContents());
        log.debug("GabyvalSystem - Se obtuvo el archivo, transformando para renderizar.");
        photo_profile=new DefaultStreamedContent(new ByteArrayInputStream(profile_info.getGbPhoto()), "image/png");
        try {
            log.debug("GabyvalSystem - Guardando perfil de usuario con nueva foto.");
            GBStaffController.getInstance().saveProfile(profile_info);
        } catch (GBException ex) {
            log.error("GabyvalSystem - Se ha presentado un problema al intentar guardar el perfil de usuario:");
            log.error(ex);
            UIMessageManagement.putErrorMessage("No se pudo guardar la nueva foto de perfil.");
        }
        log.debug("GabyvalSystem - Foto guardada con exito.");
        photo_profile=getPhoto_profile();
        UIMessageManagement.putInfoMessage("Se ha guardado la foto exitosamente.");
    }
    
    /**
     * Guarda un perfil de usuario en la base de datos.
     */
    public void saveProfile(){
        log.debug("GabyvalSystem - Guardando perfil de usuario.");
        try {
            log.debug("GabyvalSystem - Obteniendo el genero dado su ID.");
            profile_info.setGender(ADCatalogController.getInstance().getCatalog(genderId));
            log.debug("GabyvalSystem - Obteniendo el tipo de identificacion dado su ID.");
            profile_info.setIdType(ADCatalogController.getInstance().getCatalog(idTypeId));
            log.debug("GabyvalSystem - Actualizando valor de revision.");
            profile_info.setRowversion(profile_info.getRowversion()+1);
            log.debug("GabyvalSystem - Guardando foto de perfil.");
//            photo_profile=new DefaultStreamedContent(new ByteArrayInputStream(profile_info.getGbPhoto()), "image/png");
            log.debug("GabyvalSystem - Enviando el perfil a base de datos.");
            GBStaffController.getInstance().saveProfile(profile_info);
            log.debug("GabyvalSystem - Guardado exitoso.");
        } catch (GBException | GBPersistenceException ex) {
           log.error("GabyvalSystem - Se ha presentado un problema al guardar el perfil de usuario.");
           log.error(ex);
           UIMessageManagement.putException(ex);
        }
    }

    /**
     * Carga un perfil de usuario.
     */
    private void loadProfile() {
        log.debug("GabyvalSystem - Seteando valor de tipo de documento.");
        idTypeId=profile_info.getIdType().getCatalogId();
        log.debug("GabyvalSystem - Seteando valor de genero.");
        genderId=profile_info.getGender().getCatalogId();
        log.debug("GabyvalSystem - Re formando foto de perfil.");
        photo_profile=new DefaultStreamedContent(new ByteArrayInputStream(profile_info.getGbPhoto()));
        log.debug("Perfil de usuario cargado.");
    }

    /**
     * Crea un perfil de usuario.
     */
    private void setDefaulProfile() {
        log.debug("GabyvalSystem - Creando un nuevo perfil de usuario.");
        profile_info=new GbStaff();
        log.debug("GabyvalSystem - Asociando perfil al nombre usuario.");
        profile_info.setGbUsername(user.getGbUsername());
        log.debug("GabyvalSystem - Asociando perfil al usuario.");
        profile_info.setGbUsers(user);
        log.debug("GabyvalSystem - Asociando fecha de creacion a dia de hoy.");
        profile_info.setCreateDt(Calendar.getInstance().getTime());
        log.debug("GabyvalSystem - Asociando valor de revision.");
        profile_info.setRowversion(-1);
        log.debug("GabyvalSystem - Asociando foto de perfil vacia.");
        profile_info.setGbPhoto(new byte[0]);
        log.debug("GabyvalSystem - Perfil por defecto creado.");
    }
}
