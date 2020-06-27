package com.example.sagliklibeslen.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Besinler implements Serializable {
    public int BesinlerId;



    public int KullaniciId;
    public boolean ozel;
    public String besinAdi;
    public double besinProtein;
    public double besinKarbonhidrat;
    public double besinYag;
    public double besinKalori;
    public String barkodNo;

    public String getBarkodNo() {
        return barkodNo;
    }

    public void setBarkodNo(String barkodNo) {
        this.barkodNo = barkodNo;
    }

    public boolean isOzel() {
        return ozel;
    }

    public void setOzel(boolean ozel) {
        this.ozel = ozel;
    }
    public int getKullaniciId() {
        return KullaniciId;
    }

    public void setKullaniciId(int kullaniciId) {
        KullaniciId = kullaniciId;
    }
    public static List<Besinler> JsonObject(String s) throws JSONException {
        List<Besinler> besinler = new ArrayList<Besinler>();
        JSONArray json = new JSONArray(s);
        for(int i=0; i<json.length(); i++){
            JSONObject e = json.getJSONObject(i);
            Besinler besin = new Besinler();
            besin.BesinlerId = e.getInt("besinlerId");
            besin.KullaniciId = e.getInt("kullaniciId");
            besin.besinYag = e.getDouble("besinYag");
            besin.besinAdi = e.getString("besinAdi");
            besin.besinProtein = e.getDouble("besinProtein");
            besin.besinKarbonhidrat = e.getDouble("besinKarbonhidrat");
            besin.besinKalori = e.getDouble("besinKalori");
            if(!e.isNull("barkodNo")){
                besin.barkodNo = e.getString("barkodNo");
            }
            else{
                besin.barkodNo = e.getString("besinlerId");
            }
            besin.ozel = e.getBoolean("ozel");
            besinler.add(besin);
        }

        return besinler;
    }

    public static Besinler JsonObject2(String s) throws JSONException {
        Besinler besinler = new Besinler();

            JSONObject e = new JSONObject(s);
            Besinler besin = new Besinler();
            besin.BesinlerId = e.getInt("besinlerId");
            besin.KullaniciId = e.getInt("kullaniciId");
            besin.besinYag = e.getDouble("besinYag");
            besin.besinAdi = e.getString("besinAdi");
            besin.besinProtein = e.getDouble("besinProtein");
            besin.besinKarbonhidrat = e.getDouble("besinKarbonhidrat");
            besin.besinKalori = e.getDouble("besinKalori");
            if(!e.isNull("barkodNo")){
                besin.barkodNo = e.getString("barkodNo");
            }
            else{
                besin.barkodNo = e.getString("besinlerId");
            }
            besin.ozel = e.getBoolean("ozel");
            besinler = besin;
     return besinler;
    }

    public Besinler(Besinler besin){
        BesinlerId = besin.getBesinlerId();
        KullaniciId = besin.getKullaniciId();
        besinAdi = besin.getBesinAdi();
        besinKalori = besin.getBesinKalori();
        besinKarbonhidrat = besin.getBesinKarbonhidrat();
        besinProtein = besin.getBesinProtein();
        besinYag = besin.getBesinYag();
        barkodNo = besin.getBarkodNo();
        ozel = besin.isOzel();
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
                ", kullaniciId=" + KullaniciId +
                ", barkodNo=" + barkodNo +
                ", ozel=" + ozel +
                '}';
    }

    public Besinler(int besinlerId, int KullaniciId, String besinAdi, double besinProtein, double besinKarbonhidrat, double besinYag, double besinKalori, String barkodNo, boolean ozel) {
        this.BesinlerId = besinlerId;
        this.KullaniciId = KullaniciId;
        this.besinAdi = besinAdi;
        this.besinProtein = besinProtein;
        this.besinKarbonhidrat = besinKarbonhidrat;
        this.besinYag = besinYag;
        this.besinKalori = besinKalori;
        this.barkodNo = barkodNo;
        this.ozel = ozel;
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
