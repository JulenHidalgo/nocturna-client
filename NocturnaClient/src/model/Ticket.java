/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase que representa un ticket de venta para eventos.
 * Esta clase implementa Serializable para permitir su persistencia y transferencia,
 * y está marcada como @XmlRootElement para facilitar la serialización XML.
 *
 * @author Adrian Rocha
 */
@XmlRootElement
public class Ticket implements Serializable {

    /**
    * Identificador único para la versión de la clase, utilizado durante el proceso de
    * serialización y deserialización de objetos.
    * Este valor asegura la compatibilidad entre versiones de la clase.
    */
    private static final long serialVersionUID = 1L;

    
    /**
     * Identificador único del ticket en el sistema.
     */
    private Long id;

    /**
     * DNI del comprador que realizó la transacción.
     */
    @NotNull
    private String dniComprador;

    /**
     * Lista de DNIs de los asistentes al evento.
     */
    private String dniAsistentes;

    /**
     * Monto total de la compra en euros.
     */
    @NotNull
    private Double importeCompra;

    /**
     * Fecha y hora exacta cuando se realizó la compra.
     */
    private Date fechaCompra;

    /**
     * Número total de entradas compradas.
     */
    @NotNull
    private Integer cantidad;

    /**
     * Método de pago utilizado para realizar la transacción.
     */
    private FormaPago formapago;

    /**
     * Evento al que corresponde el ticket.
     */
    private Event event;

    /**
     * Usuario que realizó la compra del ticket.
     */
    private User user;

    /**
     * Constructor por defecto de la clase Ticket.
     */
    public Ticket() {
    }

    /**
     * Establece el evento asociado al ticket.
     *
     * @param event El evento al que corresponde este ticket
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Establece el usuario que realizó la compra del ticket.
     *
     * @param user El usuario comprador
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Obtiene el evento asociado al ticket.
     *
     * @return El evento al que corresponde este ticket
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Obtiene el usuario que realizó la compra del ticket.
     *
     * @return El usuario comprador
     */
    public User getUser() {
        return user;
    }

    /**
     * Obtiene el DNI del comprador del ticket.
     *
     * @return El DNI del comprador
     */
    public String getDniComprador() {
        return dniComprador;
    }

    /**
     * Obtiene los DNIs de los asistentes al evento.
     *
     * @return Los DNIs de los asistentes
     */
    public String getDniAsistentes() {
        return dniAsistentes;
    }

    /**
     * Obtiene el monto total de la compra.
     *
     * @return El importe total en euros
     */
    public Double getImporteCompra() {
        return importeCompra;
    }

    /**
     * Obtiene la fecha y hora de la compra del ticket.
     *
     * @return La fecha y hora de compra
     */
    public Date getFechaCompra() {
        return fechaCompra;
    }

    /**
     * Obtiene la cantidad de entradas compradas.
     *
     * @return El número total de entradas
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * Obtiene el método de pago utilizado en la transacción.
     *
     * @return La forma de pago utilizada
     */
    public FormaPago getFormapago() {
        return formapago;
    }

    /**
     * Establece el DNI del comprador del ticket.
     *
     * @param dniComprador El DNI del comprador
     */
    public void setDniComprador(String dniComprador) {
        this.dniComprador = dniComprador;
    }

    /**
     * Establece los DNIs de los asistentes al evento.
     *
     * @param dniAsistentes Los DNIs de los asistentes
     */
    public void setDniAsistentes(String dniAsistentes) {
        this.dniAsistentes = dniAsistentes;
    }

    /**
     * Establece el monto total de la compra.
     *
     * @param importeCompra El monto total en euros
     */
    public void setImporteCompra(Double importeCompra) {
        this.importeCompra = importeCompra;
    }

    /**
     * Establece 
     * @param fechaCompra Fecha de compra de la entrada
     */
    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    /**
     * Establece la cantidad de entradas que compra el usuario
     * @param cantidad Cantidad de entradas
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Establece la forma de pago del usuario.
     * 
     * @param formapago La forma de pago.
     */
    public void setFormapago(FormaPago formapago) {
        this.formapago = formapago;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ticket)) {
            return false;
        }
        Ticket other = (Ticket) object;
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
        return "entities.Ticket[ id=" + id + " ]";
    }

}
