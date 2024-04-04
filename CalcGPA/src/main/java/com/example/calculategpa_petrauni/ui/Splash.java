package com.example.calculategpa_petrauni.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.calculategpa_petrauni.BuildConfig;
import com.example.calculategpa_petrauni.Classes.AppConst;
import com.example.calculategpa_petrauni.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends AppCompatActivity implements SplashView {


    @BindView(R.id.updateMessage)
    TextView updateMessage;
    @BindView(R.id.alert)
    RelativeLayout alert;
    @BindView(R.id.nameLabel)
    TextView nameLabel;

    private String language;
    private String message = "";

    private int wait = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isDarkTheme())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setTheme(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES ? R.style.AppThemeDark : R.style.AppTheme);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        language = loadLocale();

        nameLabel.setText(nameLabel.getText().toString().concat(", v").concat(BuildConfig.VERSION_NAME));

        SplashPresenter presenter = new SplashPresenter(this, this);
        presenter.checkVersion();
    }

    /**
     * Shared Preferences
     */
    private boolean isDarkTheme() {
        SharedPreferences preferences = getSharedPreferences(AppConst.name, MODE_PRIVATE);
        return preferences.getBoolean(AppConst._theme, true);
    }

    private void setLocale(String language) {
        language = language.toLowerCase();
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(new Locale(language));
        else
            config.locale = new Locale(language);
        resources.updateConfiguration(config, dm);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences(AppConst.name, MODE_PRIVATE).edit();
        editor.putString(AppConst._language, language);
        editor.apply();
    }

    private String loadLocale() {
        SharedPreferences preferences = getSharedPreferences(AppConst.name, MODE_PRIVATE);
        String language = preferences.getString(AppConst._language, "ar");
        setLocale(language);
        return language;
    }

    private void checkUpdate() {
        boolean toReturn = getSharedPreferences(AppConst.name, MODE_PRIVATE).getBoolean(AppConst._version, true);
        Log.e("is up to date?", toReturn + "");
        if (!toReturn) {
            wait = 5000;
            alert.setVisibility(View.VISIBLE);
        }
    }

    private void changeAlertState(boolean activate) {
        alert.setVisibility(activate ? View.VISIBLE : View.INVISIBLE);
        SharedPreferences.Editor editor = getSharedPreferences(AppConst.name, MODE_PRIVATE).edit();
        editor.putBoolean(AppConst._version, !activate);
        editor.apply();
    }

    @Override
    public void checkLastVersion() {
        checkUpdate();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("upToDate");
        ValueEventListener versionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("version").getValue(String.class).equals(BuildConfig.VERSION_NAME)) {
                    message = dataSnapshot.child(language.equals("en") ? "msg_en" : "msg_ar").getValue(String.class);
                    Log.e("Message", message + "");
                    updateMessage.setText(message + "");
                    changeAlertState(true);
                } else changeAlertState(false);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                Log.w("SplashScreen", "loadVersion:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addListenerForSingleValueEvent(versionListener);
    }

    @Override
    public void move() {
        Toast.makeText(this, "Welcome :)", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> {
            Intent i = new Intent(Splash.this, Calculate.class);
            i.putExtra("lang", language);
            startActivity(i);
            finish();
        }, wait);
    }
}
