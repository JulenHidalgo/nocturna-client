/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * Clase que representa un evento en el sistema, como una fiesta o concierto.
 * Esta clase implementa Serializable para permitir su persistencia y transferencia,
 * y está marcada como @XmlRootElement para facilitar la serialización XML.
 *
 * @author Erlantz Rey
 */
@XmlRootElement
public class Event implements Serializable {
    
    /**
    * Identificador único para la versión de la clase, utilizado durante el proceso de
    * serialización y deserialización de objetos.
    * Este valor asegura la compatibilidad entre versiones de la clase.
    */
    private static final long serialVersionUID = 1L;
    
    /**
     * Identificador único del evento en el sistema.
     */
    private Long idEvent;
    
    /**
     * Nombre del evento (fiesta, concierto, etc.).
     */
    private String nombre = "";
    
    /**
     * Fecha y hora en que se realizará el evento.
     */
    private Date fecha = valueOf(LocalDate.now());
    
    /**
     * Número total de entradas disponibles para el evento.
     */
    private Integer NumEntradas = 0;
    
    /**
     * Mínimo de consumo obligatorio en el evento.
     */
    private Integer consumicion = 0;
    
    /**
     * Precio por entrada al evento en euros.
     */
    private Double precioEntrada = 0.0;
    
    /**
     * Club donde se realizará el evento.
     */
    private Club club;
    
    /**
     * Conjunto de artistas que actuarán en el evento.
     */
    private Set<Artist> artists;
    
    /**
     * Conjunto de tickets vendidos para el evento.
     */
    private Set<Ticket> tickets;

    /**
     * Constructor vacío para inicialización básica del evento.
     */
    public Event() {
    }

    /**
     * Obtiene el identificador único del evento.
     *
     * @return El ID del evento
     */
    public Long getId() {
        return idEvent;
    }

    /**
     * Establece el identificador único del evento.
     *
     * @param idEvent El ID del evento
     */
    public void setId(Long idEvent) {
        this.idEvent = idEvent;
    }

    /**
     * Obtiene el nombre del evento.
     *
     * @return El nombre del evento
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del evento.
     *
     * @param nombre El nombre del evento
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la fecha programada del evento.
     *
     * @return La fecha del evento
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha del evento.
     *
     * @param fecha La nueva fecha del evento
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el número total de entradas disponibles.
     *
     * @return El número de entradas disponibles
     */
    public int getNumEntradas() {
        return NumEntradas;
    }

    /**
     * Establece el número total de entradas disponibles.
     *
     * @param NumEntradas El número de entradas
     */
    public void setNumEntradas(int NumEntradas) {
        this.NumEntradas = NumEntradas;
    }

    /**
     * Obtiene el mínimo de consumo obligatorio en euros.
     *
     * @return El mínimo de consumo
     */
    public int getConsumicion() {
        return consumicion;
    }

    /**
     * Establece el mínimo de consumo obligatorio.
     *
     * @param consumicion El mínimo de consumo en euros
     */
    public void setConsumicion(int consumicion) {
        this.consumicion = consumicion;
    }

    /**
     * Obtiene el precio de cada entrada al evento.
     *
     * @return El precio por entrada en euros
     */
    public Double getPrecioEntrada() {
        return precioEntrada;
    }

    /**
     * Establece el precio de cada entrada.
     *
     * @param precioEntrada El precio por entrada en euros
     */
    public void setPrecioEntrada(Double precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    /**
     * Obtiene el club donde se realizará el evento.
     *
     * @return El club organizador
     */
    public Club getClub() {
        return this.club;
    }

    /**
     * Establece el club organizador del evento.
     *
     * @param club El club organizador
     */
    public void setClub(Club club) {
        this.club = club;
    }

    /**
     * Obtiene el conjunto de artistas que actuarán en el evento.
     *
     * @return Los artistas participantes
     */
    public Set<Artist> getArtists() {
        return this.artists;
    }

    /**
     * Establece los artistas que actuarán en el evento.
     *
     * @param artists Los artistas participantes
     */
    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    /**
     * Obtiene el conjunto de tickets vendidos para el evento.
     * Este método está marcado como @XmlTransient para excluirlo de la serialización XML.
     *
     * @return Los tickets vendidos
     */
    @XmlTransient
    public Set<Ticket> getTickets() {
        return this.tickets;
    }

    /**
     * Establece los tickets vendidos para el evento.
     *
     * @param tickets Los tickets vendidos
     */
    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Calcula el código hash del evento basado en su identificador único.
     *
     * @return El código hash del evento
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEvent != null ? idEvent.hashCode() : 0);
        return hash;
    }

    /**
     * Compara este evento con otro objeto para determinar si son iguales.
     * La comparación se realiza basándose en el identificador único del evento.
     *
     * @param object El objeto a comparar con este evento
     * @return true si los eventos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.idEvent == null && other.idEvent != null) || (this.idEvent != null && !this.idEvent.equals(other.idEvent))) {
            return false;
        }
        return true;
    }

    /**
     * Genera una representación en cadena del evento mostrando su identificador único.
     *
     * @return Una cadena que representa el evento en formato [id="valor"]
     */
    @Override
    public String toString() {
        return "enties.EventEntity[ id=" + idEvent + " ]";
    }
}