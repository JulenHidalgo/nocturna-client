/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.util.ResourceBundle;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import logic.AdminManager;
import model.Admin;

/**
 * Cliente REST para gestionar administradores mediante servicios web.
 * Implementa la interfaz AdminManager para proporcionar operaciones CRUD
 * sobre recursos de tipo administrador.
 * 
 * @author 2dam
 */
public class AdminRESTClient implements AdminManager {

    /**
     * Punto objetivo web para realizar peticiones HTTP al servidor.
     */
    private WebTarget webTarget;

    /**
     * Cliente HTTP que gestiona las conexiones con el servidor.
     */
    private Client client;

    /**
     * URI base para acceder al servicio web.
     */
    private static String BASE_URI;

    /**
     * Constructor que inicializa el cliente REST.
     * Configura la conexión al servidor y establece el punto objetivo base.
     */
    public AdminRESTClient() {
        setUri();
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.admin");
    }

    /**
     * Obtiene el número total de administradores en el sistema.
     * 
     * @return cadena con el recuento total de administradores
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    /**
     * Actualiza un administrador existente con datos XML.
     * 
     * @param requestEntity objeto que contiene los datos actualizados del administrador
     * @param id identificador único del administrador a modificar
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                 .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                 .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML)
                 , requestEntity.getClass());
    }

    /**
     * Actualiza un administrador existente con datos JSON.
     * 
     * @param requestEntity objeto que contiene los datos actualizados del administrador
     * @param id identificador único del administrador a modificar
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                 .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                 .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON)
                 , requestEntity.getClass());
    }

    /**
     * Recupera un administrador específico por su identificador en formato XML.
     * 
     * @param responseType tipo de respuesta esperado
     * @param id identificador único del administrador
     * @return el administrador solicitado
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Recupera un administrador específico por su identificador en formato JSON.
     * 
     * @param responseType tipo de respuesta esperado
     * @param id identificador único del administrador
     * @return el administrador solicitado
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Recupera un rango de administradores según los identificadores proporcionados en XML.
     * 
     * @param responseType tipo de respuesta esperado
     * @param from identificador inicial del rango
     * @param to identificador final del rango
     * @return lista de administradores en el rango especificado
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Recupera un rango de administradores según los identificadores proporcionados en JSON.
     * 
     * @param responseType tipo de respuesta esperado
     * @param from identificador inicial del rango
     * @param to identificador final del rango
     * @return lista de administradores en el rango especificado
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Crea un nuevo administrador con datos XML.
     * 
     * @param requestEntity objeto que contiene los datos del nuevo administrador
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public void create_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML)
                , requestEntity.getClass());
    }

    /**
     * Crea un nuevo administrador con datos JSON.
     * 
     * @param requestEntity objeto que contiene los datos del nuevo administrador
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public void create_JSON(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON)
                , requestEntity.getClass());
    }

    /**
     * Recupera todos los administradores disponibles en formato XML.
     * 
     * @param responseType tipo de respuesta esperado
     * @return lista completa de administradores
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Recupera todos los administradores disponibles en formato JSON.
     * 
     * @param responseType tipo de respuesta esperado
     * @return lista completa de administradores
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Elimina un administrador del sistema según su identificador.
     * 
     * @param id identificador único del administrador a eliminar
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public void remove(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                 .request()
                 .delete(Admin.class);
    }

    /**
     * Cierra el cliente y libera los recursos asociados.
     */
    @Override
    public void close() {
        client.close();
    }

    /**
     * Establece la URI base del servicio desde el archivo de configuración.
     */
    private void setUri() {
        ResourceBundle fichConf = ResourceBundle.getBundle("rest.uri");
        BASE_URI = fichConf.getString("BASE_URI");
    }
}