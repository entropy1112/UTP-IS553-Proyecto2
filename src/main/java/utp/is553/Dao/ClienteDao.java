
package utp.is553.Dao;

import utp.is553.Excepciones.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import utp.is553.Entidades.Cajero;
import utp.is553.Entidades.Cliente;

/**
 *
 * @author Sebastian
 */
public class ClienteDao implements Verificadores{
    
    private static ClienteDao instancia;
    
    public static ClienteDao getInstance(){
        if(instancia == null){
            instancia = new ClienteDao();
        }
        return instancia;
    }
    
    public final EntityManagerFactory emf;
    
    public ClienteDao(){
        emf = Persistence.createEntityManagerFactory("proyecto2");
    }
    
    public Integer consultarSaldo(String usuario, String clave) 
                               throws BaseDatosException, 
                                      ClaveErroneaException {
        
        Integer usuarioInt = Integer.valueOf(usuario);
        Integer claveInt = Integer.valueOf(clave);
        Integer saldo = null;
        
        try {
            Cliente cliente = verificarDatos(usuarioInt, claveInt);
            
            saldo = cliente.getSaldo();
            
        } catch (BaseDatosException e) {
            throw new BaseDatosException(e.getMessage());
        } catch (ClaveErroneaException e) {
            throw new ClaveErroneaException(e.getMessage());
        }
        
        return saldo;
    }
    
    public Integer retirarDinero(Integer usuario, Integer clave, Integer retiro, 
                                 Cajero cajero)
                                 throws BaseDatosException, 
                                        SaldoInsuficienteException, 
                                        ClaveErroneaException,
                                        BilletesException {

        Integer saldo = null;
        CajeroDao cajeroDao = CajeroDao.getInstance();
        
        try {
            Cliente cliente = verificarDatos(usuario, clave);
            verificarSaldo(retiro, cliente);
            verificarExistencia(retiro, cajero);
            cajeroDao.retirar(retiro, cajero);
            saldo = cliente.getSaldo();
            saldo -= retiro;
            cliente.setSaldo(saldo);
            
        } catch (BaseDatosException e) {
            throw new BaseDatosException(e.getMessage());
        } catch (ClaveErroneaException e) {
            throw new ClaveErroneaException(e.getMessage());
        } catch (SaldoInsuficienteException e) {
            throw new SaldoInsuficienteException(e.getMessage());
        } catch (BilletesException e) {
            throw new BilletesException(e.getMessage());
        }
        
        return saldo;
        
    }


    @Override
    public Cliente verificarDatos(Integer usuario, Integer clave) 
                              throws BaseDatosException, ClaveErroneaException {
        
        var em = emf.createEntityManager();
        
        try {
            var query = em.createQuery("select e from Cliente e where e.usuario"
                                   + " = :usuario", Cliente.class);
            query.setParameter("usuario", usuario);
        
            Cliente cliente = query.getSingleResult();
            
            if(!cliente.getClave().equals(clave)) {
                throw new ClaveErroneaException("Clave equivocada");
            }
            
            return cliente;
            
        } catch (ClaveErroneaException e) {
            throw new ClaveErroneaException(e.getMessage());
        } catch (NoResultException e) {
            throw new BaseDatosException("Usuario inexistente");
        } finally {
            em.close();
        }
    }
    
    @Override
    public void verificarSaldo(Integer retiro, Cliente cliente)
                               throws SaldoInsuficienteException {
        
        if(cliente.getSaldo() < retiro) {
            throw new SaldoInsuficienteException("El valor a retirar"
                                           + " es mayor al saldo en la cuenta");
        }
    
    }
    
    @Override
    public void verificarExistencia(Integer retiro, Cajero cajero)
                                throws SaldoInsuficienteException {
        if(cajero.getSaldo() < retiro) {
            throw new SaldoInsuficienteException("No hay suficiente dinero para "
                                        + "entregar al usuario");
        }
        
    }
    
    
}
