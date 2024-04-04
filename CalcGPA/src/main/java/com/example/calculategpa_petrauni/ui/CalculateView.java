package com.example.calculategpa_petrauni.ui;

import com.example.calculategpa_petrauni.Classes.BL;

public interface CalculateView {
    void onCalculateGPA(BL bl);
    void addCourse();
    void reset();
    void showMenu();
}
