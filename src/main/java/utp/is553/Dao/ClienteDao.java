
package utp.is553.Dao;

import utp.is553.Excepciones.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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
    
    public Cliente añadirCliente(Integer usuario, String clave, Integer saldo) 
                                 throws BaseDatosException {
        var em = emf.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            
            Cliente cliente = new Cliente(usuario,clave,saldo);
            em.persist(cliente);
            
            et.commit();
            return cliente;
            
        } catch (Exception e) {
            if(et != null) {
                et.rollback();
                throw new BaseDatosException(e.getMessage());
            }
        } finally {
            em.close();
        }
        return null;
        
    }
    
    public Integer consultarSaldo(Integer usuario, String clave) 
                               throws BaseDatosException, 
                                      ClaveErroneaException {
        
        Integer saldo = null;
        
        try {
            Cliente cliente = verificarDatos(usuario, clave);
            
            saldo = cliente.getSaldo();
            
        } catch (BaseDatosException e) {
            throw new BaseDatosException(e.getMessage());
        } catch (ClaveErroneaException e) {
            throw new ClaveErroneaException(e.getMessage());
        }
        
        return saldo;
    }
    
    public Integer[] retirarDinero(Integer usuario, String clave, Integer retiro, 
                                    Cajero cajero)
                                    throws BaseDatosException, 
                                           SaldoInsuficienteException, 
                                           ClaveErroneaException,
                                           BilletesException {

        Integer saldo = null;
        CajeroDao cajeroDao = CajeroDao.getInstance();
        Integer[] salidaBilletes;
        var em = emf.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            
            Cliente cliente = verificarDatos(usuario, clave);
            verificarSaldo(retiro, cliente);
            verificarExistencia(retiro, cajero);
            salidaBilletes = cajeroDao.retirar(retiro, cajero);
            saldo = cliente.getSaldo();
            saldo -= retiro;
            cliente.setSaldo(saldo);
            em.merge(cliente);
            em.merge(cajero);
            
            et.commit();
            return salidaBilletes;
            
        } catch (BaseDatosException e) {
            if(et != null) {
                et.rollback();
                throw new BaseDatosException(e.getMessage());
            }
        } catch (ClaveErroneaException e) {
            if(et != null) {
                et.rollback();
                throw new ClaveErroneaException(e.getMessage());
            }
        } catch (SaldoInsuficienteException e) {
            if(et != null) {
                et.rollback();
                throw new SaldoInsuficienteException(e.getMessage());
            }
        } catch (BilletesException e) {
            if(et != null) {
                et.rollback();
                throw new BilletesException(e.getMessage());
            }
        } finally {
            em.close();
        }
        return null;
    }


    @Override
    public Cliente verificarDatos(Integer usuario, String clave) 
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
