package com.example.sagliklibeslen.Model;

import java.sql.Timestamp;

public class GetSu {
    public int kullanici;
    public Timestamp Date;

    public GetSu() {
    }

    public GetSu(int kullanici, Timestamp date) {
        this.kullanici = kullanici;
        Date = date;
    }

    public int getKullanici() {
        return kullanici;
    }

    public void setKullanici(int kullanici) {
        this.kullanici = kullanici;
    }

    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    @Override
    public String toString() {
        return "GetSu{" +
                "kullanici=" + kullanici +
                ", Date=" + Date +
                '}';
    }
}
