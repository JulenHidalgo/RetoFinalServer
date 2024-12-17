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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Future;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 2dam
 */
@Entity
@Table(name="event" , schema="nocturna")
@XmlRootElement
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String nombre;
    private Date fecha;
    private int NumEntradas;
    private int consumicion;
    private Double precioEntrada;
    private Club club; 
    private Set artists;
    private Set tickets;

    public Event(String nombre, Date fecha, int NumEntradas, int consumicion, Double precioEntrada) {
        this.nombre = "";
        this.fecha = valueOf(LocalDate.now());
        this.NumEntradas = 0;
        this.consumicion = 0;
        this.precioEntrada = 0.0;
    }
 
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long idEvent) {
        this.id = idEvent;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Future
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getNumEntradas() {
        return NumEntradas;
    }

    public void setNumEntradas(int NumEntradas) {
        this.NumEntradas = NumEntradas;
    }

    public int getConsumicion() {
        return consumicion;
    }

    public void setConsumicion(int consumicion) {
        this.consumicion = consumicion;
    }

    public Double getPrecioEntrada() {
        return precioEntrada;
    }

    public void setPrecioEntrada(Double precioEntrada) {
        this.precioEntrada = precioEntrada;
    }
    
    
    @ManyToOne
    @JoinColumn(name="club")
    public Club getClub(){
       return  this.club;
        
    }
    
    @ManyToMany(mappedBy="events")
    public Set getArtits(){
       return  this.artists;
        
    }
    
    @OneToMany(mappedBy="ticket")
    public Set getTickets(){
       return  this.tickets;
        
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
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "enties.EventEntity[ id=" + id + " ]";
    }
    
}
