package com.example.sagliklibeslen.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

public class YedigiBesin {
    public int usersId;
    public int BesinlerId;
    public int Porsiyon;
    public Timestamp Date ;

    @Override
    public String toString() {
        return "YedigiBesin{" +
                "usersId=" + usersId +
                ", BesinId=" + BesinlerId +
                ", Porsiyon=" + Porsiyon +
                ", Date=" + Date +
                '}';
    }

    public int getUsersId() {
        return usersId;
    }

    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }

    public int getBesinId() {
        return BesinlerId;
    }

    public void setBesinId(int besinId) {
        BesinlerId = besinId;
    }

    public int getPorsiyon() {
        return Porsiyon;
    }

    public void setPorsiyon(int porsiyon) {
        Porsiyon = porsiyon;
    }

    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    public YedigiBesin(int usersId, int besinId, int porsiyon, Timestamp date) {
        this.usersId = usersId;
        BesinlerId = besinId;
        Porsiyon = porsiyon;
        Date = date;
    }

    public YedigiBesin() {
    }
}
