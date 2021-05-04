
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
        cajero.setSaldo(cajero.contarSaldo());
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
    
    public Integer consultarSaldo(Cajero cajero) {
        
        Integer saldoCajero = cajero.getSaldo();
        return saldoCajero;
    }
    
}
