/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.is553.Dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utp.is553.Entidades.Cajero;
import utp.is553.Entidades.Cliente;
import utp.is553.Excepciones.BaseDatosException;

/**
 *
 * @author Sebastian
 */
public class CajeroDaoTest {
    
    public CajeroDaoTest() {
    }
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
    
    @Test
    public void cargarDinero() throws BaseDatosException {
        CajeroDao cajerodao = CajeroDao.getInstance();
        Integer[] billetes = new Integer[5];
        billetes[0] = 10;
        billetes[1] = 10;
        billetes[2] = 10;
        billetes[3] = 10;
        billetes[4] = 10;    
        
        Cajero cajero = cajerodao.añadirCajero("Humanidades", billetes);
        
        Integer[] aCargar = new Integer[5];
        aCargar[0] = 10;
        aCargar[1] = 15;
        aCargar[2] = 20;
        aCargar[3] = 30;
        aCargar[4] = 50;
        
        cajerodao.cargarDinero(cajero, aCargar);
        
        Integer[] total = new Integer[5];
        total[0] = 20;
        total[1] = 25;
        total[2] = 30;
        total[3] = 40;
        total[4] = 60;
        
        EntityManager em = emf.createEntityManager();
        
        try { 
            var query = em.createQuery("select e from Cajero e where e.nombre = :nombre", Cajero.class);
            query.setParameter("nombre", "Humanidades");
            
            Cajero caj = query.getSingleResult();
            
            Integer[] existencia = caj.getBilletes();
            
            assertArrayEquals(existencia, total);
            
        } catch (Exception e) {
            throw new BaseDatosException(e.getMessage());
        } finally {
            em.close();
        }
        
    }
    
    @Test
    public void consignar() throws BaseDatosException {
        CajeroDao cajeroDao = CajeroDao.getInstance();
        ClienteDao clienteDao = ClienteDao.getInstance();
        
        Integer usuario = 1007;
        Integer clave = 2368;
        Integer saldo = 1000000;
        Integer consignacion = 500000;
        clienteDao.añadirCliente(usuario,clave,saldo);
        cajeroDao.consignar(usuario, consignacion);
        Integer total = saldo + consignacion;
        
        Integer usuario2 = 1217;
        Integer clave2 = 6598;
        Integer saldo2 = 10000;
        Integer consignacion2 = 25000;
        clienteDao.añadirCliente(usuario2,clave2,saldo2);
        cajeroDao.consignar(usuario2, consignacion2);
        Integer total2 = saldo2 + consignacion2;
        
        EntityManager em = emf.createEntityManager();
        
        try { 
            var query = em.createQuery("select e from Cliente e where e.usuario"
                                        + " = :usuario", Cliente.class);
            query.setParameter("usuario", usuario);
            Cliente cli = query.getSingleResult();
            Integer sal = cli.getSaldo();
            
            assertEquals(sal, total);
            
            var query2 = em.createQuery("select e from Cliente e where e.usuario"
                                         + " = :usuario", Cliente.class);
            query2.setParameter("usuario", usuario2);
            Cliente cli2 = query2.getSingleResult();
            Integer sal2 = cli2.getSaldo();
            
            assertEquals(sal2, total2);
            
        } catch (Exception e) {
            throw new BaseDatosException(e.getMessage());
        } finally {
            em.close();
        }
    }
    
}
