// File: FlyweightPattern.java
// This file demonstrates the Flyweight Pattern in detail
// The user can create different types of customers

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * =====================================================================
 * FLYWEIGHT PATTERN
 * =====================================================================
 * This design pattern allows reusing objects of similar types.
 */

// =========================================================================
// Section 1: Flyweight Interface
// =========================================================================

interface CustomerType {
    String getTypeName();
    double getDiscountRate();
    int getPriority();
    List<String> getBenefits();
    void displayBenefits();
}

// =========================================================================
// Section 2: Concrete Flyweight
// =========================================================================

class FlyweightCustomerType implements CustomerType {
    private String typeName;
    private double discountRate;
    private int priority;
    private List<String> benefits;

    public FlyweightCustomerType(String typeName, double discountRate, int priority) {
        this.typeName = typeName;
        this.discountRate = discountRate;
        this.priority = priority;
        this.benefits = new ArrayList<>();

        setupBenefits();
        System.out.println("  [Flyweight] 📦 New '" + typeName + "' customer type created");
    }

    private void setupBenefits() {
        benefits.add("Free WiFi");

        if (typeName.equals("Gold")) {
            benefits.add("50% Discount");
            benefits.add("Free Transportation");
            benefits.add("Priority Service");
        } else if (typeName.equals("Silver")) {
            benefits.add("25% Discount");
            benefits.add("Free Drink");
        } else if (typeName.equals("Bronze")) {
            benefits.add("10% Discount");
        }
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public List<String> getBenefits() {
        return benefits;
    }

    @Override
    public void displayBenefits() {
        System.out.println("   " + typeName + " level benefits:");
        for (String benefit : benefits) {
            System.out.println("      • " + benefit);
        }
    }
}

// =========================================================================
// Section 3: Context Class
// =========================================================================

class FlyweightCustomer {
    private String customerId;
    private String name;
    private String phone;
    private double totalSpent;
    private CustomerType customerType;
    private static int idCounter = 1000;

    public FlyweightCustomer(String name, String phone, CustomerType customerType) {
        this.customerId = "C" + (idCounter++);
        this.name = name;
        this.phone = phone;
        this.customerType = customerType;
        this.totalSpent = 0.0;
    }

    public void addPurchase(double amount) {
        double discount = amount * customerType.getDiscountRate();
        double finalAmount = amount - discount;
        totalSpent += finalAmount;

        System.out.println("  [Purchase] 🛒 " + name + " purchased: " +
                String.format("%.2f", amount) + " Birr");
        System.out.println("             Discount: " + String.format("%.2f", discount) + " Birr");
        System.out.println("             Paid: " + String.format("%.2f", finalAmount) + " Birr");
    }

    public void displayInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 👤 Customer: " + name);
        System.out.println("  │ ID: " + customerId);
        System.out.println("  │ Phone: " + phone);
        System.out.println("  │ Level: " + customerType.getTypeName());
        System.out.println("  │ Discount: " + (customerType.getDiscountRate() * 100) + "%");
        System.out.println("  │ Total Spent: " + String.format("%,.2f", totalSpent) + " Birr");
        System.out.println("  └─────────────────────────────────");
    }

    public String getCustomerId() {
        return customerId;
    }
}

// =========================================================================
// Section 4: Flyweight Factory
// =========================================================================

class FlyweightFactory {
    private Map<String, CustomerType> customerTypes;

    public FlyweightFactory() {
        this.customerTypes = new HashMap<>();
        initializeBasicTypes();
    }

    private void initializeBasicTypes() {
        customerTypes.put("Bronze", new FlyweightCustomerType("Bronze", 0.10, 1));
        customerTypes.put("Silver", new FlyweightCustomerType("Silver", 0.25, 2));
        customerTypes.put("Gold", new FlyweightCustomerType("Gold", 0.50, 3));
    }

    public CustomerType getCustomerType(String type) {
        System.out.println("  [Factory] 🔍 Looking for '" + type + "' customer type...");

        if (customerTypes.containsKey(type)) {
            System.out.println("  [Factory] ✅ Found existing '" + type + "' type");
            return customerTypes.get(type);
        }

        if (type.equals("Platinum")) {
            CustomerType platinum = new FlyweightCustomerType("Platinum", 0.70, 4);
            customerTypes.put("Platinum", platinum);
            return platinum;
        }

        System.out.println("  [Factory] ⚠️ Unknown type, returning Bronze");
        return customerTypes.get("Bronze");
    }

    public void showCacheInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📦 Customer types in cache:");
        for (String type : customerTypes.keySet()) {
            System.out.println("  │    • " + type);
        }
        System.out.println("  │    Total: " + customerTypes.size() + " types");
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Section 5: Main Class - With User Input
// =========================================================================

public class FlyweightPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║              FLYWEIGHT PATTERN DEMO                    ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        FlyweightFactory factory = new FlyweightFactory();
        List<FlyweightCustomer> customers = new ArrayList<>();

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. View customer types");
            System.out.println("2. Register new customer");
            System.out.println("3. Add purchase");
            System.out.println("4. View customer list");
            System.out.println("5. View cache information");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    factory.showCacheInfo();
                    System.out.println("\nCustomer type benefits:");
                    for (String type : new String[]{"Bronze", "Silver", "Gold", "Platinum"}) {
                        CustomerType ct = factory.getCustomerType(type);
                        ct.displayBenefits();
                    }
                    break;

                case 2:
                    System.out.print("Enter customer name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter customer phone: ");
                    String phone = scanner.nextLine();

                    System.out.print("Enter customer level (Bronze/Silver/Gold/Platinum): ");
                    String type = scanner.nextLine();

                    CustomerType customerType = factory.getCustomerType(type);
                    FlyweightCustomer customer = new FlyweightCustomer(name, phone, customerType);
                    customers.add(customer);

                    System.out.println("✅ Customer registered! ID: " + customer.getCustomerId());
                    break;

                case 3:
                    if (customers.isEmpty()) {
                        System.out.println("⚠️ No customers available!");
                        break;
                    }

                    System.out.println("\nRegistered customers:");
                    for (int i = 0; i < customers.size(); i++) {
                        System.out.println("   " + (i + 1) + ". " + customers.get(i).getCustomerId());
                    }

                    System.out.print("Enter customer number: ");
                    int custIndex = scanner.nextInt() - 1;
                    scanner.nextLine();

                    System.out.print("Enter purchase amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();

                    if (custIndex >= 0 && custIndex < customers.size()) {
                        customers.get(custIndex).addPurchase(amount);
                    }
                    break;

                case 4:
                    if (customers.isEmpty()) {
                        System.out.println("📭 No customers found");
                    } else {
                        for (FlyweightCustomer c : customers) {
                            c.displayInfo();
                        }
                    }
                    break;

                case 5:
                    factory.showCacheInfo();
                    break;

                case 6:
                    continueRunning = false;
                    System.out.println("\nThank you for using the program! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-6)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              Program terminated! Thank you              ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}