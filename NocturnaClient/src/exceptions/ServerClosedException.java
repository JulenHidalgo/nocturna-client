/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * Clase que gestiona excepcion del servidor
 * @author Adrian Rocha
 */
public class ServerClosedException extends Exception {

    /**
     * Constructor de la excepcion con un mensaje
     */
    public ServerClosedException() {
        super("El servidor no está disponible en este momento, intentalo más tarde");
    }
}
