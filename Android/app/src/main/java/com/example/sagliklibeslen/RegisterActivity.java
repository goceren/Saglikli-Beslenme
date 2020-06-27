package com.example.sagliklibeslen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sagliklibeslen.Model.Users;
import com.example.sagliklibeslen.Remote.IMyAPI;
import com.example.sagliklibeslen.Remote.RetrofitClient;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button_signup;
    private EditText editText_adi_soyadi, editText_email, editText_sifre, editText_sifre_tekrar;

    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tanimla();
        iMyAPI = RetrofitClient.getInstance().create(IMyAPI.class);
    }
    private void tanimla(){
        button_signup = findViewById(R.id.signup);
        editText_adi_soyadi = findViewById(R.id.adi_soyadi);
        editText_email = findViewById(R.id.email);
        editText_sifre = findViewById(R.id.sifre);
        editText_sifre_tekrar = findViewById(R.id.sifre_tekrar);
        button_signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == button_signup){
            kontrol();
        }
    }

    private void kontrol(){
        String adi_soyadi = editText_adi_soyadi.getText().toString().trim();
        String email = editText_email.getText().toString().trim();
        String sifre = editText_sifre.getText().toString().trim();
        String sifre_tekrar = editText_sifre_tekrar.getText().toString().trim();

        if(adi_soyadi.isEmpty()){
            editText_adi_soyadi.setError("Ad Soyad Girilmelidir.");
            editText_adi_soyadi.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editText_email.setError("Email Boş Olamaz");
            editText_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText_email.setError("Lütfen Geçerli Bir Mail Adresi Giriniz");
            editText_email.requestFocus();
            return;
        }
        if(sifre.isEmpty()){
            editText_sifre.setError("Şifre Boş Olamaz");
            editText_sifre.requestFocus();
            return;
        }
        if(sifre.length() < 8){
            editText_sifre.setError("Şifre En Az 8 Karakter Olmalıdır.");
            editText_sifre.requestFocus();
            return;
        }
        if(sifre_tekrar.isEmpty()){
            editText_sifre_tekrar.setError("Şifre Tekrarı Boş Olamaz");
            editText_sifre_tekrar.requestFocus();
            return;
        }
        if(!sifre.equals(sifre_tekrar)) {
            editText_sifre_tekrar.setError("Şifreler Uyuşmuyor");
            editText_sifre_tekrar.requestFocus();
            editText_sifre.setError("Şifreler Uyuşmuyor");
            editText_sifre.requestFocus();
            return;
        }
        else{
            register();
        }
    }

    void register(){
        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(RegisterActivity.this)
                .build();
        dialog.show();


        Users user = new Users();
        user.Name = editText_adi_soyadi.getText().toString();
        user.Email = editText_email.getText().toString();
        user.Password = editText_sifre.getText().toString();
        user.Salt = "";

        compositeDisposable.add(iMyAPI.registerUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                  if (s.equals("Kayit Basarili")){
                                      Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                      startActivity(intent);
                                  }
                                  // Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                                   dialog.dismiss();
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   dialog.dismiss();
                                  // Toast.makeText(RegisterActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                )
        );
    }
}
