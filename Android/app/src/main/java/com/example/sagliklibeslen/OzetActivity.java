package com.example.sagliklibeslen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagliklibeslen.Model.Besinler;
import com.example.sagliklibeslen.Model.BesinlerModel;
import com.example.sagliklibeslen.Model.GetGunlukModel;
import com.example.sagliklibeslen.Model.UserBesinModel;
import com.example.sagliklibeslen.Remote.IMyAPI;
import com.example.sagliklibeslen.Remote.RetrofitClient;
import com.example.sagliklibeslen.Session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OzetActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private BottomNavigationView mainNav;
    Intent intent;
    private Calendar calendar;
    private List<Besinler> besinler;
    IMyAPI iMyAPI;
    private DatePickerDialog dateTimePicker;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    OzetAdapter ozetAdapter;
    ListView listView;
    TextView kaloriyuzde, alinanKalori, gunlukKalori;
    private UserBesinModel currentUser;
    private ProgressBar kaloriProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ozet);
        tanimla();
        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);
        setTitle("Bugün");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Session var mı yani giriş yapılmış mı kontrol et
        checkSession();
    }

    private void checkSession(){
        SessionManagement sessionManagement = new SessionManagement(OzetActivity.this);
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
        Intent intent = new Intent(OzetActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    void tanimla(){
        mainNav = findViewById(R.id.main_nav);
        mainNav.setSelectedItemId(R.id.nav_foodpage);
        listView = findViewById(R.id.ozetlistview);


        kaloriyuzde = findViewById(R.id.kaloriyuzde);
        kaloriProgress = findViewById(R.id.kaloriprogressBar);
        alinanKalori = findViewById(R.id.kalori_alinan);
        kaloriProgress.setMax(100);
        gunlukKalori = findViewById(R.id.kalori_gunluk);
        alinanKalori = findViewById(R.id.kalori_alinan);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_homepage :
                        intent = new Intent(OzetActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.nav_foodpage :
                        return true;
                    case R.id.nav_settingspage :
                        intent = new Intent(OzetActivity.this, AyarlarActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.nav_alarm :
                        intent = new Intent(OzetActivity.this, AlarmActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });


        fab = findViewById(R.id.main_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OzetActivity.this, YedigimEkleActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_navigation, menu);
        return true;
    }

    // Menülere tıklama eventini ayarlayan fonksiyon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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

        dateTimePicker = new DatePickerDialog(OzetActivity.this, android.R.style.Theme_Material_Light_Dialog ,new DatePickerDialog.OnDateSetListener() {
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
                SessionManagement sessionManagement = new SessionManagement(OzetActivity.this);
                int UserID3 = sessionManagement.getSession();
                model.UsersId = UserID3;
                model.Date = Date;
                // BAŞLIK
                String sDate1 = Day + "/" + Month + "/" + year;
                java.util.Date date1= null;
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
                                       List<BesinlerModel> model = new ArrayList<>();
                                       model.addAll(currentUser.BesinlerModel);
                                       ozetAdapter = new OzetAdapter(getApplicationContext(), model);
                                       listView.setAdapter(ozetAdapter);
                                       //Toast.makeText(OzetActivity.this, s, Toast.LENGTH_SHORT).show();
                                       setItems();
                                   }
                                   else{
                                       //Toast.makeText(OzetActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   //Toast.makeText(OzetActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }

    void setItems() {

        int yuzdeKalori = (int) ((100 * currentUser.ToplamKalori) / currentUser.LimitKalori);
        kaloriyuzde.setText("%" + yuzdeKalori);
        kaloriProgress.setProgress(yuzdeKalori);
        String st_gunluk_Kalori = "" + (int) currentUser.LimitKalori + " kcal";
        String st_alinan_kalori = "" + (int) currentUser.ToplamKalori + " kcal";
        gunlukKalori.setText(st_gunluk_Kalori);
        alinanKalori.setText(st_alinan_kalori);

        if (yuzdeKalori > 100){
            kaloriyuzde.setTextSize(16);
            kaloriyuzde.setTextColor(Color.RED);
        }else{
            kaloriyuzde.setTextSize(16);
            kaloriyuzde.setTextColor(Color.WHITE);
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