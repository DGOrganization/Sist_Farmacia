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
public class Marca {
    private long _id;
    private String _nombre;
    private boolean _estado;

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public String getNombre() {
        return _nombre;
    }

    public void setNombre(String _nombre) {
        this._nombre = _nombre;
    }

    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Marca() {
    }

    public Marca(long _id, String _nombre, boolean _estado) {
        this._id = _id;
        this._nombre = _nombre;
        this._estado = _estado;
    }

    @Override
    public String toString() {
        return _nombre;
    }
    
}
