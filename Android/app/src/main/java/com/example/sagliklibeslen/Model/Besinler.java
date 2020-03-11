package com.example.sagliklibeslen.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Besinler {
    public int BesinlerId;
    public String besinAdi;
    public double besinProtein;
    public double besinKarbonhidrat;
    public double besinYag;
    public double besinKalori;

    public static Besinler JsontoObject(String s) throws JSONException {
        Besinler besin = new Besinler();
        if (!s.isEmpty()){
            JSONObject object = new JSONObject(s);
            if (object != null){
                besin.BesinlerId = object.getInt("besinlerid");
                besin.besinAdi = object.getString("besinadi");
                besin.besinProtein = object.getDouble("besinprotein");
                besin.besinKarbonhidrat = object.getDouble("besinkarbonhidrat");
                besin.besinYag = object.getDouble("besinyag");
                besin.besinKalori = object.getDouble("besinkalori");
                return besin;
            }
        }
        return null;
    }

    public Besinler(Besinler besin){
        BesinlerId = besin.getBesinlerId();
        besinAdi = besin.getBesinAdi();
        besinKalori = besin.getBesinKalori();
        besinKarbonhidrat = besin.getBesinKarbonhidrat();
        besinProtein = besin.getBesinProtein();
        besinYag = besin.getBesinYag();
    }

    @Override
    public String toString() {
        return "Besinler{" +
                "BesinlerId=" + BesinlerId +
                ", besinAdi='" + besinAdi + '\'' +
                ", besinProtein=" + besinProtein +
                ", besinKarbonhidrat=" + besinKarbonhidrat +
                ", besinYag=" + besinYag +
                ", besinKalori=" + besinKalori +
                '}';
    }

    public Besinler(int besinlerId, String besinAdi, double besinProtein, double besinKarbonhidrat, double besinYag, double besinKalori) {
        BesinlerId = besinlerId;
        this.besinAdi = besinAdi;
        this.besinProtein = besinProtein;
        this.besinKarbonhidrat = besinKarbonhidrat;
        this.besinYag = besinYag;
        this.besinKalori = besinKalori;
    }
    public Besinler() {
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
}
