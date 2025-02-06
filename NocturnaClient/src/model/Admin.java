/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase que representa un administrador en el sistema, extendiendo la funcionalidad de la clase User.
 * Esta clase implementa Serializable para permitir su persistencia y transferencia,
 * y está marcada como @XmlRootElement para facilitar la serialización XML.
 *
 * @author Adrian Rocha
 */
@XmlRootElement
public class Admin extends User implements Serializable {

    /**
     * Departamento al que pertenece el administrador en la organización.
     */
    private String departamento;
    
    /**
     * Constructor vacío que inicializa el administrador con valores por defecto.
     */
    public Admin() {
        super();
    }

    /**
     * Constructor completo que inicializa todos los campos del administrador.
     * 
     * @param departamento El departamento al que pertenece el administrador
     * @param mail El email del administrador
     * @param passwd La contraseña del administrador
     */
    public Admin(String departamento, String mail, String passwd) {
        super(mail, passwd, true);
        this.departamento = departamento;
    }

    /**
     * Obtiene el departamento al que pertenece el administrador.
     * 
     * @return El departamento del administrador
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Establece el departamento al que pertenece el administrador.
     * 
     * @param departamento El departamento del administrador
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}