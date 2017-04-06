/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Venta;
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
public class Venta_modelo {
    
    public boolean Registrar(Venta pVenta){
        boolean exito = false;
        Conexion conn = new Conexion();
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call registrarventa(?,?,?,?,?,?,?) }");
                cmd.setInt(1, pVenta.getCliente().getId());
                cmd.setInt(2, pVenta.getEmpleado().getId());
                cmd.setBigDecimal(3, pVenta.getTotal());
                cmd.setBigDecimal(4, pVenta.getCambio());
                cmd.setString(5, pVenta.getObservacion());
                cmd.setString(6, pVenta.getLetras());
                JSONArray detalles_v = new JSONArray();
                pVenta.getDetalle().stream().forEach(detalle ->{
                    JSONObject detalle_v = new JSONObject();
                    detalle_v.put("idinv", detalle.getInventario().getId());
                    detalle_v.put("cant", detalle.getCantidad());
                    detalle_v.put("unidad", detalle.getInventario().getUnidad().getId());
                    detalle_v.put("precio", detalle.getPrecio());
                    detalle_v.put("importe", detalle.getImporte());
                    detalle_v.put("descpor", detalle.getDescuento());
                    detalles_v.put(detalle_v);
                });
                PGobject detalles = new PGobject();
                detalles.setType("json");
                detalles.setValue(detalles_v.toString());
                cmd.setObject(7, detalles);
                exito = cmd.execute();
            }
        } catch (SQLException ex){
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
