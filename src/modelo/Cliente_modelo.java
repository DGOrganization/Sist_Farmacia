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
            cmd.setString(4, pCliente.getDui());
            cmd.setString(5, pCliente.getNit());
            cmd.setString(6, pCliente.getSexo());
            cmd.setObject(7, pCliente.getNacimiento());
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
            cmd.setString(5, pCliente.getDui());
            cmd.setString(6, pCliente.getNit());
            cmd.setString(7, pCliente.getSexo());
            cmd.setDate(8, pCliente.getNacimiento());
            cmd.setString(9, pCliente.getDireccion());
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
            cmd.setString(10, pCliente.getEmail());
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
