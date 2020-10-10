package com.example.calculategpa_petrauni.ui;

import android.content.Context;

import com.example.calculategpa_petrauni.R;

public class ResultPresenter {

    ResultView view;
    Context context;

    public ResultPresenter(ResultView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void showResult(String semesterGPA, String overallGPA, String outOf100, int newPassedHours){
        view.showResult(semesterGPA, overallGPA, outOf100, newPassedHours, level(Double.parseDouble(overallGPA)));
    }
    public void back(){ view.back(); }


    private String level(double average) {
        if(average >= 3.89)
            return context.getResources().getStringArray(R.array.levels)[0];
        else if(average >= 3.67)
            return context.getResources().getStringArray(R.array.levels)[1];
        else if(average >= 3)
            return context.getResources().getStringArray(R.array.levels)[2];
        else if(average >= 2.33)
            return context.getResources().getStringArray(R.array.levels)[3];
        else if(average >= 2)
            return context.getResources().getStringArray(R.array.levels)[4];
        else if(average < 2 && average >= 0)
            return context.getResources().getStringArray(R.array.levels)[5];
        else
            return "something wrong";
    }
}
