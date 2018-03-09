package com.soussidev.iptv.iptv_tunisia.model;

import java.io.Serializable;

/**
 * Created by Soussi on 12/02/2018.
 */

public class Category implements Serializable{
    private int Id_cat;
    private String Nom_cat;
    private String Url_background_image;

    public Category() {
    }

    public int getId_cat() {
        return Id_cat;
    }

    public void setId_cat(int id_cat) {
        Id_cat = id_cat;
    }

    public String getNom_cat() {
        return Nom_cat;
    }

    public void setNom_cat(String nom_cat) {
        Nom_cat = nom_cat;
    }

    public String getUrl_background_image() {
        return Url_background_image;
    }

    public void setUrl_background_image(String url_background_image) {
        Url_background_image = url_background_image;
    }
}
