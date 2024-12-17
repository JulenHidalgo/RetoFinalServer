/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import static java.sql.Date.valueOf;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author 2dam
 */
@Entity
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    private String dniComprador;
    
    @NotNull
    private Long idEvento;

    private Set<String> dniAsistentes;
    
    @NotNull
    private Double importeCompra;
    
    @NotNull
    private Date fechaCompra;
    
    @NotNull
    private Integer cantidad;
    
    @NotNull
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

    public Set<String> getDniAsistentes() {
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

    public void setDniAsistentes(Set<String> dniAsistentes) {
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
