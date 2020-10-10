package com.example.calculategpa_petrauni.ui;

public interface ResultView {
    void showResult(String semesterGPA, String overallGPA, String outOf100, int newPassedHours, String rate);
    void back();
}
