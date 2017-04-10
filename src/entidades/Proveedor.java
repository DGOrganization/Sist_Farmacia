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
public class Proveedor {

    private int _id;
    private String _respresentante;
    private String _nombre;
    private String _NRC;
    private String _domicilio;
    private Departamento _departamento;
    private String _telefono;
    private String _celular;
    private String _email;
    private String _website;
    private boolean _estado;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getRespresentante() {
        return _respresentante;
    }

    public void setRespresentante(String _respresentante) {
        this._respresentante = _respresentante;
    }

    public String getNombre() {
        return _nombre;
    }

    public void setNombre(String _nombre) {
        this._nombre = _nombre;
    }

    public String getNRC() {
        return _NRC;
    }

    public void setNRC(String _NRC) {
        this._NRC = _NRC;
    }

    public String getDomicilio() {
        return _domicilio;
    }

    public void setDomicilio(String _domicilio) {
        this._domicilio = _domicilio;
    }

    public Departamento getDepartamento() {
        return _departamento;
    }

    public void setDepartamento(Departamento _departamento) {
        this._departamento = _departamento;
    }

    public String getTelefono() {
        return _telefono;
    }

    public void setTelefono(String _telefono) {
        this._telefono = _telefono;
    }

    public String getCelular() {
        return _celular;
    }

    public void setCelular(String _celular) {
        this._celular = _celular;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }

    public String getWebsite() {
        return _website;
    }

    public void setWebsite(String _website) {
        this._website = _website;
    }

    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Proveedor() {
    }

    public Proveedor(int _id, String _respresentante, String _nombre, String _NRC, String _domicilio, Departamento _departamento, String _telefono, String _celular, String _email, String _website) {
        this._id = _id;
        this._respresentante = _respresentante;
        this._nombre = _nombre;
        this._NRC = _NRC;
        this._domicilio = _domicilio;
        this._departamento = _departamento;
        this._telefono = _telefono;
        this._celular = _celular;
        this._email = _email;
        this._website = _website;
    }

    @Override
    public String toString() {
        return _nombre;
    }

}
