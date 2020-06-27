package com.example.sagliklibeslen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagliklibeslen.Model.GetGunlukModel;
import com.example.sagliklibeslen.Model.UserBesinModel;
import com.example.sagliklibeslen.Model.Users;
import com.example.sagliklibeslen.Remote.IMyAPI;
import com.example.sagliklibeslen.Remote.RetrofitClient;
import com.example.sagliklibeslen.Session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private BottomNavigationView mainNav;
    private Calendar calendar;
    private DatePickerDialog dateTimePicker;
    private FloatingActionButton fab;

    private ProgressBar kaloriProgress, proteinProggress, karbonhidratProgress, yagProgress;
    private TextView  proteinyuzde, kaloriyuzde, ozet, detaylar, yagYuzde, karbonhidratYuzde, alinanProtein, gunlukProtein, alinanKalori, gunlukKalori, alinanYag, gunlukYag, alinanKarbonhidrat, gunlukKarbonhidrat;

    Intent intent;
    private UserBesinModel currentUser;
    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);
        tanimla();

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
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        int UserID = sessionManagement.getSession();
        // Giriş yapılı değil o zaman Login ekranına yönlendir
        if (UserID == -1){
            moveToLoginActivity();
        }

        // Kullanıcı ID'si varsa kullanıcı bilgilerini getir
        else{
            Date now = new Date();
            long time = now.getTime();
            Timestamp Date = new Timestamp(time);
            GetGunlukModel model = new GetGunlukModel();
            model.UsersId = UserID;
            model.Date = Date;
            getGunlukYeme(model);

        }
    }

    // Login Aktivity e yönlendiren fonksiyon
    private void moveToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // Tanımlamaları yapan fonksiyon
    void tanimla(){
        fab = findViewById(R.id.main_add_fab);
        ozet = findViewById(R.id.ozet);
        ozet.setOnClickListener(this);
        detaylar = findViewById(R.id.detaylar);
        detaylar.setOnClickListener(this);
        yagYuzde = findViewById(R.id.yagyuzde);
        karbonhidratYuzde = findViewById(R.id.karbonhidratyuzde);
        kaloriyuzde = findViewById(R.id.kaloriyuzde);
        proteinyuzde = findViewById(R.id.proteinyuzde);
        kaloriProgress = findViewById(R.id.kaloriprogressBar);
        proteinProggress = findViewById(R.id.proteinprogressBar);
        yagProgress = findViewById(R.id.yagprogress);
        karbonhidratProgress = findViewById(R.id.karbonhidratprogress);
        alinanProtein = findViewById(R.id.protein_alinan);
        gunlukProtein = findViewById(R.id.protein_gunluk);
        gunlukKalori = findViewById(R.id.kalori_gunluk);
        alinanKalori = findViewById(R.id.kalori_alinan);
        alinanYag = findViewById(R.id.yag_alinan);
        gunlukYag = findViewById(R.id.yag_gunluk);
        alinanKarbonhidrat = findViewById(R.id.karbonhidrat_alinan);
        gunlukKarbonhidrat = findViewById(R.id.karbonhidrat_gunluk);
        yagProgress.setMax(100);
        karbonhidratProgress.setMax(100);
        kaloriProgress.setMax(100);
        proteinProggress.setMax(100);

        mainNav = findViewById(R.id.main_nav);
        mainNav.setSelectedItemId(R.id.nav_homepage);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_homepage :
                        return true;
                    case R.id.nav_foodpage :
                        intent = new Intent(MainActivity.this, BesinlerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.nav_settingspage :
                        intent = new Intent(MainActivity.this, AyarlarActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.nav_alarm :
                        intent = new Intent(MainActivity.this, AlarmActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, YedigimEkleActivity.class));
            }
        });
    }
    // Menüleri oluşturan fonksiyon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_navigation, menu);
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    // Menülere tıklama eventini ayarlayan fonksiyon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            logout();
            return true;
        }
        if (id == R.id.dateTime) {
            dateTimePick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Tarih seçme ekranını başlatan fonksiyon
    void dateTimePick(){
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateTimePicker = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog ,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String Month;
                String Day;
                if ((month+1) < 10){
                     Month = "0" + (month+1);
                }else{
                    Month = Integer.toString((month+1));
                }
                if (day < 10){
                    Day = "0" + day;
                }
                else{
                    Day = Integer.toString(day);
                }
                String date = year + "-" + Month + "-" + Day + " "+ "00:00:00.000";
                Timestamp Date = convertStringToTimestamp(date);
                GetGunlukModel model = new GetGunlukModel();
                SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
                int UserID3 = sessionManagement.getSession();
                model.UsersId = UserID3;
                model.Date = Date;
                // BAŞLIK
                String sDate1 = Day + "/" + Month + "/" + year;
                Date date1= null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                Date current = new Date();
                setTitle(format.format(date1));
                getGunlukYeme(model);
            }
        },year, month, day);
        dateTimePicker.show();
    }

    void getGunlukYeme(GetGunlukModel model){

        compositeDisposable.add(iMyAPI.getGunlukByDate(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Boyle bir kullanici yok") && !s.equals("Bos")){
                                       currentUser = UserBesinModel.JsontoObject(s);
                                       Log.d("Deneme", currentUser.toString());
                                       setItems();
                                       //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                                   else{
                                       //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   //Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }

    // ÇIKIŞ YAP FONKSIYONU
    private void logout(){
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        sessionManagement.removeSession();
        moveToLoginActivity();
    }

    @Override
    public void onClick(View view) {
        if(view == ozet){
            startActivity(new Intent(MainActivity.this, OzetActivity.class));
        }
        if(view == detaylar){
            startActivity(new Intent(MainActivity.this, OzetActivity.class));
        }
    }

    void setItems(){

        int yuzdeKalori = (int)((100 * currentUser.ToplamKalori) / currentUser.LimitKalori);
        kaloriyuzde.setText("%"+yuzdeKalori);
        kaloriProgress.setProgress(yuzdeKalori);
        String st_gunluk_Kalori = "" + (int)currentUser.LimitKalori + " kcal";
        String st_alinan_kalori = "" + (int)currentUser.ToplamKalori + " kcal";
        gunlukKalori.setText(st_gunluk_Kalori);
        alinanKalori.setText(st_alinan_kalori);
        int yuzdeProtein = (int) (100 * ((currentUser.ToplamProtein ) / (currentUser.UstLimitProtein / 4)));
        if(yuzdeProtein < 0){
            yuzdeProtein = 0;
        }
        proteinyuzde.setText("%"+yuzdeProtein);
        proteinProggress.setProgress(yuzdeProtein);
        String st_gunluk_protein = "" + (int)(currentUser.AltLimitProtein / 4) + " - " + (int)(currentUser.UstLimitProtein / 4) + " gram" ;
        String st_alinan_protein = "" + (int)currentUser.ToplamProtein + " gram";
        gunlukProtein.setText(st_gunluk_protein);
        alinanProtein.setText(st_alinan_protein);


        int yuzdeYag = (int) (100 * ((currentUser.ToplamYag ) / (currentUser.UstLimitYag / 9)));
        if(yuzdeYag < 0){
            yuzdeYag = 0;
        }
        yagYuzde.setText("%"+yuzdeYag);
        yagProgress.setProgress(yuzdeYag);
        String st_gunluk_yag = "" + (int)(currentUser.AltLimitYag / 9) + " - " + (int)(currentUser.UstLimitYag / 9) + " gram" ;
        String st_alinan_yag = "" + (int)currentUser.ToplamYag + " gram";
        gunlukYag.setText(st_gunluk_yag);
        alinanYag.setText(st_alinan_yag);

        int yuzdeKarbon = (int) (100 * ((currentUser.ToplamKarbonhidrat ) / (currentUser.UstLimitKarbon / 4)));
        if(yuzdeKarbon < 0){
            yuzdeKarbon = 0;
        }
        karbonhidratYuzde.setText("%"+yuzdeKarbon);
        karbonhidratProgress.setProgress(yuzdeKarbon);
        String st_gunluk_kar = "" + (int)(currentUser.AltLimitKarbon / 4) + " - " + (int)(currentUser.UstLimitKarbon / 4) + " gram" ;
        String st_alinan_kar = "" + (int)currentUser.ToplamKarbonhidrat + " gram";
        gunlukKarbonhidrat.setText(st_gunluk_kar);
        alinanKarbonhidrat.setText(st_alinan_kar);


        if (yuzdeKalori > 100){
            kaloriyuzde.setTextSize(16);
            kaloriyuzde.setTextColor(Color.RED);
        }else{
            kaloriyuzde.setTextSize(16);
            kaloriyuzde.setTextColor(Color.WHITE);
        }

        if (yuzdeKarbon > 100){
            karbonhidratYuzde.setTextSize(16);
            karbonhidratYuzde.setTextColor(Color.RED);
        }else{
            karbonhidratYuzde.setTextSize(16);
            karbonhidratYuzde.setTextColor(Color.WHITE);
        }

        if (yuzdeYag > 100){
            yagYuzde.setTextSize(16);
            yagYuzde.setTextColor(Color.RED);
        }else{
            yagYuzde.setTextSize(16);
            yagYuzde.setTextColor(Color.WHITE);
        }

        if (yuzdeProtein > 100){
            proteinyuzde.setTextSize(16);
            proteinyuzde.setTextColor(Color.RED);
        }else{
            proteinyuzde.setTextSize(16);
            proteinyuzde.setTextColor(Color.WHITE);
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

}
