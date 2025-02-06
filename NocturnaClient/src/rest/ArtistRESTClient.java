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
import logic.ArtistManager;
import model.Artist;

/**
 * Cliente REST para gestionar artistas mediante servicios web.
 * Implementa la interfaz ArtistManager para proporcionar operaciones CRUD
 * sobre recursos de tipo artista.
 * 
 * @author Julen Hidalgo
 */
public class ArtistRESTClient implements ArtistManager {

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
    public ArtistRESTClient() {
        setUri();
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.artist");
    }

    /**
     * Establece la URI base del servicio desde el archivo de configuración.
     */
    private void setUri() {
        ResourceBundle fichConf = ResourceBundle.getBundle("rest.uri");
        BASE_URI = fichConf.getString("BASE_URI");
    }

    /**
     * Obtiene el número total de artistas en el sistema.
     * 
     * @return cadena con el recuento total de artistas
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public String countREST() throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    /**
     * Actualiza un artista existente con la información proporcionada.
     * 
     * @param requestEntity objeto que contiene los datos actualizados del artista
     * @param id identificador único del artista a modificar
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                 .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                 .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Artist.class);
    }

    /**
     * Recupera un artista específico por su identificador.
     * 
     * @param responseType tipo de respuesta esperado
     * @param id identificador único del artista
     * @return el artista solicitado
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Recupera un rango de artistas según los identificadores proporcionados.
     * 
     * @param responseType tipo de respuesta esperado
     * @param from identificador inicial del rango
     * @param to identificador final del rango
     * @return lista de artistas en el rango especificado
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Crea un nuevo artista en el sistema.
     * 
     * @param requestEntity objeto que contiene los datos del nuevo artista
     * @throws InternalServerErrorException si ocurre un error interno en el servidor
     */
    @Override
    public void create_XML(Object requestEntity) throws InternalServerErrorException {
        WebTarget resource = webTarget;
        Response response = resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                                 .post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));

        if (response.getStatus() >= 500) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * Recupera todos los artistas disponibles en el sistema.
     * 
     * @param responseType tipo de respuesta esperado
     * @return lista completa de artistas
     * @throws WebApplicationException si ocurre un error en la petición HTTP
     */
    @Override
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Recupera los artistas que no están asociados a un evento específico.
     * 
     * @param responseType tipo de respuesta esperado
     * @param idEvent identificador del evento
     * @return lista de artistas no asociados al evento
     * @throws ReadException si ocurre un error al leer los datos
     * @throws InternalServerErrorException si ocurre un error interno en el servidor
     */
    @Override
    public <T> T findNotByEvent_XML(Class<T> responseType, String idEvent) throws ReadException, InternalServerErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("artistsNotByEvent/{0}", new Object[]{idEvent}));
        Response response = resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get();
        
        if (response.getStatus() > 404) {
            throw new ReadException();
        }
        
        if (response.getStatus() > 500) {
            throw new InternalServerErrorException();
        }
        
        return response.readEntity(responseType);
    }

    /**
     * Elimina un artista del sistema según su identificador.
     * 
     * @param id identificador único del artista a eliminar
     * @throws InternalServerErrorException si ocurre un error interno en el servidor
     */
    @Override
    public void remove(String id) throws InternalServerErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                 .request()
                 .delete();
    }

    /**
     * Recupera los artistas asociados a un evento específico.
     * 
     * @param responseType tipo de respuesta esperado
     * @param idEvent identificador del evento
     * @return lista de artistas asociados al evento
     * @throws ReadException si ocurre un error al leer los datos
     * @throws InternalServerErrorException si ocurre un error interno en el servidor
     */
    @Override
    public <T> T findByEvent_XML(Class<T> responseType, String idEvent) throws ReadException, InternalServerErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("findArtistsByEvent/{0}", new Object[]{idEvent}));
        
        Response response = resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get();

        if (response.getStatus() > 404) {
            throw new ReadException();
        }

        if (response.getStatus() > 500) {
            throw new InternalServerErrorException();
        }

        return response.readEntity(responseType);
    }

    /**
     * Cierra el cliente y libera los recursos asociados.
     */
    @Override
    public void close() {
        client.close();
    }
}