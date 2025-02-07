/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * Excepción personalizada que se lanza cuando ocurre un error durante la lectura
 * de información. Esta excepción proporciona una manera estandarizada de manejar
 * errores de lectura en la aplicación.
 * 
 * @author Julen Hidalgo
 */
public class ReadException extends Exception {

    /**
     * Constructor que crea una nueva instancia de ReadException con un mensaje
     * predeterminado indicando que ha ocurrido un error al recibir la información.
     */
    public ReadException() {
        super("Ha sucedido un error al recibir la información.");
    }
}
