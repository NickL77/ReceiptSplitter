package com.example.nick.receiptsplitter;

import java.io.Serializable;

public class Person implements Serializable {
    public int rgb1;
    public int rgb2;
    public int rgb3;
    public String name;
    public double payment;

    public Person(String name, int rgb1, int rgb2, int rgb3){
        this.name = name;
        this.rgb1 = rgb1;
        this.rgb2 = rgb2;
        this.rgb3 = rgb3;
    }
}