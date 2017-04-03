/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Articulo;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author dakrpastiursSennin
 */
public class Articulo_modelo {
    
    public Articulo ListarArticulo(Articulo pArticulo){
        Articulo articulo = new Articulo();
        Conexion conn = new Conexion();        
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerarticulo(?) }");
                cmd.setInt(1, pArticulo.getId());
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        articulo.setId(resultado.getInt("cod"));
                        articulo.setNombre(resultado.getString("prod"));
                        articulo.setEstado(resultado.getBoolean("estado"));
                    }
                }
            }
        } catch(SQLException ex){
            JOptionPane.showMessageDialog(
                    null, 
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                            + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre") + " - " + this.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            conn.Desconectar();
        }
        
        return articulo;
    }
    
}
