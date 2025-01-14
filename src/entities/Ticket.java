/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import data.FormaPago;
import java.io.Serializable;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 2dam
 */
@NamedQueries({
    /**sacar las entradas del usuaario que ha iniciado sesion*/
    @NamedQuery(name = "findTicketByUser", 
            query = "SELECT t FROM  Ticket t WHERE t.dniComprador = :dni"),
    
})

@Entity
@Table(name = "ticket", schema = "nocturna")
@XmlRootElement
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String dniComprador;

    @NotNull
    private Long idEvento;

    @ElementCollection
    @CollectionTable(
            name = "ticket_dni_asistentes", // Nombre de la tabla
            schema = "nocturna",
            joinColumns = @JoinColumn(name = "ticket_id") // Llave foránea hacia la tabla principal
    )
    @Column(name = "dni_asistente")
    private Set<String> dniAsistentes;

    @NotNull
    private Double importeCompra;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCompra;

    @NotNull
    private Integer cantidad;

    @Enumerated(EnumType.ORDINAL)
    private FormaPago formapago;

    @ManyToOne
    private Event event;

    @ManyToOne
    private User user;

    public Ticket(String dniComprador, Long idEvento, Set<String> dniAsistentes, Double importeCompra, Integer cantidad, FormaPago formapago) {
        this.dniComprador = dniComprador;
        this.idEvento = idEvento;
        this.dniAsistentes = dniAsistentes;
        this.importeCompra = importeCompra;
        this.fechaCompra = valueOf(LocalDate.now());
        this.cantidad = cantidad;
        this.formapago = formapago;
    }

    public Ticket() {
    }
    
    

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public User getUser() {
        return user;
    }

    public String getDniComprador() {
        return dniComprador;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public Set getDniAsistentes() {
        return dniAsistentes;
    }

    public Double getImporteCompra() {
        return importeCompra;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public FormaPago getFormapago() {
        return formapago;
    }

    public void setDniComprador(String dniComprador) {
        this.dniComprador = dniComprador;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public void setDniAsistentes(Set dniAsistentes) {
        this.dniAsistentes = dniAsistentes;
    }

    public void setImporteCompra(Double importeCompra) {
        this.importeCompra = importeCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setFormapago(FormaPago formapago) {
        this.formapago = formapago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

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

    @Override
    public String toString() {
        return "entities.Ticket[ id=" + id + " ]";
    }

}
