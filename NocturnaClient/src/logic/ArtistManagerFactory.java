package logic;

import rest.ArtistRESTClient;

/**
 * Clase que provee una fábrica para crear una instancia de ArtistManager.
 * Implementa el patrón Singleton para asegurar que solo haya una instancia de ArtistManager.
 * 
 * @author Julen Hidalgo
 */
public class ArtistManagerFactory {

    private static ArtistManager artistManager;

    /**
     * Obtiene la instancia de ArtistManager.
     * Si no existe una instancia, se crea una nueva utilizando ArtistRESTClient.
     * 
     * @return Una instancia de ArtistManager.
     */
    public static ArtistManager get() {
        if (artistManager == null) {
            artistManager = new ArtistRESTClient();
        }
        return new ArtistRESTClient();
    }
}
