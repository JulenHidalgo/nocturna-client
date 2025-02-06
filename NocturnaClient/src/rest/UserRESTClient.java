/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import exceptions.SignInException;
import java.util.ResourceBundle;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import logic.UserManager;
import model.User;

/**
 * Cliente REST para el recurso UserFacadeREST [entities.user].
 * Permite interactuar con el servicio REST para gestionar usuarios.
 *
 * @author Erlantz Rey
 */
public class UserRESTClient implements UserManager {
    
    private WebTarget webTarget;
    private Client client;
    private static String BASE_URI;

    /**
     * Constructor que inicializa el cliente REST con la URL base del servicio.
     * Lee la configuración desde el archivo de propiedades rest.uri.
     */
    public UserRESTClient() {
        setUri();
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.user");
    }
    
    /**
     * Establece la URL base del servicio desde el archivo de configuración.
     */
    private void setUri() {
        ResourceBundle fichConf = ResourceBundle.getBundle("rest.uri");
        BASE_URI = fichConf.getString("BASE_URI");
    }

    /**
     * Realiza una solicitud de reinicio de contraseña en formato XML.
     * 
     * @param responseType Tipo de respuesta esperada
     * @param userEmail Email del usuario
     * @return Respuesta del servidor en el tipo especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    @Override
    public <T> T resetPassword_XML(Class<T> responseType, String userEmail) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("resetPassword/{0}", new Object[]{userEmail}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Realiza una solicitud de reinicio de contraseña en formato JSON.
     * 
     * @param responseType Tipo de respuesta esperada
     * @param userEmail Email del usuario
     * @return Respuesta del servidor en el tipo especificado
     * @throws ClientErrorException Si ocurre un error en el cliente
     */
    public <T> T resetPassword_JSON(Class<T> responseType, String userEmail) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("resetPassword/{0}", new Object[]{userEmail}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Obtiene el número total de usuarios en formato texto plano.
     * 
     * @return Número total de usuarios como String
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    @Override
    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    /**
     * Actualiza un usuario existente en formato XML.
     * 
     * @param requestEntity Entidad del usuario a actualizar
     * @param id Identificador del usuario
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    @Override
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), User.class);
    }

    /**
     * Actualiza un usuario existente en formato JSON.
     * 
     * @param requestEntity Entidad del usuario a actualizar
     * @param id Identificador del usuario
     * @throws ClientErrorException Si ocurre un error en el cliente
     */
    public void edit_JSON(Object requestEntity, String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * Busca un usuario por su identificador en formato XML.
     * 
     * @param responseType Tipo de respuesta esperada
     * @param id Identificador del usuario
     * @return Usuario encontrado en el tipo especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    @Override
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Busca un usuario por su identificador en formato JSON.
     * 
     * @param responseType Tipo de respuesta esperada
     * @param id Identificador del usuario
     * @return Usuario encontrado en el tipo especificado
     * @throws ClientErrorException Si ocurre un error en el cliente
     */
    public <T> T find_JSON(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Busca un rango de usuarios en formato XML.
     * 
     * @param responseType Tipo de respuesta esperada
     * @param from Valor inicial del rango
     * @param to Valor final del rango
     * @return Lista de usuarios en el tipo especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    @Override
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Busca un rango de usuarios en formato JSON.
     * 
     * @param responseType Tipo de respuesta esperada
     * @param from Valor inicial del rango
     * @param to Valor final del rango
     * @return Lista de usuarios en el tipo especificado
     * @throws ClientErrorException Si ocurre un error en el cliente
     */
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Crea un nuevo usuario en formato XML.
     * 
     * @param requestEntity Entidad del usuario a crear
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    @Override
    public void create_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), User.class);
    }

    /**
     * Crea un nuevo usuario en formato JSON.
     * 
     * @param requestEntity Entidad del usuario a crear
     * @throws ClientErrorException Si ocurre un error en el cliente
     */
    public void create_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * Inicia sesión en el sistema en formato XML.
     * 
     * @param responseType Tipo de respuesta esperada
     * @param mail Email del usuario
     * @param passwd Contraseña del usuario
     * @return Respuesta del servidor en el tipo especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     * @throws SignInException Si el inicio de sesión falla
     */
    @Override
    public <T> T login_XML(Class<T> responseType, String mail, String passwd) throws WebApplicationException, SignInException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("login/{0}/{1}", new Object[]{mail, passwd}));

        Response response = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .get();

        if (response.getStatus() == 404) {
            throw new SignInException();
        }

        if (response.getStatus() >= 400) {
            throw new WebApplicationException("Error HTTP: " + response.getStatus());
        }

        return response.readEntity(responseType);
    }

    /**
     * Inicia sesión en el sistema en formato JSON.
     * 
     * @param responseType Tipo de respuesta esperada
     * @param mail Email del usuario
     * @param passwd Contraseña del usuario
     * @return Respuesta del servidor en el tipo especificado
     * @throws ClientErrorException Si ocurre un error en el cliente
     */
    public <T> T login_JSON(Class<T> responseType, String mail, String passwd) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("login/{0}/{1}", new Object[]{mail, passwd}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Actualiza la contraseña de un usuario en formato XML.
     * 
     * @param requestEntity Entidad del usuario
     * @param newPasswd Nueva contraseña
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    @Override
    public void updatePasswd_XML(Object requestEntity, String newPasswd) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("updatePasswd/{0}", new Object[]{newPasswd})).request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), User.class);
    }

    /**
     * Obtiene todos los usuarios en formato XML.
     * 
     * @param responseType Tipo de respuesta esperada
     * @return Lista de usuarios en el tipo especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    @Override
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Obtiene todos los usuarios en formato JSON.
     * 
     * @param responseType Tipo de respuesta esperada
     * @return Lista de usuarios en el tipo especificado
     * @throws ClientErrorException Si ocurre un error en el cliente
     */
    public <T> T findAll_JSON(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Elimina un usuario por su identificador.
     * 
     * @param id Identificador del usuario a eliminar
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    @Override
    public void remove(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    /**
     * Cierra el cliente y libera los recursos.
     */
    @Override
    public void close() {
        client.close();
    }
}