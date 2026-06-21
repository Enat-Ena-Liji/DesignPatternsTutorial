// File: FactoryMethodPattern.java
// This file demonstrates the Factory Method Pattern in detail
// All classes and interfaces are included in one file
// The user can select the type of plan they want

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * FACTORY METHOD PATTERN - The Factory Method Design Pattern
 * =====================================================================
 * This design pattern defines an interface for creating an object,
 * but lets subclasses decide which class to instantiate.
 *
 * In this example, the user selects the type of electricity plan they want.
 */

// =========================================================================
// Part 1: Abstract Product - Base for all products
// =========================================================================

/**
 * Abstract Class - Base for all electricity bill plans
 */
abstract class Plan {
    // Electricity rate - different for each plan
    protected double rate;

    // Plan name - for display
    protected String planName;

    // Plan description - for customer information
    protected String description;

    // List of customers - customers who selected this plan
    protected List<String> customers;

    /**
     * Constructor - initializes customer list when a new plan is created
     */
    public Plan() {
        this.customers = new ArrayList<>();
    }

    /**
     * Abstract method - implemented by subclasses
     * This method sets the rate for each plan
     */
    public abstract void getRate();

    /**
     * Method to calculate electricity bill
     *
     * @param units - amount of electricity used (in kilowatt-hours)
     * @return total cost (rate * units)
     */
    public double calculateBill(int units) {
        if (rate == 0) {
            getRate(); // Set the rate
        }
        return rate * units;
    }

    /**
     * Add new customer to the list
     *
     * @param customerName customer name
     */
    public void addCustomer(String customerName) {
        customers.add(customerName);
        System.out.println("  [Plan] 👤 " + customerName + " added to " + planName + " plan");
    }

    /**
     * Display plan information
     */
    public void displayPlanInfo() {
        System.out.println("\n┌─────────────────────────────────");
        System.out.println("  │ Plan: " + planName);
        System.out.println("  │ Rate: " + rate + " Birr/unit");
        System.out.println("  │ Description: " + description);
        System.out.println("  │ Customers: " + customers.size());
        for (String customer : customers) {
            System.out.println("  │   • " + customer);
        }
        System.out.println("  └─────────────────────────────────");
    }

    /**
     * Get plan name
     * @return plan name
     */
    public String getPlanName() {
        return planName;
    }
}

// =========================================================================
// Part 2: Concrete Products - Different types of plans
// =========================================================================

/**
 * Domestic Service Plan
 */
class DomesticPlan extends Plan {

    public DomesticPlan() {
        this.planName = "Domestic Service";
        this.description = "Convenient plan for home users. Low rates and special discounts.";
    }

    @Override
    public void getRate() {
        rate = 3.50; // 3.50 Birr per unit
        System.out.println("  [DomesticPlan] 🏠 Domestic service rate set to: " + rate + " Birr");
    }
}

/**
 * Commercial Service Plan
 */
class CommercialPlan extends Plan {

    public CommercialPlan() {
        this.planName = "Commercial Service";
        this.description = "Plan for business organizations. High capacity and efficient service.";
    }

    @Override
    public void getRate() {
        rate = 7.50; // 7.50 Birr per unit
        System.out.println("  [CommercialPlan] 🏢 Commercial service rate set to: " + rate + " Birr");
    }
}

/**
 * Institutional Service Plan
 */
class InstitutionalPlan extends Plan {

    public InstitutionalPlan() {
        this.planName = "Institutional Service";
        this.description = "Special plan for government institutions and organizations. Long-term contracts and additional benefits.";
    }

    @Override
    public void getRate() {
        rate = 5.50; // 5.50 Birr per unit
        System.out.println("  [InstitutionalPlan] 🏛️ Institutional service rate set to: " + rate + " Birr");
    }
}

// =========================================================================
// Part 3: Factory Class
// =========================================================================

/**
 * Plan Factory Class
 */
class PlanFactory {
    /**
     * Factory method - creates the appropriate Plan object based on user input
     */
    public Plan getPlan(String planType) {
        if (planType == null || planType.isEmpty()) {
            System.out.println("  [Factory] ⚠️ Empty or unspecified plan type!");
            return null;
        }

        System.out.println("  [Factory] 🏭 Preparing to create '" + planType + "' plan...");

        Plan plan = null;

        if (planType.equalsIgnoreCase("DOMESTIC")) {
            plan = new DomesticPlan();
        } else if (planType.equalsIgnoreCase("COMMERCIAL")) {
            plan = new CommercialPlan();
        } else if (planType.equalsIgnoreCase("INSTITUTIONAL")) {
            plan = new InstitutionalPlan();
        } else {
            System.out.println("  [Factory] ❌ Unknown plan type: '" + planType + "'");
            return null;
        }

        System.out.println("  [Factory] ✅ " + plan.getPlanName() + " plan created successfully!");
        return plan;
    }

    /**
     * Display list of available plan types
     */
    public void displayAvailablePlanTypes() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 🏭 Available plan types in the factory:");
        System.out.println("  │   1. DOMESTIC - Domestic Service");
        System.out.println("  │   2. COMMERCIAL - Commercial Service");
        System.out.println("  │   3. INSTITUTIONAL - Institutional Service");
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Part 4: Main Class - With User Input
// =========================================================================

/**
 * FactoryMethodPattern - Main Class
 */
public class FactoryMethodPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║     FACTORY METHOD PATTERN - Factory Method Design Pattern Demo    ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        PlanFactory factory = new PlanFactory();

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. View plan types");
            System.out.println("2. Calculate bill");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    factory.displayAvailablePlanTypes();
                    break;

                case 2:
                    System.out.print("\nPlease enter plan type (DOMESTIC/COMMERCIAL/INSTITUTIONAL): ");
                    String planType = scanner.nextLine();

                    System.out.print("Please enter the number of electricity units used: ");
                    int units = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Please enter customer name: ");
                    String customerName = scanner.nextLine();

                    Plan selectedPlan = factory.getPlan(planType);

                    if (selectedPlan != null) {
                        selectedPlan.addCustomer(customerName);
                        double totalBill = selectedPlan.calculateBill(units);

                        System.out.println("\n=== Electricity Bill Details ===");
                        System.out.println("Customer: " + customerName);
                        System.out.println("Plan: " + planType.toUpperCase());
                        System.out.println("Units: " + units);
                        System.out.println("Total Amount: " + String.format("%.2f", totalBill) + " Birr");

                        selectedPlan.displayPlanInfo();
                    }
                    break;

                case 3:
                    continueRunning = false;
                    System.out.println("\nThank you for using the system! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-3)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                Program terminated! Thank you                ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}