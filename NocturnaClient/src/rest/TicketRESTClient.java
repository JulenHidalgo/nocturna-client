/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.util.ResourceBundle;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import logic.TicketManager;
import model.Ticket;

/**
 * Cliente REST para el recurso TicketFacadeREST [entities.ticket].
 * Permite interactuar con el servicio REST para gestionar tickets.
 * 
 * @author Julen Hidalgo
 */
public class TicketRESTClient implements TicketManager {

    /**
     * Objeto WebTarget para realizar las solicitudes al servicio REST.
     */
    private WebTarget webTarget;

    /**
     * Cliente para la comunicación con el servicio REST.
     */
    private Client client;

    /**
     * URI base del servicio REST.
     */
    private static String BASE_URI;

    /**
     * Constructor de la clase TicketRESTClient.
     * Inicializa el cliente y configura la URI base.
     */
    public TicketRESTClient() {
        setUri();
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.ticket");
    }

    /**
     * Configura la URI base del servicio REST a partir de un archivo de configuración.
     */
    private void setUri() {
        ResourceBundle fichConf = ResourceBundle.getBundle("rest.uri");
        BASE_URI = fichConf.getString("BASE_URI");
    }

    /**
     * Obtiene el número total de tickets.
     * 
     * @return El número total de tickets en formato String.
     * @throws ClientErrorException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public String countREST() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    /**
     * Edita un ticket existente en formato XML.
     * 
     * @param requestEntity El objeto Ticket a editar.
     * @param id El ID del ticket a editar.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Ticket.class);
    }

    /**
     * Edita un ticket existente en formato JSON.
     * 
     * @param requestEntity El objeto Ticket a editar.
     * @param id El ID del ticket a editar.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Ticket.class);
    }

    /**
     * Busca un ticket por su ID en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param id El ID del ticket a buscar.
     * @return El ticket encontrado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Busca un ticket por su ID en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param id El ID del ticket a buscar.
     * @return El ticket encontrado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Busca tickets en un rango específico en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param from Índice de inicio del rango.
     * @param to Índice de fin del rango.
     * @return Lista de tickets en el rango especificado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Busca tickets en un rango específico en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param from Índice de inicio del rango.
     * @param to Índice de fin del rango.
     * @return Lista de tickets en el rango especificado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Crea un nuevo ticket en formato XML.
     * 
     * @param requestEntity El objeto Ticket a crear.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public void create_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Ticket.class);
    }

    /**
     * Crea un nuevo ticket en formato JSON.
     * 
     * @param requestEntity El objeto Ticket a crear.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public void create_JSON(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Ticket.class);
    }

    /**
     * Busca tickets asociados a un usuario en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param dni El DNI del usuario.
     * @return Lista de tickets asociados al usuario.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public <T> T findTicketByUser_XML(Class<T> responseType, String dni) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findTicketByUser/{0}", new Object[]{dni}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Busca tickets asociados a un usuario en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param dni El DNI del usuario.
     * @return Lista de tickets asociados al usuario.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public <T> T findTicketByUser_JSON(Class<T> responseType, String dni) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findTicketByUser/{0}", new Object[]{dni}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Obtiene todos los tickets en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @return Lista de todos los tickets.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Obtiene todos los tickets en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @return Lista de todos los tickets.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Elimina un ticket por su ID.
     * 
     * @param id El ID del ticket a eliminar.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    @Override
    public void remove(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    /**
     * Cierra el cliente REST.
     */
    @Override
    public void close() {
        client.close();
    }
}