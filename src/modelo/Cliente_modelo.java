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
import entidades.Cliente;
import entidades.Municipio;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Cliente_modelo {
     public List<Cliente> ListarClientes(){
        List<Cliente> lista = new ArrayList<>();
        Conexion conn = new Conexion();
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerclientes() }");
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        Cliente cliente = new Cliente();
                        cliente.setId(resultado.getLong("id"));
                        cliente.setNombre(resultado.getString("nombre"));
                        cliente.setApellidoPaterno(resultado.getString("apellidopaterno"));
                        cliente.setApellidoMaterno(resultado.getObject("apellidomaterno") == null ? "" : resultado.getString("apellidomaterno"));
                        cliente.setDui(resultado.getString("dui"));
                        cliente.setNit(resultado.getString("nit"));
                        cliente.setSexo(resultado.getString("genero").equals("F") ? "Femenino" : "Masculino");
                        cliente.setNacimiento(resultado.getDate("fechanacimiento"));
                        cliente.setDireccion(resultado.getString("direccion"));
                        cliente.setMunicipio(new Municipio_modelo().ListarMunicipio(new Municipio(resultado.getLong("idmunicipio"))));
                        cliente.setTelefono(resultado.getString("telefono"));
                        cliente.setMovil(resultado.getString("celular"));
                        cliente.setEmail(resultado.getObject("email") == null ? "" : resultado.getString("email"));
                        cliente.setEstado(resultado.getBoolean("estado"));
                        lista.add(cliente);
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
    
    public Cliente ListarCliente(Cliente pCliente){
        Cliente cliente = new Cliente();
        Conexion conn = new Conexion();
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenercliente(?) }");
                cmd.setLong(1, pCliente.getId());
                if(cmd.execute()){
                    ResultSet resultado = cmd.getResultSet();
                    while(resultado.next()){
                        cliente.setId(resultado.getLong("id"));
                        cliente.setNombre(resultado.getString("nombre"));
                        cliente.setApellidoPaterno(resultado.getString("apellidopaterno"));
                        cliente.setApellidoMaterno(resultado.getObject("apellidomaterno") == null ? "" : resultado.getString("apellidomaterno"));
                        cliente.setDui(resultado.getString("dui"));
                        cliente.setNit(resultado.getString("nit"));
                        cliente.setSexo(resultado.getString("genero").equals("F") ? "Femenino" : "Masculino");
                        cliente.setNacimiento(resultado.getDate("fechanacimiento"));
                        cliente.setDireccion(resultado.getString("direccion"));
                        cliente.setMunicipio(new Municipio_modelo().ListarMunicipio(new Municipio(resultado.getLong("idmunicipio"))));
                        cliente.setTelefono(resultado.getString("telefono"));
                        cliente.setMovil(resultado.getString("celular"));
                        cliente.setEmail(resultado.getObject("email") == null ? "" : resultado.getString("email"));
                        cliente.setEstado(resultado.getBoolean("estado"));
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
        return cliente;
    }
    
    public boolean Registrar(Cliente pCliente){
        boolean exito = false;
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call registrarcliente(?,?,?,?,?,?,?,?,?,?,?,?,?) }");
                cmd.setString(1, pCliente.getNombre());
                cmd.setString(2, pCliente.getApellidoPaterno());
                cmd.setString(3, pCliente.getApellidoMaterno());
                cmd.setString(4, pCliente.getDui());
                cmd.setString(5, pCliente.getNit());
                cmd.setString(6, pCliente.getSexo());
                cmd.setDate(7, pCliente.getNacimiento());
                cmd.setString(8, pCliente.getDireccion());
                cmd.setLong(9, pCliente.getMunicipio().getId());
                cmd.setString(10, pCliente.getTelefono());
                cmd.setString(11, pCliente.getMovil());
                cmd.setString(12, pCliente.getEmail());
                cmd.setBoolean(13, pCliente.isEstado());
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
    
    public boolean Editar(Cliente pCliente){
        boolean exito = false;
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call actualizarcliente(?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
                cmd.setLong(1, pCliente.getId());
                cmd.setString(2, pCliente.getNombre());
                cmd.setString(3, pCliente.getApellidoPaterno());
                cmd.setString(4, pCliente.getApellidoMaterno());
                cmd.setString(5, pCliente.getDui());
                cmd.setString(6, pCliente.getNit());
                cmd.setString(7, pCliente.getSexo());
                cmd.setDate(8, pCliente.getNacimiento());
                cmd.setString(9, pCliente.getDireccion());
                cmd.setLong(10, pCliente.getMunicipio().getId());
                cmd.setString(11, pCliente.getTelefono());
                cmd.setString(12, pCliente.getMovil());
                cmd.setString(13, pCliente.getEmail());
                cmd.setBoolean(14, pCliente.isEstado());
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
    
    public boolean Eliminar(Cliente pCliente){
        boolean exito = false;
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call eliminarcliente(?) }");
                cmd.setLong(1, pCliente.getId());
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
