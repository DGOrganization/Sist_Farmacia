/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Compra;
import java.sql.CallableStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

/**
 *
 * @author dakrpastiursSennin
 */
public class Compra_modelo {

    public boolean Registrar(Compra pCompra) {
        boolean exito = false;
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call registrarcompra(?,?,?,?,?,?) }")) {
            cmd.setString(1, pCompra.getNumFactura());
            cmd.setString(2, pCompra.getDescripcion());
            cmd.setBigDecimal(3, pCompra.getTotal());
            cmd.setInt(4, pCompra.getEmpleado().getId());
            cmd.setInt(5, pCompra.getProveedor().getId());
            JSONArray detalles_c = new JSONArray();
            pCompra.getDetalle().stream().forEach(detalle -> {
                JSONObject detalle_c = new JSONObject();
                detalle_c.put("idinv", detalle.getInventario().getId());
                detalle_c.put("cant", detalle.getCantidad());
                detalle_c.put("unidad", detalle.getInventario().getUnidad().getId());
                detalle_c.put("precio", detalle.getPrecio());
                detalle_c.put("imp", detalle.getImporte());
                detalles_c.put(detalle_c);
            });
            PGobject detalles = new PGobject();
            detalles.setType("json");
            detalles.setValue(detalles_c.toString());
            cmd.setObject(6, detalles);
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