package com.example.sagliklibeslen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sagliklibeslen.Model.GetGunlukModel;
import com.example.sagliklibeslen.Model.Users;
import com.example.sagliklibeslen.Remote.IMyAPI;
import com.example.sagliklibeslen.Remote.RetrofitClient;
import com.example.sagliklibeslen.Session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shawnlin.numberpicker.NumberPicker;

import java.sql.Timestamp;
import java.util.Date;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AyarlarActivity extends AppCompatActivity implements View.OnClickListener{

    private BottomNavigationView mainNav;
    Intent intent;
    private NumberPicker boy_picker, kilo_picker, yas_picker, cinsiyet;
    Button button_onayla;
    int yas = 0;
    int kilo, boy, cinsiyetSonuc;
    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    Users user = new Users();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);
        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);

    }


    // Anasayfa açıldığında session kontrolü yapılacak
    @Override
    protected void onStart() {
        super.onStart();
        // Session var mı yani giriş yapılmış mı kontrol et
        checkSession();
    }


    // Session kontrolü yapan fonksiyon
    private void checkSession(){
        SessionManagement sessionManagement = new SessionManagement(AyarlarActivity.this);
        int UserID = sessionManagement.getSession();
        // Giriş yapılı değil o zaman Login ekranına yönlendir
        if (UserID == -1){
            moveToLoginActivity();
        }

        // Kullanıcı ID'si varsa kullanıcı bilgilerini getir
        else{
            getUser(UserID);
        }
    }

    // Login Aktivity e yönlendiren fonksiyon
    private void moveToLoginActivity() {
        Intent intent = new Intent(AyarlarActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    void tanimla(){
        mainNav = findViewById(R.id.main_nav);
        mainNav.setSelectedItemId(R.id.nav_settingspage);
        button_onayla = findViewById(R.id.button_onayla);
        button_onayla.setOnClickListener(this);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_homepage :
                        intent = new Intent(AyarlarActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.nav_foodpage :
                        intent = new Intent(AyarlarActivity.this, BesinlerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.nav_settingspage :
                        return true;
                    case R.id.nav_alarm :
                        intent = new Intent(AyarlarActivity.this, AlarmActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        yas_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                yas = newVal;
                //Toast.makeText(AyarlarActivity.this, "Yaş : " + yas, Toast.LENGTH_LONG).show();
            }
        });


        kilo_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                kilo = newVal;
                //Toast.makeText(AyarlarActivity.this, "Kilo : " + kilo, Toast.LENGTH_LONG).show();
            }
        });


        boy_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                boy = newVal;
                //Toast.makeText(AyarlarActivity.this, "Boy : " + boy, Toast.LENGTH_LONG).show();
            }
        });

        cinsiyet.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                cinsiyetSonuc = newVal;
                //Toast.makeText(AyarlarActivity.this, "Cinsiyet : " + cinsiyetSonuc, Toast.LENGTH_LONG).show();
            }
        });
    }

    void getUser(int id){

        compositeDisposable.add(iMyAPI.getUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Boyle bir kullanici yok")){
                                       user = Users.JsontoObject(s);
                                       setText(user);
                                       tanimla();

                                   }
                                   else{
                                       //Toast.makeText(AyarlarActivity.this, s, Toast.LENGTH_SHORT).show();

                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {

                                   //Toast.makeText(AyarlarActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }

    void setText(Users user){
        button_onayla = findViewById(R.id.button_onayla);
        button_onayla.setOnClickListener(this);
        yas_picker = findViewById(R.id.yas_picker);
        kilo_picker = findViewById(R.id.kilo_picker);
        boy_picker = findViewById(R.id.boy_picker);
        cinsiyet = findViewById(R.id.cinsiyet);

        yas_picker.setMaxValue(95);
        yas_picker.setMinValue(10);
        yas_picker.setValue(user.Age);

        kilo_picker.setMaxValue(120);
        kilo_picker.setMinValue(30);
        kilo_picker.setValue((int)user.Weight);

        boy_picker.setMaxValue(215);
        boy_picker.setMinValue(120);
        boy_picker.setValue((int)user.Length);

        final String[] cinsiyetList = {"Erkek", "Kadın"};
        cinsiyet.setMinValue(1);
        cinsiyet.setMaxValue(cinsiyetList.length);
        cinsiyet.setDisplayedValues(cinsiyetList);
        if(user.Cinsiyet){
            cinsiyet.setValue(1);
        }else{
            cinsiyet.setValue(2);
        }

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
    }

    @Override
    public void onClick(View v) {
        if(v == button_onayla){
            Users updatedUser = new Users();
            updatedUser.UsersId = user.UsersId;
            updatedUser.Email = user.Email;
            updatedUser.Name = user.Name;
            updatedUser.Salt = "";
            updatedUser.Password = "";
            updatedUser.Age = yas_picker.getValue();
            updatedUser.Weight = kilo_picker.getValue();
            updatedUser.Length = boy_picker.getValue();
            if (cinsiyetSonuc == 1){
                updatedUser.Cinsiyet = true;
            }
            if(cinsiyetSonuc == 2){

                updatedUser.Cinsiyet = false;
            }
            updatedUser.Admin = false;

            updateUser(updatedUser);
        }
    }
    void updateUser(Users updatedUser) {
        compositeDisposable.add(iMyAPI.updateUser(updatedUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Bu email adresi kayitli degil")) {
                                       //Toast.makeText(AyarlarActivity.this, s, Toast.LENGTH_LONG).show();
                                       androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(AyarlarActivity.this);
                                       builder.setTitle("Bilgileriniz Güncellendi");
                                       builder.setMessage(s);
                                       builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface, int i) {
                                               startActivity(new Intent(AyarlarActivity.this, BesinlerActivity.class));
                                           }
                                       });
                                       builder.show();
                                   } else {
                                       //Toast.makeText(AyarlarActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   //Toast.makeText(AyarlarActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }
}
