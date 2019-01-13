package com.example.nick.receiptsplitter;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    double price;
    public ArrayList<Person> splitters = new ArrayList<Person>();
    public double split_cost;

    public Item(double price, Person p){
        this.price = price;
        this.split_cost = price;
        splitters.add(p);
    }

    public void addSplitter(Person s){
        splitters.add(s);
    }

    public void getSplitCost(){
        int number_of_people = splitters.size();
        split_cost = price / number_of_people;
    }
}
