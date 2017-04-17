/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author dakrpastiursSennin
 */
public class Inventario {
    
    private int _id;
    private Articulo _articulo;
    private Categoria _categoria;
    private Bodega _bodega;    
    private Unidad _unidad;
    private BigDecimal _stock;
    private int _stockMin;
    private int _stockMax;
    private List<Precio> _precio;
    private java.sql.Date _vencimiento;
    private Imagen _imagen;
    private List<Inventario> _compatibles;
    private boolean _estado;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public Articulo getArticulo() {
        return _articulo;
    }

    public void setArticulo(Articulo _articulo) {
        this._articulo = _articulo;
    }

    public Categoria getCategoria() {
        return _categoria;
    }

    public void setCategoria(Categoria _categoria) {
        this._categoria = _categoria;
    }

    public Bodega getBodega() {
        return _bodega;
    }

    public void setBodega(Bodega _bodega) {
        this._bodega = _bodega;
    }

    public Unidad getUnidad() {
        return _unidad;
    }

    public void setUnidad(Unidad _unidad) {
        this._unidad = _unidad;
    }

    public BigDecimal getStock() {
        return _stock;
    }

    public void setStock(BigDecimal _stock) {
        this._stock = _stock;
    }

    public int getStockMin() {
        return _stockMin;
    }

    public void setStockMin(int _stockMin) {
        this._stockMin = _stockMin;
    }

    public int getStockMax() {
        return _stockMax;
    }

    public void setStockMax(int _stockMax) {
        this._stockMax = _stockMax;
    }

    public List<Precio> getPrecio() {
        return _precio;
    }

    public void setPrecio(List<Precio> _precio) {
        this._precio = _precio;
    }

    public java.sql.Date getVencimiento() {
        return _vencimiento;
    }

    public Imagen getImagen() {
        return _imagen;
    }

    public void setImagen(Imagen _imagen) {
        this._imagen = _imagen;
    }
    
    public void setVencimiento(java.sql.Date _vencimiento) {
        this._vencimiento = _vencimiento;
    }
    
    public void setVencimiento(java.util.Date _vencimiento) {
        this._vencimiento = new java.sql.Date(_vencimiento.getTime());
    }
    
    public boolean isEstado() {
        return _estado;
    }

    public List<Inventario> getCompatibles() {
        return _compatibles;
    }

    public void setCompatibles(List<Inventario> _compatibles) {
        this._compatibles = _compatibles;
    }
    
    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Inventario() {
    }

    public Inventario(int _id) {
        this._id = _id;
    }

    public Inventario(int _id, Articulo _articulo, Categoria _categoria, Bodega _bodega, Unidad _unidad, BigDecimal _stock, int _stockMin, int _stockMax, List<Precio> _precio, Date _vencimiento, Imagen _imagen, boolean _estado) {
        this._id = _id;
        this._articulo = _articulo;
        this._categoria = _categoria;
        this._bodega = _bodega;
        this._unidad = _unidad;
        this._stock = _stock;
        this._stockMin = _stockMin;
        this._stockMax = _stockMax;
        this._precio = _precio;
        this._vencimiento = _vencimiento;
        this._imagen = _imagen;
        this._estado = _estado;
    }
    
    @Override
    public String toString() {
        return _categoria + " " + _articulo;
    }
    
}
