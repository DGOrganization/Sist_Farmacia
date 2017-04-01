/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.util.List;

/**
 *
 * @author dakrpastiursSennin
 */
public class Unidad {
    private int _id;
    private String _nombre;
    private String _abreviado;
    private List<Unidad> _conversiones;
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

    public String getAbreviado() {
        return _abreviado;
    }

    public void setAbreviado(String _abreviado) {
        this._abreviado = _abreviado;
    }

    public List<Unidad> getConversiones() {
        return _conversiones;
    }

    public void setConversiones(List<Unidad> _conversiones) {
        this._conversiones = _conversiones;
    }

    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Unidad() {
    }

    public Unidad(int _id) {
        this._id = _id;
    }

    public Unidad(int _id, String _nombre, String _abreviado, boolean _estado) {
        this._id = _id;
        this._nombre = _nombre;
        this._abreviado = _abreviado;
        this._estado = _estado;
    }

    public Unidad(int _id, String _nombre, String _abreviado, List<Unidad> _conversiones, boolean _estado) {
        this._id = _id;
        this._nombre = _nombre;
        this._abreviado = _abreviado;
        this._conversiones = _conversiones;
        this._estado = _estado;
    }

    @Override
    public String toString() {
        return _nombre + "(" + _abreviado + ")";
    }
    
}
