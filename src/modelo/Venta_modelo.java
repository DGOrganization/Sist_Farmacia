/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import controlador.Cliente_controlador;
import entidades.Cliente;
import entidades.Empleado;
import entidades.Venta;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

/**
 *
 * @author dakrpastiursSennin
 */
public class Venta_modelo {

    public boolean Registrar(Venta pVenta) {
        boolean exito = false;
        try (
            java.sql.Connection conn = new Conexion().getConnection();
            CallableStatement cmd = conn.prepareCall("{ call registrarventa(?,?,?,?,?,?,?,?,?,?) }")){
            cmd.setInt(1, pVenta.getCliente().getId());
            cmd.setInt(2, pVenta.getEmpleado().getId());
            cmd.setBigDecimal(3, pVenta.getTotal());
            cmd.setBigDecimal(4, pVenta.getCambio());
            if (pVenta.getObservacion().isEmpty()) {
                cmd.setNull(5, java.sql.Types.LONGVARCHAR);
            } else {
                cmd.setString(5, pVenta.getObservacion());
            }
            cmd.setString(6, pVenta.getLetras());
            JSONArray detalles_v = new JSONArray();
            pVenta.getDetalle().stream().forEach(detalle -> {
                JSONObject detalle_v = new JSONObject();
                detalle_v.put("idinv", detalle.getInventario().getId());
                detalle_v.put("cant", detalle.getCantidad());
                detalle_v.put("unidad", detalle.getInventario().getUnidad().getId());
                detalle_v.put("precio", detalle.getPrecio());
                detalle_v.put("imp", detalle.getImporte());
                detalle_v.put("descpor", detalle.getDescuento());
                detalles_v.put(detalle_v);
            });
            PGobject detalles = new PGobject();
            detalles.setType("json");
            detalles.setValue(detalles_v.toString());
            cmd.setObject(7, detalles);

            if (pVenta.getSubtotal().compareTo(BigDecimal.ZERO) == 0) {
                cmd.setNull(8, java.sql.Types.NUMERIC);
            } else {
                cmd.setBigDecimal(8, pVenta.getSubtotal());
            }

            if (pVenta.getIva().compareTo(BigDecimal.ZERO) == 0) {
                cmd.setNull(9, java.sql.Types.NUMERIC);
            } else {
                cmd.setBigDecimal(9, pVenta.getIva());
            }

            if (pVenta.getNFactura().isEmpty()) {
                cmd.setNull(10, java.sql.Types.VARCHAR);
            } else {
                cmd.setString(10, pVenta.getNFactura());
            }
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

    public List<Venta> ListarVentas() {
        List<Venta> lista = new ArrayList<>();
        try (
            java.sql.Connection conn = new Conexion().getConnection();
            CallableStatement cmd = conn.prepareCall("{ call obtenerventas() }")){
            if (cmd.execute()) {
                try(ResultSet resultado = cmd.getResultSet()){
                while (resultado.next()) {
                    Venta venta = new Venta();
                    venta.setId(resultado.getLong("cod"));
                    venta.setCliente(new Cliente_controlador().Obtener(new Cliente(resultado.getInt("cliente"))));
                    venta.setEmpleado(new Empleado_modelo().ListarEmpleado(new Empleado(resultado.getInt("empleado"))));
                    venta.setTotal(resultado.getBigDecimal("tot"));
                    venta.setCambio(resultado.getBigDecimal("camb"));
                    venta.setObservacion(resultado.getObject("comentario") == null ? "No hay observaciones" : resultado.getString("comentario"));
                    venta.setLetras(resultado.getString("ltr"));
                    venta.setSubtotal(resultado.getBigDecimal("subtot"));
                    venta.setIva(resultado.getBigDecimal("iva"));
                    venta.setNFactura(resultado.getObject("nfact") == null ? "Ticket" : resultado.getString("nfact"));
                    venta.setFecha(resultado.getTimestamp("fecha"));
                    venta.setEstado(resultado.getBoolean("estado"));
                    venta.setDetalle(new DetalleVenta_modelo().ObtenerDetalleVenta(venta));
                    lista.add(venta);
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

}
