/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import rest.TicketRESTClient;

/**
 * Fábrica que implementa el patrón Singleton para la gestión de tickets.
 * Esta clase proporciona un punto único de acceso para obtener una instancia
 * de TicketManager, garantizando que solo exista una instancia en toda la aplicación.
 * 
 * @author Adrian Rocha
 */
public class TicketManagerFactory {
    
    /**
     * Instancia única de TicketManager que se utiliza en toda la aplicación.
     * Esta variable estática garantiza que solo exista una instancia.
     */
    private static TicketManager ticketManager;
    
    /**
     * Obtiene la instancia de TicketManager. Si no existe una instancia previa,
     * crea una nueva usando TicketRESTClient como implementación.
     * 
     * @return La instancia única de TicketManager
     */
    public static TicketManager get() {
        if (ticketManager == null) {
            ticketManager = new TicketRESTClient();
        }
        return (TicketManager) new TicketRESTClient();
    }
}