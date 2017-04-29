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
import controlador.Municipio_controlador;
import controlador.Telefono_controlador;
import entidades.Cliente;
import entidades.Persona;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

public class Cliente_modelo {

    public List<Cliente> ListarClientes() {
        List<Cliente> lista = new ArrayList<>();
        try (
                Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenerclientes() }")) {
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        Cliente cliente = new Cliente();
                        cliente.setId(resultado.getInt("codigo"));
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
                        cliente.setMunicipio(new Municipio_controlador().Obtener(new Persona(resultado.getInt("codpersona"))));
                        cliente.setTelefono(new Telefono_controlador().Obtener(new Persona(resultado.getInt("codpersona"))));
                        lista.add(cliente);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre") + " - " + this.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return lista;
    }

    public Cliente ListarCliente(Cliente pCliente) {
        Cliente cliente = new Cliente();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenercliente(?) }")) {
            cmd.setInt(1, pCliente.getId());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        cliente.setId(resultado.getInt("codigo"));
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
                        cliente.setMunicipio(new Municipio_controlador().Obtener(new Persona(resultado.getInt("codpersona"))));
                        cliente.setTelefono(new Telefono_controlador().Obtener(new Persona(resultado.getInt("codpersona"))));
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre") + " - " + this.getClass().getName(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return cliente;
    }

    public boolean Registrar(Cliente pCliente) {
        boolean exito = false;
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call registrarcliente(?,?,?,?,?,?,?,?,?,?,?) }")) {
            cmd.setString(1, pCliente.getNombre());
            cmd.setString(2, pCliente.getApellidoPaterno());
            cmd.setString(3, pCliente.getApellidoMaterno());
            if(!pCliente.getDui().isEmpty()){
                cmd.setString(4, pCliente.getDui());
            } else {
                cmd.setNull(4, Types.VARCHAR);
            }
            if(!pCliente.getNit().isEmpty()){
                cmd.setString(5, pCliente.getNit());
            } else {
                cmd.setNull(5, Types.VARCHAR);
            }
            cmd.setString(6, pCliente.getSexo());
            if(!(pCliente.getNacimiento() == null)){
                cmd.setDate(7, pCliente.getNacimiento());
            } else {
                cmd.setNull(7, Types.DATE);
            }
            if(!pCliente.getDireccion().isEmpty()){
                cmd.setString(8, pCliente.getDireccion());
            } else {
                cmd.setString(8, null);
            }
            JSONArray jsona = new JSONArray();
            pCliente.getTelefono().forEach(datos -> {
                JSONObject jsono = new JSONObject();
                jsono.put("num", datos.getNumero().isEmpty() ? null : datos.getNumero());
                jsono.put("tip", datos.getTipo());
                jsona.put(jsono);
            });
            PGobject telefonos = new PGobject();
            telefonos.setType("json");
            telefonos.setValue(jsona.toString());
            if(!pCliente.getEmail().isEmpty()){
                cmd.setString(9, pCliente.getEmail());
            } else {
                 cmd.setNull(9, Types.VARCHAR);
            }
            cmd.setInt(10, pCliente.getMunicipio().getId());
            cmd.setObject(11, telefonos);
            exito = cmd.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
            System.out.println("Eror == " + ex.getMessage());
            for (int i = 0; i < ex.getStackTrace().length; i++) {
                System.err.println("ERROR: " + ex.getStackTrace()[i]);
            }
        }
        return exito;
    }

    public boolean Editar(Cliente pCliente) {
        boolean exito = false;
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call editarcliente(?,?,?,?,?,?,?,?,?,?,?,?) }")) {
            cmd.setInt(1, pCliente.getId());
            cmd.setString(2, pCliente.getNombre());
            cmd.setString(3, pCliente.getApellidoPaterno());
            cmd.setString(4, pCliente.getApellidoMaterno());
            if(!pCliente.getDui().isEmpty()){
                cmd.setString(5, pCliente.getDui());
            } else {
                cmd.setNull(5, Types.VARCHAR);
            }
            if(!pCliente.getNit().isEmpty()){
                cmd.setString(6, pCliente.getNit());
            } else {
                cmd.setNull(6, Types.VARCHAR);
            }
            cmd.setString(7, pCliente.getSexo());
            if(!pCliente.getNacimiento().toString().isEmpty()){
                cmd.setDate(8, pCliente.getNacimiento());
            } else {
                cmd.setNull(8, Types.VARCHAR);
            }
            if(!pCliente.getDireccion().isEmpty()){
                cmd.setString(9, pCliente.getDireccion());
            } else {
                cmd.setNull(8, Types.LONGNVARCHAR);
            }
            JSONArray jsona = new JSONArray();
            pCliente.getTelefono().forEach(datos -> {
                JSONObject jsono = new JSONObject();
                jsono.put("num", datos.getNumero().isEmpty() ? null : datos.getNumero());
                jsono.put("tip", datos.getTipo());
                jsona.put(jsono);
            });
            PGobject telefonos = new PGobject();
            telefonos.setType("json");
            telefonos.setValue(jsona.toString());
            if(!pCliente.getEmail().isEmpty()){
                cmd.setString(10, pCliente.getEmail());
            } else {
                 cmd.setNull(10, Types.VARCHAR);
            }
            cmd.setInt(11, pCliente.getMunicipio().getId());
            cmd.setObject(12, telefonos);
            exito = cmd.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return exito;
    }

    public boolean Eliminar(Cliente pCliente) {
        boolean exito = false;
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call eliminarcliente(?) }")) {
            cmd.setInt(1, pCliente.getId());
            exito = cmd.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se han cargado datos debido al error: \n" + ex.getMessage()
                    + "\nFavor contacte al desarrollador",
                    new Gestionar().Leer("Empresa", "nombre"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
        return exito;
    }
}
