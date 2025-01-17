/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
    /**sacar loseventos donde actua un artista introducido*/
    @NamedQuery(name = "login", 
            query = "SELECT u FROM User u WHERE u.mail= :mail AND u.passwd= :passwd"),
    @NamedQuery(name = "resetPasswd", 
            query = "UPDATE User u SET u.passwd = :newPasswd WHERE u.mail = :mail"),
    @NamedQuery(name = "getUserByEmail", 
            query = "SELECT u FROM User u WHERE u.mail = :mail")
})


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="user" , schema="nocturna")
@XmlRootElement
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    private String mail = "";
    
    @NotNull
    private String passwd = "";
    
    @NotNull
    private Boolean isAdmin = false;
    
    @OneToMany(cascade=ALL, mappedBy="user")
    private Set<Ticket> tickets;

    public User() {
    }

    public User(String mail, String passwd, Boolean isAdmin) {
        this.mail = mail;
        this.passwd = passwd;
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @XmlTransient
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
    
    @XmlTransient
    public Set<Ticket> getTickets() {
        return tickets;
    }
    
    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.User[ id=" + id + " ]";
    }
    
}
