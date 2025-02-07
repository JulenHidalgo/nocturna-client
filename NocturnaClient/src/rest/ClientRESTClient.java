/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import exceptions.InternalServerErrorException;
import exceptions.ReadException;
import exceptions.SignUpException;
import java.util.ResourceBundle;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import logic.ClientManager;
import javax.ws.rs.core.Response;

/**
 * Cliente REST generado para el recurso REST: ClientFacadeREST [entities.client].
 * Permite interactuar con el servidor para realizar operaciones CRUD sobre clientes.
 * 
 * @author Adrian Rocha
 */
public class ClientRESTClient implements ClientManager {

    /**
     * Objeto WebTarget para realizar las peticiones al servidor.
     */
    private WebTarget webTarget;

    /**
     * Cliente REST para realizar las conexiones.
     */
    private Client client;

    /**
     * URI base del servidor REST.
     */
    private static String BASE_URI = "http://localhost:8080/NocturnaServer/webresources";

    /**
     * Constructor de la clase ClientRESTClient.
     * Inicializa el cliente y configura la URI base.
     */
    public ClientRESTClient() {
        setUri();
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.client");
    }

    /**
     * Configura la URI base del servidor desde un archivo de configuración.
     */
    private void setUri() {
        ResourceBundle fichConf = ResourceBundle.getBundle("rest.uri");
        BASE_URI = fichConf.getString("BASE_URI");
    }

    /**
    * Realiza una llamada HTTP GET al endpoint 'count' para obtener el recuento solicitado.
    * 
    * Este método utiliza la configuración existente en 
    * para realizar la petición
    * y devuelve la respuesta como texto plano.
    * 
    * @return La respuesta del servidor en formato texto plano
    * @throws WebApplicationException si ocurre un error en la aplicación web durante la petición
    * @since [versión] - Versión inicial del método
    */
    @Override
    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    /**
    * Edita un recurso existente mediante una petición PUT con contenido XML.
    * 
    * @param requestEntity Entidad que contiene los datos actualizados
    * @param id Identificador del recurso a modificar
    * @throws WebApplicationException si ocurre un error en la operación
    */
    @Override
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Client.class);
    }

    /**
     * Edita un cliente en formato JSON.
     * 
     * @param requestEntity El objeto cliente a editar.
     * @param id El ID del cliente a editar.
     * @throws ClientErrorException Si ocurre un error en el cliente.
     */
    public void edit_JSON(Object requestEntity, String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
 * Recupera un recurso específico mediante una petición GET con respuesta XML.
 * 
 * @param responseType Tipo de clase esperado en la respuesta
 * @param id Identificador del recurso a recuperar
 * @return Instancia del tipo especificado con los datos obtenidos
 * @throws WebApplicationException si ocurre un error en la operación
 */
    @Override
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Busca un cliente en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase de la respuesta esperada.
     * @param id El ID del cliente a buscar.
     * @return El cliente encontrado.
     * @throws ClientErrorException Si ocurre un error en el cliente.
     */
    public <T> T find_JSON(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    
    /**
    * Recupera un rango de recursos mediante una petición GET con respuesta XML.
    * 
    * @param responseType Tipo de clase esperado en la respuesta
    * @param from Valor inicial del rango
    * @param to Valor final del rango
    * @return Instancia del tipo especificado con los datos obtenidos
    * @throws WebApplicationException si ocurre un error en la operación
    */
    @Override
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Busca un rango de clientes en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase de la respuesta esperada.
     * @param from Índice inicial del rango.
     * @param to Índice final del rango.
     * @return Lista de clientes encontrados.
     * @throws ClientErrorException Si ocurre un error en el cliente.
     */
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
    * Crea un nuevo recurso mediante una petición POST con contenido XML.
    * 
    * @param requestEntity Objeto que contiene los datos a crear
    * @throws WebApplicationException si ocurre un error en la aplicación web
    * @throws SignUpException si hay conflictos en el registro
    * @throws InternalServerErrorException si hay errores internos del servidor
    * @throws ReadException si no se puede acceder al recurso
    */
    @Override
    public void create_XML(Object requestEntity) throws WebApplicationException, SignUpException, InternalServerErrorException, ReadException {
        WebTarget resource = webTarget;
        Response response = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));

        // Verifica el estado de la respuesta
        if (response.getStatus() == 400 || response.getStatus() == 500) {
            throw new InternalServerErrorException();
        }

        if (response.getStatus() == 404) {
            throw new ReadException();
        }

        if (response.getStatus() == 406) {
            throw new SignUpException();
        }

        if (response.getStatus() >= 400) {
            throw new WebApplicationException("Error HTTP: " + response.getStatus());
        }
    }

    /**
     * Crea un nuevo cliente en formato JSON.
     * 
     * @param requestEntity El objeto cliente a crear.
     * @throws ClientErrorException Si ocurre un error en el cliente.
     */
    public void create_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
    * Recupera todos los recursos disponibles mediante una petición GET con respuesta XML.
    * 
    * @param responseType Tipo de clase esperado en la respuesta
    * @return Instancia del tipo especificado con todos los recursos obtenidos
    * @throws WebApplicationException si ocurre un error en la operación
    */
    @Override
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Obtiene todos los clientes en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase de la respuesta esperada.
     * @return Lista de todos los clientes.
     * @throws ClientErrorException Si ocurre un error en el cliente.
     */
    public <T> T findAll_JSON(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
    * Elimina un recurso específico identificado por el ID proporcionado.
    * 
    * @param id la identificación única del recurso a eliminar
    * @throws WebApplicationException si ocurre un error durante la operación de eliminación
    */
    @Override
    public void remove(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    /**
    * Cierra el cliente y libera los recursos asociados.
    * Esta operación es irreversible y el cliente no podrá volver a usarse después de llamar este método.
    */
    @Override
    public void close() {
        client.close();
    }
}