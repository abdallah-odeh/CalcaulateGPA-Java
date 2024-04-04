package com.example.calculategpa_petrauni.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

import com.example.calculategpa_petrauni.Classes.AppConst;
import com.example.calculategpa_petrauni.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Settings extends AppCompatActivity implements SettingsView {

    TextView currentLanguage;
    Switch theme, grids;
    String language;

    SettingsPresenter presenter;
    private boolean languageChanged = false;
    private boolean themeChanged = false;
    private boolean gridsChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES?R.style.AppThemeDark:R.style.AppTheme);
        setContentView(R.layout.activity_settings);

        presenter = new SettingsPresenter(this, this);

        currentLanguage = findViewById(R.id.currentLanguage);
        theme = findViewById(R.id.themeSwitch);
        grids = findViewById(R.id.gridSwitch);

        SharedPreferences preferences = getSharedPreferences(AppConst.name, MODE_PRIVATE);
        grids.setChecked(preferences.getBoolean(AppConst._grids,true));
        theme.setChecked(preferences.getBoolean(AppConst._theme,true));

        String lang = loadLocale();

        currentLanguage.setText(lang.equals("en") ? "English" : "العربية");

        theme.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.changeTheme(isChecked));
        grids.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.showGrids(isChecked));
    }

    /**
     * Views methods
     */
    public void ShowLanguagesDialog(View view) {
        final List<String> languages = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.languages)));
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,languages
        ){
            @Override
            public View getView(int position, View convertView, @NotNull ViewGroup parent){
                TextView text_view = (TextView) super.getView(position, convertView, parent);
                text_view.setTypeface(ResourcesCompat.getFont(Settings.this, R.font.din_next_lt_w23_regular));
                return text_view;
            }
        };
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.language);
        // Item click listener
        builder.setSingleChoiceItems(
                arrayAdapter, // Items list
                -1, // Index of checked item (-1 = no selection)
                (dialogInterface, i1) -> {
                    presenter.changeLanguage(i1 == 0 ? "en" : "ar");
                    recreate();
                    dialogInterface.dismiss();
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog1, which) -> dialog1.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

        Typeface font = ResourcesCompat.getFont(this, R.font.din_next_lt_w23_regular);
        TextView title = (TextView) dialog.getWindow().findViewById(R.id.alertTitle);
        Button cancel = (Button) dialog.getWindow().findViewById(android.R.id.button2);

        assert (title != null);
            title.setTypeface(font);
        cancel.setTypeface(font);
        SharedPreferences preferences = getSharedPreferences(AppConst.name, MODE_PRIVATE);
        cancel.setTextColor(Color.parseColor(preferences.getBoolean(AppConst._theme, false) ? "#FFFFFFFF" : "#FF000000"));
    }
    public void Report(View view) { presenter.contactUs(); }
    public void back(View view) { presenter.back(); }

    @Override
    public void changeTheme(boolean show) {
        themeChanged = true;
        SharedPreferences.Editor editor = getSharedPreferences(AppConst.name, MODE_PRIVATE).edit();
        editor.putBoolean(AppConst._theme, show);
        editor.apply();
        AppCompatDelegate.setDefaultNightMode(show ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        reset();
    }
    @Override
    public void changeLanguage(String language) {
        Log.e("changeLanguage()", "entered");
        languageChanged = true;
        this.language = language;
        language = language.toLowerCase();
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(new Locale(language));
        else
            config.locale = new Locale(language);
        resources.updateConfiguration(config, dm);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences(AppConst.name, MODE_PRIVATE).edit();
        editor.putString(AppConst._language, language);
        editor.apply();
    }
    @Override
    public void changeGrids(boolean show) {
        gridsChanged = true;
        SharedPreferences.Editor editor = getSharedPreferences(AppConst.name, MODE_PRIVATE).edit();
        editor.putBoolean(AppConst._grids, show);
        editor.apply();
    }
    @Override
    public void contactUs() {
        startActivity(new Intent(this, ContactMe.class));
    }
    @Override
    public void back() {
        Intent result = new Intent();
        result.putExtra("theme", themeChanged);
        result.putExtra("grids", gridsChanged);
        result.putExtra("language", languageChanged);
        setResult(RESULT_OK, result);
        finish();
    }

    /**
     * Private methods
     */
    private String loadLocale(){
        SharedPreferences preferences = getSharedPreferences(AppConst.name, MODE_PRIVATE);
        //presenter.changeLanguage(language);
        return preferences.getString(AppConst._language,"ar");
    }
    private void reset(){
        finish();
        startActivity(new Intent(this, getClass()));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
