/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import javafx.stage.Stage;
import model.Event;
import model.User;

/**
 *
 * @author 2dam
 */
public class Sesion {
    
    private static  Stage stage;

    private static  User user;

    private static  boolean tema;
    
    
    public static void setStage(Stage stage) {
        Sesion.stage = stage;
    }

    public static  Stage getStage() {
        return stage;
    }
    
    public static void setUser(User user) {
        Sesion.user = user;
    }
    
    public static User getUser() {
        return user;
    }
    
    public static void setTema(boolean tema) {
        Sesion.tema = tema;
    }
    
    public static boolean getTema() {
        return tema;
    }
     
}
