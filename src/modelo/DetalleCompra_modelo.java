/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Compra;
import entidades.DetalleCompra;
import entidades.Inventario;
import entidades.Unidad;
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
public class DetalleCompra_modelo {
    
    public List<DetalleCompra> ObtenerDetalle(Compra pCompra){
        List<DetalleCompra> lista = new ArrayList<>();
        Conexion conn = new Conexion();
        try{
            if(conn.Conectar()){
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerdetallescompra(?) }");
                cmd.setLong(1, pCompra.getId());
                if(cmd.execute()){
                    ResultSet _resultado = cmd.getResultSet();
                    while(_resultado.next()){
                        DetalleCompra detalle = new DetalleCompra();
                        detalle.setInventario(new Inventario_modelo().ListarInventario(new Inventario(_resultado.getInt("codinv"))));
                        detalle.setCantidad(_resultado.getBigDecimal("cant"));
                        detalle.setUnidad(new Unidad_modelo().ListarUnidad(new Unidad(_resultado.getInt("unidad"))));
                        detalle.setPrecio(_resultado.getBigDecimal("precio"));
                        detalle.setImporte(_resultado.getBigDecimal("imp"));
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
