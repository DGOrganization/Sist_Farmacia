/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import configuracion.Gestionar;
import entidades.Inventario;
import entidades.Articulo;
import entidades.Bodega;
import entidades.Categoria;
import entidades.Unidad;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

/**
 *
 * @author dakrpastiursSennin
 */
public class Inventario_modelo {

    public List<Inventario> ListarInventarioCompleto() {
        List<Inventario> lista = new ArrayList<>();
        Conexion conn = new Conexion();
        try {
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerinventarios() }");
                if (cmd.execute()) {
                    ResultSet resultado = cmd.getResultSet();
                    while (resultado.next()) {
                        Inventario inventario = new Inventario();
                        inventario.setId(resultado.getInt("codigo"));
                        inventario.setArticulo(new Articulo_modelo().ListarArticulo(new Articulo(resultado.getInt("articulo"))));
                        inventario.setCategoria(new Categoria_modelo().ListarCategoria(new Categoria(resultado.getInt("categoria"))));
                        inventario.setUnidad(new Unidad_modelo().ListarUnidad(new Unidad(resultado.getInt("unidad"))));
                        inventario.setEstado(resultado.getBoolean("est"));
                        inventario.setStock(resultado.getBigDecimal("stck"));
                        inventario.setStockMin(resultado.getInt("stckmin"));
                        inventario.setStockMax(resultado.getInt("stckmax"));
                        inventario.setPrecio(new Precio_modelo().ListarPrecioInv(inventario));
                        inventario.setVencimiento(resultado.getDate("vencimiento"));
                        inventario.setBodega(new Bodega_modelo().ListarBodega(new Bodega(resultado.getInt("bodega"))));
                        inventario.setImagen(new Imagen_modelo().ListarImagen(inventario));
                        inventario.setCompatibles(new Compatible_modelo().ListarCompatibles(inventario));
                        inventario.setCodigoInterno(resultado.getString("intcod"));
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
        } finally {
            conn.Desconectar();
        }

        return lista;
    }
    
    public Inventario ListarInventario(Inventario pInventario) {
        Inventario inventario = new Inventario();
        Conexion conn = new Conexion();
        try {
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call obtenerinventario(?) }");
                cmd.setInt(1, pInventario.getId());
                if (cmd.execute()) {
                    ResultSet resultado = cmd.getResultSet();
                    while (resultado.next()) {
                        inventario.setId(resultado.getInt("codigo"));
                        inventario.setArticulo(new Articulo_modelo().ListarArticulo(new Articulo(resultado.getInt("articulo"))));
                        inventario.setCategoria(new Categoria_modelo().ListarCategoria(new Categoria(resultado.getInt("categoria"))));
                        inventario.setUnidad(new Unidad_modelo().ListarUnidad(new Unidad(resultado.getInt("unidad"))));
                        inventario.setEstado(resultado.getBoolean("est"));
                        inventario.setStock(resultado.getBigDecimal("stck"));
                        inventario.setStockMin(resultado.getInt("stckmin"));
                        inventario.setStockMax(resultado.getInt("stckmax"));
                        inventario.setPrecio(new Precio_modelo().ListarPrecioInv(inventario));
                        inventario.setVencimiento(resultado.getDate("vencimiento"));
                        inventario.setBodega(new Bodega_modelo().ListarBodega(new Bodega(resultado.getInt("bodega"))));
                        inventario.setImagen(new Imagen_modelo().ListarImagen(inventario));
                        inventario.setCompatibles(new Compatible_modelo().ListarCompatibles(inventario));
                        inventario.setCodigoInterno(resultado.getString("intcod"));
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
        } finally {
            conn.Desconectar();
        }

        return inventario;
    }

    public boolean Registrar(Inventario pInventario) {
        boolean exito = false;
        Conexion conn = new Conexion();
        try {
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call registrarinventario(?,?,?,?,?,?,?,?,?,?,?) }");
                 if(pInventario.getArticulo().getDescripcion().isEmpty()){
                    cmd.setNull(2, java.sql.Types.LONGNVARCHAR);
                } else {                    
                    cmd.setString(2, pInventario.getArticulo().getDescripcion());
                }
                cmd.setInt(3, pInventario.getUnidad().getId());
                cmd.setInt(4, pInventario.getCategoria().getId());
                cmd.setInt(5, pInventario.getBodega().getId());
                if(pInventario.getStockMin() == 0){
                    cmd.setNull(6, java.sql.Types.INTEGER);
                } else {                    
                    cmd.setInt(6, pInventario.getStockMin());
                }
                if(pInventario.getStockMax() == 0){
                    cmd.setNull(7, java.sql.Types.INTEGER);
                } else {                    
                    cmd.setInt(7, pInventario.getStockMax());
                }
                JSONArray precios = new JSONArray();
                pInventario.getPrecio().stream().forEach(datos -> {
                    JSONObject precio = new JSONObject();
                    precio.put("cant", datos.getCantidad());
                    precios.put(precio);
                });
                PGobject invprecios = new PGobject();
                invprecios.setType("json");
                invprecios.setValue(precios.toString());
                cmd.setObject(9, invprecios);
                if (pInventario.getImagen().getUrl().isEmpty()) {
                    cmd.setNull(10, java.sql.Types.LONGVARCHAR);
                } else {
                    cmd.setString(10, pInventario.getImagen().getUrl());
                }
                if(pInventario.getCompatibles().isEmpty()){
                    cmd.setNull(11, java.sql.Types.LONGVARCHAR, "json");
                } else {
                    JSONArray compatibles = new JSONArray();
                    pInventario.getCompatibles().stream().forEach(datos -> {
                        JSONObject compatible = new JSONObject();
                        compatible.put("codcomp", datos.getId());
                        compatible.put("est", datos.isEstado());
                        compatibles.put(compatible);
                    });
                    PGobject invcompatible = new PGobject();
                    invcompatible.setType("json");
                    invcompatible.setValue(compatibles.toString());
                    cmd.setObject(11, invcompatible);
                }
                exito = cmd.execute();
            }
        } catch (SQLException ex) {
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
        return exito;
    }

    public boolean Editar(Inventario pInventario) {
        boolean exito = false;
        Conexion conn = new Conexion();
        try {
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call editarinventario(?,?,?,?,?,?,?,?,?,?,?,?) }");
                cmd.setString(1, pInventario.getArticulo().getNombre());
                if(pInventario.getArticulo().getDescripcion().isEmpty()){
                    cmd.setNull(2, java.sql.Types.LONGNVARCHAR);
                } else {                    
                    cmd.setString(2, pInventario.getArticulo().getDescripcion());
                }
                cmd.setInt(3, pInventario.getUnidad().getId());
                cmd.setInt(4, pInventario.getCategoria().getId());
                cmd.setInt(5, pInventario.getBodega().getId());
                if(pInventario.getStockMin() == 0){
                    cmd.setNull(6, java.sql.Types.INTEGER);
                } else {                    
                    cmd.setInt(6, pInventario.getStockMin());
                }
                if(pInventario.getStockMax() == 0){
                    cmd.setNull(7, java.sql.Types.INTEGER);
                } else {                    
                    cmd.setInt(7, pInventario.getStockMax());
                }
                cmd.setDate(8, pInventario.getVencimiento());
                JSONArray precios = new JSONArray();
                pInventario.getPrecio().stream().forEach(datos -> {
                    JSONObject precio = new JSONObject();
                    precio.put("idp", datos.getId());
                    precio.put("cant", datos.getCantidad().toString());
                    precios.put(precio);
                });
                PGobject invprecios = new PGobject();
                invprecios.setType("json");
                invprecios.setValue(precios.toString());
                cmd.setObject(9, invprecios);
                if (pInventario.getImagen().getUrl() == null || pInventario.getImagen().getUrl().isEmpty()) {
                    cmd.setNull(10, java.sql.Types.LONGVARCHAR);
                } else {
                    cmd.setString(10, pInventario.getImagen().getUrl());
                }
                cmd.setInt(11, pInventario.getId());
                
                if(pInventario.getCompatibles().isEmpty()){
                    cmd.setNull(12, java.sql.Types.LONGVARCHAR, "json");
                } else {
                    JSONArray compatibles = new JSONArray();
                    pInventario.getCompatibles().stream().forEach(datos -> {
                        JSONObject compatible = new JSONObject();
                        compatible.put("codcomp", datos.getId());
                        compatible.put("est", datos.isEstado());
                        compatibles.put(compatible);
                    });
                    System.out.println("Compatibles == " + compatibles.toString());
                    PGobject invcompatible = new PGobject();
                    invcompatible.setType("json");
                    invcompatible.setValue(compatibles.toString());
                    cmd.setObject(12, invcompatible);
                }
                System.out.println("Query == " + cmd.toString());
                exito = cmd.execute();
            }
        } catch (SQLException ex) {
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
        return exito;
    }

    public boolean CambiarStock(Inventario pInventario){
        boolean exito = false;
        Conexion conn = new Conexion();
        try {
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call editarstockinventario(?,?) }");
                cmd.setInt(1, pInventario.getId());
                cmd.setBigDecimal(2, pInventario.getStock());
                exito = cmd.execute();
            }
        } catch (SQLException ex) {
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
        return exito;
    }
    
    public boolean Eliminar(Inventario pInventario){
        boolean exito = false;
        Conexion conn = new Conexion();
        try {
            if (conn.Conectar()) {
                CallableStatement cmd = conn.getConnection().prepareCall("{ call eliminarinventario(?) }");                
                cmd.setInt(1, pInventario.getId());
                exito = cmd.execute();
            }
        } catch (SQLException ex) {
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
        return exito;
    }
}
