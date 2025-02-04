package logic;

import rest.AdminRESTClient;

/**
 * Clase que provee una fábrica para crear una instancia de AdminManager.
 * Implementa el patrón Singleton para asegurar que solo haya una instancia de AdminManager.
 * 
 * @author Julen Hidalgo
 */
public class AdminManagerFactory {
    
    private static AdminManager adminManager;
    
    /**
     * Obtiene la instancia de AdminManager.
     * Si no existe una instancia, se crea una nueva utilizando AdminRESTClient.
     * 
     * @return Una instancia de AdminManager.
     */
    public static AdminManager get(){
        if(adminManager == null){
            adminManager = new AdminRESTClient();
        }
        return adminManager;
    }
}
