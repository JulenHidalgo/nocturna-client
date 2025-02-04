/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;

/**
 * Interfaz que define los métodos para la gestión de tickets en la aplicación.
 * Esta interfaz proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la gestión de tickets, con soporte tanto para formato XML como JSON.
 * Todas las operaciones están diseñadas para trabajar con la API REST y manejan
 * excepciones específicas de la aplicación web.
 * 
 * @author Adrian Rocha
 */
public interface TicketManager {
    
    /**
     * Obtiene el número total de tickets registrados en el sistema.
     * 
     * @return El número total de tickets como String
     * @throws ClientErrorException Si ocurre un error en el cliente
     */
    public String countREST() throws ClientErrorException;

    /**
     * Actualiza la información de un ticket existente usando formato XML.
     * 
     * @param requestEntity El objeto que contiene los datos actualizados
     * @param id El identificador del ticket a actualizar
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Actualiza la información de un ticket existente usando formato JSON.
     * 
     * @param requestEntity El objeto que contiene los datos actualizados
     * @param id El identificador del ticket a actualizar
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void edit_JSON(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Busca un ticket específico por su identificador usando formato XML.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param id El identificador del ticket a buscar
     * @return El ticket encontrado en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException;

    /**
     * Busca un ticket específico por su identificador usando formato JSON.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param id El identificador del ticket a buscar
     * @return El ticket encontrado en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T find_JSON(Class<T> responseType, String id) throws WebApplicationException;

    /**
     * Obtiene un rango de tickets según los parámetros especificados usando formato XML.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param from El índice inicial del rango
     * @param to El índice final del rango
     * @return La lista de tickets en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    /**
     * Obtiene un rango de tickets según los parámetros especificados usando formato JSON.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param from El índice inicial del rango
     * @param to El índice final del rango
     * @return La lista de tickets en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws WebApplicationException;

    /**
     * Crea un nuevo ticket en el sistema usando formato XML.
     * 
     * @param requestEntity El objeto que contiene los datos del nuevo ticket
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void create_XML(Object requestEntity) throws WebApplicationException;

    /**
     * Crea un nuevo ticket en el sistema usando formato JSON.
     * 
     * @param requestEntity El objeto que contiene los datos del nuevo ticket
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void create_JSON(Object requestEntity) throws WebApplicationException;

    /**
     * Busca tickets asociados a un usuario específico usando formato XML.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param dni El DNI del usuario cuyos tickets se buscan
     * @return Los tickets del usuario en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findTicketByUser_XML(Class<T> responseType, String dni) throws WebApplicationException;

    /**
     * Busca tickets asociados a un usuario específico usando formato JSON.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param dni El DNI del usuario cuyos tickets se buscan
     * @return Los tickets del usuario en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findTicketByUser_JSON(Class<T> responseType, String dni) throws WebApplicationException;

    /**
     * Obtiene una lista de todos los tickets registrados en el sistema usando formato XML.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @return La lista de tickets en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException;

    /**
     * Obtiene una lista de todos los tickets registrados en el sistema usando formato JSON.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @return La lista de tickets en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findAll_JSON(Class<T> responseType) throws WebApplicationException;

    /**
     * Elimina un ticket del sistema.
     * 
     * @param id El identificador del ticket a eliminar
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void remove(String id) throws WebApplicationException;

    /**
     * Cierra los recursos utilizados por el gestor de tickets.
     * Este método debe ser llamado cuando ya no se necesite el gestor.
     */
    public void close();
}
