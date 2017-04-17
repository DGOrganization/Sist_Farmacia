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
public class Compra {
    private long _id;
    private String _numFactura;
    private java.sql.Date _fecha;
    private String _descripcion;
    private BigDecimal _total;
    private Empleado _empleado;
    private Proveedor _proveedor;
    private List<DetalleCompra> _detalle;

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public String getNumFactura() {
        return _numFactura;
    }

    public void setNumFactura(String _numFactura) {
        this._numFactura = _numFactura;
    }

    public java.sql.Date getFecha() {
        return _fecha;
    }

    public void setFecha(java.sql.Date _fecha) {
        this._fecha = _fecha;
    }

    public String getDescripcion() {
        return _descripcion;
    }

    public void setDescripcion(String _descripcion) {
        this._descripcion = _descripcion;
    }

    public BigDecimal getTotal() {
        return _total;
    }

    public void setTotal(BigDecimal _total) {
        this._total = _total;
    }

    public Empleado getEmpleado() {
        return _empleado;
    }

    public void setEmpleado(Empleado _empleado) {
        this._empleado = _empleado;
    }

    public Proveedor getProveedor() {
        return _proveedor;
    }

    public void setProveedor(Proveedor _proveedor) {
        this._proveedor = _proveedor;
    }

    public List<DetalleCompra> getDetalle() {
        return _detalle;
    }

    public void setDetalle(List<DetalleCompra> _detalle) {
        this._detalle = _detalle;
    }

    public Compra() {
    }

    public Compra(long _id, String _numFactura, Date _fecha, String _descripcion, BigDecimal _total, Empleado _empleado, Proveedor _proveedor, List<DetalleCompra> _detalle) {
        this._id = _id;
        this._numFactura = _numFactura;
        this._fecha = _fecha;
        this._descripcion = _descripcion;
        this._total = _total;
        this._empleado = _empleado;
        this._proveedor = _proveedor;
        this._detalle = _detalle;
    }

    @Override
    public String toString() {
        return _numFactura + " " + _fecha;
    }
    
    
}
