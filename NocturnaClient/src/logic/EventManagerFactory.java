/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import rest.EventRESTClient;

/**
 * Fábrica que implementa el patrón Singleton para la gestión de eventos.
 * Esta clase proporciona un punto único de acceso para obtener una instancia
 * de EventManager, garantizando que solo exista una instancia en toda la aplicación.
 * 
 * @author Erlantz Rey
 */
public class EventManagerFactory {
    
    /**
     * Instancia única de EventManager que se utiliza en toda la aplicación.
     * Esta variable estática garantiza que solo exista una instancia.
     */
    private static EventManager eventManager;
    
    /**
     * Obtiene la instancia de EventManager. Si no existe una instancia previa,
     * crea una nueva usando EventRESTClient como implementación.
     * 
     * @return La instancia única de EventManager
     */
    public static EventManager get() {
        if (eventManager == null) {
            eventManager = new EventRESTClient();
        }
        return eventManager;
    }
}
