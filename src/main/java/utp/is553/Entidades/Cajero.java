
package utp.is553.Entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import utp.is553.Excepciones.BilletesException;

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
    
    @Column(name = "Saldo")
    private Integer saldo;
    
    private Integer[] billetes = new Integer[5];
    

    public Cajero() {
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
    
    public Integer contarSaldo(){
        Integer total = (billetes[0]*50000) + (billetes[1]*20000) 
                       + (billetes[2]*10000) + (billetes[3]*5000)
                       + (billetes[4]*2000); 
        return total;
    }

    public void retirar(Integer retiro) throws BilletesException {
        Integer[] salida = new Integer[5];
        Integer[] existencia = billetes;
        
        if((retiro % 10000) == 1000 || (retiro % 10000) == 3000){
            throw new BilletesException("No hay manera de entregarle la suma"
                                        + " de dinero exacta");
        }
        
        while(retiro >= 50000 && existencia[0] > 0){
            retiro -= 50000;
            existencia[0] -= 1;
            salida[0] += 1;
        }
        while(retiro >= 20000 && existencia[1] > 0){
            retiro -= 20000;
            existencia[1] -= 1;
            salida[1] += 1;
        }
        while(retiro >= 10000 && existencia[2] > 0){
            retiro -= 10000;
            existencia[2] -= 1;
            salida[2] += 1;
        }
        while(retiro >= 5000 && existencia[3] > 0 && ((retiro - 5000) % 2000) == 0 ){
            retiro -= 5000;
            existencia[3] -= 1;
            salida[3] += 1;
        }
        while(retiro >= 2000 && existencia[4] > 0){
            retiro -= 2000;
            existencia[4] -= 1;
            salida[4] += 1;
        }
        
        if(!retiro.equals(0)){
            throw new BilletesException("No hay suficiente dinero para "
                                        + "entregar al usuario");
        } else {
            billetes = existencia;
        }
    }
}
