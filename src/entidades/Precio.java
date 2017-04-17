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
public class Precio {
    private int _id;
    private BigDecimal _cantidad;
    private String _tipo;
    private boolean _estado;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public BigDecimal getCantidad() {
        return _cantidad;
    }

    public void setCantidad(BigDecimal _cantidad) {
        this._cantidad = _cantidad;
    }

    public String getTipo() {
        return _tipo;
    }

    public void setTipo(String _tipo) {
        this._tipo = _tipo;
    }
    
    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Precio() {
    }

    public Precio(int _id) {
        this._id = _id;
    }

    public Precio(int _id, BigDecimal _cantidad, String _tipo, boolean _estado) {
        this._id = _id;
        this._cantidad = _cantidad;
        this._tipo = _tipo;
        this._estado = _estado;
    }

    @Override
    public String toString() {
        return _tipo + " " +_cantidad.toString();
    }
    
}
