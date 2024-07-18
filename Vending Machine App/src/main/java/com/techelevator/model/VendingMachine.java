package com.techelevator.model;



import com.techelevator.generators.InventoryGenerator;

import java.math.BigDecimal;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class VendingMachine {

    private Map<String, Item> inventory;
    private BigDecimal currentBalance;

    private final BigDecimal DOLLAR_BILL = new BigDecimal(1);

    private final BigDecimal FIVE_DOLLAR_BILL = new BigDecimal(5);
    private final BigDecimal TEN_DOLLAR_BILL = new BigDecimal(10);
    private final BigDecimal TWENTY_DOLLAR_BILL = new BigDecimal(20);
    private final BigDecimal FIFTY_DOLLAR_BILL = new BigDecimal(50);
    private final BigDecimal HUNDRED_DOLLAR_BILL = new BigDecimal(100);

    private final BigDecimal QUARTER_VALUE = new BigDecimal(0.25);
    private final BigDecimal DIME_VALUE = new BigDecimal(0.10);
    private final BigDecimal NICKEL_VALUE = new BigDecimal(0.05);
    private BigDecimal totalSales;

    public VendingMachine() {
        this.inventory = InventoryGenerator.getListOfItemsFromFile();
        this.currentBalance = currentBalance.ZERO;
        this.totalSales = BigDecimal.ZERO;

    }

    // Getters and setters for inventory and currentBalance

    public Map<String, Item> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Item> inventory) {
        this.inventory = new HashMap<>(inventory);
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public void printInventory() {
        List<Map.Entry<String, Item>> entries = new ArrayList<>(inventory.entrySet());
        entries.sort(Map.Entry.comparingByKey());

        for (Map.Entry<String, Item> entry : entries) {

            Item item = entry.getValue();
            if (item.getQuantity() == 0) {
                System.out.println(entry.getKey() + " | " + item.getName() + " | $" + item.getPrice() + " | " + item.getType() + " | SOLD OUT");
            } else {
                System.out.println(item.toString());
            }
        }
    }

    public void feedMoney(BigDecimal amount) {
        List<BigDecimal> acceptedBills = new ArrayList<>();
        acceptedBills.add(DOLLAR_BILL);
        acceptedBills.add(FIVE_DOLLAR_BILL);
        acceptedBills.add(TEN_DOLLAR_BILL);
        acceptedBills.add(TWENTY_DOLLAR_BILL);
        acceptedBills.add(FIFTY_DOLLAR_BILL);
        acceptedBills.add(HUNDRED_DOLLAR_BILL);

        if (acceptedBills.contains(amount)) {
            currentBalance = currentBalance.add(amount);
            System.out.println("Added $" + amount + ". Current balance: $" + currentBalance);
        } else {
            System.out.println("Invalid bill amount. Please insert a valid bill.");
        }
    }

    public void dispenseItem(String slot) {
        Item item = inventory.get(slot);
        if (item != null && item.getQuantity() > 0 && currentBalance.compareTo(item.getPrice()) >= 0) {

            item.decreaseQuantity();

            currentBalance = currentBalance.subtract(item.getPrice());

            //Adding totalSales so a Sales Report can be generatored.
            totalSales = totalSales.add(item.getPrice());

            //This is the code block for logging a transaction.
            logTransaction("DISPENSED: " + item.getName() + " $" + item.getPrice(), currentBalance);

            //message block for eadh item dispensed
            String message = "Dispensing: " + item.getName() + " $" + item.getPrice() +"\n"+ " Remaining balance: $" + currentBalance;
            switch (item.getType()) {
                case "Chip":
                    message += " Crunch Crunch, Yum!";
                    break;
                case "Candy":
                    message += " Munch Munch, Yum!";
                    break;
                case "Drink":
                    message += " Glug Glug, Yum!";
                    break;
                case "Gum":
                    message += " Chew Chew, Yum!";
                    break;
            }
            System.out.println(message);
        } else if (item != null && item.getQuantity() == 0) {
            System.out.println("SOLD OUT: " + item.getName());
            System.out.println("Please try again.");
        } else {
            System.out.println("Invalid selection or insufficient funds");
        }

    }

    public String finishTransaction() {
        // Initialize a map to store the change
        Map<String, Integer> change = new HashMap<>();

        // Calculate the total change amount in cents
        int totalChangeInCents = currentBalance.multiply(new BigDecimal(100)).intValue();

        // Calculate the number of quarters
        int quarters = totalChangeInCents / 25;
        totalChangeInCents %= 25;

        // Calculate the number of dimes
        int dimes = totalChangeInCents / 10;
        totalChangeInCents %= 10;

        // Calculate the number of nickels
        int nickels = totalChangeInCents / 5;

        // Store the change in the map
        change.put("quarters", quarters);
        change.put("dimes", dimes);
        change.put("nickels", nickels);


        /// Build the change message
        String changeMessage = "CHANGE GIVEN: " + quarters + " quarters, " + dimes + " dimes, " + nickels + " nickels.";

        // Log transaction
        logTransaction("GIVE CHANGE: " + changeMessage, BigDecimal.ZERO);

        // Reset current balance to zero
        currentBalance = BigDecimal.ZERO;

        // Return the change message
        System.out.println(changeMessage);
        return changeMessage;
    }

    private void logTransaction(String action, BigDecimal balance) {
        // Implement logic to log transaction
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            String timestamp = dateFormat.format(new Date());
            FileWriter writer = new FileWriter("transaction_log.txt", true);
            writer.write(timestamp + " " + action + " $" + balance + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateSaleReport() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = dateFormat.format(new Date());

            SalesReport salesReport = new SalesReport( "sales_report.txt");

            // Write total sales to the sales report
            salesReport.writeLogEntry("Total Sales: $" + totalSales);

            // Reset total sales after generating the report
            totalSales = BigDecimal.ZERO;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}






