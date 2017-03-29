/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author dakrpastiursSennin
 */

import configuracion.Gestionar;
import entidades.Empleado;
import entidades.Usuario;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Usuario_modelo {
    
    public boolean Login(Usuario pUsuario){
        boolean validado = false;
        Conexion conn = new Conexion();
        try{
            if(conn.Conectar()){                
                CallableStatement cmd = conn.getConnection().prepareCall("{ call login(?,?) }");
                cmd.setString(1, pUsuario.getUsername());
                cmd.setString(2, pUsuario.getPassword());
                if (cmd.execute()) {
                    ResultSet _reader = cmd.getResultSet();
                    if (_reader.next()) {
                        validado = _reader.getBoolean(1);
                    }
                }
            }
        } catch(SQLException ex){
            JOptionPane.showMessageDialog(
                    null, 
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                            + "\nFavor contacte al desarrollador",
                    "Sistema de Compras y Ventas - Usuario",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            conn.Desconectar();
        }
        return validado;
    }
    
    public List<Usuario> ListarUsuarios(){
        List<Usuario> lista = new ArrayList<>();
        Conexion conn = new Conexion();
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerusuarios() }");
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        Usuario usuario = new Usuario();
                        usuario.setUsername(resultado.getString("nickname"));
                        usuario.setEmpleado(new Empleado_modelo().Empleado(new Empleado(resultado.getInt("idempleado"))));
                        usuario.setEstado(resultado.getBoolean("estado"));
                        lista.add(usuario);
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
    
    public Usuario ListarUsuario(Usuario pUsuario){
        Usuario usuario = new Usuario();
        Conexion conn = new Conexion();
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerusuario(?) }");
                cmd.setString(1, pUsuario.getUsername());
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        usuario.setUsername(resultado.getString("nickname"));
                        usuario.setEmpleado(new Empleado_modelo().Empleado(new Empleado(resultado.getInt("idempleado"))));
                        usuario.setEstado(resultado.getBoolean("estado"));
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
        return usuario;
    }
    
    
    
    public boolean Registrar(Usuario pUsuario){
        boolean exito = false;
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call registrarusuario(?,?,?,?,?) }");
                cmd.setString(1, pUsuario.getUsername());
                cmd.setString(2, pUsuario.getPassword());
//                cmd.setString(3, pUsuario.getEmpleado().getNombre());
//                cmd.setString(4, pUsuario.getEmpleado().getApellidopaterno());
//                cmd.setString(5, pUsuario.getEmpleado().getApellidomaterno());
                exito = cmd.execute();
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
        return exito;
    }
    
    public boolean Editar(Usuario pUsuario){
        boolean exito = false;
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call actualizarusuario(?,?,?,?,?,?) }");
//                cmd.setLong(1, pUsuario.getId());
//                cmd.setString(2, pUsuario.getUsername());
//                cmd.setString(3, pUsuario.getPassword());
//                cmd.setString(4, pUsuario.getEmpleado().getNombre());
//                cmd.setString(5, pUsuario.getEmpleado().getApellidopaterno());
//                cmd.setString(6, pUsuario.getEmpleado().getApellidomaterno());
                exito = cmd.execute();
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
        return exito;
    }
    
    public boolean Eliminar(Usuario pUsuario){
        boolean exito = false;
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call eliminarusuario(?) }");
//                cmd.setLong(1, pUsuario.getId());
                exito = cmd.execute();
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
        return exito;
    }
    
}
