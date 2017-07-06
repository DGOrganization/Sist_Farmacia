/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author dakrpastiursSennin
 */
public class Venta {
    private long _id;
    private String _nFactura;
    private java.sql.Timestamp _fecha;
    private Tipo_Venta _tipo;
    private BigDecimal _subtotal;
    private BigDecimal _iva;
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

    public String getNFactura() {
        return _nFactura;
    }

    public void setNFactura(String _nFactura) {
        this._nFactura = _nFactura;
    }
    
    public java.sql.Timestamp getFecha() {
        return _fecha;
    }

    public void setFecha(java.sql.Timestamp _fecha) {
        this._fecha = _fecha;
    }
    
    public void setFecha(java.util.Date _fecha) {
        this._fecha = new java.sql.Timestamp(_fecha.getTime());
    }
    
    public Tipo_Venta getTipo() {
        return _tipo;
    }

    public void setTipo(Tipo_Venta _tipo) {
        this._tipo = _tipo;
    }
   
    public BigDecimal getSubtotal() {
        return _subtotal;
    }

    public void setSubtotal(BigDecimal _subtotal) {
        this._subtotal = _subtotal;
    }

    public BigDecimal getIva() {
        return _iva;
    }

    public void setIva(BigDecimal _iva) {
        this._iva = _iva;
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

    @Override
    public String toString() {
        return _nFactura;
    }

}
