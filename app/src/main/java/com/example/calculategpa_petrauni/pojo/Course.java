package com.example.calculategpa_petrauni.pojo;


public class Course {
    private String mark;
    private String hours;
    private String lastMark;

    public boolean isSet = false;

    public Course() {
    }

    public Course(String mark, String hours, String lastMark, String ignore) {
        this.mark = mark;
        this.hours = hours;
        this.lastMark = lastMark;
        isSet = !mark.equals(ignore);
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark, String ignore) {
        this.mark = mark;
        isSet = !mark.equals(ignore);
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getLastMark() {
        return lastMark;
    }

    public void setLastMark(String lastMark) {
        this.lastMark = lastMark;
    }

    @Override
    public String toString() {
        return "last mark: "+lastMark+", current mark: "+mark+", credit hours: "+hours;
    }
}
