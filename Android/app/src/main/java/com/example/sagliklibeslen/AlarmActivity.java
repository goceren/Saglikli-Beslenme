package com.example.sagliklibeslen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagliklibeslen.Model.BesinlerModel;
import com.example.sagliklibeslen.Model.GetGunlukModel;
import com.example.sagliklibeslen.Model.GetSu;
import com.example.sagliklibeslen.Model.Su;
import com.example.sagliklibeslen.Model.UserBesinModel;
import com.example.sagliklibeslen.Model.YedigiBesin;
import com.example.sagliklibeslen.Remote.IMyAPI;
import com.example.sagliklibeslen.Remote.RetrofitClient;
import com.example.sagliklibeslen.Session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shawnlin.numberpicker.NumberPicker;

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

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class AlarmActivity extends AppCompatActivity {

    private BottomNavigationView mainNav;
    Intent intent;
    Dialog dialog;
    int curUser, bardakSayi = 1;
    private FloatingActionButton fab;
    Button kaydet;
    List<Su> suModel = new ArrayList<>();
    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SuAdapter suAdapter;
    ListView listView;
    TextView suYuzde, alinanSu, gunlukSu;
    private UserBesinModel currentUser;
    private ProgressBar suProgress;
    private DatePickerDialog dateTimePicker;
    private Calendar calendar;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        tanimla();
        setTitle("Su Tüketimi");
        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);

        scheduleNotification(getNotification( "Üşenmeyin sağlığınız için bir bardak su için :)" ));

    }

    private void scheduleNotification (Notification notification) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        Calendar cal_alarm = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0 , 1000, pendingIntent);
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Su Hatırlatıcı" ) ;
        builder.setContentText(content) ;
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setSmallIcon(R.drawable. ic_baseline_insert_emoticon_24 ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId(NOTIFICATION_CHANNEL_ID) ;
        return builder.build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Session var mı yani giriş yapılmış mı kontrol et
        checkSession();
    }


    // Session kontrolü yapan fonksiyon
    private void checkSession(){
        SessionManagement sessionManagement = new SessionManagement(AlarmActivity.this);
        int UserID = sessionManagement.getSession();
        // Giriş yapılı değil o zaman Login ekranına yönlendir
        if (UserID == -1){
            moveToLoginActivity();
        }

        // Kullanıcı ID'si varsa kullanıcı bilgilerini getir
        else{
           curUser = UserID;
            Date now = new Date();
            long time = now.getTime();
            Timestamp Date = new Timestamp(time);
            GetSu model = new GetSu();
            model.kullanici = UserID;
            model.Date = Date;
            getGunlukSu(model);
        }
    }


    // Login Aktivity e yönlendiren fonksiyon
    private void moveToLoginActivity() {
        Intent intent = new Intent(AlarmActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    void tanimla() {
        mainNav = findViewById(R.id.main_nav);
        mainNav.setSelectedItemId(R.id.nav_alarm);
        fab = findViewById(R.id.main_add_fab);
        listView = findViewById(R.id.sulistview);

        suYuzde = findViewById(R.id.suyuzde);
        suProgress = findViewById(R.id.waterprogressBar);
        alinanSu = findViewById(R.id.su_alinan);
        suProgress.setMax(100);
        gunlukSu = findViewById(R.id.su_gunluk);
        alinanSu = findViewById(R.id.su_alinan);



        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_homepage:
                        intent = new Intent(AlarmActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.nav_foodpage:
                        intent = new Intent(AlarmActivity.this, BesinlerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.nav_settingspage:
                        intent = new Intent(AlarmActivity.this, AyarlarActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.nav_alarm:
                        return true;
                    default:
                        return false;
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AlarmActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_su);
                kaydet = dialog.findViewById(R.id.su_kaydet);
                NumberPicker su_picker = dialog.findViewById(R.id.bardak_picker);

                su_picker.setMaxValue(10);
                su_picker.setMinValue(1);
                su_picker.setValue(1);

                su_picker.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {
                        bardakSayi = newVal;
                        //Toast.makeText(AlarmActivity.this, "Porsiyon : " + bardakSayi, Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();

                kaydet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Su icilenSu = new Su();
                        icilenSu.Kullanici = curUser;
                        icilenSu.bardakSayisi = bardakSayi;
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        icilenSu.Date = convertStringToTimestamp(timestamp.toString());
                        //Toast.makeText(AlarmActivity.this, icilenSu.toString(), Toast.LENGTH_SHORT).show();
                        suEkle(icilenSu);
                        finish();
                        startActivity(getIntent());
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.dateTime) {
            dateTimePick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void dateTimePick(){
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateTimePicker = new DatePickerDialog(AlarmActivity.this, android.R.style.Theme_Material_Light_Dialog ,new DatePickerDialog.OnDateSetListener() {
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
                GetSu model = new GetSu();
                SessionManagement sessionManagement = new SessionManagement(AlarmActivity.this);
                int UserID3 = sessionManagement.getSession();
                model.kullanici = UserID3;
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
                getGunlukSu(model);
            }
        },year, month, day);
        dateTimePicker.show();
    }
    void suEkle(final Su su){

        compositeDisposable.add(iMyAPI.suEkle(su)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Hatali")){

                                       //Toast.makeText(AlarmActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                                   else{
                                       //Toast.makeText(AlarmActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   //Toast.makeText(AlarmActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }

    void getGunlukSu(final GetSu model){

        compositeDisposable.add(iMyAPI.getGunlukSu(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Boyle bir kullanici yok") && !s.equals("Bos")){
                                       suModel = Su.JsonObject(s);
                                       Log.d("Deneme", suModel.toString());

                                       suAdapter = new SuAdapter(getApplicationContext(), suModel);
                                       listView.setAdapter(suAdapter);
                                       //Toast.makeText(AlarmActivity.this, s, Toast.LENGTH_SHORT).show();
                                       GetGunlukModel Getmodel = new GetGunlukModel();
                                       Getmodel.Date = model.Date;
                                       Getmodel.UsersId = model.kullanici;
                                       getGunlukYeme(Getmodel);
                                   }
                                   else{
                                       //Toast.makeText(AlarmActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   //Toast.makeText(AlarmActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
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
                                       setItems();
                                   }
                                   else{
                                       //Toast.makeText(AlarmActivity.this, s, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   //Toast.makeText(AlarmActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }

    void setItems() {
        double ToplamSu = 0;
        for (Su sux : suModel) {
            ToplamSu += sux.bardakSayisi * 0.250;
        }
        int yuzdeSu = (int) ((100 * ToplamSu) / currentUser.SuLimit);
        suYuzde.setText("%" + yuzdeSu);
        suProgress.setProgress(yuzdeSu);
        String st_gunluk_Su = "" + String.format("%.2f", currentUser.SuLimit) + " Litre";
        String st_alinan_Su = "" + (double) ToplamSu + " Litre";
        gunlukSu.setText(st_gunluk_Su);
        alinanSu.setText(st_alinan_Su);

        if (yuzdeSu > 100){
            suYuzde.setTextSize(16);
            suYuzde.setTextColor(Color.RED);
        }else{
            suYuzde.setTextSize(16);
            suYuzde.setTextColor(Color.WHITE);
        }

    }

}
