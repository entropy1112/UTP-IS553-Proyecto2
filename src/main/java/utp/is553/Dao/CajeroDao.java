
package utp.is553.Dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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
    
    private static CajeroDao instancia;
    
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
    
    public Cajero añadirCajero(String nombre, Integer[] billetes) 
                               throws BaseDatosException {
        var em = emf.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            
            Cajero cajero = new Cajero();
            cajero.setNombre(nombre);
            cajero.setBilletes(billetes);
            cajero.setSaldo(contarSaldo(cajero));
            
            em.persist(cajero);
            
            et.commit();
            return cajero;
            
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            throw new BaseDatosException(e.getMessage());
        } finally {
            em.close();
        }
    }
    
    public void cargarDinero(Cajero cajero, Integer[] aCargar) 
                            throws BaseDatosException { 
        
        var em = emf.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            
            Integer[] total = cajero.getBilletes();
            for(int i = 0; i<=4; i++) {
                total[i] += aCargar[i];
            }
            cajero.setBilletes(total);
            cajero.setSaldo(contarSaldo(cajero));
            
            em.merge(cajero);
            
            et.commit();
            
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            throw new BaseDatosException(e.getMessage());
        } finally {
            em.close();
        }

    }
    
    public void consignar(Integer usuario, Integer monto) 
                          throws BaseDatosException {
        
        var em = emf.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            
            var query = em.createQuery("select e from Cliente e where e.usuario"
                                       + " = :usuario", Cliente.class);
            query.setParameter("usuario", usuario);
            
            Cliente cliente = query.getSingleResult();
            Integer total = cliente.getSaldo() + monto;
            cliente.setSaldo(total);
            
            em.merge(cliente);
            
            et.commit();
        
        } catch (Exception e) {
            if(et != null) {
                et.rollback();
                throw new BaseDatosException("Usuario inexistente");
            }
        } finally { 
            em.close();
        }
        
    }
    
    public Integer contarSaldo(Cajero cajero){
        Integer[] billetes = cajero.getBilletes();
        Integer total = (billetes[0]*50000) + (billetes[1]*20000) 
                       + (billetes[2]*10000) + (billetes[3]*5000)
                       + (billetes[4]*2000); 
        return total;
    }
    
    public Integer[] retirar(Integer retiro, Cajero cajero) throws BilletesException {
        Integer[] salida = new Integer[5];
        for(int i=0;i<=4;i++) {
            salida[i] = 0;
        }
        Integer[] existencia = cajero.getBilletes();
        
        if((retiro % 10000) == 1000 || (retiro % 10000) == 3000 || retiro < 2000){
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
        
        return salida;
    }
    
}
