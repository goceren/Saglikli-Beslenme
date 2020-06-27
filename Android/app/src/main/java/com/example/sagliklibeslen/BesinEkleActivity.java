package com.example.sagliklibeslen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagliklibeslen.Model.Besinler;
import com.example.sagliklibeslen.Remote.IMyAPI;
import com.example.sagliklibeslen.Remote.RetrofitClient;
import com.example.sagliklibeslen.Session.SessionManagement;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BesinEkleActivity extends AppCompatActivity implements View.OnClickListener{


    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean edit = false;
    EditText editText_besin_protein, editText_besin_yag, editText_besin_adi, editText_besin_kalori, editText_besin_id, editText_besin_karbonhidrat;
    Button button_barkod_oku, button_besin_ekle;
    String text_besin_id, text_besin_adi;
    TextView hata_mesaji;
    static int curUser;
    float text_besin_protein, text_besin_karbonhidrat, text_besin_yag, text_besin_kalori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_besin_ekle);

        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);
        tanimla();
        setTitle("Besin Ekle");

    }


    @Override
    protected void onStart() {
        super.onStart();
        // Session var mı yani giriş yapılmış mı kontrol et
        checkSession();
    }

    private void checkSession(){
        SessionManagement sessionManagement = new SessionManagement(BesinEkleActivity.this);
        int UserID = sessionManagement.getSession();
        // Giriş yapılı değil o zaman Login ekranına yönlendir
        if (UserID == -1){
            moveToLoginActivity();
        }

        // Kullanıcı ID'si varsa kullanıcı bilgilerini getir
        else{
            curUser = UserID;
        }
    }
    private void moveToLoginActivity() {
        Intent intent = new Intent(BesinEkleActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    void tanimla() {
        editText_besin_protein = findViewById(R.id.besin_protein);
        editText_besin_yag = findViewById(R.id.besin_yag);
        editText_besin_adi = findViewById(R.id.besin_adi);
        editText_besin_kalori = findViewById(R.id.besin_kalori);
        editText_besin_id = findViewById(R.id.besin_id);
        editText_besin_karbonhidrat = findViewById(R.id.besin_karbonhidrat);
        editText_besin_id.setEnabled(false);
        button_barkod_oku = findViewById(R.id.barkod_oku_button);
        button_besin_ekle = findViewById(R.id.besin_ekle_button);
        button_barkod_oku.setOnClickListener(this);
        button_besin_ekle.setOnClickListener(this);
        if(!edit){
            editText_besin_id.setText("0");
        }
    }

    @Override
    public void onClick(View v) {
        if( v == button_barkod_oku){
            final IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setCameraId(0);
            //intentIntegrator.setBarcodeImageEnabled(true);
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.initiateScan();
        }

        if( v == button_besin_ekle){
            atama_islemleri();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                hata_mesaji.setText("Barkod Numarası Boş");
            } else {

                String scanContent = result.getContents();
                String scanFormat = result.getFormatName();
                editText_besin_id.setText(scanContent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    void besinEkle(Besinler besin){

        compositeDisposable.add(iMyAPI.besinEkle(besin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Hatali")){
                                       startActivity(new Intent(BesinEkleActivity.this, BesinlerActivity.class));
                                   }
                                   else{
                                       //Toast.makeText(BesinEkleActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   //Toast.makeText(BesinEkleActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }


    void atama_islemleri() {
        text_besin_id = editText_besin_id.getText().toString();
        text_besin_adi = editText_besin_adi.getText().toString();
        text_besin_protein = Float.parseFloat(editText_besin_protein.getText().toString());
        text_besin_karbonhidrat = Float.parseFloat(editText_besin_karbonhidrat.getText().toString());
        text_besin_yag = Float.parseFloat(editText_besin_yag.getText().toString());
        text_besin_kalori = Float.parseFloat(editText_besin_kalori.getText().toString());
        if(text_besin_adi.isEmpty()){
            uyari();
        }
        else{
            Besinler besin = new Besinler();
            besin.besinAdi = text_besin_adi;
            besin.KullaniciId = curUser;
            besin.besinKalori = text_besin_kalori;
            besin.besinKarbonhidrat = text_besin_karbonhidrat;
            besin.barkodNo = text_besin_id;
            besin.besinProtein =text_besin_protein;
            besin.besinYag =text_besin_yag;
            besin.ozel = true;
            besinEkle(besin);
        }
    }

    void uyari(){
        hata_mesaji.setText("Lütfen Barkod Numarası veya Besin adı girdiğinizden emin olunuz.");
        hata_mesaji.setTextColor(Color.RED);

    }

    void temizle() {
        editText_besin_id.setText("");
        editText_besin_adi.setText("");
        editText_besin_protein.setText("");
        editText_besin_karbonhidrat.setText("");
        editText_besin_yag.setText("");
        editText_besin_kalori.setText("");
    }


}
