package com.example.calculategpa_petrauni.pojo;

public class Info {
    private String right, left;

    public Info(String[] data) {
        right = data[0];
        left = data[1];
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }
}
