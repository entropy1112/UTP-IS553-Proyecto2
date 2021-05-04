
package utp.is553.Entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Sebastian
 */
@Entity
@Table(name = "CLIENTE")
public class Cliente implements Serializable{
    
    @Id
    @Column(name = "Usuario", unique = true)
    private Integer usuario;
    
    @Column(name = "Clave")
    private Integer clave;
    
    @Column(name = "Saldo")
    private Integer saldo;
    
    public Cliente(){
        
    }

    public Cliente(Integer usuario, Integer clave, Integer saldo) {
        this.usuario = usuario;
        this.clave = clave;
        this.saldo = saldo;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public Integer getClave() {
        return clave;
    }

    public void setClave(Integer clave) {
        this.clave = clave;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Cliente{" 
                + ", usuario=" + usuario 
                + ", clave=" + clave 
                + ", saldo=" + saldo 
                + '}';
    }
    
    
    
}
