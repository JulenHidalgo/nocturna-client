/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import rest.ClientRESTClient;

/**
 * Fábrica que implementa el patrón Singleton para la gestión de clientes.
 * Esta clase proporciona un punto único de acceso para obtener una instancia
 * de ClientManager, garantizando que solo exista una instancia en toda la aplicación.
 * 
 * @author Erlantz rey
 */
public class ClientManagerFactory {
    
    /**
     * Instancia única de ClientManager que se utiliza en toda la aplicación.
     * Esta variable estática garantiza que solo exista una instancia.
     */
    private static ClientManager clientManager;
    
    /**
     * Obtiene la instancia de ClientManager. Si no existe una instancia previa,
     * crea una nueva usando ClientRESTClient como implementación.
     * 
     * @return La instancia única de ClientManager
     */
    public static ClientManager get() {
        if (clientManager == null) {
            clientManager = new ClientRESTClient();
        }
        return new ClientRESTClient();
    }
}