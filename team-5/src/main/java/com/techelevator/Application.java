package com.techelevator;

import com.techelevator.model.Item;
import com.techelevator.model.VendingMachine;
import com.techelevator.model.PurchaseMenu;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;

public class Application {
	public static void main(String[] args) {
		VendingMachine vendingMachine = new VendingMachine();
		Scanner scanner = new Scanner(System.in);
		Application application = new Application();
		while (true) {
			System.out.println("Welcome to the World's Best Vending Machine!");
			System.out.println("_____________________________________________");
			System.out.println("\n(1) Display Vending Machine Items");
			System.out.println("(2) Purchase");
			System.out.println("(3) Exit");
			System.out.println("**********HIDDEN***********");
			System.out.println("(4) Sales Report");
			System.out.print("Select an option: ");
			String choice = scanner.nextLine();

			if (choice.equals("1")) {
				vendingMachine.printInventory();
			} else if (choice.equals("2")) {
				application.purchaseMenu(vendingMachine, scanner);
			} else if (choice.equals("3")) {
				System.exit(0);
				break;
			} else if (choice.equals("4")){
				vendingMachine.generateSaleReport();
				System.out.println("Sales Report generated. Please exit the program and check your files.");
			} else {
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private void purchaseMenu(VendingMachine vendingMachine, Scanner scanner) {
		while (true) {
			System.out.println("\nCurrent Money Provided: $" + vendingMachine.getCurrentBalance());
			System.out.println("(1) Feed Money");
			System.out.println("(2) Select Product");
			System.out.println("(3) Finish Transaction");
			System.out.print("Select an option: ");
			String choice = scanner.nextLine();

			if (choice.equals("1")) {
				System.out.print("Enter amount to feed (WHOLE BILLS ONLY): ");
				BigDecimal amount = new BigDecimal(scanner.nextLine());
				vendingMachine.feedMoney(amount);
			} else if (choice.equals("2")) {
				vendingMachine.printInventory();
				System.out.print("Enter product code: ");
				String slotId = scanner.nextLine();
				vendingMachine.dispenseItem(slotId);
			} else if (choice.equals("3")) {
				vendingMachine.finishTransaction();
				break;
			} else {
				System.out.println("Invalid option. Please try again.");
			}
		}
	}
}









