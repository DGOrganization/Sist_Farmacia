/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author dakrpastiursSennin
 */
public class Telefono {
    private long _id;
    private String _numero;
    private String _tipo;
    private boolean _estado;

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public String getNumero() {
        return _numero;
    }

    public void setNumero(String _numero) {
        this._numero = _numero;
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
    
    public Telefono() {
    }

    public Telefono(long _id, String _numero, String _tipo, boolean _estado) {
        this._id = _id;
        this._numero = _numero;
        this._tipo = _tipo;
        this._estado = _estado;
    }

    public Telefono(long _id) {
        this._id = _id;
    }

    public Telefono(String _numero, String _tipo) {
        this._numero = _numero;
        this._tipo = _tipo;
    }
    
    
    
    @Override
    public String toString() {
        return _numero;
    }
    
    
}
