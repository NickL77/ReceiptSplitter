package com.example.nick.receiptsplitter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Item implements Serializable {
    double price;
    public Set<Person> splitters = new HashSet<Person>();
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
