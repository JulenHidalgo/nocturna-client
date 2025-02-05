/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.stage.Stage;


/**
 * Clase que gestiona el estado de la sesión actual de la aplicación.
 * Esta clase proporciona un punto único de acceso para el manejo del escenario
 * principal y el usuario actual, siguiendo el patrón Singleton.
 * 
 * @author Erlantz Rey
 */
public class Sesion {
    
    /**
     * Referencia al escenario principal de la aplicación JavaFX.
     * Este atributo mantiene una referencia estática al Stage principal
     * donde se renderiza la interfaz gráfica.
     */
    private static Stage stage;
    
    /**
     * Usuario actualmente conectado a la aplicación.
     * Este atributo mantiene el estado del usuario que ha iniciado sesión,
     * permitiendo acceder a sus datos en cualquier parte de la aplicación.
     */
    private static User user;
    
    /**
     * Indicador del tema visual actual de la aplicación.
     * true: tema claro
     * false: tema oscuro
     */
    private static boolean tema;

    /**
     * Establece el escenario principal de la aplicación.
     * 
     * @param stage El escenario principal donde se mostrará la interfaz
     */
    public static void setStage(Stage stage) {
        Sesion.stage = stage;
    }

    /**
     * Obtiene el escenario principal de la aplicación.
     * 
     * @return El escenario principal actual
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Establece el usuario actual de la sesión.
     * 
     * @param user El usuario que ha iniciado sesión
     */
    public static void setUser(User user) {
        Sesion.user = user;
    }

    /**
     * Obtiene el usuario actual de la sesión.
     * 
     * @return El usuario actualmente conectado
     */
    public static User getUser() {
        return user;
    }

    /**
     * Establece el tema visual actual de la aplicación.
     * 
     * @param tema true para tema claro, false para tema oscuro
     */
    public static void setTema(boolean tema) {
        Sesion.tema = tema;
    }

    /**
     * Obtiene el tema visual actual de la aplicación.
     * 
     * @return true si está en tema claro, false si está en tema oscuro
     */
    public static boolean getTema() {
        return tema;
    }
}