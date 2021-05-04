/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.is553.Entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Sebastian
 */

@Entity
@Table(name = "CAJERO")
public class Cajero implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "Nombre")
    private String nombre;
    
    @Column(name = "Billetes en Existencia")
    private Integer[] billetes;

    public Cajero() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer[] getBilletes() {
        return billetes;
    }

    public void setBilletes(Integer[] billetes) {
        this.billetes = billetes;
    }

    @Override
    public String toString() {
        return "Cajero{" + "id=" + id 
                + ", nombre=" + nombre 
                + '}';
    }
    
}
