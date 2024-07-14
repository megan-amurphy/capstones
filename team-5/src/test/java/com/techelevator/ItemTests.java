package com.techelevator;
import com.techelevator.model.Item;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.math.BigDecimal;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemTests {
    @Test //Testing the constructor, that list index aligns with each item element.
    public void test01_ItemConstructor(){
        Item item = new Item("A1","Potato Crisps",new BigDecimal(3.05), "Chip", 5);
        Assert.assertEquals("A1",item.getLocation());
        Assert.assertEquals("Potato Crisps", item.getName());
        Assert.assertEquals(new BigDecimal(3.05), item.getPrice());
        Assert.assertEquals("Chip", item.getType());
    }

    @Test //if the elements are empty.
    public void test02_EmptyConstructor(){
        Item item = new Item();
        Assert.assertNull("The item location is empty", item.getLocation());
        Assert.assertNull("The item name is empty.", item.getName());
        Assert.assertNull("The item price is empty.", item.getPrice());
        Assert.assertNull("The item type is empty.", item.getType());

    }
    @Test //testing each getter and setter. it should be only one test for each.
    public void test03_GetLocation(){
        Item item = new Item("A1", "Potato Crisps", new BigDecimal(3.05), "Chip", 5);
        Assert.assertEquals("A1",item.getLocation());
    }

    @Test
    public void test04_SetLocation(){
        Item item = new Item();
        item.setLocation("C4");
        Assert.assertEquals("C4", item.getLocation());
    }

    @Test
    public void test05_GetName(){
        Item item = new Item("A1", "Potato Crisps", new BigDecimal(3.05), "Chip", 5);
        Assert.assertEquals("Potato Crisps",item.getName());
    }
    @Test
    public void test06_SetName(){
        Item item = new Item();
        item.setName("Moonpie");
        Assert.assertEquals("Moonpie", item.getName());
    }
    @Test
    public void test07_GetPrice(){
        Item item = new Item("A1", "Potato Crisps", new BigDecimal(3.05), "Chip", 5);
        Assert.assertEquals(new BigDecimal(3.05),item.getPrice());

    }

    @Test
    public void test08_SetPrice(){
        Item item = new Item();
        item.setPrice(new BigDecimal(1.80));
        Assert.assertEquals(new BigDecimal(1.80), item.getPrice());
    }

    @Test
    public void test09_GetType(){
        Item item = new Item("A1", "Potato Crisps", new BigDecimal(3.05), "Chip",5);
        Assert.assertEquals("Chip", item.getType());

    }
    @Test
    public void test10_SetType(){
        Item item = new Item();
        item.setType("Candy");
        Assert.assertEquals("Candy", item.getType());
    }
    @Test
    public void test11_NullItem(){
        Item item = new Item(null, null, null, null,0);
        Assert.assertNull("Your item location is null.", item.getLocation());
        Assert.assertNull("Your item name is null.", item.getName());
        Assert.assertNull("Your item price is null.", item.getPrice());
        Assert.assertNull("Yout item type is null.", item.getType());

    }
    @Test
    public void test12_EdgeCaseHighPrice(){
        Item item = new Item("A1", "Potato Crisps", new BigDecimal(9999999999.99), "Chip", 5);
        Assert.assertEquals(new BigDecimal(9999999999.99), item.getPrice());


    }



    //test if item is null
    //test edge case if price


}
