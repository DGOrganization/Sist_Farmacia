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
public class Menu {
    private int _id;
    private String _nombre;
    private boolean _permiso;

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

    public boolean isPermiso() {
        return _permiso;
    }

    public void setPermiso(boolean _permiso) {
        this._permiso = _permiso;
    }

    public Menu() {
    }

    public Menu(int _id) {
        this._id = _id;
    }

    public Menu(int _id, String _nombre, boolean _permiso) {
        this._id = _id;
        this._nombre = _nombre;
        this._permiso = _permiso;
    }

    @Override
    public String toString() {
        return _nombre;
    }
    
    
}
