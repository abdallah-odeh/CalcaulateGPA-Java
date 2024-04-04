package com.example.calculategpa_petrauni.ui;

import android.content.Context;

import com.example.calculategpa_petrauni.Classes.BL;
import com.example.calculategpa_petrauni.pojo.Course;

import java.util.List;

public class CalculatePresenter {

    CalculateView view;
    Context context;

    public CalculatePresenter(CalculateView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getResult(double gpa, int hours, List<Course> courses){
        view.onCalculateGPA(calculate(gpa, hours, courses));
    }

    public void addCourse(){ view.addCourse(); }

    public void reset(){ view.reset(); }

    public void showMenu(){ view.showMenu(); }

    //-----------------------PRIVATE METHODS-----------------------//

    private BL calculate(double gpa, int hours, List<Course> courses){
        return new BL(context, gpa, hours, courses);
    }
}
