package com.example.d8.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Receipt implements Serializable {
    private String receipId;
    private String name;
    private String date;
    private double totalCost;
    private double tax;
    private String businessName;
    private String userId;
    private String category;
    private ArrayList<Item> items = new ArrayList<Item>();

    //getter
    public Item getItembyId(int id){
        return items.get(id);
    }
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

    public String getBusinessName() {
        return businessName;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getCategory() {
        return category;
    }

    public String getUserId() {
        return userId;
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

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void addItem(String itemName, String itemDesc, double itemPrice, String itemID){
        Item item = new Item(itemName, itemDesc, itemPrice, itemID);
        items.add(item);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }




//<<<<<<< HEAD
//    public class Item{
//=======
    public class Item implements  Serializable{
        public String itemName;
        public String itemDesc;
        public double itemPrice;
        public String itemID;


        public Item(String itemName, String itemDesc, double itemPrice, String itemID) {
            this.itemID = itemID;
            this.itemName = itemName;
            this.itemDesc = itemDesc;
            this.itemPrice = itemPrice;
        }

        public Item(){
            this.itemName = "N/A";
            this.itemDesc = "N/A";
            this.itemPrice = 0.00;
        }

        public String getItemName() {
            return itemName;
        }

        public String getItemDesc() {
            return itemDesc;
        }

        public double getItemPrice() {
            return itemPrice;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public void setItemDesc(String itemDesc) {
            this.itemDesc = itemDesc;
        }

        public void setItemPrice(double itemPrice) {
            this.itemPrice = itemPrice;
        }

        public String getItemID() {
            return itemID;
        }

        public void setItemID(String itemID) {
            this.itemID = itemID;
        }
    }

}




