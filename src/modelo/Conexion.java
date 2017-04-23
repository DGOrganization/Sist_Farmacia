/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.postgresql.ds.PGConnectionPoolDataSource;

/**
 *
 * @author dakrpastiursSennin
 */
public class Conexion {
    protected final String usuario = "postgres";
    protected final String contraseña = "adminroot";
    protected final String servidor = "localhost";
    protected final int puerto = 5432;
    protected final String base = "db_farmacia";
    protected String url = "";
    private Connection conn;
    private PGConnectionPoolDataSource pool;
    public Conexion(){
        conn = null;
        pool.setServerName(servidor);
        pool.setPortNumber(puerto);
        pool.setDatabaseName(base);
        pool.setUser(usuario);
        pool.setPassword(contraseña);
        url = "jdbc:postgresql://" + servidor + ":" + puerto + "/" + base;
    }

    public Connection getConnection() {
        return conn;
    }

    public boolean Conectar(){
        boolean exito = false;
        try{
            if(conn == null || conn.isClosed()){
                Class.forName("org.postgresql.Driver").newInstance();
            }
            conn = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexion con la base de datos " + base + " ha sido exitosa");
            exito = true;
        }catch(ClassNotFoundException | SQLException ex){
              JOptionPane.showMessageDialog(null, 
                    "Ha ocurrido un error al conectar a la base de datos: \n" + ex.getMessage() + "\n.Por favor contacte al desarrollador", 
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exito;
    }
    
    public void Desconectar(){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Se ha desconectado de la base de datos " + base);
    }
}
