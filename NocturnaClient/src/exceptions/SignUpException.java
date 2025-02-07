/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * Clase que gestiona excepcion al registrar si el email ya esta
 * @author Adrian Rocha
 */
public class SignUpException extends Exception {

    /**
     * Constructor de la excepcion con un mensaje
     */
    public SignUpException() {
        super("El correo con el que intentas registrarte ya existe.");
    }
}
