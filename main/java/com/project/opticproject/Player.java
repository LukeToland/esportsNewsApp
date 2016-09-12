package com.project.opticproject;

/**
 * Created by Luke on 02/09/2016.
 */
public class Player {
    public String name;
    public String kd;
    public String spm;
    public String photo;

    public Player(String name, String spm, String kd, String p) {
        this.name = name;
        this.spm = spm;
        this.kd = kd;
        this.photo = p;
    }

    public String getName() {
        return name;
    }

    public String getKd() {
        return kd;
    }

    public String getSpm() {
        return spm;
    }

    public String getPhoto() {
        return photo;
    }
}
