/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuracion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author dakrpastiursSennin
 */
public class Gestionar {

    private InputStream _archivo = null;
    private final Properties propiedades = new Properties();

    public String Leer(String archivo, String propiedad) {
        try {
            _archivo = getClass().getResourceAsStream(archivo + ".properties");
            propiedades.load(_archivo);
        } catch (IOException ex) {
            System.err.println("Error " + ex.getMessage());
        } finally {
            if (_archivo != null) {
                try {
                    _archivo.close();
                } catch (IOException ex) {
                    System.err.println("Error " + ex.getMessage());
                }
            }
        }
        return propiedades.getProperty(propiedad);
    }
}
