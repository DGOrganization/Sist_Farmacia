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
public class DetalleVenta {
    private Inventario _inventario;
    private BigDecimal _cantidad;
    private Unidad _unidad;
    private BigDecimal _precio;
    private BigDecimal _importe;
    private int _descuento;
    private BigDecimal _descuentoTotal;

    public Inventario getInventario() {
        return _inventario;
    }

    public void setInventario(Inventario _inventario) {
        this._inventario = _inventario;
    }

    public BigDecimal getCantidad() {
        return _cantidad;
    }

    public void setCantidad(BigDecimal _cantidad) {
        this._cantidad = _cantidad;
    }

    public Unidad getUnidad() {
        return _unidad;
    }

    public void setUnidad(Unidad _unidad) {
        this._unidad = _unidad;
    }

    public BigDecimal getPrecio() {
        return _precio;
    }

    public void setPrecio(BigDecimal _precio) {
        this._precio = _precio;
    }

    public BigDecimal getImporte() {
        return _importe;
    }

    public void setImporte(BigDecimal _importe) {
        this._importe = _importe;
    }

    public int getDescuento() {
        return _descuento;
    }

    public void setDescuento(int _descuento) {
        this._descuento = _descuento;
    }

    public BigDecimal getDescuentoTotal() {
        return _descuentoTotal;
    }

    public void setDescuentoTotal(BigDecimal _descuentoTotal) {
        this._descuentoTotal = _descuentoTotal;
    }

    public DetalleVenta() {
    }

    public DetalleVenta(Inventario _inventario, BigDecimal _cantidad, Unidad _unidad, BigDecimal _precio, BigDecimal _importe, int _descuento, BigDecimal _descuentoTotal) {
        this._inventario = _inventario;
        this._cantidad = _cantidad;
        this._unidad = _unidad;
        this._precio = _precio;
        this._importe = _importe;
        this._descuento = _descuento;
        this._descuentoTotal = _descuentoTotal;
    }
    
}
