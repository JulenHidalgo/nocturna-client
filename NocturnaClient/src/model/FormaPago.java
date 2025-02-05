/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Enumeración que representa las diferentes formas de pago disponibles en el sistema.
 * Esta enumeración define los métodos de pago soportados para realizar transacciones.
 *
 * @author Erlantz Rey
 */
public enum FormaPago {
    /**
     * Pago mediante BIZUM, servicio de pago móvil entre particulares.
     */
    BIZUM,
    
    /**
     * Pago mediante tarjeta bancaria (crédito/débito).
     */
    TARJETA
}
