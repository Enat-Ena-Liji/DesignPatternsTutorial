// File: SingletonPattern.java
// This file demonstrates the Singleton Pattern in detail
// A class that ensures only one instance exists

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * SINGLETON PATTERN
 * =====================================================================
 * This pattern ensures a class has only one instance and
 * provides a global point of access to it.
 */

// =========================================================================
// Part 1: Singleton Class - Service Center
// =========================================================================

/**
 * ServiceCenter Class - This is the Singleton
 * It will have only one instance
 */
class ServiceCenter {
    // The single instance - static so it's created only once
    private static ServiceCenter instance;

    // Service center information
    private String centerName;
    private String location;
    private String phoneNumber;
    private List<String> serviceRequests;
    private List<String> completedRequests;

    /**
     * Private constructor - cannot create new instance from outside
     */
    private ServiceCenter() {
        this.centerName = "group 5 Service Center";
        this.location = "Injibara, Ethiopia";
        this.phoneNumber = "+251-938-803-929";
        this.serviceRequests = new ArrayList<>();
        this.completedRequests = new ArrayList<>();

        System.out.println("  [Singleton] 🏢 Service Center created!");
    }

    /**
     * Global access point - returns the single instance
     */
    public static synchronized ServiceCenter getInstance() {
        if (instance == null) {
            instance = new ServiceCenter();
        }
        return instance;
    }

    /**
     * Add new service request
     */
    public void addServiceRequest(String customerName, String serviceType) {
        String request = customerName + " - " + serviceType;
        serviceRequests.add(request);
        System.out.println("  [ServiceCenter] 📝 New service request added: " + request);
    }

    /**
     * Complete a service
     */
    public void completeService(int index) {
        if (index >= 0 && index < serviceRequests.size()) {
            String completed = serviceRequests.remove(index);
            completedRequests.add(completed);
            System.out.println("  [ServiceCenter] ✅ Service completed: " + completed);
        } else {
            System.out.println("  [ServiceCenter] ⚠️ Invalid index!");
        }
    }

    /**
     * Show pending services
     */
    public void showPendingRequests() {
        System.out.println("\n┌─────────────────────────────────");
        System.out.println("  │ 📋 Pending Services:");
        if (serviceRequests.isEmpty()) {
            System.out.println("│    No pending services");
        } else {
            for (int i = 0; i < serviceRequests.size(); i++) {
                System.out.println("  │   " + (i+1) + ". " + serviceRequests.get(i));
            }
        }
        System.out.println("  └─────────────────────────────────");
    }

    /**
     * Show completed services
     */
    public void showCompletedRequests() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ ✅ Completed Services:");
        if (completedRequests.isEmpty()) {
            System.out.println("  │    No completed services");
        } else {
            for (String req : completedRequests) {
                System.out.println("  │   • " + req);
            }
        }
        System.out.println("  └─────────────────────────────────");
    }

    /**
     * Display service center information
     */
    public void displayInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 🏢 " + centerName);
        System.out.println("  │ 📍 Address: " + location);
        System.out.println("  │ 📞 Phone: " + phoneNumber);
        System.out.println("  │ 📊 Total Services: " + (serviceRequests.size() + completedRequests.size()));
        System.out.println("  └─────────────────────────────────");
    }

    /**
     * Get count of pending services
     * @return number of pending services
     */
    public int getPendingCount() {
        return serviceRequests.size();
    }
}

// =========================================================================
// Part 2: Main Class - With User Input
// =========================================================================

public class SingletonPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           SINGLETON PATTERN DEMO                       ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("This demo shows how Singleton Pattern ensures only");
        System.out.println("one instance is created. All operations work on the same instance.\n");

        // Get Singleton instance
        ServiceCenter serviceCenter = ServiceCenter.getInstance();
        serviceCenter.displayInfo();

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Add new service request");
            System.out.println("2. View pending services");
            System.out.println("3. Complete a service");
            System.out.println("4. View completed services");
            System.out.println("5. View service center information");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();

                    System.out.print("Enter service type (e.g., Repair, Consultation, etc): ");
                    String serviceType = scanner.nextLine();

                    serviceCenter.addServiceRequest(customerName, serviceType);
                    break;

                case 2:
                    serviceCenter.showPendingRequests();
                    break;

                case 3:
                    serviceCenter.showPendingRequests();
                    if (serviceCenter.getPendingCount() > 0) {
                        System.out.print("Enter service number to complete: ");
                        int index = scanner.nextInt() - 1;
                        scanner.nextLine();
                        serviceCenter.completeService(index);
                    }
                    break;

                case 4:
                    serviceCenter.showCompletedRequests();
                    break;

                case 5:
                    serviceCenter.displayInfo();
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
        System.out.println("  ║                PROGRAM COMPLETED! THANK YOU            ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");
    }
}