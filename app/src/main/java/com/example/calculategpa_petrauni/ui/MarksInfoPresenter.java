package com.example.calculategpa_petrauni.ui;

import android.content.Context;

public class MarksInfoPresenter {

    MarksInfoView view;
    Context context;

    public MarksInfoPresenter(MarksInfoView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void showMarks(){ view.showMarks(); }
    public void showMarksDistribution(){ view.showMarksDistribution(); }
    public void showGPAs(){ view.showGPAs(); }
}
