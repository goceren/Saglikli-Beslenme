package com.example.sagliklibeslen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagliklibeslen.Model.Besinler;
import com.example.sagliklibeslen.Remote.IMyAPI;
import com.example.sagliklibeslen.Remote.RetrofitClient;
import com.example.sagliklibeslen.Session.SessionManagement;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BesinGuncelle extends AppCompatActivity implements View.OnClickListener{
    Besinler besin = new Besinler();
    Besinler gelenBesin = new Besinler();
    int besininKullanicisi;
    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean edit = false;
    EditText editText_besin_protein, editText_besin_yag, editText_besin_adi, editText_besin_kalori, editText_besin_id, editText_besin_karbonhidrat;
    Button button_barkod_oku, guncelle_button;
    String text_besin_id, text_besin_adi;
    TextView hata_mesaji;
    static int curUser;
    double text_besin_protein, text_besin_karbonhidrat, text_besin_yag, text_besin_kalori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_besin_guncelle);
        setTitle("Besin Güncelle");
        besin = (Besinler) getIntent().getSerializableExtra("obj");
        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);
        tanimla();
        besinGetir(besin.BesinlerId);

    }

    void yenile(){
        editText_besin_adi.setText(gelenBesin.besinAdi);

        DecimalFormat df = new DecimalFormat("#.000");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(sym);

        editText_besin_kalori.setText("" +  df.format(gelenBesin.besinKalori));
        editText_besin_karbonhidrat.setText("" +  df.format(gelenBesin.besinKarbonhidrat));
        editText_besin_protein.setText("" + df.format(gelenBesin.besinProtein));
        editText_besin_yag.setText("" + df.format(gelenBesin.besinYag));
        if(gelenBesin.barkodNo == "0"){
            editText_besin_id.setText(gelenBesin.BesinlerId);
        }else{
            editText_besin_id.setText(gelenBesin.barkodNo);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_besinsil, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.besinGuncelle) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BesinGuncelle.this);
            builder.setTitle("Emin misiniz ?");
            builder.setMessage(besin.besinAdi + " isimli besini silmek istediğinize emin misiniz ?");
            builder.setNegativeButton("Hayır", null);
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(curUser == besininKullanicisi){
                        besinSil(besin.BesinlerId);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(BesinGuncelle.this);
                        builder.setTitle("Yetkiniz yok...");
                        builder.setMessage("Silmeye Çalıştığınız besin herkeste görünen temel besinlerdir ve kullanıcıların silme yetkisi yoktur.");
                        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(BesinGuncelle.this, BesinlerActivity.class));
                            }
                        });
                        builder.show();
                    }
                }
            });
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Session var mı yani giriş yapılmış mı kontrol et
        checkSession();
    }

    private void checkSession(){
        SessionManagement sessionManagement = new SessionManagement(BesinGuncelle.this);
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
        Intent intent = new Intent(BesinGuncelle.this, LoginActivity.class);
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
        guncelle_button = findViewById(R.id.guncelle);
        button_barkod_oku.setOnClickListener(this);
        guncelle_button.setOnClickListener(this);
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

        if( v == guncelle_button){
            if(curUser == besininKullanicisi){
                atama_islemleri();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(BesinGuncelle.this);
                builder.setTitle("Yetkiniz yok...");
                builder.setMessage("Güncellemeye Çalıştığınız besin herkeste görünen temel besinlerdir ve kullanıcıların güncelleme yetkisi yoktur. Besin güncellemek yerine eklemeyi tercih ediniz.");
                builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(BesinGuncelle.this, BesinlerActivity.class));
                    }
                });
                builder.show();
            }
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


    void besinGuncelle(Besinler besin){

        compositeDisposable.add(iMyAPI.besinGuncelle(besin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Hatali")){
                                       finish();
                                       startActivity(getIntent());
                                       //Toast.makeText(BesinGuncelle.this, "Güncelleme Başarılı", Toast.LENGTH_SHORT).show();

                                   }
                                   else{
                                       //Toast.makeText(BesinGuncelle.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   //Toast.makeText(BesinGuncelle.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }

    void besinGetir(int besinId){

        compositeDisposable.add(iMyAPI.besinGetir(besinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Hatali")){
                                       gelenBesin = Besinler.JsonObject2(s);
                                       besininKullanicisi = gelenBesin.KullaniciId;
                                       yenile();
                                   }
                                   else{
                                      // Toast.makeText(BesinGuncelle.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                  // Toast.makeText(BesinGuncelle.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }

    void besinSil(int bid){

        compositeDisposable.add(iMyAPI.besinSil(bid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Hatali")){
                                       startActivity(new Intent(BesinGuncelle.this, BesinlerActivity.class));
                                      // Toast.makeText(BesinGuncelle.this, "Başarılı", Toast.LENGTH_SHORT).show();

                                   }
                                   else{
                                       //Toast.makeText(BesinGuncelle.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   //Toast.makeText(BesinGuncelle.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }


    void atama_islemleri() {
        text_besin_id = editText_besin_id.getText().toString();
        text_besin_adi = editText_besin_adi.getText().toString();
        text_besin_protein = Double.parseDouble(editText_besin_protein.getText().toString());
        text_besin_karbonhidrat = Double.parseDouble(editText_besin_karbonhidrat.getText().toString());
        text_besin_yag = Double.parseDouble(editText_besin_yag.getText().toString());
        text_besin_kalori = Double.parseDouble(editText_besin_kalori.getText().toString());
        if(text_besin_adi.isEmpty()){
            uyari();
        }
        else{
            Besinler besinx = new Besinler();
            besinx.BesinlerId = besin.BesinlerId;
            besinx.besinAdi = text_besin_adi;
            besinx.KullaniciId = curUser;
            besinx.besinKalori = text_besin_kalori;
            besinx.besinKarbonhidrat = text_besin_karbonhidrat;
            besinx.barkodNo = text_besin_id;
            besinx.besinProtein =text_besin_protein;
            besinx.besinYag =text_besin_yag;
            besinx.ozel = true;

            besinGuncelle(besinx);

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
