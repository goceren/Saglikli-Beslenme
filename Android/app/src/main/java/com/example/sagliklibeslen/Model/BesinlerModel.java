package com.example.sagliklibeslen.Model;

import java.sql.Timestamp;
import java.util.Date;

public class BesinlerModel {
    public int BesinlerId ;
    public String  besinAdi;
    public double besinProtein ;
    public double besinKarbonhidrat;
    public double besinYag ;
    public double besinKalori ;
    public Timestamp Date ;
    public int Porsiyon ;

    @Override
    public String toString() {
        return "BesinlerModel{" +
                "BesinlerId=" + BesinlerId +
                ", besinAdi='" + besinAdi + '\'' +
                ", besinProtein=" + besinProtein +
                ", besinKarbonhidrat=" + besinKarbonhidrat +
                ", besinYag=" + besinYag +
                ", besinKalori=" + besinKalori +
                ", Date=" + Date +
                ", Porsiyon=" + Porsiyon +
                '}';
    }

    public BesinlerModel(int besinlerId, String besinAdi, double besinProtein, double besinKarbonhidrat, double besinYag, double besinKalori, Timestamp date, int porsiyon) {
        BesinlerId = besinlerId;
        this.besinAdi = besinAdi;
        this.besinProtein = besinProtein;
        this.besinKarbonhidrat = besinKarbonhidrat;
        this.besinYag = besinYag;
        this.besinKalori = besinKalori;
        Date = date;
        Porsiyon = porsiyon;
    }

    public BesinlerModel() {
    }

    public BesinlerModel(BesinlerModel model) {
        BesinlerId = model.getBesinlerId();
        besinAdi = model.getBesinAdi();
        besinProtein = model.getBesinProtein();
        besinKarbonhidrat = model.getBesinKarbonhidrat();
        besinYag = model.getBesinYag();
        besinKalori = model.getBesinKalori();
        Date = model.getDate();
        Porsiyon = model.getPorsiyon();
    }



    public int getBesinlerId() {
        return BesinlerId;
    }

    public void setBesinlerId(int besinlerId) {
        BesinlerId = besinlerId;
    }

    public String getBesinAdi() {
        return besinAdi;
    }

    public void setBesinAdi(String besinAdi) {
        this.besinAdi = besinAdi;
    }

    public double getBesinProtein() {
        return besinProtein;
    }

    public void setBesinProtein(double besinProtein) {
        this.besinProtein = besinProtein;
    }

    public double getBesinKarbonhidrat() {
        return besinKarbonhidrat;
    }

    public void setBesinKarbonhidrat(double besinKarbonhidrat) {
        this.besinKarbonhidrat = besinKarbonhidrat;
    }

    public double getBesinYag() {
        return besinYag;
    }

    public void setBesinYag(double besinYag) {
        this.besinYag = besinYag;
    }

    public double getBesinKalori() {
        return besinKalori;
    }

    public void setBesinKalori(double besinKalori) {
        this.besinKalori = besinKalori;
    }

    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    public int getPorsiyon() {
        return Porsiyon;
    }

    public void setPorsiyon(int porsiyon) {
        Porsiyon = porsiyon;
    }
}
