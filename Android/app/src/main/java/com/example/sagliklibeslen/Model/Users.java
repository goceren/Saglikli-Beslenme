package com.example.sagliklibeslen.Model;

import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Users implements Serializable {

    public int UsersId;
    public String Name;
    public String Email;
    public String Password;
    public String Salt;
    public int Age;
    public double Weight;
    public double Length;
    public boolean Cinsiyet;
    public boolean Admin;

    @Override
    public String toString() {
        return "Users{" +
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
                '}';
    }

    public Users(){

    }

    public Users(Users user){
        UsersId = user.getUsersId();
        Name = user.getName();
        Email = user.getEmail();
        Password = user.getPassword();
        Salt = user.getSalt();
        Weight = user.getWeight();
        Age = user.getAge();
        Length = user.getLength();
        Admin = user.isAdmin();
        Cinsiyet = user.isCinsiyet();

    }

    public Users(int usersId, String name, String email, String password, String salt, int age, double weight, double length, boolean cinsiyet, boolean admin) {
        UsersId = usersId;
        Name = name;
        Email = email;
        Password = password;
        Salt = salt;
        Age = age;
        Weight = weight;
        Length = length;
        Admin = admin;
        Cinsiyet = cinsiyet;
    }

    public static Users JsontoObject(String s) throws JSONException {
        Users user = new Users();
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
                return user;
            }
        }
        return null;
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

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public double getLength() {
        return Length;
    }

    public void setLength(double length) {
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
}
