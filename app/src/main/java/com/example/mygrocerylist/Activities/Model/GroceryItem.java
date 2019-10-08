package com.example.mygrocerylist.Activities.Model;

public class GroceryItem {
    private String name;
    private String quantity;
    private String date;
    private int ID;

    public GroceryItem() {
    }

    public GroceryItem(String name, String quantity, String date, int ID) {
        this.name = name;
        this.quantity = quantity;
        this.date = date;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
