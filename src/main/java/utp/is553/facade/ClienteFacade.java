/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.is553.facade;

import utp.is553.Dao.ClienteDao;
import utp.is553.Entidades.Cajero;
import utp.is553.Excepciones.BaseDatosException;
import utp.is553.Excepciones.BilletesException;
import utp.is553.Excepciones.ClaveErroneaException;
import utp.is553.Excepciones.SaldoInsuficienteException;

/**
 *
 * @author Sebastian
 */
public class ClienteFacade {
    
    public ClienteDao clienteDao;

    public ClienteFacade() {
        clienteDao = ClienteDao.getInstance();
    }
    
    public Integer consultarSaldo(Integer usuario, Integer clave) 
                                  throws BaseDatosException, 
                                         ClaveErroneaException {
        try {
            Integer saldo = clienteDao.consultarSaldo(usuario, clave);
            return saldo;
        } catch (BaseDatosException e) {
            throw new BaseDatosException(e.getMessage());
        } catch (ClaveErroneaException e) {
            throw new ClaveErroneaException(e.getMessage());
        }
    }
    
    public Integer[] retirarDinero(Integer usuario, Integer clave, Integer retiro, 
                                    Cajero cajero)
                                    throws BaseDatosException, 
                                           SaldoInsuficienteException, 
                                           ClaveErroneaException,
                                           BilletesException {
        
        try {
            Integer[] saldoBilletes;
            saldoBilletes = clienteDao.retirarDinero(usuario, clave, retiro, 
                                                     cajero);
            return saldoBilletes;
        } catch (BaseDatosException e) {
            throw new BaseDatosException(e.getMessage());
        } catch (ClaveErroneaException e) {
            throw new ClaveErroneaException(e.getMessage());
        } catch (SaldoInsuficienteException e) {
            throw new SaldoInsuficienteException(e.getMessage());
        } catch (BilletesException e) {
            throw new BilletesException(e.getMessage());
        }
    }
}
