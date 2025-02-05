package logic;

import exceptions.InternalServerErrorException;
import exceptions.ReadException;
import javax.ws.rs.WebApplicationException;

/**
 * Interfaz que define los métodos para la gestión de artistas.
 * Permite realizar operaciones como contar, editar, crear, buscar y eliminar artistas.
 * 
 * @author Julen Hidalgo
 */
public interface ArtistManager {

    /**
     * Obtiene el número total de artistas.
     * 
     * @return Una cadena que representa el número total de artistas.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public String countREST() throws WebApplicationException;

    /**
     * Edita un artista utilizando los datos proporcionados.
     * 
     * @param requestEntity Los datos del artista a actualizar.
     * @param id El ID del artista a actualizar.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Busca un artista por su ID y devuelve los datos correspondientes.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param id El ID del artista a buscar.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Los datos del artista mapeados al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException;

    /**
     * Busca un rango de artistas basado en los valores proporcionados para el rango.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param from El índice inicial para el rango de artistas.
     * @param to El índice final para el rango de artistas.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Una colección de artistas dentro del rango especificado, mapeada al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    /**
     * Crea un nuevo artista con los datos proporcionados.
     * 
     * @param requestEntity Los datos del artista a crear.
     * @throws InternalServerErrorException Si ocurre un error interno del servidor.
     */
    public void create_XML(Object requestEntity) throws InternalServerErrorException;

    /**
     * Obtiene todos los artistas.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Una colección de todos los artistas, mapeada al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException;

    /**
     * Busca los artistas que no están asociados con el evento proporcionado.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param idEvent El ID del evento.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Una colección de artistas no asociados al evento, mapeada al tipo de respuesta.
     * @throws ReadException Si ocurre un error al leer los datos.
     * @throws InternalServerErrorException Si ocurre un error interno del servidor.
     */
    public <T> T findNotByEvent_XML(Class<T> responseType, String idEvent) throws ReadException, InternalServerErrorException;

    /**
     * Elimina un artista por su ID.
     * 
     * @param id El ID del artista a eliminar.
     * @throws InternalServerErrorException Si ocurre un error interno del servidor.
     */
    public void remove(String id) throws InternalServerErrorException;

    /**
     * Busca los artistas asociados con el evento proporcionado.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param idEvent El ID del evento.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Una colección de artistas asociados al evento, mapeada al tipo de respuesta.
     * @throws ReadException Si ocurre un error al leer los datos.
     * @throws InternalServerErrorException Si ocurre un error interno del servidor.
     */
    public <T> T findByEvent_XML(Class<T> responseType, String idEvent) throws ReadException, InternalServerErrorException;

    /**
     * Cierra la gestión de los artistas y libera los recursos.
     */
    public void close();
}
