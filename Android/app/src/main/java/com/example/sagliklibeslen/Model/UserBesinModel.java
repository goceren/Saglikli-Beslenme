package com.example.sagliklibeslen.Model;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.PBEKeySpec;

public class UserBesinModel implements Serializable {
    public int UsersId;
    public String Name;
    public String Email;
    public String Password;
    public String  Salt ;
    public int Age ;
    public int Weight ;
    public int Length;
    public boolean Cinsiyet ;
    public boolean Admin ;
    public double ToplamKalori ;
    public double ToplamProtein;
    public double ToplamKarbonhidrat;
    public double ToplamYag ;
    public List<BesinlerModel> BesinlerModel;

    @Override
    public String toString() {
        return "UserBesinModel{" +
                "UsersId=" + UsersId +
                ", Name='" + Name + '\'' +
                ", Email='" + Email + '\'' +
                ", Password='" + Password + '\'' +
                ", Salt='" + Salt + '\'' +
                ", Age=" + Age +
                ", Weight=" + Weight +
                ", Length=" + Length +
                ", Cinsiyet=" + Cinsiyet +
                ", Admin=" + Admin +
                ", ToplamKalori=" + ToplamKalori +
                ", ToplamProtein=" + ToplamProtein +
                ", ToplamKarbonhidrat=" + ToplamKarbonhidrat +
                ", ToplamYag=" + ToplamYag +
                ", BesinlerModel=" + BesinlerModel +
                '}';
    }

    public static UserBesinModel JsontoObject(String s) throws JSONException, ParseException {
        UserBesinModel user = new UserBesinModel();
        if (!s.isEmpty()){
            JSONObject object = new JSONObject(s);
            if (object != null){
                user.Name = object.getString("name");
                user.UsersId = object.getInt("usersId");
                user.Email = object.getString("email");
                user.Salt = "";
                user.Password = "";
                user.Length = object.getInt("length");
                user.Age = object.getInt("age");
                user.Weight = object.getInt("weight");
                user.Admin = object.getBoolean("admin");
                user.Cinsiyet = object.getBoolean("cinsiyet");
                user.ToplamKalori = object.getDouble("toplamKalori");
                user.ToplamProtein = object.getDouble("toplamProtein");
                user.ToplamKarbonhidrat = object.getDouble("toplamKarbonhidrat");
                user.ToplamYag = object.getDouble("toplamYag");
                JSONArray array =  object.getJSONArray("besinlerModel");
                List<BesinlerModel> listModel = new ArrayList<BesinlerModel>();
                for (int i = 0; i < array.length() ; i++){
                    JSONObject item = array.getJSONObject(i);
                    com.example.sagliklibeslen.Model.BesinlerModel model = new BesinlerModel();
                    model.BesinlerId = item.getInt("besinlerId");
                    model.besinAdi = item.getString("besinAdi");
                    model.besinKalori = item.getDouble("besinKalori");
                    model.besinKarbonhidrat = item.getDouble("besinKarbonhidrat");
                    model.besinProtein = item.getDouble("besinProtein");
                    model.besinYag = item.getDouble("besinYag");
                    model.Porsiyon = item.getInt("porsiyon");
                    String date = item.getString("date");
                    String first = date.substring(0, date.indexOf('T'));
                    String second = date.substring(date.indexOf('T')+1, date.length());
                    String DateSum = first + " " + second + ".000";
                    model.Date = convertStringToTimestamp(DateSum);
                    listModel.add(model);
                }
                user.BesinlerModel = listModel;
                return user;
            }
        }
        return null;
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
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(something);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return timestamp;
    }


    public UserBesinModel(int usersId, String name, String email, String password, String salt, int age, int weight, int length, boolean cinsiyet, boolean admin, double toplamKalori, double toplamProtein, double toplamKarbonhidrat, double toplamYag, List<com.example.sagliklibeslen.Model.BesinlerModel> besinlerModel) {
        UsersId = usersId;
        Name = name;
        Email = email;
        Password = password;
        Salt = salt;
        Age = age;
        Weight = weight;
        Length = length;
        Cinsiyet = cinsiyet;
        Admin = admin;
        ToplamKalori = toplamKalori;
        ToplamProtein = toplamProtein;
        ToplamKarbonhidrat = toplamKarbonhidrat;
        ToplamYag = toplamYag;
        BesinlerModel = besinlerModel;
    }
    public UserBesinModel() {
    }
    public UserBesinModel(UserBesinModel model) {
        UsersId = model.getUsersId();
        Name = model.getName();
        Email = model.getEmail();
        Password = model.getPassword();
        Salt = model.getSalt();
        Age = model.getAge();
        Weight = model.getWeight();
        Length = model.getLength();
        Cinsiyet = model.isCinsiyet();
        Admin = model.isCinsiyet();
        ToplamKalori = model.getToplamKalori();
        ToplamProtein = model.getToplamProtein();
        ToplamKarbonhidrat = model.getToplamKarbonhidrat();
        ToplamYag = model.getToplamYag();
        BesinlerModel = model.getBesinlerModel();
    }

    public int getUsersId() {
        return UsersId;
    }

    public void setUsersId(int usersId) {
        UsersId = usersId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSalt() {
        return Salt;
    }

    public void setSalt(String salt) {
        Salt = salt;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        Length = length;
    }

    public boolean isCinsiyet() {
        return Cinsiyet;
    }

    public void setCinsiyet(boolean cinsiyet) {
        Cinsiyet = cinsiyet;
    }

    public boolean isAdmin() {
        return Admin;
    }

    public void setAdmin(boolean admin) {
        Admin = admin;
    }

    public double getToplamKalori() {
        return ToplamKalori;
    }

    public void setToplamKalori(double toplamKalori) {
        ToplamKalori = toplamKalori;
    }

    public double getToplamProtein() {
        return ToplamProtein;
    }

    public void setToplamProtein(double toplamProtein) {
        ToplamProtein = toplamProtein;
    }

    public double getToplamKarbonhidrat() {
        return ToplamKarbonhidrat;
    }

    public void setToplamKarbonhidrat(double toplamKarbonhidrat) {
        ToplamKarbonhidrat = toplamKarbonhidrat;
    }

    public double getToplamYag() {
        return ToplamYag;
    }

    public void setToplamYag(double toplamYag) {
        ToplamYag = toplamYag;
    }

    public List<com.example.sagliklibeslen.Model.BesinlerModel> getBesinlerModel() {
        return BesinlerModel;
    }

    public void setBesinlerModel(List<com.example.sagliklibeslen.Model.BesinlerModel> besinlerModel) {
        BesinlerModel = besinlerModel;
    }
}
