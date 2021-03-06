/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import controlador.Articulo_controlador;
import controlador.Bodega_controlador;
import controlador.Categoria_controlador;
import entidades.Articulo;
import entidades.Bodega;
import entidades.Categoria;
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
public class Compatible_modelo {

    public List<Inventario> ListarCompatibles(Inventario pInventario) {
        List<Inventario> lista = new ArrayList<>();
        try (
                java.sql.Connection conn = new Conexion().getConnection();
                CallableStatement cmd = conn.prepareCall("{ call obtenercompatibles(?) }")) {
            cmd.setInt(1, pInventario.getId());
            if (cmd.execute()) {
                try (ResultSet resultado = cmd.getResultSet()) {
                    while (resultado.next()) {
                        Inventario inventario = new Inventario();
                        inventario.setId(resultado.getInt("codigo"));
                        inventario.setArticulo(new Articulo_controlador().Obtener(new Articulo(resultado.getInt("articulo"))));
                        inventario.setCategoria(new Categoria_controlador().Obtener(new Categoria(resultado.getInt("categoria"))));
                        inventario.setUnidad(new Unidad_modelo().ListarUnidad(new Unidad(resultado.getInt("unidad"))));
                        inventario.setEstado(resultado.getBoolean("est"));
                        inventario.setStock(resultado.getBigDecimal("stck"));
                        inventario.setStockMin(resultado.getInt("stckmin"));
                        inventario.setStockMax(resultado.getInt("stckmax"));
                        inventario.setPrecio(new Precio_modelo().ListarPrecioInv(inventario));
                        inventario.setVencimiento(resultado.getDate("vencimiento"));
                        inventario.setBodega(new Bodega_controlador().Obtener(new Bodega(resultado.getInt("bodega"))));
                        inventario.setImagen(new Imagen_modelo().ListarImagen(inventario));
                        lista.add(inventario);
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
}
