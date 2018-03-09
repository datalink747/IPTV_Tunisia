package com.soussidev.iptv.iptv_tunisia.model;

import java.io.Serializable;

/**
 * Created by Soussi on 13/02/2018.
 */

public class Vod implements Serializable{

    private int Id_vod;
    private String Nom_vod;
    private String Lang_vod;
    private String Cat_vod;
    private String Url_vod;
    private String Url_cover_vod;
    private String Description_vod;
    private String Time_vod;
    private String Source;
    private String Quality_vod;

    public Vod() {
    }

    public int getId_vod() {
        return Id_vod;
    }

    public void setId_vod(int id_vod) {
        Id_vod = id_vod;
    }

    public String getNom_vod() {
        return Nom_vod;
    }

    public void setNom_vod(String nom_vod) {
        Nom_vod = nom_vod;
    }

    public String getLang_vod() {
        return Lang_vod;
    }

    public void setLang_vod(String lang_vod) {
        Lang_vod = lang_vod;
    }

    public String getCat_vod() {
        return Cat_vod;
    }

    public void setCat_vod(String cat_vod) {
        Cat_vod = cat_vod;
    }

    public String getUrl_vod() {
        return Url_vod;
    }

    public void setUrl_vod(String url_vod) {
        Url_vod = url_vod;
    }

    public String getUrl_cover_vod() {
        return Url_cover_vod;
    }

    public void setUrl_cover_vod(String url_cover_vod) {
        Url_cover_vod = url_cover_vod;
    }

    public String getDescription_vod() {
        return Description_vod;
    }

    public void setDescription_vod(String description_vod) {
        Description_vod = description_vod;
    }

    public String getTime_vod() {
        return Time_vod;
    }

    public void setTime_vod(String time_vod) {
        Time_vod = time_vod;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getQuality_vod() {
        return Quality_vod;
    }

    public void setQuality_vod(String quality_vod) {
        Quality_vod = quality_vod;
    }
}
