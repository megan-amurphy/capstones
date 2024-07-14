package com.techelevator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Item {

    private String location;
    private String name;
    private BigDecimal price;
    private String type;
    private int quantity;


    public Item(String location, String name, BigDecimal price, String type, int quantity) {
        this.location = location;
        this.name = name;
        this.price = price;
        this.type = type;
        this.quantity = quantity;
    }
    public Item() {}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
        }

    }
//I had to add this override because I need to display the information in a string.
    @Override
    public String toString() {
        String priceString = (price!= null) ? price.setScale(2, RoundingMode.HALF_UP).toString():"N/A";
        if (this.getQuantity() == 0){
            return location + " | " + name + " | $" + priceString + " | " + type + " | SOLD OUT";
        } else {
            return location + " | " + name + " | $" + priceString + " | " + type + " | " + quantity;
        }
    }
}

