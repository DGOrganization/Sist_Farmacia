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
public class Bodega {
    private int _id;
    private String _nombre;
    private String _direccion;
    private boolean _estado;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getNombre() {
        return _nombre;
    }

    public void setNombre(String _nombre) {
        this._nombre = _nombre;
    }

    public String getDireccion() {
        return _direccion;
    }

    public void setDireccion(String _direccion) {
        this._direccion = _direccion;
    }

    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Bodega() {
    }

    public Bodega(int _id) {
        this._id = _id;
    }

    public Bodega(int _id, String _nombre, String _direccion, boolean _estado) {
        this._id = _id;
        this._nombre = _nombre;
        this._direccion = _direccion;
        this._estado = _estado;
    }

    @Override
    public String toString() {
        return _nombre;
    }
    
}
