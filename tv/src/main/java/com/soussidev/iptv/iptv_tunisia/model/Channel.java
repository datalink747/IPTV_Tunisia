package com.soussidev.iptv.iptv_tunisia.model;

import java.io.Serializable;

/**
 * Created by Soussi on 10/02/2018.
 */

public class Channel implements Serializable{
    private int Id_channel;
    private String Name_channel;
    private String Url_image;
    private String Url_channel;
    private String Category;
    private String Lang;
    private String Site_web;
    private String Url_them;
    private String Quality_ch;

    public Channel() {
    }

    public int getId_channel() {
        return Id_channel;
    }

    public void setId_channel(int id_channel) {
        Id_channel = id_channel;
    }

    public String getName_channel() {
        return Name_channel;
    }

    public void setName_channel(String name_channel) {
        Name_channel = name_channel;
    }

    public String getUrl_image() {
        return Url_image;
    }

    public void setUrl_image(String url_image) {
        Url_image = url_image;
    }

    public String getUrl_channel() {
        return Url_channel;
    }

    public void setUrl_channel(String url_channel) {
        Url_channel = url_channel;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getLang() {
        return Lang;
    }

    public void setLang(String lang) {
        Lang = lang;
    }

    public String getSite_web() {
        return Site_web;
    }

    public void setSite_web(String site_web) {
        Site_web = site_web;
    }

    public String getUrl_them() {
        return Url_them;
    }

    public void setUrl_them(String url_them) {
        Url_them = url_them;
    }

    public String getQuality_ch() {
        return Quality_ch;
    }

    public void setQuality_ch(String quality_ch) {
        Quality_ch = quality_ch;
    }
}
