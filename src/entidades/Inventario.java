/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.math.BigDecimal;

/**
 *
 * @author dakrpastiursSennin
 */
public class Inventario {
    
    private long _id;
    private Producto _producto;
    private Marca _marca;
    private Categoria _categoria;
    private Bodega _bodega;    
    private Unidad _unidad;
    private BigDecimal _stock;
    private BigDecimal _stockMin;
    private BigDecimal _stockMax;
    private BigDecimal _precio;
    private boolean _estado;

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public Producto getProducto() {
        return _producto;
    }

    public void setProducto(Producto _producto) {
        this._producto = _producto;
    }

    public Marca getMarca() {
        return _marca;
    }

    public void setMarca(Marca _marca) {
        this._marca = _marca;
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

    public BigDecimal getPrecio() {
        return _precio;
    }

    public void setPrecio(BigDecimal _precio) {
        this._precio = _precio;
    }

    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Inventario() {
    }

    public Inventario(long _id, Producto _producto, Marca _marca, Categoria _categoria, Bodega _bodega, Unidad _unidad, BigDecimal _stock, BigDecimal _stockMin, BigDecimal _stockMax, BigDecimal _precio, boolean _estado) {
        this._id = _id;
        this._producto = _producto;
        this._marca = _marca;
        this._categoria = _categoria;
        this._bodega = _bodega;
        this._unidad = _unidad;
        this._stock = _stock;
        this._stockMin = _stockMin;
        this._stockMax = _stockMax;
        this._precio = _precio;
        this._estado = _estado;
    }

    @Override
    public String toString() {
        return _producto + " " + _marca;
    }
    
}
