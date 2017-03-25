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
public class Usuario {
    private long _id;
    private String _username;
    private String _password;
    private Empleado _empleado;

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(String _username) {
        this._username = _username;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String _password) {
        this._password = _password;
    }

    public Empleado getEmpleado() {
        return _empleado;
    }

    public void setEmpleado(Empleado _empleado) {
        this._empleado = _empleado;
    }

    public Usuario() {
    }

    public Usuario(long _id, String _username, String _password, Empleado _empleado) {
        this._id = _id;
        this._username = _username;
        this._password = _password;
        this._empleado = _empleado;
    }

    @Override
    public String toString() {
        return _username;
    }
}
