package com.example.sagliklibeslen;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.sagliklibeslen.Model.Users;
import com.example.sagliklibeslen.Remote.IMyAPI;
import com.example.sagliklibeslen.Remote.RetrofitClient;
import com.example.sagliklibeslen.Session.SessionManagement;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton image_register;
    private Button button_register, button_login;
    private EditText editText_email, editText_password;
    private Users currentUser;

    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tanimla();
        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Session var mı yani giriş yapılmış mı kontrol et ve eğer giriş yapılmışsa MainActivitye yolla
        checkSession();
    }

    private void checkSession(){
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        int UserID = sessionManagement.getSession();
        if (UserID != -1){
            // Giriş yapılı o zaman MainActivityi Aç
            moveToMainActivity();
        }
    }

    private void tanimla(){
        editText_email = (EditText)findViewById(R.id.email);
        editText_password = (EditText)findViewById(R.id.password);
        image_register = (ImageButton)findViewById(R.id.img_register);
        button_register = (Button)findViewById(R.id.btn_register);
        button_login = (Button)findViewById(R.id.btn_login);
        button_login.setOnClickListener(this);
        image_register.setOnClickListener(this);
        button_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == image_register){
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        if(view == button_register){
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        if(view == button_login){
            kontrol();
        }
    }

    private void kontrol(){
        String text_email = editText_email.getText().toString().trim();
        String text_password = editText_password.getText().toString().trim();


        if(text_email.isEmpty()){
            editText_email.setError("Email Boş Olamaz");
            editText_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(text_email).matches()){
            editText_email.setError("Lütfen Geçerli Bir Mail Adresi Giriniz");
            editText_email.requestFocus();
            return;
        }

        if(text_password.length() < 8){
            editText_password.setError("Şifre En Az 8 Karakter Olmalıdır.");
            editText_password.requestFocus();
            return;
        }

        else{
            login();
        }
    }
    void login(){
        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(LoginActivity.this)
                .build();
        dialog.show();


        Users user = new Users();
        user.Email = editText_email.getText().toString();
        user.Password = editText_password.getText().toString();
        user.Salt = "";

        compositeDisposable.add(iMyAPI.loginUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   if (!s.equals("Email yada şifre hatalı") && !s.equals("Bu email adresi kayitli degil")){

                                       // Giriş başarılıysa dönen json stringini Users modeline dönüştür
                                       getUser(s);
                                       // Main Activitye Yönlendir
                                       if (currentUser.Age != 0){
                                           moveToMainActivity();
                                       }else{
                                           moveToDetailActivity();
                                       }
                                   }
                                   else{
                                       Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                                       dialog.dismiss();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   dialog.dismiss();
                                   Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }

    private void moveToDetailActivity(){
        Intent intent = new Intent(LoginActivity.this, DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void moveToMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private Users getUser(String json) throws JSONException {
        currentUser = Users.JsontoObject(json);
        Toast.makeText(LoginActivity.this, "Hoşgeldiniz " + currentUser.Name, Toast.LENGTH_LONG).show();
        // Objeyi session oluşturmak için SessionManagement e gönder
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        sessionManagement.saveSession(currentUser);
        return currentUser;
    }
}
