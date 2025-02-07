/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import exceptions.InternalServerErrorException;
import exceptions.ReadException;
import javax.ws.rs.WebApplicationException;

/**
 * Interfaz que define los métodos para la gestión de eventos en la aplicación.
 * Esta interfaz proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la gestión de eventos, con soporte tanto para formato XML como JSON.
 * Todas las operaciones están diseñadas para trabajar con la API REST y manejan
 * excepciones específicas de la aplicación web.
 * 
 * @author Erlantz Rey
 */
public interface EventManager {
    
    /**
     * Busca eventos por fecha específica usando formato XML.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param fecha La fecha en la que se buscan los eventos
     * @return Los eventos encontrados en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findByDate_XML(Class<T> responseType, String fecha) throws WebApplicationException;

    /**
     * Busca eventos por fecha específica usando formato JSON.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param fecha La fecha en la que se buscan los eventos
     * @return Los eventos encontrados en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findByDate_JSON(Class<T> responseType, String fecha) throws WebApplicationException;

    /**
     * Busca eventos por artista específico usando formato XML.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param idArtist El identificador del artista
     * @return Los eventos del artista en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     * @throws ReadException Si hay un error al leer los datos
     */
    public <T> T findByArtist_XML(Class<T> responseType, String idArtist) throws WebApplicationException, ReadException;

    /**
     * Busca eventos por artista específico usando formato JSON.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param idArtist El identificador del artista
     * @return Los eventos del artista en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findByArtist_JSON(Class<T> responseType, String idArtist) throws WebApplicationException;

    /**
     * Obtiene el número total de eventos registrados en el sistema.
     * 
     * @return El número total de eventos como String
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public String countREST() throws WebApplicationException;

    /**
     * Actualiza la información de un evento existente usando formato XML.
     * 
     * @param requestEntity El objeto que contiene los datos actualizados
     * @param id El identificador del evento a actualizar
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Actualiza la información de un evento existente usando formato JSON.
     * 
     * @param requestEntity El objeto que contiene los datos actualizados
     * @param id El identificador del evento a actualizar
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Busca un evento específico por su identificador usando formato XML.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param id El identificador del evento a buscar
     * @return El evento encontrado en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     * @throws ReadException Si hay un error al leer los datos
     */
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException, ReadException;

    /**
     * Busca un evento específico por su identificador usando formato JSON.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param id El identificador del evento a buscar
     * @return El evento encontrado en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException;

    /**
     * Obtiene un rango de eventos según los parámetros especificados usando formato XML.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param from El índice inicial del rango
     * @param to El índice final del rango
     * @return La lista de eventos en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    /**
     * Obtiene un rango de eventos según los parámetros especificados usando formato JSON.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param from El índice inicial del rango
     * @param to El índice final del rango
     * @return La lista de eventos en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException;

    /**
     * Busca eventos por rango de fechas usando formato XML.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param de_fecha La fecha inicial del rango
     * @param hasta_fecha La fecha final del rango
     * @return Los eventos encontrados en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findByDates_XML(Class<T> responseType, String de_fecha, String hasta_fecha) throws WebApplicationException;

    /**
     * Busca eventos por rango de fechas usando formato JSON.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param de_fecha La fecha inicial del rango
     * @param hasta_fecha La fecha final del rango
     * @return Los eventos encontrados en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findByDates_JSON(Class<T> responseType, String de_fecha, String hasta_fecha) throws WebApplicationException;

    /**
     * Crea un nuevo evento en el sistema usando formato XML.
     * 
     * @param requestEntity El objeto que contiene los datos del nuevo evento
     * @throws InternalServerErrorException Si ocurre un error interno en el servidor
     */
    public void create_XML(Object requestEntity) throws InternalServerErrorException;

    /**
     * Crea un nuevo evento en el sistema usando formato JSON.
     * 
     * @param requestEntity El objeto que contiene los datos del nuevo evento
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void create_JSON(Object requestEntity) throws WebApplicationException;

    /**
     * Obtiene una lista de todos los eventos registrados en el sistema usando formato XML.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @return La lista de eventos en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException;

    /**
     * Obtiene una lista de todos los eventos registrados en el sistema usando formato JSON.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @return La lista de eventos en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException;

    /**
     * Elimina un evento del sistema.
     * 
     * @param id El identificador del evento a eliminar
     * @throws InternalServerErrorException Si ocurre un error interno en el servidor
     */
    public void remove(String id) throws InternalServerErrorException;

    /**
     * Cierra los recursos utilizados por el gestor de eventos.
     * Este método debe ser llamado cuando ya no se necesite el gestor.
     */
    public void close();
}