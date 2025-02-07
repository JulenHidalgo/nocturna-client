/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase que representa un cliente en el sistema, extendiendo la funcionalidad de la clase User.
 * Esta clase implementa Serializable para permitir su persistencia y transferencia,
 * y está marcada como @XmlRootElement para facilitar la serialización XML.
 *
 * @author Julen Hidalgo
 */
@XmlRootElement
public class Client extends User implements Serializable {
    
    /**
     * Nombre del cliente (obligatorio).
     */
    @NotNull
    private String nombre = "";
    
    /**
     * Apellido del cliente (obligatorio).
     */
    @NotNull
    private String apellido = "";
    
    /**
     * Ciudad de residencia del cliente (obligatorio).
     */
    @NotNull
    private String ciudad = "";
    
    /**
     * Número de teléfono del cliente (obligatorio).
     */
    @NotNull
    private Integer telefono = 0;
    
    /**
     * Fecha de nacimiento del cliente, debe ser una fecha anterior a la actual (@Past).
     */
    @Past
    private Date fechaNacimiento;
    
    /**
     * DNI del cliente (obligatorio).
     */
    @NotNull
    private String dni;
            
    /**
     * Constructor vacío que inicializa los campos con valores por defecto.
     */
    public Client() {
        super();
        super.setIsAdmin(false);
    }

    /**
     * Constructor completo que inicializa todos los campos del cliente.
     * 
     * @param nombre Nombre del cliente
     * @param apellido Apellido del cliente
     * @param ciudad Ciudad de residencia
     * @param telefono Número de teléfono
     * @param fechaNacimiento Fecha de nacimiento (debe ser anterior a la actual)
     * @param dni DNI del cliente
     * @param mail Email del cliente
     * @param passwd Contraseña del cliente
     */
    public Client(String nombre, String apellido, String ciudad, Integer telefono, 
                 Date fechaNacimiento, String dni, String mail, String passwd) {
        super(mail, passwd, false);
        this.nombre = nombre;
        this.apellido = apellido;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
    }
    
    /**
     * Obtiene el nombre del cliente.
     * 
     * @return El nombre del cliente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente.
     * 
     * @param nombre El nombre del cliente
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del cliente.
     * 
     * @return El apellido del cliente
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del cliente.
     * 
     * @param apellido El apellido del cliente
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene la ciudad de residencia del cliente.
     * 
     * @return La ciudad de residencia
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad de residencia del cliente.
     * 
     * @param ciudad La ciudad de residencia
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     * 
     * @return El número de teléfono
     */
    public Integer getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del cliente.
     * 
     * @param telefono El número de teléfono
     */
    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la fecha de nacimiento del cliente.
     * 
     * @return La fecha de nacimiento
     */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Establece la fecha de nacimiento del cliente.
     * 
     * @param fechaNacimiento La fecha de nacimiento (debe ser anterior a la actual)
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Obtiene el DNI del cliente.
     * 
     * @return El DNI del cliente
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del cliente.
     * 
     * @param dni El DNI del cliente
     */
    public void setDni(String dni) {
        this.dni = dni;
    }
}