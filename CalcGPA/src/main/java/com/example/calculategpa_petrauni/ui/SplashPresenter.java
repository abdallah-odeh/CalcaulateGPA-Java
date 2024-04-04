package com.example.calculategpa_petrauni.ui;

import android.content.Context;

public class SplashPresenter {

    SplashView view;
    Context context;

    public SplashPresenter(SplashView view, Context context) {
        this.view = view;
        this.context = context;
    }

    void checkVersion(){
        view.checkLastVersion();
        move();
    }
    void move(){ view.move(); }
}
