/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.is553.Dao;

import utp.is553.Entidades.Cajero;
import utp.is553.Entidades.Cliente;
import utp.is553.Excepciones.BaseDatosException;
import utp.is553.Excepciones.BilletesException;
import utp.is553.Excepciones.ClaveErroneaException;
import utp.is553.Excepciones.SaldoInsuficienteException;

/**
 *
 * @author Sebastian
 */
public interface Verificadores {
    
    public Cliente verificarDatos(Integer usuario, Integer clave) 
                               throws BaseDatosException, ClaveErroneaException;
    
    public void verificarSaldo(Integer retiro, Cliente cliente)
                               throws SaldoInsuficienteException;
    
    public void verificarExistencia(Integer retiro, Cajero cajero)
                                throws SaldoInsuficienteException,
                                       BilletesException;

}
