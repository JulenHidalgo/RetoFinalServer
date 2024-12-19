/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 2dam
 */
@NamedQueries({
    @NamedQuery(name = "getClubsByEventDates", 
            query = "SELECT c FROM Club c JOIN c.events e WHERE e.fecha "
                    + "> :fechaIni and e.fecha < :fechafin"),
    @NamedQuery(name = "getClubsByEventDate", 
            query = "SELECT c FROM Club c JOIN c.events e WHERE e.fecha "
                    + "= :fecha"),
    @NamedQuery(name = "getClubNameByEventId", 
            query = "SELECT c.nombre FROM Club c JOIN c.events e WHERE e.idEvent "
                    + "= :idEvent"),
})
@Entity
@Table(name="club", schema="nocturna")
@XmlRootElement
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    private String nombre;
    
    @NotNull
    private String ubicacion;
    
    @NotNull
    private String ciudad;
    
    private String instagram;
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "club")
    private Set<Event> events;

    @XmlTransient
    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getInstagram() {
        return instagram;
    }

    public Long getId() {
        return id;
    }
    
    public Club() {
        this.nombre = "";
        this.ubicacion = "";
        this.ciudad = "";
        this.instagram = "";
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
        if (!(object instanceof Club)) {
            return false;
        }
        Club other = (Club) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    
}
