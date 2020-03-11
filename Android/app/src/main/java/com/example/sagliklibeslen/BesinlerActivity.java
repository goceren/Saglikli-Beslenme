package com.example.sagliklibeslen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.sagliklibeslen.Model.Besinler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BesinlerActivity extends AppCompatActivity {

    private BottomNavigationView mainNav;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_besinler);
        tanimla();

    }
    void tanimla(){
        mainNav = findViewById(R.id.main_nav);
        mainNav.setSelectedItemId(R.id.nav_foodpage);

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

}
