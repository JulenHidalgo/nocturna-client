package logic;

import javax.ws.rs.WebApplicationException;

/**
 * Interfaz que define los métodos para la gestión de administradores.
 * Permite realizar operaciones como contar, editar, crear, buscar y eliminar administradores.
 * 
 * @author Julen Hidalgo
 */
public interface AdminManager {

    /**
     * Obtiene el número total de administradores.
     * 
     * @return Una cadena que representa el número total de administradores.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public String countREST() throws WebApplicationException;

    /**
     * Edita un administrador utilizando los datos proporcionados en formato XML.
     * 
     * @param requestEntity Los datos del administrador a actualizar.
     * @param id El ID del administrador a actualizar.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Edita un administrador utilizando los datos proporcionados en formato JSON.
     * 
     * @param requestEntity Los datos del administrador a actualizar.
     * @param id El ID del administrador a actualizar.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Busca un administrador por su ID y devuelve los datos correspondientes en formato XML.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param id El ID del administrador a buscar.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Los datos del administrador mapeados al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException;

    /**
     * Busca un administrador por su ID y devuelve los datos correspondientes en formato JSON.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param id El ID del administrador a buscar.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Los datos del administrador mapeados al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException;

    /**
     * Busca un rango de administradores basado en los valores proporcionados para el rango, en formato XML.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param from El índice inicial para el rango de administradores.
     * @param to El índice final para el rango de administradores.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Una colección de administradores dentro del rango especificado, mapeada al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    /**
     * Busca un rango de administradores basado en los valores proporcionados para el rango, en formato JSON.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param from El índice inicial para el rango de administradores.
     * @param to El índice final para el rango de administradores.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Una colección de administradores dentro del rango especificado, mapeada al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException;

    /**
     * Crea un nuevo administrador con los datos proporcionados en formato XML.
     * 
     * @param requestEntity Los datos del administrador a crear.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public void create_XML(Object requestEntity) throws WebApplicationException;

    /**
     * Crea un nuevo administrador con los datos proporcionados en formato JSON.
     * 
     * @param requestEntity Los datos del administrador a crear.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public void create_JSON(Object requestEntity) throws WebApplicationException;

    /**
     * Obtiene todos los administradores en formato XML.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Una colección de todos los administradores, mapeada al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException;

    /**
     * Obtiene todos los administradores en formato JSON.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Una colección de todos los administradores, mapeada al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException;

    /**
     * Elimina un administrador por su ID.
     * 
     * @param id El ID del administrador a eliminar.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public void remove(String id) throws WebApplicationException;

    /**
     * Cierra la gestión de los administradores y libera los recursos.
     */
    public void close();
}
