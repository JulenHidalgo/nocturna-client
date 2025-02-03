/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import exceptions.InternalServerErrorException;
import exceptions.ReadException;
import exceptions.SignInException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import logic.EventManager;
import model.Event;

/**
 * Jersey REST client generated for REST resource:EventFacadeREST
 * [entities.event]<br>
 * USAGE:
 * <pre>
 * EventRESTClient client = new EventRESTClient();
 * Object response = client.XXX(...);
 * // do whatever with response
 * client.close();
 * </pre>
 *
 * @author 2dam
 */
public class EventRESTClient implements EventManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/NocturnaServer/webresources";

    public EventRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.event");
    }

    public <T> T findByDate_XML(Class<T> responseType, String fecha) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByDate/{0}", new Object[]{fecha}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findByDate_JSON(Class<T> responseType, String fecha) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByDate/{0}", new Object[]{fecha}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    public <T> T findByArtist_XML(Class<T> responseType, String idArtist) throws WebApplicationException, ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByArtist/{0}", new Object[]{idArtist}));

        Response response = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .get();

        // Verifica el estado de la respuesta
        if (response.getStatus() == 500) {
            // Opcionalmente, puedes extraer información del cuerpo de la respuesta
            String errorDetails = response.readEntity(String.class); // Suponiendo que el error tenga un mensaje en el cuerpo
            throw new ReadException();
        }

        return response.readEntity(responseType);
    }

    public <T> T findByArtist_JSON(Class<T> responseType, String idArtist) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByArtist/{0}", new Object[]{idArtist}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Event.class);
    }

    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Event.class);
    }

    @Override
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException, ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        Response response = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .get();

        // Verifica el estado de la respuesta+
        if (response.getStatus() == 500) {
            // Opcionalmente, puedes extraer información del cuerpo de la respuesta
            String errorDetails = response.readEntity(String.class); // Suponiendo que el error tenga un mensaje en el cuerpo
            throw new ReadException();
        }

        return response.readEntity(responseType);
    }

    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findByDates_XML(Class<T> responseType, String de_fecha, String hasta_fecha) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByDates/{0}/{1}", new Object[]{de_fecha, hasta_fecha}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findByDates_JSON(Class<T> responseType, String de_fecha, String hasta_fecha) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findEventsByDates/{0}/{1}", new Object[]{de_fecha, hasta_fecha}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    public void create_XML(Object requestEntity) throws InternalServerErrorException{
        WebTarget resource = webTarget;
         Response response = resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        
         if (response.getStatus() >= 500) {
            throw new InternalServerErrorException();
        }
    }

    public void create_JSON(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Event.class);
    }

    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void remove(String id) throws InternalServerErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    public void close() {
        client.close();
    }

}
