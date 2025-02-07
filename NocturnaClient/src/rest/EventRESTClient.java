/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import exceptions.InternalServerErrorException;
import exceptions.ReadException;
import java.util.ResourceBundle;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import logic.EventManager;
import model.Event;

/**
 * Cliente REST para el recurso EventFacadeREST [entities.event].
 * Permite interactuar con el servicio REST para gestionar eventos.
 * 
 * @author Erlantz Rey
 */
public class EventRESTClient implements EventManager {

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
     * Constructor de la clase EventRESTClient.
     * Inicializa el cliente y configura la URI base.
     */
    public EventRESTClient() {
        setUri();
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.event");
    }

    /**
     * Configura la URI base del servicio REST a partir de un archivo de configuración.
     */
    private void setUri() {
        ResourceBundle fichConf = ResourceBundle.getBundle("rest.uri");
        BASE_URI = fichConf.getString("BASE_URI");
    }

    @Override
    /**
     * Busca eventos por fecha en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param fecha La fecha de los eventos a buscar.
     * @return Lista de eventos encontrados.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findByDate_XML(Class<T> responseType, String fecha) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByDate/{0}", new Object[]{fecha}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    /**
     * Busca eventos por fecha en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param fecha La fecha de los eventos a buscar.
     * @return Lista de eventos encontrados.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findByDate_JSON(Class<T> responseType, String fecha) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByDate/{0}", new Object[]{fecha}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Busca eventos por artista en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param idArtist El ID del artista.
     * @return Lista de eventos asociados al artista.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     * @throws ReadException Si ocurre un error al leer los datos.
     */
    public <T> T findByArtist_XML(Class<T> responseType, String idArtist) throws WebApplicationException, ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByArtist/{0}", new Object[]{idArtist}));

        Response response = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .get();

        if (response.getStatus() == 500) {
            String errorDetails = response.readEntity(String.class);
            throw new ReadException();
        }

        return response.readEntity(responseType);
    }

    @Override
    /**
     * Busca eventos por artista en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param idArtist El ID del artista.
     * @return Lista de eventos asociados al artista.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findByArtist_JSON(Class<T> responseType, String idArtist) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByArtist/{0}", new Object[]{idArtist}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Obtiene el número total de eventos.
     * 
     * @return El número total de eventos en formato String.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    @Override
    /**
     * Edita un evento existente en formato XML.
     * 
     * @param requestEntity El objeto Event a editar.
     * @param id El ID del evento a editar.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Event.class);
    }

    @Override
    /**
     * Edita un evento existente en formato JSON.
     * 
     * @param requestEntity El objeto Event a editar.
     * @param id El ID del evento a editar.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Event.class);
    }

    @Override
    /**
     * Busca un evento por su ID en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param id El ID del evento a buscar.
     * @return El evento encontrado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     * @throws ReadException Si ocurre un error al leer los datos.
     */
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException, ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        Response response = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .get();

        if (response.getStatus() == 500) {
            String errorDetails = response.readEntity(String.class);
            throw new ReadException();
        }

        return response.readEntity(responseType);
    }

    @Override
    /**
     * Busca un evento por su ID en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param id El ID del evento a buscar.
     * @return El evento encontrado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Busca eventos en un rango específico en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param from Índice de inicio del rango.
     * @param to Índice de fin del rango.
     * @return Lista de eventos en el rango especificado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    /**
     * Busca eventos en un rango específico en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param from Índice de inicio del rango.
     * @param to Índice de fin del rango.
     * @return Lista de eventos en el rango especificado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Busca eventos entre dos fechas en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param de_fecha Fecha de inicio.
     * @param hasta_fecha Fecha de fin.
     * @return Lista de eventos entre las fechas especificadas.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findByDates_XML(Class<T> responseType, String de_fecha, String hasta_fecha) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByDates/{0}/{1}", new Object[]{de_fecha, hasta_fecha}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    /**
     * Busca eventos entre dos fechas en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param de_fecha Fecha de inicio.
     * @param hasta_fecha Fecha de fin.
     * @return Lista de eventos entre las fechas especificadas.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findByDates_JSON(Class<T> responseType, String de_fecha, String hasta_fecha) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByDates/{0}/{1}", new Object[]{de_fecha, hasta_fecha}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Crea un nuevo evento en formato XML.
     * 
     * @param requestEntity El objeto Event a crear.
     * @throws InternalServerErrorException Si ocurre un error interno en el servidor.
     */
    public void create_XML(Object requestEntity) throws InternalServerErrorException {
        WebTarget resource = webTarget;
        Response response = resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));

        if (response.getStatus() >= 500) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    /**
     * Crea un nuevo evento en formato JSON.
     * 
     * @param requestEntity El objeto Event a crear.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public void create_JSON(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Event.class);
    }

    @Override
    /**
     * Obtiene todos los eventos en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @return Lista de todos los eventos.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    /**
     * Obtiene todos los eventos en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @return Lista de todos los eventos.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Elimina un evento por su ID.
     * 
     * @param id El ID del evento a eliminar.
     * @throws InternalServerErrorException Si ocurre un error interno en el servidor.
     */
    public void remove(String id) throws InternalServerErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    @Override
    /**
     * Cierra el cliente REST.
     */
    public void close() {
        client.close();
    }
}