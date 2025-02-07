/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import rest.UserRESTClient;

/**
 * Fábrica que implementa el patrón Singleton para la gestión de usuarios.
 * Esta clase proporciona un punto único de acceso para obtener una instancia
 * de UserManager, garantizando que solo exista una instancia en toda la aplicación.
 * 
 * @author Julen Hidalgo
 */
public class UserManagerFactory {
    
    /**
     * Instancia única de UserManager que se utiliza en toda la aplicación.
     * Esta variable estática garantiza que solo exista una instancia.
     */
    private static UserManager userManager;
    
    /**
     * Obtiene la instancia de UserManager. Si no existe una instancia previa,
     * crea una nueva usando UserRESTClient como implementación.
     * 
     * @return La instancia única de UserManager
     */
    public static UserManager get() {
        if (userManager == null) {
            userManager = new UserRESTClient();
        }
        return userManager;
    }
}
