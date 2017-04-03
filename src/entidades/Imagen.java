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
public class Imagen {
    private int _id;
    private String _url;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String _url) {
        this._url = _url;
    }

    public Imagen() {
    }

    public Imagen(int _id) {
        this._id = _id;
    }

    public Imagen(int _id, String _url) {
        this._id = _id;
        this._url = _url;
    }

    @Override
    public String toString() {
        return _url;
    }
    
}
