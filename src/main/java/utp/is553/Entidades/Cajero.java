
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
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "Nombre")
    private String nombre;
    
    @Column(name = "Saldo")
    private Integer saldo;
    
    private Integer[] billetes = new Integer[5];

    public Cajero() {
        for(int i=0;i<5;i++){
            billetes[i] = 0;
        }
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
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
                + ", saldo=" + saldo + '}';
    }
    
}
