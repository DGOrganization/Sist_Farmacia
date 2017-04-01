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
    private BigDecimal _stockMin;
    private BigDecimal _stockMax;
    private List<Precio> _precio;
    private java.sql.Date _vencimiento;
    private boolean _estado;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public Articulo getProducto() {
        return _articulo;
    }

    public void setProducto(Articulo _articulo) {
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

    public BigDecimal getStockMin() {
        return _stockMin;
    }

    public void setStockMin(BigDecimal _stockMin) {
        this._stockMin = _stockMin;
    }

    public BigDecimal getStockMax() {
        return _stockMax;
    }

    public void setStockMax(BigDecimal _stockMax) {
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

    public void setVencimiento(java.sql.Date _vencimiento) {
        this._vencimiento = _vencimiento;
    }
    
    public void setVencimiento(java.util.Date _vencimiento) {
        this._vencimiento = new java.sql.Date(_vencimiento.getTime());
    }
    
    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Inventario() {
    }

    public Inventario(int _id, Articulo _articulo, Categoria _categoria, Bodega _bodega, Unidad _unidad, BigDecimal _stock, BigDecimal _stockMin, BigDecimal _stockMax, List<Precio> _precio, Date _vencimiento, boolean _estado) {
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
        this._estado = _estado;
    }

    public Inventario(int _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return _categoria + " " + _articulo;
    }
    
}
