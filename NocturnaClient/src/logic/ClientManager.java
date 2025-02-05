package logic;

import exceptions.InternalServerErrorException;
import exceptions.ReadException;
import exceptions.SignUpException;
import javax.ws.rs.WebApplicationException;

/**
 * Interfaz que define el contrato para la gestión de clientes en el sistema.
 * Proporciona métodos para realizar operaciones CRUD y la interacción con los datos de los clientes,
 * como contar, encontrar, crear, editar y eliminar registros de clientes.
 * 
 * @author Erlantz Rey
 */
public interface ClientManager {

    /**
     * Obtiene el número total de clientes.
     * 
     * @return Una cadena que representa el número total de clientes.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public String countREST() throws WebApplicationException;

    /**
     * Edita un registro de cliente utilizando los datos proporcionados.
     * 
     * @param requestEntity Los datos del cliente a actualizar, típicamente en formato XML.
     * @param id El ID del cliente a actualizar.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public void edit_XML(Object requestEntity, String id) throws WebApplicationException;

    /**
     * Busca un cliente por su ID y devuelve los datos correspondientes del cliente.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param id El ID del cliente a buscar.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Los datos del cliente mapeados al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T find_XML(Class<T> responseType, String id) throws WebApplicationException;

    /**
     * Busca un rango de clientes basado en los valores proporcionados para el rango.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param from El índice inicial para el rango de clientes.
     * @param to El índice final para el rango de clientes.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Una colección de datos de clientes dentro del rango especificado, mapeados al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws WebApplicationException;

    /**
     * Crea un nuevo cliente con los datos proporcionados.
     * 
     * @param requestEntity Los datos del cliente a crear, típicamente en formato XML.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     * @throws SignUpException Si ocurre un problema en el proceso de registro del cliente.
     * @throws InternalServerErrorException Si ocurre un error interno del servidor.
     * @throws ReadException Si ocurre un problema al leer los datos del cliente.
     */
    public void create_XML(Object requestEntity) throws WebApplicationException, SignUpException, InternalServerErrorException, ReadException;

    /**
     * Obtiene todos los clientes.
     * 
     * @param responseType El tipo de clase al que se mapeará la respuesta.
     * @param <T> El tipo de la entidad de respuesta.
     * @return Una colección de todos los clientes, mapeada al tipo de respuesta.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public <T> T findAll_XML(Class<T> responseType) throws WebApplicationException;

    /**
     * Elimina un cliente por su ID.
     * 
     * @param id El ID del cliente a eliminar.
     * @throws WebApplicationException Si ocurre un error al procesar la solicitud.
     */
    public void remove(String id) throws WebApplicationException;

    /**
     * Cierra el servicio de gestión de clientes.
     * Se liberan los recursos utilizados por el servicio.
     */
    public void close();
}
