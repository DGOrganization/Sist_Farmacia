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
public class Empleado {
    private long _id;
    private String _nombre;
    private String _apellidopaterno;
    private String _apellidomaterno;

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

    public String getApellidopaterno() {
        return _apellidopaterno;
    }

    public void setApellidopaterno(String _apellidopaterno) {
        this._apellidopaterno = _apellidopaterno;
    }

    public String getApellidomaterno() {
        return _apellidomaterno;
    }

    public void setApellidomaterno(String _apellidomaterno) {
        this._apellidomaterno = _apellidomaterno;
    }

    public Empleado() {
    }

    public Empleado(long _id, String _nombre, String _apellidopaterno, String _apellidomaterno) {
        this._id = _id;
        this._nombre = _nombre;
        this._apellidopaterno = _apellidopaterno;
        this._apellidomaterno = _apellidomaterno;
    }

    @Override
    public String toString() {
        return _nombre + " " + _apellidopaterno + " " + _apellidomaterno;
    }
    
    
}
