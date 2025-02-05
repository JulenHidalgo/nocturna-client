/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Clase que representa un club en el sistema, como un local nocturno o sala de conciertos.
 * Esta clase implementa Serializable para permitir su persistencia y transferencia,
 * y está marcada como @XmlRootElement para facilitar la serialización XML.
 *
 * @author Adrian Rocha
 */
@XmlRootElement
public class Club implements Serializable {
    
    /**
    * Identificador único para la versión de la clase, utilizado durante el proceso de
    * serialización y deserialización de objetos.
    * Este valor asegura la compatibilidad entre versiones de la clase.
    */
    private static final long serialVersionUID = 1L;
    
    /**
     * Identificador único del club en el sistema.
     */
    private Long id;
    
    /**
     * Nombre del club (obligatorio).
     */
    @NotNull
    private String nombre;
    
    /**
     * Dirección física donde se encuentra el club (obligatoria).
     */
    @NotNull
    private String ubicacion;
    
    /**
     * Ciudad donde está ubicado el club (obligatoria).
     */
    @NotNull
    private String ciudad;
    
    /**
     * Cuenta de Instagram del club (opcional).
     */
    private String instagram;
    
    /**
     * Conjunto de eventos programados en el club.
     */
    private Set<Event> events;

    /**
     * Constructor vacío que inicializa los campos con valores por defecto.
     */
    public Club() {
        this.nombre = "";
        this.ubicacion = "";
        this.ciudad = "";
        this.instagram = "";
    }

    /**
     * Obtiene el identificador único del club.
     *
     * @return El ID del club
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del club.
     *
     * @param id El ID del club
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del club.
     *
     * @return El nombre del club
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del club.
     *
     * @param nombre El nombre del club
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la ubicación física del club.
     *
     * @return La dirección del club
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Establece la ubicación física del club.
     *
     * @param ubicacion La dirección del club
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Obtiene la ciudad donde se encuentra el club.
     *
     * @return La ciudad del club
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad donde se encuentra el club.
     *
     * @param ciudad La ciudad del club
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Obtiene la cuenta de Instagram del club.
     *
     * @return La cuenta de Instagram
     */
    public String getInstagram() {
        return instagram;
    }

    /**
     * Establece la cuenta de Instagram del club.
     *
     * @param instagram La cuenta de Instagram
     */
    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    /**
     * Obtiene el conjunto de eventos programados en el club.
     * Este método está marcado como @XmlTransient para excluirlo de la serialización XML.
     *
     * @return Los eventos programados
     */
    @XmlTransient
    public Set<Event> getEvents() {
        return events;
    }

    /**
     * Establece el conjunto de eventos programados en el club.
     *
     * @param events Los eventos programados
     */
    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    /**
     * Calcula el código hash del club basado en su identificador único.
     *
     * @return El código hash del club
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compara este club con otro objeto para determinar si son iguales.
     * La comparación se realiza basándose en el identificador único del club.
     *
     * @param object El objeto a comparar con este club
     * @return true si los clubs son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Club)) {
            return false;
        }
        Club other = (Club) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Genera una representación en cadena del club mostrando su nombre.
     *
     * @return El nombre del club
     */
    @Override
    public String toString() {
        return this.nombre;
    }
}