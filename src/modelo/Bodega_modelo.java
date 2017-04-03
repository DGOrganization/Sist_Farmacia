/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Bodega;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author dakrpastiursSennin
 */
public class Bodega_modelo {
    
    public List<Bodega> ListarBodegas(){
        List<Bodega> lista = new ArrayList<>();
        Conexion conn = new Conexion();        
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerbodegas() }");
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        Bodega bodega = new Bodega();
                        bodega.setId(resultado.getInt("codigo"));
                        bodega.setNombre(resultado.getString("bodega"));
                        bodega.setDireccion(resultado.getString("dir"));
                        bodega.setEstado(resultado.getBoolean("est"));
                        lista.add(bodega);
                    }
                }
            }
        } catch(SQLException ex){
            JOptionPane.showMessageDialog(
                    null, 
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                            + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            conn.Desconectar();
        }
        
        return lista;
    }
    
    public Bodega ListarBodega(Bodega pBodega){
        Bodega bodega = new Bodega();
        Conexion conn = new Conexion();        
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerbodega(?) }");
                cmd.setInt(1, pBodega.getId());
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        bodega.setId(resultado.getInt("codigo"));
                        bodega.setNombre(resultado.getString("bodega"));
                        bodega.setDireccion(resultado.getString("dir"));
                        bodega.setEstado(resultado.getBoolean("est"));
                    }
                }
            }
        } catch(SQLException ex){
            JOptionPane.showMessageDialog(
                    null, 
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                            + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            conn.Desconectar();
        }
        
        return bodega;
    }
    
}
