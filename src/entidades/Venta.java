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
public class Venta {
    private long _id;
    private java.sql.Date _fecha;
    private BigDecimal _total;
    private BigDecimal _cambio;
    private String _observacion;
    private Empleado _empleado;
    private Cliente _cliente;
    private String _letras;
    private List<DetalleVenta> _detalle;
    private boolean _estado;

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public java.sql.Date getFecha() {
        return _fecha;
    }

    public void setFecha(java.sql.Date _fecha) {
        this._fecha = _fecha;
    }
    
    public void setFecha(java.util.Date _fecha) {
        this._fecha = new java.sql.Date(_fecha.getTime());
    }

    public BigDecimal getTotal() {
        return _total;
    }

    public void setTotal(BigDecimal _total) {
        this._total = _total;
    }

    public BigDecimal getCambio() {
        return _cambio;
    }

    public void setCambio(BigDecimal _cambio) {
        this._cambio = _cambio;
    }

    public String getObservacion() {
        return _observacion;
    }

    public void setObservacion(String _observacion) {
        this._observacion = _observacion;
    }

    public Empleado getEmpleado() {
        return _empleado;
    }

    public void setEmpleado(Empleado _empleado) {
        this._empleado = _empleado;
    }

    public Cliente getCliente() {
        return _cliente;
    }

    public void setCliente(Cliente _cliente) {
        this._cliente = _cliente;
    }

    public String getLetras() {
        return _letras;
    }

    public void setLetras(String _letras) {
        this._letras = _letras;
    }

    public List<DetalleVenta> getDetalle() {
        return _detalle;
    }

    public void setDetalle(List<DetalleVenta> _detalle) {
        this._detalle = _detalle;
    }

    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Venta() {
    }

    public Venta(long _id, Date _fecha, BigDecimal _total, BigDecimal _cambio, String _observacion, Empleado _empleado, Cliente _cliente, String _letras, List<DetalleVenta> _detalle, boolean _estado) {
        this._id = _id;
        this._fecha = _fecha;
        this._total = _total;
        this._cambio = _cambio;
        this._observacion = _observacion;
        this._empleado = _empleado;
        this._cliente = _cliente;
        this._letras = _letras;
        this._detalle = _detalle;
        this._estado = _estado;
    }
    
}
