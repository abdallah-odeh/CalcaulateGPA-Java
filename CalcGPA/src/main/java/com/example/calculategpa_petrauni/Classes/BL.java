package com.example.calculategpa_petrauni.Classes;

import android.content.Context;
import android.util.Log;

import com.example.calculategpa_petrauni.R;
import com.example.calculategpa_petrauni.pojo.Course;

import java.util.ArrayList;
import java.util.List;

public class BL {
    private Context activity;
    private double prevAverage;
    private int prevPassedHours;
    private int newPassedHours;
    private List<Course> courses = new ArrayList<>();

    public static BL INSTANCE;

    private double semAverage;
    private double overallAverage;
    private double avg100;
    private int failedToPassed = 0;

    public BL(Context activity, double prevAverage, int prevPassedHours, List<Course> courses) {
        this.activity = activity;
        this.prevAverage = prevAverage;
        this.prevPassedHours = prevPassedHours;
        newPassedHours = prevPassedHours;

        for(Course course: courses)
            if (!course.getMark().equals(activity.getResources().getString(R.string.ignore)))
                this.courses.add(course);

        CalculateOverallAverage();
    }

    public static BL getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new BL(context, 0, 0, new ArrayList<>());
        }
        return INSTANCE;
    }


    /**
     * Setters n getters
     */
    public String getSemAverage() {
        if(semAverage < 0)
            return "--";
        else
            return toStringAsFixed(semAverage);
    }
    public String getOverallAverage() {
        return toStringAsFixed(overallAverage);
    }
    public String getAvg100() {
        int integer = (int) avg100;
        int fraction = (int)((avg100 - integer) * 100);
        return integer+"."+fraction;
    }

    private String approx(double number){                   //Ex. 2.085962659
        //Log.e("approx", "got" + number);
        int integer = (int) number;                         //integer = 2
        int fraction = (int) ((number - integer) * 100);    //(int) 8.596.. => 8

        double rounded = (number - integer) * 100;
        int n = (int) rounded;
        double d = rounded - n;

        if(d >= 0.5)
            fraction++;
        if(fraction >= 100){
            integer += fraction / 100;
            fraction = fraction - 100;
        }
        if(fraction < 10)                                   //return 2.08
            return (integer + ".0" + fraction);
        else
            return integer + "." + fraction + (String.valueOf(fraction).length() == 1 ? "0" : "");
    }

    private String toStringAsFixed(double number){
        int digits = 3;
        String n = String.valueOf(number);
        if(n.length() >= 5) return n.substring(0, 5);
        else {
            if(!n.contains("."))
                n = n + ".";
            for(int i=n.length(); i<5; i++) n = n + "0";
        }
        return n;
    }

    /**
     * Processing methods
     */
    private double[] CalculateSemesterAverage(){
        double[] toReturn = new double[]{0, 0};
        double semesterSum = 0;
        int semesterHours = 0;

        for(int i=0; i<courses.size(); i++){
            Course current = courses.get(i);
            double mark = getMarkValue(current.getMark());
            double prevMark = getMarkValue(current.getLastMark());
            if(mark == -1)
                continue;
            if(mark >= 1){   //Passed
                //first time taking this course ***
                if(prevMark == -1 || current.getLastMark().equals(activity.getResources().getString(R.string.ignore))){
                    /**
                     * This will effect the overall and semester
                     */
                    if(mark < 5){
                        toReturn[0] += (mark * Integer.parseInt(current.getHours()));
                        toReturn[1] += Integer.parseInt(current.getHours());
                        semesterHours += Integer.parseInt(current.getHours());
                        semesterSum += (mark * Integer.parseInt(current.getHours()));
                    }
                }
                else if(prevMark >= 1) {    //passed to passed
                    newPassedHours -= Integer.parseInt(current.getHours());
                    if(prevMark >= mark){ //Ex. B -> C
                        /**
                         * This will effect only semester gpa
                         */
                        if(mark < 5){
                            semesterHours += Integer.parseInt(current.getHours());
                            semesterSum += (mark * Integer.parseInt(current.getHours()));
                        }
                    }
                    else {  //Ex. D -> B
                        /**
                         * This will effect Overall gpa and semester
                         */
                        //Remove previous mark from the previous average
                        prevAverage = prevAverage * prevPassedHours;
                        prevPassedHours -= Integer.parseInt(current.getHours());
                        prevAverage = prevAverage - (Integer.parseInt(current.getHours()) * prevMark);
                        prevAverage = prevAverage / prevPassedHours;
                        //Calculating semester average
                        if(mark < 5){
                            toReturn[0] += (mark * Integer.parseInt(current.getHours()));
                            toReturn[1] += Integer.parseInt(current.getHours());
                            semesterHours += Integer.parseInt(current.getHours());
                            semesterSum += (mark * Integer.parseInt(current.getHours()));
                        }
                    }
                }
                else {  //failed to passed
                    //Ex. F -> A
                    /**
                     * This will effect Overall and semester
                     */
                    //Increasing re-took courses hours
                    failedToPassed += Integer.parseInt(courses.get(i).getHours());
                    //Calculating semester average
                    if(mark < 5){
                        toReturn[0] += (mark * Integer.parseInt(current.getHours()));
                        toReturn[1] += Integer.parseInt(current.getHours());
                        semesterHours += Integer.parseInt(current.getHours());
                        semesterSum += (mark * Integer.parseInt(current.getHours()));
                    }
                }
                newPassedHours += Integer.parseInt(current.getHours());
            }
            else {  //Failed
                if(prevMark == -1 || current.getLastMark().equals(activity.getResources().getString(R.string.ignore))){
                    /**
                     * This will effect Semester gpa
                     */
                    toReturn[0] += (mark * Integer.parseInt(current.getHours()));
                    toReturn[1] += Integer.parseInt(current.getHours());
                    semesterHours += Integer.parseInt(current.getHours());
                    semesterSum += (mark * Integer.parseInt(current.getHours()));
                }
                else if(prevMark >= 1){   //passed to failed
                    //Ex C -> F
                    /**
                     * This will effect Only semester
                     */
                    if(mark < 5){
                        semesterHours += Integer.parseInt(current.getHours());
                        semesterSum += (mark * Integer.parseInt(current.getHours()));
                    }
                }
                else {  //failed to failed
                    //Calculating semester average
                    if(prevMark >= mark){ //Ex. D- -> F
                        /**
                         * This will effect only semester gpa
                         */
                        if(mark < 5){
                            semesterHours += Integer.parseInt(current.getHours());
                            semesterSum += (mark * Integer.parseInt(current.getHours()));
                        }
                    }
                    else {  //Ex. F -> D-
                        /**
                         * This will effect Overall gpa and semester
                         */
                        //Remove previous mark from the previous average
                        prevAverage = prevAverage * prevPassedHours;
                        prevPassedHours -= Integer.parseInt(current.getHours());
                        prevAverage = prevAverage - (Integer.parseInt(current.getHours()) * prevMark);
                        prevAverage = prevAverage / prevPassedHours;
                        //Calculating semester average
                        if(mark < 5){
                            toReturn[0] += (mark * Integer.parseInt(current.getHours()));
                            toReturn[1] += Integer.parseInt(current.getHours());
                            semesterHours += Integer.parseInt(current.getHours());
                            semesterSum += (mark * Integer.parseInt(current.getHours()));
                        }
                    }
                }
            }
        }

        if(semesterSum == 0)
            semAverage = -1;
        else
            semAverage = semesterSum / semesterHours;
        return toReturn;
    }
    private void CalculateOverallAverage(){
        double[] semesterData = CalculateSemesterAverage();
        double oldSum = prevAverage * (prevPassedHours + failedToPassed);   //GPA * hours => 2.3 * 30 = 69

        Log.e("Old Sum", prevAverage + " * (" + prevPassedHours + " + " +failedToPassed + ") => " + (prevAverage * (prevPassedHours + failedToPassed)));

        Log.e("Returned", "Sum: "+semesterData[0] + ", hours: " + semesterData[1]);

        Log.e("Old sum=", oldSum + " + " + semesterData[0] + " => " + (oldSum + semesterData[0]));

        oldSum += semesterData[0];                                          //69 + (Semester gpa * Semester hours) => 69 + (2.33 * 12) => 69 + 27.96 = 96.96

        Log.e("New sum=", oldSum + " / " + (prevPassedHours + semesterData[1]) + " => " + (oldSum / (prevPassedHours + semesterData[1])));

        double newSum = oldSum / (prevPassedHours + semesterData[1]);       //96.96 / (30 + 12) => 2.31

        avg100 = Calculate100Average(newSum);
        overallAverage = newSum > 4 ? 4 : newSum;
    }
    private double Calculate100Average(double average){
        double avg = (average + 1) * 20;
        return avg > 100 ? 100 : avg;
    }

    private double getMarkValue(String mark){
        if(mark.equals("A"))
            return 4;
        else if(mark.equals("A-"))
            return 3.67;
        else if(mark.equals("B+"))
            return 3.33;
        else if(mark.equals("B"))
            return 3;
        else if(mark.equals("B-"))
            return 2.67;
        else if(mark.equals("C+"))
            return 2.33;
        else if(mark.equals("C"))
            return 2;
        else if(mark.equals("C-"))
            return 1.67;
        else if(mark.equals("D+"))
            return 1.33;
        else if(mark.equals("D"))
            return 1;
        else if(mark.equals("D-"))
            return 0.67;
        else if(mark.equals(activity.getResources().getString(R.string.passed)))
            return 5;
        else if(mark.equals(activity.getResources().getString(R.string.failed)))
            return -1;
        else
            return 0;
    }
    public int getNewPassedHours(){
        return newPassedHours;
    }
}
