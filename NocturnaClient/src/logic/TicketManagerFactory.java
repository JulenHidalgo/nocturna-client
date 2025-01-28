/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import rest.TicketRESTClient;



/**
 *
 * @author 2dam
 */
public class TicketManagerFactory {
      private static TicketManager ticketManager;
    
    public static TicketManager get(){
        if(ticketManager == null){
            ticketManager = new TicketRESTClient();
        }
        return (TicketManager) new TicketRESTClient();
    }
}
