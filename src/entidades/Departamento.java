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
public class Departamento {
    private int _id;
    private String _nombre;
    private List<Municipio> _municipios;
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

    public List<Municipio> getMunicipios() {
        return _municipios;
    }

    public void setMunicipios(List<Municipio> _municipios) {
        this._municipios = _municipios;
    }

    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Departamento() {
    }

    public Departamento(int _id, String _nombre, List<Municipio> _municipios, boolean _estado) {
        this._id = _id;
        this._nombre = _nombre;
        this._municipios = _municipios;
        this._estado = _estado;
    }

    @Override
    public String toString() {
        return _nombre;
    }
}
