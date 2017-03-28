/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Empleado;
import entidades.Usuario;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author dakrpastiursSennin
 */
public class Empleado_modelo {
    
    public Empleado Empleado(Usuario pUsuario){
        Empleado empleado = new Empleado();
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerempleado(?) }");
                cmd.setLong(1, pUsuario.getId());
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    if(resultado.next()){
                        empleado.setId(resultado.getLong("id"));
                        empleado.setNombre(resultado.getString("nombre"));
                        empleado.setApellidopaterno(resultado.getString("apellidopaterno"));
                        empleado.setApellidomaterno(resultado.getString("apellidomaterno"));
                        empleado.setEstado(resultado.getBoolean("estado"));
                    }
                }
            }
        } catch(Exception ex){
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
        return empleado;
    }
    
}
