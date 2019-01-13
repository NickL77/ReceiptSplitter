package com.example.nick.receiptsplitter;

import com.example.nick.receiptsplitter.Item;
import com.example.nick.receiptsplitter.Person;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Receipt implements Serializable {
    double subtotal;
    double tax;
    double taxrate;
    boolean givetip;
    double tip;
    double total;
    ArrayList<Person> people = new ArrayList<Person>();
    ArrayList<Item> items = new ArrayList<Item>();

    DecimalFormat df = new DecimalFormat("#.##");

    public Receipt(double subtotal, double tax, boolean givetip, double tip, double total){
        this.subtotal = subtotal;
        this.tax = tax;
        this.taxrate = this.tax / this.subtotal;
        this.givetip = givetip;
        this.tip = tip;
        this.total = total;
    }

    public void getAllPayments(){
        for (Item i : items){
            i.getSplitCost();
        }
        for(Person x : people){
            double sum = 0;
            for(Item y : items){
                if(y.splitters.contains(x)){
                    sum += y.split_cost;
                }
            }
            sum *= (1 + taxrate);
            if(givetip){
                sum += ((sum / total) * tip);
            }
            x.payment = Double.parseDouble(df.format(sum));
        }
    }

    public void addPerson(Person p){
        people.add(p);
    }

    public void addItem(Item i){
        items.add(i);
    }
}