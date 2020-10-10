package com.example.calculategpa_petrauni.ui;

import android.content.Context;

public class SettingsPresenter {

    SettingsView view;
    Context context;

    public SettingsPresenter(SettingsView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void changeTheme(boolean show){ view.changeTheme(show); }
    public void showGrids(boolean show){ view.changeGrids(show); }
    public void changeLanguage(String newLanguage){ view.changeLanguage(newLanguage); }
    public void back(){ view.back(); }
    public void contactUs(){ view.contactUs(); }
}
