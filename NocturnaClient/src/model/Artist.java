/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase que representa un artista en el sistema, como un DJ o grupo musical.
 * Esta clase implementa Serializable para permitir su persistencia y transferencia,
 * y está marcada como @XmlRootElement para facilitar la serialización XML.
 *
 * @author Julen Hidalgo
 */
@XmlRootElement
public class Artist implements Serializable {
    
    /**
     * Identificador único para la versión de la clase, utilizado durante el proceso de
     * serialización y deserialización de objetos.
     * Este valor asegura la compatibilidad entre versiones de la clase.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del artista en el sistema.
     */
    private Long idArtist;
    
    /**
     * Nombre del artista (obligatorio).
     */
    @NotNull
    protected String nombre = "";
    
    /**
     * Género o tipo de música que interpreta el artista (obligatorio).
     */
    @NotNull
    protected String tipoMusica = "";
    
    /**
     * Descripción del artista y su estilo musical (opcional).
     */
    protected String descripcion = "";

    /**
     * Conjunto de eventos en los que participará el artista.
     */
    protected Set<Event> events;

    /**
     * Constructor vacío que inicializa los campos con valores por defecto.
     */
    public Artist() {
    }

    /**
     * Obtiene el identificador único del artista.
     *
     * @return El ID del artista
     */
    public Long getId() {
        return idArtist;
    }

    /**
     * Establece el identificador único del artista.
     *
     * @param idArtista El ID del artista
     */
    public void setId(Long idArtista) {
        this.idArtist = idArtista;
    }

    /**
     * Obtiene el nombre del artista.
     *
     * @return El nombre del artista
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del artista.
     *
     * @param nombre El nombre del artista
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el tipo de música que interpreta el artista.
     *
     * @return El género musical del artista
     */
    public String getTipoMusica() {
        return tipoMusica;
    }

    /**
     * Establece el tipo de música que interpreta el artista.
     *
     * @param tipoMusica El género musical
     */
    public void setTipoMusica(String tipoMusica) {
        this.tipoMusica = tipoMusica;
    }

    /**
     * Obtiene la descripción del artista.
     *
     * @return La descripción del artista
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del artista.
     *
     * @param descripcion La descripción del artista
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el conjunto de eventos en los que participará el artista.
     *
     * @return Los eventos programados
     */
    public Set<Event> getEvents() {
        return events;
    }

    /**
     * Establece el conjunto de eventos en los que participará el artista.
     *
     * @param events Los eventos programados
     */
    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    /**
     * Calcula el código hash del artista basado en su identificador único.
     *
     * @return El código hash del artista
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArtist != null ? idArtist.hashCode() : 0);
        return hash;
    }

    /**
     * Compara este artista con otro objeto para determinar si son iguales.
     * La comparación se realiza basándose en todos los campos del artista.
     *
     * @param object El objeto a comparar con este artista
     * @return true si los artistas son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Artist other = (Artist) object;

        return Objects.equals(idArtist, other.idArtist)
                && Objects.equals(nombre, other.nombre)
                && Objects.equals(tipoMusica, other.tipoMusica)
                && Objects.equals(descripcion, other.descripcion)
                && Objects.equals(events, other.events);
    }

    /**
     * Genera una representación en cadena del artista mostrando su identificador único.
     *
     * @return Una cadena que representa el artista en formato [id="valor"]
     */
    @Override
    public String toString() {
        return "entities.ArtistEntity[ id=" + idArtist + " ]";
    }
}