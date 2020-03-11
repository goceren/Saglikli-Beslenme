package com.example.sagliklibeslen.Session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sagliklibeslen.Model.Users;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";
    String SESSION_EMAIL = "session_email";
    String SESSION_NAME = "session_name";
    String SESSION_AGE = "session_age";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(Users user){
        // session kaydı kullanıcıya göre yapılacak
        int id = user.getUsersId();
        String email = user.getEmail();
        String name = user.getName();
        int age = user.getAge();
        editor.putInt(SESSION_KEY, id).commit();
        editor.putString(SESSION_EMAIL, email).commit();
        editor.putString(SESSION_NAME, name).commit();
        editor.putInt(SESSION_AGE, age).commit();
    }

    public int getSession(){
        // session ykaydı yapılan kullanıcının idsi return edilecek
        return sharedPreferences.getInt(SESSION_KEY, -1);
    }
    public String getEmail(){
        return sharedPreferences.getString(SESSION_EMAIL, null);
    }
    public String getName(){
        return sharedPreferences.getString(SESSION_NAME, null);
    }
    public int getAge(){
        return sharedPreferences.getInt(SESSION_AGE, 0);
    }
    public void removeSession(){
        // çıkış yapma işlemi
        editor.putInt(SESSION_KEY, -1).commit();
    }

}
