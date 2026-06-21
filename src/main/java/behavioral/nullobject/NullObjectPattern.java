// File: NullObjectPattern.java
// This file demonstrates the Null Object Pattern in detail
// The user can search for customers and avoid null checks

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * NULL OBJECT PATTERN
 * =====================================================================
 * This design pattern creates an object with default behavior as a
 * substitute for null values. This eliminates null checks.
 */

// =========================================================================
// Section 1: Abstract Class
// =========================================================================

abstract class Customer {
    protected String name;
    protected String email;
    protected String phone;
    protected String membershipLevel;
    protected double discountRate;

    public abstract String getName();
    public abstract String getEmail();
    public abstract String getPhone();
    public abstract String getMembershipLevel();
    public abstract double getDiscountRate();
    public abstract void sendNotification(String message);
    public abstract void displayInfo();
    public abstract boolean isNull();
}

// =========================================================================
// Section 2: Real Customer
// =========================================================================

class RealCustomer extends Customer {
    private String name;
    private String email;
    private String phone;
    private String membershipLevel;
    private double discountRate;
    private boolean isActive;
    private List<String> notifications;

    public RealCustomer(String name, String email, String phone, String membershipLevel) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipLevel = membershipLevel;
        this.isActive = true;
        this.notifications = new ArrayList<>();

        switch(membershipLevel) {
            case "Gold":
                this.discountRate = 0.15;
                break;
            case "Silver":
                this.discountRate = 0.10;
                break;
            case "Bronze":
                this.discountRate = 0.05;
                break;
            default:
                this.discountRate = 0.0;
        }
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getEmail() { return email; }

    @Override
    public String getPhone() { return phone; }

    @Override
    public String getMembershipLevel() { return membershipLevel; }

    @Override
    public double getDiscountRate() { return discountRate; }

    public boolean isActive() { return isActive; }

    public void deactivate() {
        this.isActive = false;
        System.out.println("  [Customer] ⚠️ " + name + " has been deactivated");
    }

    public void activate() {
        this.isActive = true;
        System.out.println("  [Customer] ✅ " + name + " has been activated");
    }

    @Override
    public void sendNotification(String message) {
        if (isActive) {
            notifications.add(message);
            System.out.println("  [Customer] 📧 To " + name + ": " + message);
        } else {
            System.out.println("  [Customer] ⚠️ " + name + " is deactivated - notification not sent");
        }
    }

    @Override
    public void displayInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 👤 Real Customer");
        System.out.println("  │ Name: " + name);
        System.out.println("  │ Email: " + email);
        System.out.println("  │ Phone: " + phone);
        System.out.println("  │ Membership Level: " + membershipLevel);
        System.out.println("  │ Discount: " + (discountRate * 100) + "%");
        System.out.println("  │ Status: " + (isActive ? "Active" : "Deactivated"));
        System.out.println("  │ Notifications: " + notifications.size());
        System.out.println("  └─────────────────────────────────");
    }

    @Override
    public boolean isNull() {
        return false;
    }
}

// =========================================================================
// Section 3: Null Object Class
// =========================================================================

class NullCustomer extends Customer {

    public NullCustomer() {
        // Does nothing
    }

    @Override
    public String getName() {
        return "Customer Not Found";
    }

    @Override
    public String getEmail() {
        return "No Email";
    }

    @Override
    public String getPhone() {
        return "No Phone";
    }

    @Override
    public String getMembershipLevel() {
        return "Not a Member";
    }

    @Override
    public double getDiscountRate() {
        return 0.0;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("  [NullCustomer] 📧 Notification not sent - customer does not exist");
    }

    @Override
    public void displayInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 👻 Null Customer");
        System.out.println("  │ This customer does not exist or was not found");
        System.out.println("  └─────────────────────────────────");
    }

    @Override
    public boolean isNull() {
        return true;
    }
}

// =========================================================================
// Section 4: Customer Service
// =========================================================================

class CustomerService {
    private String serviceName;
    private List<RealCustomer> customers;
    private int nullCustomerRequests;

    public CustomerService(String name) {
        this.serviceName = name;
        this.customers = new ArrayList<>();
        this.nullCustomerRequests = 0;
        initializeCustomers();

        System.out.println("\n==========================================");
        System.out.println("🏢 " + serviceName + " Customer Service Started");
        System.out.println("==========================================");
    }

    private void initializeCustomers() {
        customers.add(new RealCustomer("Abera Bekele", "abera@email.com", "0912345678", "Gold"));
        customers.add(new RealCustomer("Asnakech Tilahun", "asnakech@email.com", "0923456789", "Silver"));
        customers.add(new RealCustomer("Samuel Haile", "samuel@email.com", "0934567890", "Bronze"));
    }

    public Customer findCustomer(String name) {
        System.out.println("\n  [Service] 🔍 Searching for customer: '" + name + "'");

        for (RealCustomer customer : customers) {
            if (customer.getName().equalsIgnoreCase(name)) {
                System.out.println("  [Service] ✅ Customer found: " + customer.getName());
                return customer;
            }
        }

        System.out.println("  [Service] ❌ Customer not found: '" + name + "'");
        nullCustomerRequests++;
        return new NullCustomer(); // Return null object
    }

    public void addCustomer(RealCustomer customer) {
        customers.add(customer);
        System.out.println("  [Service] ➕ New customer added: " + customer.getName());
    }

    public void notifyAllCustomers(String message) {
        System.out.println("\n  [Service] 📢 Sending notification to all customers...");
        for (RealCustomer customer : customers) {
            if (customer.isActive()) {
                customer.sendNotification(message);
            }
        }
    }

    public void showAllCustomers() {
        System.out.println("\n📋 All Customers (" + customers.size() + "):");
        for (RealCustomer c : customers) {
            System.out.println("   • " + c.getName() + " (" + c.getMembershipLevel() +
                    ") - " + (c.isActive() ? "✅ Active" : "💤 Deactivated"));
        }
    }

    public void showSummary() {
        int active = 0;
        for (RealCustomer c : customers) {
            if (c.isActive()) active++;
        }

        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║  📊 " + serviceName + " Summary");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.println("║  👥 Total Customers: " + customers.size());
        System.out.println("║  ✅ Active Customers: " + active);
        System.out.println("║  💤 Deactivated: " + (customers.size() - active));
        System.out.println("║  👻 Customer Not Found Requests: " + nullCustomerRequests);
        System.out.println("╚════════════════════════════════════════════════════╝");
    }
}

// =========================================================================
// Section 5: Main Class - With User Input
// =========================================================================

public class NullObjectPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║            NULL OBJECT PATTERN DEMO                    ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.print("Enter service name: ");
        String serviceName = scanner.nextLine();

        CustomerService service = new CustomerService(serviceName);

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Find customer");
            System.out.println("2. Add new customer");
            System.out.println("3. Deactivate customer");
            System.out.println("4. Activate customer");
            System.out.println("5. Send notification to all customers");
            System.out.println("6. Show all customers");
            System.out.println("7. Show summary");
            System.out.println("8. Exit");
            System.out.print("Enter your choice (1-8): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter customer name: ");
                    String searchName = scanner.nextLine();

                    Customer found = service.findCustomer(searchName);
                    found.displayInfo();

                    // No null check needed!
                    found.sendNotification("Hello " + found.getName() + "! There's a new message");
                    break;

                case 2:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = scanner.nextLine();
                    System.out.print("Enter membership level (Bronze/Silver/Gold): ");
                    String level = scanner.nextLine();

                    RealCustomer newCustomer = new RealCustomer(name, email, phone, level);
                    service.addCustomer(newCustomer);
                    break;

                case 3:
                    service.showAllCustomers();
                    System.out.print("Enter customer name to deactivate: ");
                    String deactName = scanner.nextLine();

                    Customer deact = service.findCustomer(deactName);
                    if (!deact.isNull() && deact instanceof RealCustomer) {
                        ((RealCustomer) deact).deactivate();
                    } else if (deact.isNull()) {
                        System.out.println("⚠️ Customer not found!");
                    }
                    break;

                case 4:
                    service.showAllCustomers();
                    System.out.print("Enter customer name to activate: ");
                    String actName = scanner.nextLine();

                    Customer act = service.findCustomer(actName);
                    if (!act.isNull() && act instanceof RealCustomer) {
                        ((RealCustomer) act).activate();
                    } else if (act.isNull()) {
                        System.out.println("⚠️ Customer not found!");
                    }
                    break;

                case 5:
                    System.out.print("Enter notification message: ");
                    String message = scanner.nextLine();
                    service.notifyAllCustomers(message);
                    break;

                case 6:
                    service.showAllCustomers();
                    break;

                case 7:
                    service.showSummary();
                    break;

                case 8:
                    continueRunning = false;
                    System.out.println("\nThank you for using the program! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-8)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              Program terminated! Thank you              ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}