package com.example.sagliklibeslen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sagliklibeslen.Model.Besinler;
import com.example.sagliklibeslen.Model.GetGunlukModel;
import com.example.sagliklibeslen.Model.UserBesinModel;
import com.example.sagliklibeslen.Remote.IMyAPI;
import com.example.sagliklibeslen.Remote.RetrofitClient;
import com.example.sagliklibeslen.Session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BesinlerActivity extends AppCompatActivity {

    private BottomNavigationView mainNav;
    Intent intent;
    private List<Besinler> besinler;
    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    BesinAdapter besinAdp;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_besinler);
        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);
        tanimla();
        setTitle("Besinlerim");

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Session var mı yani giriş yapılmış mı kontrol et
        checkSession();
    }

    private void checkSession(){
        SessionManagement sessionManagement = new SessionManagement(BesinlerActivity.this);
        int UserID = sessionManagement.getSession();
        // Giriş yapılı değil o zaman Login ekranına yönlendir
        if (UserID == -1){
            moveToLoginActivity();
        }

        // Kullanıcı ID'si varsa kullanıcı bilgilerini getir
        else{
            getButunBesinler(UserID);
        }
    }

    // Login Aktivity e yönlendiren fonksiyon
    private void moveToLoginActivity() {
        Intent intent = new Intent(BesinlerActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    void tanimla(){
        mainNav = findViewById(R.id.main_nav);
        mainNav.setSelectedItemId(R.id.nav_foodpage);
        listView = findViewById(R.id.besinlerListview);



        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_homepage :
                        intent = new Intent(BesinlerActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.nav_foodpage :
                        return true;
                    case R.id.nav_settingspage :
                        intent = new Intent(BesinlerActivity.this, AyarlarActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.nav_alarm :
                        intent = new Intent(BesinlerActivity.this, AlarmActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_besinekle, menu);
        return true;
    }

    // Menülere tıklama eventini ayarlayan fonksiyon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.besinEkle) {
            intent = new Intent(BesinlerActivity.this, BesinEkleActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void getButunBesinler(int kid){

        compositeDisposable.add(iMyAPI.getButunBesinler(kid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Boyle bir kullanici yok") && !s.equals("Bos")){
                                       besinler = Besinler.JsonObject(s);
                                       besinAdp = new BesinAdapter(getApplicationContext(), besinler);
                                       listView.setAdapter(besinAdp);

                                       Log.d("Deneme", besinler.toString());
                                       //Toast.makeText(BesinlerActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                                   else{
                                       //Toast.makeText(BesinlerActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   //Toast.makeText(BesinlerActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }

}
