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
import logic.ArtistManager;
import model.Artist;

/**
 * Jersey REST client generated for REST resource:ArtistFacadeREST
 * [entities.artist]<br>
 * USAGE:
 * <pre>
 *        ArtistRESTClient client = new ArtistRESTClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author 2dam
 */
public class ArtistRESTClient implements ArtistManager {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/NocturnaServer/webresources";

    public ArtistRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.artist");
    }

    @Override
    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    @Override
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Artist.class);
    }

    @Override
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public void create_XML(Object requestEntity) throws InternalServerErrorException {
        WebTarget resource = webTarget;

        Response response = resource
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));

        if (response.getStatus() >= 500) {
            throw new InternalServerErrorException();
        }

    }

    @Override
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public <T> T findNotByEvent_XML(Class<T> responseType, String idEvent) throws ReadException, InternalServerErrorException {
        WebTarget resource = webTarget;

        resource = resource.path(java.text.MessageFormat.format("artistsNotByEvent/{0}", new Object[]{idEvent}));

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

    @Override
    public void remove(String id) throws InternalServerErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    @Override
    public <T> T findByEvent_XML(Class<T> responseType, String idEvent) throws ReadException, InternalServerErrorException {
        WebTarget resource = webTarget;

        resource = resource.path(java.text.MessageFormat.format("findArtistsByEvent/{0}", new Object[]{idEvent}));

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

    @Override
    public void close() {
        client.close();
    }

}
