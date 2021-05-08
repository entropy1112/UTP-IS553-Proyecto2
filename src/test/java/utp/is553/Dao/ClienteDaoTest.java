/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.is553.Dao;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utp.is553.Entidades.Cajero;
import utp.is553.Excepciones.BaseDatosException;
import utp.is553.Excepciones.BilletesException;
import utp.is553.Excepciones.ClaveErroneaException;
import utp.is553.Excepciones.SaldoInsuficienteException;

/**
 *
 * @author Sebastian
 */
public class ClienteDaoTest {
    
    public ClienteDaoTest() {
    }

    @Test
    public void consultarSaldo() throws BaseDatosException, ClaveErroneaException {
        ClienteDao clienteDao = ClienteDao.getInstance();
        
        Integer usuario = 1007;
        Integer clave = 2368;
        Integer saldo = 1000000;
        clienteDao.añadirCliente(usuario,clave,saldo);
        
        Integer usuario2 = 1217;
        Integer clave2 = 6598;
        Integer saldo2 = 10000;
        clienteDao.añadirCliente(usuario2,clave2,saldo2);
        
        Integer total = clienteDao.consultarSaldo(1007, 2368);
        Integer total2 = clienteDao.consultarSaldo(1217, 6598);
        
        assertEquals(total, saldo);
        assertEquals(total2, saldo2);
    }
    
    @Test
    public void retirarDinero() throws BaseDatosException, 
                                SaldoInsuficienteException,
                                ClaveErroneaException,
                                BilletesException {
        
        ClienteDao clienteDao = ClienteDao.getInstance();
        CajeroDao cajerodao = CajeroDao.getInstance();
        Integer[] billetes = new Integer[5];
        billetes[0] = 10;
        billetes[1] = 10;
        billetes[2] = 10;
        billetes[3] = 10;
        billetes[4] = 10;    
        
        Cajero cajero = cajerodao.añadirCajero("Humanidades", billetes);
        
        Integer usuario = 1007;
        Integer clave = 2368;
        Integer saldo = 1000000;
        Integer retiro = 500000;
        Integer[] billetes1 = new Integer[5];
        billetes1[0] = 10;
        billetes1[1] = 0;
        billetes1[2] = 0;
        billetes1[3] = 0;
        billetes1[4] = 0;
        clienteDao.añadirCliente(usuario,clave,saldo);
        
        Integer usuario2 = 1217;
        Integer clave2 = 6598;
        Integer saldo2 = 100000;
        Integer retiro2 = 74000;
        Integer[] billetes2 = new Integer[5];
        billetes2[0] = 0;
        billetes2[1] = 3;
        billetes2[2] = 1;
        billetes2[3] = 0;
        billetes2[4] = 2;
        clienteDao.añadirCliente(usuario2,clave2,saldo2);
        
        Integer[] salida1 = clienteDao.retirarDinero(usuario, clave, retiro, cajero);
        Integer[] salida2 = clienteDao.retirarDinero(usuario2, clave2, retiro2, cajero);
        
        assertArrayEquals(billetes1, salida1);
        assertArrayEquals(billetes2, salida2);
    }
    
}
