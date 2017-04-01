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
public class Nivel {
    private int _id;
    private String _nombre;
    private List<Menu> _menus;
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

    public List<Menu> getMenus() {
        return _menus;
    }

    public void setMenus(List<Menu> _menus) {
        this._menus = _menus;
    }

    public boolean isEstado() {
        return _estado;
    }

    public void setEstado(boolean _estado) {
        this._estado = _estado;
    }

    public Nivel() {
    }

    public Nivel(int _id) {
        this._id = _id;
    }

    public Nivel(int _id, String _nombre, boolean _estado) {
        this._id = _id;
        this._nombre = _nombre;
        this._estado = _estado;
    }

    @Override
    public String toString() {
        return _nombre;
    }   
}
