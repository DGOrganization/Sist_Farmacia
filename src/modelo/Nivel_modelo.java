/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Empleado;
import entidades.Nivel;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author dakrpastiursSennin
 */
public class Nivel_modelo {
    
    public Nivel ListarNivel(Nivel pNivel, boolean listarMenus){
        Nivel nivel = new Nivel();
        Conexion conn = new Conexion();
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenernivel(?) }");
                cmd.setInt(1, pNivel.getId());
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        nivel.setId(resultado.getInt("codigo"));
                        nivel.setNombre(resultado.getString("nivel"));
                        nivel.setEstado(resultado.getBoolean("est"));
                        if(listarMenus){
                            nivel.setMenus(new Menu_modelo().obtenerMenus(pNivel));
                        }
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
        return nivel;
    }
}
