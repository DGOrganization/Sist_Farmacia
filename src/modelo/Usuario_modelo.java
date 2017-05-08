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
import controlador.Nivel_controlador;
import entidades.Empleado;
import entidades.Nivel;
import entidades.Usuario;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Usuario_modelo {

    public boolean Login(Usuario pUsuario) {
        boolean validado = false;
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call login(?,?) }")) {
            cmd.setString(1, pUsuario.getUsername());
            cmd.setString(2, pUsuario.getPassword());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    if (resultado.next()) {
                        validado = resultado.getBoolean(1);
                    }
                    resultado.close();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new configuracion.Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return validado;
    }

    public List<Usuario> ListarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenerusuarios() }")) {
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        Usuario usuario = new Usuario();
                        usuario.setUsername(resultado.getString("nickname"));
                        usuario.setEmpleado(new Empleado_modelo().ListarEmpleado(new Empleado(resultado.getInt("idempleado"))));
                        usuario.setNivel(new Nivel_controlador().Obtener(new Nivel(resultado.getInt("idnivel")), true));
                        usuario.setEstado(resultado.getBoolean("estado"));
                        lista.add(usuario);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return lista;
    }

    public Usuario ListarUsuario(Usuario pUsuario) {
        Usuario usuario = new Usuario();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenerusuario(?) }")) {
            cmd.setString(1, pUsuario.getUsername());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        usuario.setUsername(resultado.getString("nickname"));
                        usuario.setEmpleado(new Empleado_modelo().ListarEmpleado(new Empleado(resultado.getInt("idempleado"))));
                        usuario.setNivel(new Nivel_controlador().Obtener(new Nivel(resultado.getInt("idnivel")), true));
                        usuario.setEstado(resultado.getBoolean("estado"));
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return usuario;
    }

    /*public boolean Registrar(Usuario pUsuario){
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
    }*/
}
