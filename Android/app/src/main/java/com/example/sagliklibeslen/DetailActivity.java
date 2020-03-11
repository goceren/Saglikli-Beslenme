package com.example.sagliklibeslen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sagliklibeslen.Model.Users;
import com.example.sagliklibeslen.Remote.IMyAPI;
import com.example.sagliklibeslen.Remote.RetrofitClient;
import com.example.sagliklibeslen.Session.SessionManagement;
import com.shawnlin.numberpicker.NumberPicker;

import org.json.JSONException;

import java.util.Locale;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private NumberPicker boy_picker, kilo_picker, yas_picker, cinsiyet;
    Button button_onayla;
    int yas = 0;
    int kilo, boy, cinsiyetSonuc;
    private Users currentUser;
    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tanimla();
        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);
    }
    void tanimla(){
        button_onayla = findViewById(R.id.button_onayla);
        button_onayla.setOnClickListener(this);
        yas_picker = findViewById(R.id.yas_picker);
        kilo_picker = findViewById(R.id.kilo_picker);
        boy_picker = findViewById(R.id.boy_picker);
        cinsiyet = findViewById(R.id.cinsiyet);

        yas_picker.setMaxValue(95);
        yas_picker.setMinValue(10);
        yas_picker.setValue(25);

        kilo_picker.setMaxValue(120);
        kilo_picker.setMinValue(30);
        kilo_picker.setValue(45);

        boy_picker.setMaxValue(215);
        boy_picker.setMinValue(120);
        boy_picker.setValue(150);

        final String[] cinsiyetList = {"Erkek", "Kadın"};
        cinsiyet.setMinValue(1);
        cinsiyet.setMaxValue(cinsiyetList.length);
        cinsiyet.setDisplayedValues(cinsiyetList);
        cinsiyet.setValue(2);

        yas_picker.setFadingEdgeEnabled(true);
        yas_picker.setScrollerEnabled(true);
        yas_picker.setWrapSelectorWheel(true);
        yas_picker.setAccessibilityDescriptionEnabled(true);

        kilo_picker.setFadingEdgeEnabled(true);
        kilo_picker.setScrollerEnabled(true);
        kilo_picker.setWrapSelectorWheel(true);
        kilo_picker.setAccessibilityDescriptionEnabled(true);

        boy_picker.setFadingEdgeEnabled(true);
        boy_picker.setScrollerEnabled(true);
        boy_picker.setWrapSelectorWheel(true);
        boy_picker.setAccessibilityDescriptionEnabled(true);


        yas_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                yas = newVal;
                Toast.makeText(DetailActivity.this, "Yaş : " + yas, Toast.LENGTH_LONG).show();
            }
        });


        kilo_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                kilo = newVal;
                Toast.makeText(DetailActivity.this, "Kilo : " + kilo, Toast.LENGTH_LONG).show();
            }
        });


        boy_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                boy = newVal;
                Toast.makeText(DetailActivity.this, "Boy : " + boy, Toast.LENGTH_LONG).show();
            }
        });

        cinsiyet.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                cinsiyetSonuc = newVal;
                Toast.makeText(DetailActivity.this, "Cinsiyet : " + cinsiyetSonuc, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == button_onayla){
            if (boy == 0){
                boy = boy_picker.getValue();
            }
            if (yas == 0){
                yas = yas_picker.getValue();
            }
            if (kilo == 0){
                kilo = kilo_picker.getValue();
            }
            if (cinsiyetSonuc == 0){
                cinsiyetSonuc = cinsiyet.getValue();
            }
            updateUser();
        }
    }

    void updateUser(){
        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(DetailActivity.this)
                .build();
        dialog.show();
        SessionManagement sessionManagement = new SessionManagement(DetailActivity.this);
        Users user = new Users();
        user.UsersId = sessionManagement.getSession();
        user.Email = sessionManagement.getEmail();
        user.Name = sessionManagement.getName();
        user.Salt = "";
        user.Password = "";
        user.Age = yas;
        user.Weight = kilo;
        user.Length = boy;
        if (cinsiyetSonuc == 1){
            user.Cinsiyet = true;
        }
        if(cinsiyetSonuc == 2){

            user.Cinsiyet = false;
        }
        user.Admin = false;

        compositeDisposable.add(iMyAPI.updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Bu email adresi kayitli degil")){
                                       // Main Activitye Yönlendir
                                       Toast.makeText(DetailActivity.this, s, Toast.LENGTH_LONG).show();
                                       getUser(s);
                                       moveToMainActivity();
                                   }
                                   else{
                                       Toast.makeText(DetailActivity.this, s, Toast.LENGTH_SHORT).show();
                                       dialog.dismiss();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   dialog.dismiss();
                                   Toast.makeText(DetailActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }
    private void moveToMainActivity(){
        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public Users getUser(String json) throws JSONException {
        currentUser = Users.JsontoObject(json);
        // Objeyi session oluşturmak için SessionManagement e gönder
        SessionManagement sessionManagement = new SessionManagement(DetailActivity.this);
        sessionManagement.saveSession(currentUser);
        return currentUser;
    }
}
