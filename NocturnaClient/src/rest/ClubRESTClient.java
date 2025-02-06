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
import logic.ClubManager;
import model.Club;

/**
 * Cliente REST para el recurso ClubFacadeREST [entities.club].
 * Permite interactuar con el servicio REST para gestionar clubes.
 * 
 * @author Adrian Rocha
 */
public class ClubRESTClient implements ClubManager {

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
     * Constructor de la clase ClubRESTClient.
     * Inicializa el cliente y configura la URI base.
     */
    public ClubRESTClient() {
        setUri();
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.club");
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
     * Obtiene los clubes asociados a eventos en una fecha específica en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param fecha La fecha de los eventos.
     * @return Lista de clubes asociados a eventos en la fecha especificada.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T getClubsByEventDate_XML(Class<T> responseType, String fecha) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/date/{0}", new Object[]{fecha}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    /**
     * Obtiene los clubes asociados a eventos en una fecha específica en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param fecha La fecha de los eventos.
     * @return Lista de clubes asociados a eventos en la fecha especificada.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T getClubsByEventDate_JSON(Class<T> responseType, String fecha) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/date/{0}", new Object[]{fecha}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Obtiene el número total de clubes.
     * 
     * @return El número total de clubes en formato String.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    @Override
    /**
     * Edita un club existente en formato XML.
     * 
     * @param requestEntity El objeto Club a editar.
     * @param id El ID del club a editar.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), requestEntity.getClass());
    }

    @Override
    /**
     * Edita un club existente en formato JSON.
     * 
     * @param requestEntity El objeto Club a editar.
     * @param id El ID del club a editar.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    @Override
    /**
     * Obtiene el nombre del club asociado a un evento en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param idEvent El ID del evento.
     * @return El nombre del club asociado al evento.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T getClubNameByEventId_XML(Class<T> responseType, String idEvent) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/{0}", new Object[]{idEvent}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    /**
     * Obtiene el nombre del club asociado a un evento en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param idEvent El ID del evento.
     * @return El nombre del club asociado al evento.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T getClubNameByEventId_JSON(Class<T> responseType, String idEvent) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/{0}", new Object[]{idEvent}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Busca un club por su ID en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param id El ID del club a buscar.
     * @return El club encontrado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    /**
     * Busca un club por su ID en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param id El ID del club a buscar.
     * @return El club encontrado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Busca clubes en un rango específico en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param from Índice de inicio del rango.
     * @param to Índice de fin del rango.
     * @return Lista de clubes en el rango especificado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    /**
     * Busca clubes en un rango específico en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param from Índice de inicio del rango.
     * @param to Índice de fin del rango.
     * @return Lista de clubes en el rango especificado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Crea un nuevo club en formato XML.
     * 
     * @param requestEntity El objeto Club a crear.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public void create_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), requestEntity.getClass());
    }

    @Override
    /**
     * Crea un nuevo club en formato JSON.
     * 
     * @param requestEntity El objeto Club a crear.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public void create_JSON(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), requestEntity.getClass());
    }

    @Override
    /**
     * Obtiene todos los clubes en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @return Lista de todos los clubes.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    /**
     * Obtiene todos los clubes en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @return Lista de todos los clubes.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Elimina un club por su ID.
     * 
     * @param id El ID del club a eliminar.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public void remove(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                .request().delete(Club.class);
    }

    @Override
    /**
     * Obtiene los clubes asociados a eventos en un rango de fechas en formato XML.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param fechaIni Fecha de inicio del rango.
     * @param fechafin Fecha de fin del rango.
     * @return Lista de clubes asociados a eventos en el rango de fechas especificado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T getClubsByEventDates_XML(Class<T> responseType, String fechaIni, String fechafin) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/date/{0}/{1}", new Object[]{fechaIni, fechafin}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    /**
     * Obtiene los clubes asociados a eventos en un rango de fechas en formato JSON.
     * 
     * @param <T> Tipo de respuesta esperada.
     * @param responseType Clase del tipo de respuesta.
     * @param fechaIni Fecha de inicio del rango.
     * @param fechafin Fecha de fin del rango.
     * @return Lista de clubes asociados a eventos en el rango de fechas especificado.
     * @throws WebApplicationException Si ocurre un error durante la comunicación con el servidor.
     */
    public <T> T getClubsByEventDates_JSON(Class<T> responseType, String fechaIni, String fechafin) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/date/{0}/{1}", new Object[]{fechaIni, fechafin}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    /**
     * Cierra el cliente REST.
     */
    public void close() {
        client.close();
    }
}