/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
/**
 *
 * @author 2dam
 */
@Entity
public class Client extends User implements Serializable {
   
    @NotNull
    private String nombre = "";
    @NotNull
    private String apellido = "";
    @NotNull
    private String ciudad = "";
    @NotNull
    private Integer telefono = 0;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Past
    private Date fehcaNacimiento; 
    @NotNull
    private String dni;
            
    public Client(){
        super();
    }

    public Client(String nombre, String apellido, String ciudad, Integer telefono, Date fehcaNacimiento, String dni, String mail, String passwd) {
        super(mail, passwd, false);
        this.nombre = nombre;
        this.apellido = apellido;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.fehcaNacimiento = fehcaNacimiento;
        this.dni = dni;
    }
    
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
    
}
