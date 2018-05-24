package com.example.d8.myapplication;

import java.util.ArrayList;

public class Receipt {
    private String receipId;
    private String name;
    private String date;
    private double totalCost;
    private double tax;
    private ArrayList<Item> items;

    //getter
    public String getReceipId() {
        return receipId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getTax() {
        return tax;
    }

    public ArrayList<Item> getItems() {
        return items;
    }


    //setter
    public void setReceipId(String receipId) {
        this.receipId = receipId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public class Item{
        public String itemName;
        public String itemDesc;
        public double itemPrice;
    }

}




