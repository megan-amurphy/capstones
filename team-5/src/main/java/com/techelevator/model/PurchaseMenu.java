package com.techelevator.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;


public class PurchaseMenu {
    class Item {
        private String slotLocation;
        private String itemName;
        private BigDecimal price;
        private String type;
        public Item(String slotLocation, String itemName, BigDecimal price, String type) {
            this.slotLocation = slotLocation;
            this.itemName = itemName;
            this.price = price;
            this.type = type;

        }

        public String getSlotLocation() {
            return slotLocation;
        }

        public String getItemName() {
            return itemName;
        }


        public BigDecimal getPrice() {
            return price;
        }


        public String getType() {
            return type;
        }
    }
    public class VendingMachine {
        private List<Item> item;

        public VendingMachine(List<Item> items) {
            this.item = items;
        }
        public void displayMenu() {
            System.out.println("=== Vending Machine Menu ===");
            for (Item item : item) {
                System.out.println(item);
            }
            System.out.println("======================");
        }
        public void purchaseItem(String slotLocation) {

            System.out.println("Purchasing item from slot: " + slotLocation);
        }

    }


        public void main(String[] args){
        VendingMachine vendingMachine = new VendingMachine(List.of(

                new Item("A1", "Potato Crisps", new BigDecimal("3.05"), "Chip"),
                new Item("A2", "Stackers", new BigDecimal("1.45"), "Chip"),
                new Item("A3", "Grain Waves", new BigDecimal("2.75"), "Chip"),
                new Item("A4", "Potato Crisps", new BigDecimal("3.05"), "Chip"),
                new Item("B1", "Moonpie", new BigDecimal("1.80"), "Candy"),
                new Item("B2", "Cowtales", new BigDecimal("1.50"), "Candy"),
                new Item("B3", "Wonka Bar", new BigDecimal("1.50"), "Candy"),
                new Item("B4", "Crunchie", new BigDecimal("1.75"), "Candy"),
                new Item("C1", "Cola", new BigDecimal("1.25"), "Drink"),
                new Item("C2", "Dr. Salt", new BigDecimal("1.50"), "Drink"),
                new Item("C3", "Mountain Melter", new BigDecimal("1.50"), "Drink"),
                new Item("C4", "Heavy", new BigDecimal("1.50"), "Drink"),
                new Item("D1", "U-Chews", new BigDecimal("0.85"), "Gum"),
                new Item("D2", "Little League Chew", new BigDecimal("0.95"), "Gum"),
                new Item("D3", "Chiclets", new BigDecimal("0.75"), "Gum"),
                new Item("D4", "TripleMint", new BigDecimal("0.75"), "Gum")

        ));
        vendingMachine.displayMenu();

        vendingMachine.purchaseItem("");

        }
    }





