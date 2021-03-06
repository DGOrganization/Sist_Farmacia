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
public class Articulo {
    private int _id;
    private String _nombre;
    private String _descripcion;
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

    public String getDescripcion() {
        return _descripcion;
    }

    public void setDescripcion(String _descripcion) {
        this._descripcion = _descripcion;
    }

    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Articulo() {
    }

    public Articulo(int _id) {
        this._id = _id;
    }
    
    public Articulo(int _id, String _nombre, String _descripcion, boolean _estado) {
        this._id = _id;
        this._nombre = _nombre;
        this._descripcion = _descripcion;
        this._estado = _estado;
    }

    @Override
    public String toString() {
        return _nombre;
    }
}
