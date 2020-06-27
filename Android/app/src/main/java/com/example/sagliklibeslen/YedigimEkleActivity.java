package com.example.sagliklibeslen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.sagliklibeslen.Model.YedigiBesin;
import com.shawnlin.numberpicker.NumberPicker;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagliklibeslen.Model.Besinler;
import com.example.sagliklibeslen.Remote.IMyAPI;
import com.example.sagliklibeslen.Remote.RetrofitClient;
import com.example.sagliklibeslen.Session.SessionManagement;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class YedigimEkleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView hata_mesaji;
    private Spinner spinner;
    int curID, besinID;
    List<Besinler> besinler = new ArrayList<>();
    private NumberPicker porsiyon_picker;
    Button yedigim_ekle_button;
    int porsiyon = 1;
    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yedigim_ekle);
        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);
        tanimla();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Session var mı yani giriş yapılmış mı kontrol et
        checkSession();
    }

    private void checkSession(){
        SessionManagement sessionManagement = new SessionManagement(YedigimEkleActivity.this);
        int UserID = sessionManagement.getSession();
        // Giriş yapılı değil o zaman Login ekranına yönlendir
        if (UserID == -1){
            moveToLoginActivity();
        }

        // Kullanıcı ID'si varsa kullanıcı bilgilerini getir
        else{
            getButunBesinler(UserID);
            curID = UserID;
        }
    }

    // Login Aktivity e yönlendiren fonksiyon
    private void moveToLoginActivity() {
        Intent intent = new Intent(YedigimEkleActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    void tanimla(){
        hata_mesaji = findViewById(R.id.hata);
        yedigim_ekle_button = findViewById(R.id.yedigim_ekle);
        spinner = findViewById(R.id.spinntest);
        porsiyon_picker = findViewById(R.id.porsiyon_picker);
        porsiyon_picker.setMaxValue(10);
        porsiyon_picker.setMinValue(1);
        porsiyon_picker.setValue(1);


        porsiyon_picker.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {
                porsiyon = newVal;
                //Toast.makeText(YedigimEkleActivity.this, "Porsiyon : " + porsiyon, Toast.LENGTH_LONG).show();
            }
        });

        yedigim_ekle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atamaIslemleri();
            }
        });
    }

    void atamaIslemleri(){
        YedigiBesin yedigi = new YedigiBesin();
        if(besinID - 1 < 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(YedigimEkleActivity.this);
            builder.setTitle("Uyarı");
            builder.setMessage("Lütfen Yediğiniz Besini Seçiniz");
            builder.setNegativeButton("Tamam", null);
            builder.show();
        }else{
            yedigi.BesinlerId = besinler.get(besinID-1).BesinlerId;
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            yedigi.Date = convertStringToTimestamp(timestamp.toString());
            yedigi.usersId = curID;
            yedigi.Porsiyon = porsiyon;
            yedigimEkle(yedigi);
        }
    }

    private static Timestamp  convertStringToTimestamp(String something) {

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
    void yedigimEkle(final YedigiBesin besin){

        compositeDisposable.add(iMyAPI.yedigimEkle(besin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Hatali")){

                                       startActivity(new Intent(YedigimEkleActivity.this, OzetActivity.class));

                                   }
                                   else{
                                      // Toast.makeText(YedigimEkleActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                 //  Toast.makeText(YedigimEkleActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }
    void initDynamicListSpinner(final List<Besinler> besinler){
        List<CharSequence> liste = new ArrayList<>();
        liste.add("Lütfen Yediğiniz Besini Seçiniz");
        String formati;
        for (Besinler list : besinler) {
            formati = String.format("%.2f", list.besinKalori);
            liste.add(list.besinAdi + "  -->  " + formati + " cal");
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, liste);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    void initSelectionSpinner(){
        spinner = findViewById(R.id.spinntest);

        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        //Toast.makeText(this, spinner.getSelectedItemId() + " selected", Toast.LENGTH_SHORT).show();
        besinID = (int) spinner.getSelectedItemId();
    }

    void uyari(){
        hata_mesaji.setText("Lütfen Besin Seçiniz");
        hata_mesaji.setTextColor(Color.RED);

    }

    public void onNothingSelected(AdapterView<?> parent) {
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
                                       initDynamicListSpinner(besinler);
                                       initSelectionSpinner();

                                   }
                                   else{
                                      // Toast.makeText(YedigimEkleActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   //Toast.makeText(YedigimEkleActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }
}