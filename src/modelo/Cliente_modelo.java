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
import entidades.Persona;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

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
                        cliente.setId(resultado.getLong("codigo"));
                        cliente.setNombre(resultado.getString("nombre"));
                        cliente.setApellidoPaterno(resultado.getString("apellido1"));
                        cliente.setApellidoMaterno(resultado.getObject("apellido2") == null ? "" : resultado.getString("apellido2"));
                        cliente.setDui(resultado.getString("dui"));
                        cliente.setNit(resultado.getString("nit"));
                        cliente.setSexo(resultado.getString("genero").equals("F") ? "Femenino" : "Masculino");
                        cliente.setNacimiento(resultado.getDate("nacimiento"));
                        cliente.setDireccion(resultado.getString("direccion"));
                        cliente.setEmail(resultado.getObject("correo") == null ? "" : resultado.getString("correo"));
                        cliente.setEstado(resultado.getBoolean("est"));
                        cliente.setMunicipio(new Municipio_modelo().ListarMunicipio(new Persona(resultado.getInt("codpersona"))));
                        cliente.setTelefono(new Telefono_modelo().ListarTelefonos(new Persona(resultado.getInt("codpersona"))));
                        lista.add(cliente);
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
                        cliente.setId(resultado.getLong("codigo"));
                        cliente.setNombre(resultado.getString("nombre"));
                        cliente.setApellidoPaterno(resultado.getString("apellido1"));
                        cliente.setApellidoMaterno(resultado.getObject("apellido2") == null ? "" : resultado.getString("apellido2"));
                        cliente.setDui(resultado.getString("dui"));
                        cliente.setNit(resultado.getString("nit"));
                        cliente.setSexo(resultado.getString("genero").equals("F") ? "Femenino" : "Masculino");
                        cliente.setNacimiento(resultado.getDate("nacimiento"));
                        cliente.setDireccion(resultado.getString("direccion"));
                        cliente.setEmail(resultado.getObject("correo") == null ? "" : resultado.getString("correo"));
                        cliente.setEstado(resultado.getBoolean("est"));
                        cliente.setMunicipio(new Municipio_modelo().ListarMunicipio(new Persona(resultado.getInt("codpersona"))));
                        cliente.setTelefono(new Telefono_modelo().ListarTelefonos(new Persona(resultado.getInt("codpersona"))));
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
        return cliente;
    }
    
    public boolean Registrar(Cliente pCliente){
        boolean exito = false;
        Conexion conn = new Conexion();
        try{
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call registrarcliente(?,?,?,?,?,?,?,?,?,?,?,?) }");
                cmd.setString(1, pCliente.getNombre());
                cmd.setString(2, pCliente.getApellidoPaterno());
                cmd.setString(3, pCliente.getApellidoMaterno());
                cmd.setString(4, pCliente.getDui());
                cmd.setString(5, pCliente.getNit());
                cmd.setString(6, pCliente.getSexo());
                PGobject nacimiento = new PGobject();
                nacimiento.setType("date");
                nacimiento.setValue(pCliente.getNacimiento().toString());
                cmd.setObject(7, nacimiento);
                cmd.setString(8, pCliente.getDireccion());
                JSONArray jsona = new JSONArray();
                pCliente.getTelefono().forEach(datos -> {
                    JSONObject jsono = new JSONObject();
                    jsono.put("num", datos.getNumero());
                    jsono.put("tip", datos.getTipo());
                    jsona.put(jsono);
                });
                PGobject telefonos = new PGobject();
                telefonos.setType("json");
                telefonos.setValue(jsona.toString());
                cmd.setString(9, pCliente.getEmail());
                cmd.setBoolean(10, pCliente.isEstado());
                cmd.setInt(11, pCliente.getMunicipio().getId());
                cmd.setObject(12, telefonos);
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
//                cmd.setString(11, pCliente.getTelefono());
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
