/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import exceptions.InternalServerErrorException;
import exceptions.ReadException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import logic.ClubManager;
import model.Club;

/**
 * Jersey REST client generated for REST resource:ClubFacadeREST
 * [entities.club]<br>
 * USAGE:
 * <pre>
 *        ClubRESTClient client = new ClubRESTClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author 2dam
 */
public class ClubRESTClient implements ClubManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/NocturnaServer/webresources";

    public ClubRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.club");
    }

    @Override
    public <T> T getClubsByEventDate_XML(Class<T> responseType, String fecha) throws InternalServerErrorException, ReadException{
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/date/{0}", new Object[]{fecha}));

        Response response = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .get();

        if (response.getStatus() > 404) {
            throw new ReadException();
        }

        if (response.getStatus() > 500) {
            throw new InternalServerErrorException();
        }

        return response.readEntity(responseType);
    }

    public <T> T getClubsByEventDate_JSON(Class<T> responseType, String fecha) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/date/{0}", new Object[]{fecha}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML),
                        requestEntity.getClass());
    }

    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }
    
    @Override
    public <T> T getClubNameByEventId_XML(Class<T> responseType, String idEvent) throws InternalServerErrorException, ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/{0}", new Object[]{idEvent}));

        Response response = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .get();

        if (response.getStatus() > 404) {
            throw new ReadException();
        }

        if (response.getStatus() > 500) {
            throw new InternalServerErrorException();
        }

        return response.readEntity(responseType);
    }

    public <T> T getClubNameByEventId_JSON(Class<T> responseType, String idEvent) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/{0}", new Object[]{idEvent}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
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

    public void create_XML(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML),
                        requestEntity.getClass());
    }

    public void create_JSON(Object requestEntity) throws WebApplicationException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON),
                        requestEntity.getClass());
    }

    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void remove(String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                .request().delete(Club.class);
    }

    @Override
    public <T> T getClubsByEventDates_XML(Class<T> responseType, String fechaIni, String fechafin) throws InternalServerErrorException, ReadException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/date/{0}/{1}", new Object[]{fechaIni, fechafin}));

        Response response = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .get();

        if (response.getStatus() > 404) {
            throw new ReadException();
        }

        if (response.getStatus() > 500) {
            throw new InternalServerErrorException();
        }

        return response.readEntity(responseType);
    }

    public <T> T getClubsByEventDates_JSON(Class<T> responseType, String fechaIni, String fechafin) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("club/date/{0}/{1}", new Object[]{fechaIni, fechafin}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.close();
    }

}
