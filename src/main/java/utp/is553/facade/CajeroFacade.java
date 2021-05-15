/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.is553.facade;

import utp.is553.Dao.CajeroDao;
import utp.is553.Entidades.Cajero;
import utp.is553.Excepciones.BaseDatosException;

/**
 *
 * @author Sebastian
 */
public class CajeroFacade {
    
    public CajeroDao cajeroDao;

    public CajeroFacade() {
        cajeroDao = CajeroDao.getInstance();
    }
    
    public void cargarDinero(Cajero cajero, Integer[] aCargar) 
                            throws BaseDatosException {
        try {
            cajeroDao.cargarDinero(cajero, aCargar);
        } catch (BaseDatosException e) {
            throw new BaseDatosException(e.getMessage());
        }
    }
    
    public void consignar(Integer usuario, Integer monto) 
                          throws BaseDatosException {
        try {
            cajeroDao.consignar(usuario, monto);
        } catch (BaseDatosException e) {
            throw new BaseDatosException(e.getMessage());
        }
    }
}
