/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import exceptions.SignInException;
import javax.ws.rs.WebApplicationException;

/**
 * Interfaz que define los métodos para la gestión de usuarios en la aplicación.
 * Esta interfaz proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la gestión de usuarios, así como operaciones adicionales para el manejo
 * de contraseñas y autenticación. Todas las operaciones están diseñadas para
 * trabajar con formato XML y manejan excepciones específicas de la aplicación.
 * 
 * @author Julen Hidalgo
 */
public interface UserManager {

    /**
     * Realiza el restablecimiento de contraseña de un usuario mediante correo electrónico.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param userEmail El correo electrónico del usuario que solicita el restablecimiento
     * @return La respuesta del servidor en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T resetPassword_XML(Class<T> responseType, String userEmail) throws WebApplicationException;

    /**
     * Obtiene el número total de usuarios registrados en el sistema.
     * 
     * @return El número total de usuarios como String
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public String countREST() throws WebApplicationException;

    /**
     * Actualiza la información de un usuario existente.
     * 
     * @param requestEntity El objeto que contiene los datos actualizados
     * @param id El identificador del usuario a actualizar
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Busca un usuario específico por su identificador.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param id El identificador del usuario a buscar
     * @return El usuario encontrado en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException;

    /**
     * Obtiene un rango de usuarios según los parámetros especificados.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param from El índice inicial del rango
     * @param to El índice final del rango
     * @return La lista de usuarios en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    /**
     * Crea un nuevo usuario en el sistema.
     * 
     * @param requestEntity El objeto que contiene los datos del nuevo usuario
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void create_XML(Object requestEntity) throws WebApplicationException;

    /**
     * Realiza el proceso de inicio de sesión de un usuario.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @param mail El correo electrónico del usuario
     * @param passwd La contraseña del usuario
     * @return El usuario autenticado en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     * @throws SignInException Si las credenciales no son válidas
     */
    public <T> T login_XML(Class<T> responseType, String mail, String passwd) throws WebApplicationException, SignInException;

    /**
     * Actualiza la contraseña de un usuario existente.
     * 
     * @param requestEntity El objeto que contiene los datos actualizados
     * @param newPasswd La nueva contraseña del usuario
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void updatePasswd_XML(Object requestEntity, String newPasswd) throws WebApplicationException;

    /**
     * Obtiene una lista de todos los usuarios registrados en el sistema.
     * 
     * @param <T> El tipo de respuesta esperado
     * @param responseType El tipo de clase que se espera en la respuesta
     * @return La lista de usuarios en el formato especificado
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException;

    /**
     * Elimina un usuario del sistema.
     * 
     * @param id El identificador del usuario a eliminar
     * @throws WebApplicationException Si ocurre un error en la aplicación web
     */
    public void remove(String id) throws WebApplicationException;

    /**
     * Cierra los recursos utilizados por el gestor de usuarios.
     * Este método debe ser llamado cuando ya no se necesite el gestor.
     */
    public void close();
}