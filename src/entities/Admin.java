/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author 2dam
 */
@Entity
public class Admin extends User implements Serializable {

    private String departamento;
    
    public Admin(){
        super();
    }

    public Admin(String departamento, String mail, String passwd) {
        super(mail, passwd, true);
        this.departamento = departamento;
    }
    
    
    
    public Admin(String departamento){
        super();
        this.departamento = departamento;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

}
