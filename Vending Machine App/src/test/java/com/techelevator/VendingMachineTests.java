package com.techelevator;
import com.techelevator.model.Item;
import com.techelevator.model.VendingMachine;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Assert;

import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class VendingMachineTests {

    private VendingMachine vendingMachine;

    @BeforeEach
    public void setUp() {
        vendingMachine = new VendingMachine();
        vendingMachine.setInventory(Map.of(
                "A1", new Item("A1", "Chips", new BigDecimal("1.50"), "Chip", 5),
                "B1", new Item("B1", "Candy", new BigDecimal("1.00"), "Candy", 5)
        ));
    }

    @Test
    public void test01_FeedMoney() {
        vendingMachine.feedMoney(new BigDecimal(10));
        Assert.assertEquals(new BigDecimal("10.00"), vendingMachine.getCurrentBalance());

        vendingMachine.feedMoney(new BigDecimal(5));
        Assert.assertEquals(new BigDecimal("15.00"), vendingMachine.getCurrentBalance());
    }

    @Test
    public void test02_DispenseItem() {
        vendingMachine.setCurrentBalance(new BigDecimal("2.00"));

        vendingMachine.dispenseItem("A1");
        Assert. assertEquals(new BigDecimal("0.50"), vendingMachine.getCurrentBalance());
        Assert.assertEquals(4, vendingMachine.getInventory().get("A1").getQuantity());

        vendingMachine.dispenseItem("A1");
        Assert.assertEquals(new BigDecimal("0.50"), vendingMachine.getCurrentBalance());
        Assert. assertEquals(4, vendingMachine.getInventory().get("A1").getQuantity());
    }


    @Test
    public void test04_PrintInventory() {
        // This test simply prints the inventory to the console.
        vendingMachine.printInventory();
    }
}