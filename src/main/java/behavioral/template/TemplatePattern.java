// File: TemplatePattern.java
// This file demonstrates the Template Pattern in detail
// The user can generate different types of reports

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * TEMPLATE PATTERN
 * =====================================================================
 * This pattern defines the skeleton of an algorithm in a method,
 * deferring some steps to subclasses.
 */

// =========================================================================
// Part 1: Abstract Template Class
// =========================================================================

abstract class ReportGenerator {
    protected String title;
    protected String author;
    protected List<String> data;
    protected List<String> report;

    public ReportGenerator(String title, String author) {
        this.title = title;
        this.author = author;
        this.data = new ArrayList<>();
        this.report = new ArrayList<>();
    }

    // Template method - the report generation process
    public final void generateReport() {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║  🔄 Report generation started: " + title);
        System.out.println("╚════════════════════════════════════════════════════╝");

        collectData();
        processData();
        formatReport();
        addSummary();
        printReport();

        if (shouldSaveToFile()) {
            saveToFile();
        }

        System.out.println("\n  [Template] ✅ Report generated!");
    }

    // Concrete method - collect data
    private void collectData() {
        System.out.println("\n  [Step 1] 📊 Collecting data...");
        // Allow subclasses to add data
        addData();
    }

    // Hook method for subclasses to add data
    protected void addData() {
        // Default is empty
    }

    protected abstract void processData();
    protected abstract void formatReport();

    private void addSummary() {
        System.out.println("\n  [Step 4] 📝 Adding summary...");
        report.add("");
        report.add("Summary:");
        report.add("─────────────────────");
        calculateSummary();
    }

    protected void calculateSummary() {
        // Default summary
        report.add("This report was generated on " + java.time.LocalDate.now());
    }

    private void printReport() {
        System.out.println("\n  [Step 5] 🖨️ Printing report...");
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + title);
        System.out.println("  Author: " + author);
        System.out.println("  Date: " + java.time.LocalDate.now());
        System.out.println("=".repeat(60));

        for (String line : report) {
            System.out.println("  " + line);
        }
        System.out.println("=".repeat(60));
    }

    protected boolean shouldSaveToFile() {
        return false;
    }

    private void saveToFile() {
        System.out.println("\n  [Template] 💾 Report saved to file");
    }

    protected void addToReport(String line) {
        report.add(line);
    }
}

// =========================================================================
// Part 2: Concrete Templates
// =========================================================================

class SalesReport extends ReportGenerator {
    private double totalSales;
    private int numItems;

    public SalesReport(String title, String author) {
        super(title, author);
        this.totalSales = 0;
        this.numItems = 0;
    }

    @Override
    protected void addData() {
        data.add("Laptop: 5 units @ 45000 Birr");
        data.add("Phone: 10 units @ 15000 Birr");
        data.add("Television: 3 units @ 35000 Birr");
        data.add("Refrigerator: 2 units @ 55000 Birr");
    }

    @Override
    protected void processData() {
        System.out.println("\n  [Step 2] 📈 Processing sales data...");

        for (String item : data) {
            String[] parts = item.split("[: @]");
            String product = parts[0].trim();
            int quantity = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
            double price = Double.parseDouble(parts[3].replaceAll("[^0-9.]", ""));

            double total = quantity * price;
            totalSales += total;
            numItems += quantity;

            addToReport(String.format("%s: %d units at %.0f Birr = %.0f Birr",
                    product, quantity, price, total));
        }
    }

    @Override
    protected void formatReport() {
        System.out.println("\n  [Step 3] ✨ Formatting sales report...");
        addToReport("");
        addToReport("Sales Details");
        addToReport("─────────────────────");
    }

    @Override
    protected void calculateSummary() {
        addToReport("Total units sold: " + numItems);
        addToReport("Total sales: " + String.format("%.2f", totalSales) + " Birr");
        addToReport("Average price per unit: " + String.format("%.2f", totalSales/numItems) + " Birr");
    }

    @Override
    protected boolean shouldSaveToFile() {
        return true;
    }
}

class InventoryReport extends ReportGenerator {
    private int totalItems;
    private double totalValue;

    public InventoryReport(String title, String author) {
        super(title, author);
        this.totalItems = 0;
        this.totalValue = 0;
    }

    @Override
    protected void addData() {
        data.add("Laptop: 45 units @ 45000 Birr");
        data.add("Phone: 120 units @ 15000 Birr");
        data.add("Television: 30 units @ 35000 Birr");
        data.add("Refrigerator: 15 units @ 55000 Birr");
    }

    @Override
    protected void processData() {
        System.out.println("\n  [Step 2] 📦 Processing inventory data...");

        for (String item : data) {
            String[] parts = item.split("[: @]");
            String product = parts[0].trim();
            int quantity = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
            double price = Double.parseDouble(parts[3].replaceAll("[^0-9.]", ""));

            double value = quantity * price;
            totalItems += quantity;
            totalValue += value;

            String status = quantity < 20 ? "⚠️ Low stock" : "✅ Sufficient stock";
            addToReport(String.format("%s: %d units at %.0f Birr = %.0f Birr %s",
                    product, quantity, price, value, status));
        }
    }

    @Override
    protected void formatReport() {
        System.out.println("\n  [Step 3] ✨ Formatting inventory report...");
        addToReport("");
        addToReport("Inventory Status");
        addToReport("─────────────────────");
    }

    @Override
    protected void calculateSummary() {
        addToReport("Total items: " + totalItems + " units");
        addToReport("Total value: " + String.format("%.2f", totalValue) + " Birr");

        int lowStock = 0;
        for (String item : data) {
            int qty = Integer.parseInt(item.split(":")[1].replaceAll("[^0-9]", ""));
            if (qty < 20) lowStock++;
        }

        if (lowStock > 0) {
            addToReport("⚠️ " + lowStock + " items have low stock!");
        }
    }
}

// =========================================================================
// Part 3: Main Class - With User Input
// =========================================================================

public class TemplatePattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           TEMPLATE PATTERN DEMO                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Generate Sales Report");
            System.out.println("2. Generate Inventory Report");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter report title: ");
                    String salesTitle = scanner.nextLine();
                    System.out.print("Enter report author: ");
                    String salesAuthor = scanner.nextLine();

                    SalesReport salesReport = new SalesReport(salesTitle, salesAuthor);
                    salesReport.generateReport();
                    break;

                case 2:
                    System.out.print("Enter report title: ");
                    String invTitle = scanner.nextLine();
                    System.out.print("Enter report author: ");
                    String invAuthor = scanner.nextLine();

                    InventoryReport invReport = new InventoryReport(invTitle, invAuthor);
                    invReport.generateReport();
                    break;

                case 3:
                    continueRunning = false;
                    System.out.println("\nThank you for using the program! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-3)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                PROGRAM COMPLETED! THANK YOU            ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}