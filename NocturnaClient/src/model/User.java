package model;

import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Representa a un usuario en el sistema. Un usuario tiene un correo electrónico, una contraseña,
 * un estado de administrador, y una colección de tickets asociados.
 * 
 * @author Julen Hidalgo
 */
@XmlRootElement
public class User implements Serializable {
    
    /**
    * Identificador único para la versión de la clase, utilizado durante el proceso de
    * serialización y deserialización de objetos.
    * Este valor asegura la compatibilidad entre versiones de la clase.
    */
    private static final long serialVersionUID = 1L;


    /**
     * Identificador único del usuario.
     */
    private Long id;
    
    /**
     * Correo electrónico del usuario.
     * Este valor no puede ser nulo.
     */
    @NotNull
    private String mail = "";
    
    /**
     * Contraseña del usuario.
     * Este valor no puede ser nulo.
     */
    @NotNull
    private String passwd = "";
    
    /**
     * Estado que indica si el usuario es administrador.
     * Este valor no puede ser nulo.
     */
    @NotNull
    private Boolean isAdmin = false;
    
    /**
     * Conjunto de tickets asociados al usuario.
     * Este conjunto puede ser nulo.
     */
    private Set<Ticket> tickets;

    /**
     * Constructor vacío para crear una instancia de User.
     */
    public User() {
    }

    /**
     * Constructor con parámetros para crear una instancia de User.
     * 
     * @param mail El correo electrónico del usuario.
     * @param passwd La contraseña del usuario.
     * @param isAdmin El estado que indica si el usuario es administrador.
     */
    public User(String mail, String passwd, Boolean isAdmin) {
        this.mail = mail;
        this.passwd = passwd;
        this.isAdmin = isAdmin;
    }

    /**
     * Obtiene el identificador único del usuario.
     * 
     * @return El ID del usuario.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     * 
     * @param id El ID del usuario.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * 
     * @return El correo electrónico del usuario.
     */
    public String getMail() {
        return mail;
    }

    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param mail El correo electrónico del usuario.
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return La contraseña del usuario.
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * @param passwd La contraseña del usuario.
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    
    /**
     * Obtiene los tickets asociados al usuario.
     * 
     * @return Un conjunto de tickets asociados al usuario.
     */
    @XmlTransient
    public Set<Ticket> getTickets() {
        return tickets;
    }

    /**
     * Establece los tickets asociados al usuario.
     * 
     * @param tickets El conjunto de tickets asociados al usuario.
     */
    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    /**
     * Obtiene el estado de administrador del usuario.
     * 
     * @return Un valor booleano que indica si el usuario es administrador.
     */
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * Establece el estado de administrador del usuario.
     * 
     * @param isAdmin Un valor booleano que indica si el usuario es administrador.
     */
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Calcula el código hash del usuario basado en su ID.
     * 
     * @return El código hash del usuario.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compara el usuario con otro objeto para verificar si son iguales.
     * 
     * @param object El objeto con el que se compara el usuario.
     * @return Verdadero si los usuarios son iguales, falso en caso contrario.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Representación en cadena del usuario, mostrando su ID.
     * 
     * @return Una cadena que representa al usuario.
     */
    @Override
    public String toString() {
        return "entities.User[ id=" + id + " ]";
    }
}
