/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.DetalleVenta;
import entidades.Inventario;
import entidades.Unidad;
import entidades.Venta;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author dakrpastiursSennin
 */
public class DetalleVenta_modelo {
    
    public List<DetalleVenta> ObtenerDetalleVenta(Venta pVenta){
        List<DetalleVenta> lista = new ArrayList<>();
        Conexion conn = new Conexion();
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerdetallesventa(?) }");
                cmd.setLong(1, pVenta.getId());
                if(cmd.execute()){
                    ResultSet _resultado = cmd.getResultSet();
                    while(_resultado.next()){
                        DetalleVenta detalle = new DetalleVenta();
                        detalle.setInventario(new Inventario_modelo().ListarInventario(new Inventario(_resultado.getInt("codinv"))));
                        detalle.setCantidad(_resultado.getBigDecimal("cant"));
                        detalle.setUnidad(new Unidad_modelo().ListarUnidad(new Unidad(_resultado.getInt("unidad"))));
                        detalle.setPrecio(_resultado.getBigDecimal("precio"));
                        detalle.setImporte(_resultado.getBigDecimal("imp"));
                        detalle.setDescuento(Integer.parseInt(_resultado.getBigDecimal("descpor").toString()));
                        lista.add(detalle);
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
        } finally{
            conn.Desconectar();
        }
        return lista;
    }
    
}
