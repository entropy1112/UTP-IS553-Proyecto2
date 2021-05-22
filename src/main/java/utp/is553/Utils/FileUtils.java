/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utp.is553.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import utp.is553.Dao.CajeroDao;
import utp.is553.Entidades.Cajero;
import utp.is553.Entidades.Cliente;
import utp.is553.Excepciones.BaseDatosException;
import utp.is553.Excepciones.FileException;

/**
 *
 * @author Sebastian
 */
public class FileUtils {
    
    private final String rutaClientes = "Clientes.txt";
    private final String rutaCajeros = "Cajeros.txt";
    private final CajeroDao cajeroDao = new CajeroDao();
    private final EntityManagerFactory emf = 
            Persistence.createEntityManagerFactory("proyecto2");
    
    
    public FileUtils (){ 
    }
    
    public List<Cliente> cargarClientes () throws FileException {
        
        List<Cliente> clientes = new ArrayList<>();
        File archivoClientes = new File(rutaClientes);
        
        try {
            var lector = new Scanner(archivoClientes);
            
            while(lector.hasNextLine()){
                var campos = lector.nextLine().split(";",3);
                
                Integer codigo = Integer.valueOf(campos[0]);
                Integer clave = Integer.valueOf(campos[1]);
                Integer saldo = Integer.valueOf(campos[2]);
                
                Cliente cliente = new Cliente(codigo,clave,saldo);
                
                clientes.add(cliente);
            }
            
        } catch (FileNotFoundException e) {
            throw new FileException("No se encontró el archivo");
        }
        
        return clientes;
    }
    
    public List<Cajero> cargarCajeros () throws FileException {
        
        List<Cajero> cajeros = new ArrayList<>();
        File archivoCajeros = new File(rutaCajeros);
        
        try {
            var lector = new Scanner(archivoCajeros);
            
            while (lector.hasNextLine()){
                var campos = lector.nextLine().split(";",2);
                
                var nombre = campos[0];
                String[] stringBilletes = campos[1].split(",",5);
                
                Integer[] billetes = new Integer[5];
                
                for(int i=0; i<5; i++){
                    billetes[i] = Integer.valueOf(stringBilletes[i]);
                }

                Cajero c = new Cajero(nombre,billetes);
                c.setSaldo(cajeroDao.contarSaldo(c));
                
                cajeros.add(c);
            }
            
        } catch (FileNotFoundException | NumberFormatException e) {
            throw new FileException("No se encontró el archivo");
        }
        
        return cajeros;
    }
    
    public void montarABase (List<Cliente> clientes, List<Cajero> cajeros) 
                            throws BaseDatosException {
        
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            
            clientes.forEach((cliente) -> {
                em.persist(cliente);
            });
            
            cajeros.forEach((cajero) -> {
                em.persist(cajero);
            });
            
            et.commit();
            
        } catch (Exception e) {
            if(et != null) {
                et.rollback();
                throw new BaseDatosException(e.getMessage());
            }
        } finally {
            em.close();
        }
        
    }
    
}
