/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import rest.ClubRESTClient;

/**
 * Factoria de objetos de la interfaz de Club Manager
 * 
 * @author Adrian Rocha
 */
public class ClubManagerFactory {
    
    private static ClubManager clubManager;
    
    public static ClubManager get(){
        if(clubManager == null){
            clubManager = new ClubRESTClient();
        }
        return clubManager;
    }
    
}
