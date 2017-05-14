/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author dakrpastiursSennin
 */
public class Movimiento {
    private long _id;
    private BigDecimal _cantidad;
    private String _comentario;
    private Inventario _inventario;
    private java.sql.Date _fecha;
    private BigDecimal _total;
    private boolean _estado;

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public BigDecimal getCantidad() {
        return _cantidad;
    }

    public void setCantidad(BigDecimal _cantidad) {
        this._cantidad = _cantidad;
    }

    public String getComentario() {
        return _comentario;
    }

    public void setComentario(String _comentario) {
        this._comentario = _comentario;
    }

    public Inventario getInventario() {
        return _inventario;
    }

    public void setInventario(Inventario _inventario) {
        this._inventario = _inventario;
    }

    public java.sql.Date getFecha() {
        return _fecha;
    }

    public void setFecha(java.sql.Date _fecha) {
        this._fecha = _fecha;
    }

    public BigDecimal getTotal() {
        return _total;
    }

    public void setTotal(BigDecimal _total) {
        this._total = _total;
    }
    
    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Movimiento() {
    }

    public Movimiento(long _id) {
        this._id = _id;
    }

    public Movimiento(long _id, BigDecimal _cantidad, String _comentario, Inventario _inventario, Date _fecha, boolean _estado) {
        this._id = _id;
        this._cantidad = _cantidad;
        this._comentario = _comentario;
        this._inventario = _inventario;
        this._fecha = _fecha;
        this._estado = _estado;
    }

}
