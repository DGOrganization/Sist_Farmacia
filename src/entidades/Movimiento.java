/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author dakrpastiursSennin
 */
public class Movimiento {
    private long _id;
    private BigDecimal _cantidad;
    private String _comentario;
    private Inventario _inventario;
    private Timestamp _fecha;
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

    public Timestamp getFecha() {
        return _fecha;
    }

    public void setFecha(Timestamp _fecha) {
        this._fecha = _fecha;
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

}
