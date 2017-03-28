/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.sql.Date;

/**
 *
 * @author dakrpastiursSennin
 */
public class Cliente {
    private long _id;
    private String _nombre;
    private String _apellidopaterno;
    private String _apellidomaterno;
    private String _dui;
    private String _nit;
    private String _sexo;
    private java.sql.Date _nacimiento;
    private String _direccion;
    private Municipio _municipio;
    private String _telefono;
    private String _movil;
    private String _email;
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

    public String getApellidoPaterno() {
        return _apellidopaterno;
    }

    public void setApellidoPaterno(String _apellidopaterno) {
        this._apellidopaterno = _apellidopaterno;
    }

    public String getApellidoMaterno() {
        return _apellidomaterno;
    }

    public void setApellidoMaterno(String _apellidomaterno) {
        this._apellidomaterno = _apellidomaterno;
    }

    public String getDui() {
        return _dui;
    }

    public void setDui(String _dui) {
        this._dui = _dui;
    }

    public String getNit() {
        return _nit;
    }

    public void setNit(String _nit) {
        this._nit = _nit;
    }

    public String getSexo() {
        return _sexo;
    }

    public void setSexo(String _sexo) {
        this._sexo = _sexo;
    }

    public Date getNacimiento() {
        return _nacimiento;
    }

    public void setNacimiento(java.util.Date _nacimiento) {
        this._nacimiento = new Date(_nacimiento.getTime());
    }

    public String getDireccion() {
        return _direccion;
    }

    public void setDireccion(String _direccion) {
        this._direccion = _direccion;
    }

    public Municipio getMunicipio() {
        return _municipio;
    }

    public void setMunicipio(Municipio _municipio) {
        this._municipio = _municipio;
    }

    public String getTelefono() {
        return _telefono;
    }

    public void setTelefono(String _telefono) {
        this._telefono = _telefono;
    }

    public String getMovil() {
        return _movil;
    }

    public void setMovil(String _movil) {
        this._movil = _movil;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }

    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Cliente() {
    }

    public Cliente(long _id, String _nombre, String _apellidopaterno, String _apellidomaterno, String _dui, String _nit, String _sexo, java.util.Date _nacimiento, String _direccion, Municipio _municipio, String _telefono, String _movil, String _email, boolean _estado) {
        this._id = _id;
        this._nombre = _nombre;
        this._apellidopaterno = _apellidopaterno;
        this._apellidomaterno = _apellidomaterno;
        this._dui = _dui;
        this._nit = _nit;
        this._sexo = _sexo;
        this._nacimiento = new Date(_nacimiento.getTime());
        this._direccion = _direccion;
        this._municipio = _municipio;
        this._telefono = _telefono;
        this._movil = _movil;
        this._email = _email;
        this._estado = _estado;
    }

    @Override
    public String toString() {
        return _nombre + " " + _apellidopaterno + " " + _apellidomaterno;
    }
    
    
}
