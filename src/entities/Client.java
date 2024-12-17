/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.validation.constraints.Future;
import javax.validation.constraints.Past;
/**
 *
 * @author 2dam
 */
@Entity
public class Client extends User implements Serializable {

   
    private String nombre;
    private String apellido;
    private String ciudad;
    private Integer telefono;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Past
    private Date fehcaNacimiento; 
    private String dni;
            
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public Date getFehcaNacimiento() {
        return fehcaNacimiento;
    }

    public void setFehcaNacimiento(Date fehcaNacimiento) {
        this.fehcaNacimiento = fehcaNacimiento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
    
    
    

    @Override
    public String toString() {
        return "";
    }
    
}
