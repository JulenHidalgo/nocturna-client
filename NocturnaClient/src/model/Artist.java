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
 *
 * @author 2dam
 */
@XmlRootElement
public class Artist implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idArtist;
    @NotNull
    protected String nombre = "";
    @NotNull
    protected String tipoMusica = "";
    protected String descripcion = "";

    protected Set<Event> events;

    public Artist() {

    }

    public Long getId() {
        return idArtist;
    }

    public void setId(Long idArtista) {
        this.idArtist = idArtista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoMusica() {
        return tipoMusica;
    }

    public void setTipoMusica(String tipoMusica) {
        this.tipoMusica = tipoMusica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public Set<Event> getEvents() {
        return events;
    }

    public void getEvents(Set<Event> events) {
        this.events = events;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArtist != null ? idArtist.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artist)) {
            return false;
        }
        Artist other = (Artist) object;
        if ((this.idArtist == null && other.idArtist != null) || (this.idArtist != null && !this.idArtist.equals(other.idArtist))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ArtistEntity[ id=" + idArtist + " ]";
    }

}
