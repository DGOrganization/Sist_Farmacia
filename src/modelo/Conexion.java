/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author dakrpastiursSennin
 */
public class Conexion {
    protected final String usuario = "postgres";
    protected final String contraseña = "adminroot";
    protected final String servidor = "localhost";
    protected final String puerto = "5432";
    protected final String base = "bd_farmacia";
    protected String url = "";
    private Connection conn;
    
    public Conexion(){
        conn = null;
        url = "jdbc:postgresql://" + servidor + ":" + puerto + "/" + base;
    }

    public Connection getConnection() {
        return conn;
    }

    public boolean Conectar(){
        boolean exito = false;
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexion con la base de datos " + base + " ha sido exitosa");
            exito = true;
        }catch(Exception ex){
              JOptionPane.showMessageDialog(null, 
                    "Ha ocurrido un error al conectar a la base de datos: \n" + ex.getMessage() + "\n, Por favor contacte al desarrollador", 
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE);
        }
        return exito;
    }
    
    public void Desconectar(){
        conn = null;
        System.out.println("Se ha desconectado de la base de datos " + base);
    }
}
