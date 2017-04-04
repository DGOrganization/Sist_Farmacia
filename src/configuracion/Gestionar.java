/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuracion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author dakrpastiursSennin
 */
public class Gestionar {

    protected InputStream _input = null;
    protected OutputStream _output = null;
    protected final Properties propiedades = new Properties();
    
    public String Leer(String archivo, String propiedad) {
        try {
            _input = new FileInputStream(archivo + ".properties");
            propiedades.load(_input);
        } catch (IOException ex) {
            System.err.println("Error " + ex.getMessage());
        } finally {
            if (_input != null) {
                try {
                    _input.close();
                } catch (IOException ex) {
                    System.err.println("Error " + ex.getMessage());
                }
            }
        }
        return propiedades.getProperty(propiedad);
    }
    
    public void Modificar(String archivo, String propiedad, String valor){
        try{
            File _archivo = new File(archivo + ".properties");
            if(!_archivo.exists()){
                _archivo.createNewFile();
            } 
            _input = new FileInputStream(_archivo);
            propiedades.load(_input);
            propiedades.setProperty(propiedad, valor);
            _output = new FileOutputStream(_archivo);
            propiedades.store(_output, "prueba de escritura de propiedades");
        } catch(IOException ex){
            System.err.println("Error " + ex.getMessage());
        } finally {
            if(_output != null){
                try{
                    _output.close();
                } catch(Exception ex){
                    System.err.println("Error " + ex.getMessage());
                }
            }
        }
    }
}
