package com.soussidev.iptv.iptv_tunisia.model;

import java.io.Serializable;

/**
 * Created by Soussi on 15/02/2018.
 */

public class Programme implements Serializable{

    private int Id_prog;
    private String Date_prog;
    private String Name_channel;
    private String Start_time;
    private String End_time;
    private String Titre_prog;
    private String Description_prog;

    public Programme() {
    }

    public int getId_prog() {
        return Id_prog;
    }

    public void setId_prog(int id_prog) {
        Id_prog = id_prog;
    }

    public String getDate_prog() {
        return Date_prog;
    }

    public void setDate_prog(String date_prog) {
        Date_prog = date_prog;
    }

    public String getName_channel() {
        return Name_channel;
    }

    public void setName_channel(String name_channel) {
        Name_channel = name_channel;
    }

    public String getStart_time() {
        return Start_time;
    }

    public void setStart_time(String start_time) {
        Start_time = start_time;
    }

    public String getEnd_time() {
        return End_time;
    }

    public void setEnd_time(String end_time) {
        End_time = end_time;
    }

    public String getTitre_prog() {
        return Titre_prog;
    }

    public void setTitre_prog(String titre_prog) {
        Titre_prog = titre_prog;
    }

    public String getDescription_prog() {
        return Description_prog;
    }

    public void setDescription_prog(String description_prog) {
        Description_prog = description_prog;
    }
}
