
package utp.is553.Dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import utp.is553.Entidades.Cajero;
import utp.is553.Entidades.Cliente;
import utp.is553.Excepciones.BaseDatosException;
import utp.is553.Excepciones.BilletesException;

/**
 *
 * @author Sebastian
 */
public class CajeroDao {
    
    public static CajeroDao instancia;
    
    public static CajeroDao getInstance(){
        if(instancia == null){
            instancia = new CajeroDao();
        }
        return instancia;
    }
    
    public final EntityManagerFactory emf;
    
    public CajeroDao(){
        emf = Persistence.createEntityManagerFactory("proyecto2");
    }
    
    public void cargarDinero(Cajero cajero, Integer[] aCargar){ 
        
        Integer[] total = cajero.getBilletes();
        for(int i = 0; i<=4; i++) {
            total[i] += aCargar[i];
        }
        cajero.setBilletes(total);
        cajero.setSaldo(contarSaldo(cajero));
    }
    
    public void consignar(Integer usuario, Integer monto) 
                          throws BaseDatosException {
        
        var em = emf.createEntityManager();
        try {
            var query = em.createQuery("select e from Cliente e where e.usuario"
                                       + " = :usuario", Cliente.class);
            query.setParameter("usuario", usuario);
            
            Cliente cliente = query.getSingleResult();
            Integer total = cliente.getSaldo() + monto;
            cliente.setSaldo(total);
            
        } catch (NoResultException e) {
            throw new BaseDatosException("Usuario inexistente");
        }
        
    }
    
    public Integer contarSaldo(Cajero cajero){
        Integer[] billetes = cajero.getBilletes();
        Integer total = (billetes[0]*50000) + (billetes[1]*20000) 
                       + (billetes[2]*10000) + (billetes[3]*5000)
                       + (billetes[4]*2000); 
        return total;
    }
    
    public void retirar(Integer retiro, Cajero cajero) throws BilletesException {
        Integer[] salida = new Integer[5];
        Integer[] existencia = cajero.getBilletes();
        
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
            cajero.setBilletes(existencia);
        }
    }
    
}
