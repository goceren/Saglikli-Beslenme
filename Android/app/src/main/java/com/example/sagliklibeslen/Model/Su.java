package com.example.sagliklibeslen.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Su {
    public int SuId;
    public int Kullanici;
    public int bardakSayisi;
    public Timestamp Date;

    public Su() {
    }

    public Su(int suId, int kullanici, int bardakSayisi, Timestamp date) {
        SuId = suId;
        Kullanici = kullanici;
        this.bardakSayisi = bardakSayisi;
        Date = date;
    }



    public static List<Su> JsonObject(String s) throws JSONException {
        List<Su> sular = new ArrayList<Su>();
        JSONArray json = new JSONArray(s);
        for(int i=0; i<json.length(); i++){
            JSONObject e = json.getJSONObject(i);
            Su su = new Su();
            su.SuId = e.getInt("suId");
            su.Kullanici = e.getInt("kullanici");
            su.bardakSayisi = e.getInt("bardakSayisi");
            String date = e.getString("date");
            String first = date.substring(0, date.indexOf('T'));
            String second = date.substring(date.indexOf('T')+1, date.length());
            String DateSum = first + " " + second + ".000";
            su.Date = convertStringToTimestamp(DateSum);
            sular.add(su);
        }
        return sular;
    }
    private static Timestamp convertStringToTimestamp(String something) {


        SimpleDateFormat dateFormat = null;
        if(something.contains(".")) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        }
        if(something.contains(",")) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS");
        }
        Timestamp timestamp = null;
        java.util.Date parsedDate;
        try {
            parsedDate = dateFormat.parse(something);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return timestamp;
    }


    @Override
    public String toString() {
        return "Su{" +
                "SuId=" + SuId +
                ", Kullanici=" + Kullanici +
                ", bardakSayisi=" + bardakSayisi +
                ", Date=" + Date +
                '}';
    }

    public int getSuId() {
        return SuId;
    }

    public void setSuId(int suId) {
        SuId = suId;
    }

    public int getKullanici() {
        return Kullanici;
    }

    public void setKullanici(int kullanici) {
        Kullanici = kullanici;
    }

    public int getBardakSayisi() {
        return bardakSayisi;
    }

    public void setBardakSayisi(int bardakSayisi) {
        this.bardakSayisi = bardakSayisi;
    }

    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }
}
