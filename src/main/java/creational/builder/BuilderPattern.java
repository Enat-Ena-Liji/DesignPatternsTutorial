// File: BuilderPattern.java
// This file demonstrates the Builder Pattern in detail
// The user can build a computer step by step

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * BUILDER PATTERN - The Builder Design Pattern
 * =====================================================================
 * This design pattern allows constructing complex objects
 * step by step from simpler components.
 */

// =========================================================================
// Part 1: Product - Computer
// =========================================================================

class Computer {
    private String cpu;
    private String ram;
    private String storage;
    private String gpu;
    private String motherboard;
    private String powerSupply;
    private String case_;
    private List<String> accessories;
    private double totalPrice;

    public Computer() {
        this.accessories = new ArrayList<>();
        this.totalPrice = 0;
    }

    // Setters
    public void setCpu(String cpu, double price) {
        this.cpu = cpu;
        this.totalPrice += price;
    }

    public void setRam(String ram, double price) {
        this.ram = ram;
        this.totalPrice += price;
    }

    public void setStorage(String storage, double price) {
        this.storage = storage;
        this.totalPrice += price;
    }

    public void setGpu(String gpu, double price) {
        this.gpu = gpu;
        this.totalPrice += price;
    }

    public void setMotherboard(String motherboard, double price) {
        this.motherboard = motherboard;
        this.totalPrice += price;
    }

    public void setPowerSupply(String powerSupply, double price) {
        this.powerSupply = powerSupply;
        this.totalPrice += price;
    }

    public void setCase(String case_, double price) {
        this.case_ = case_;
        this.totalPrice += price;
    }

    public void addAccessory(String accessory, double price) {
        accessories.add(accessory + " - " + price + " Birr");
        this.totalPrice += price;
    }

    public void displayInfo() {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║           🖥️ Built Computer Information");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.println("║  CPU: " + cpu);
        System.out.println("║  RAM: " + ram);
        System.out.println("║  Storage: " + storage);
        System.out.println("║  GPU: " + (gpu != null ? gpu : "Not selected"));
        System.out.println("║  Motherboard: " + motherboard);
        System.out.println("║  Power Supply: " + powerSupply);
        System.out.println("║  Case: " + case_);

        if (!accessories.isEmpty()) {
            System.out.println("║  Accessories:");
            for (String acc : accessories) {
                System.out.println("║    • " + acc);
            }
        }

        System.out.println("║  💰 Total Price: " + String.format("%,.2f", totalPrice) + " Birr");
        System.out.println("╚════════════════════════════════════════════════════╝");
    }
}

// =========================================================================
// Part 2: Builder Interface
// =========================================================================

interface ComputerBuilder {
    void buildCpu();
    void buildRam();
    void buildStorage();
    void buildGpu();
    void buildMotherboard();
    void buildPowerSupply();
    void buildCase();
    void addAccessories();
    Computer getComputer();
}

// =========================================================================
// Part 3: Concrete Builders
// =========================================================================

class GamingComputerBuilder implements ComputerBuilder {
    private Computer computer;

    public GamingComputerBuilder() {
        this.computer = new Computer();
    }

    @Override
    public void buildCpu() {
        computer.setCpu("Intel Core i9-13900K", 45000);
    }

    @Override
    public void buildRam() {
        computer.setRam("32GB DDR5 6000MHz", 18000);
    }

    @Override
    public void buildStorage() {
        computer.setStorage("2TB NVMe SSD", 25000);
    }

    @Override
    public void buildGpu() {
        computer.setGpu("NVIDIA RTX 4080 16GB", 85000);
    }

    @Override
    public void buildMotherboard() {
        computer.setMotherboard("ASUS ROG Z790", 35000);
    }

    @Override
    public void buildPowerSupply() {
        computer.setPowerSupply("850W 80+ Gold", 12000);
    }

    @Override
    public void buildCase() {
        computer.setCase("Gaming Case RGB", 15000);
    }

    @Override
    public void addAccessories() {
        computer.addAccessory("Gaming Mouse", 3500);
        computer.addAccessory("Mechanical Keyboard", 5000);
        computer.addAccessory("Gaming Headset", 4000);
    }

    @Override
    public Computer getComputer() {
        return computer;
    }
}

class OfficeComputerBuilder implements ComputerBuilder {
    private Computer computer;

    public OfficeComputerBuilder() {
        this.computer = new Computer();
    }

    @Override
    public void buildCpu() {
        computer.setCpu("Intel Core i5-13400", 18000);
    }

    @Override
    public void buildRam() {
        computer.setRam("16GB DDR4 3200MHz", 6000);
    }

    @Override
    public void buildStorage() {
        computer.setStorage("512GB SSD + 1TB HDD", 10000);
    }

    @Override
    public void buildGpu() {
        // Office computer doesn't need a dedicated graphics card
        System.out.println("  [Builder] ⚠️ Office computer doesn't need a dedicated graphics card");
    }

    @Override
    public void buildMotherboard() {
        computer.setMotherboard("MSI B760", 12000);
    }

    @Override
    public void buildPowerSupply() {
        computer.setPowerSupply("500W 80+ Bronze", 5000);
    }

    @Override
    public void buildCase() {
        computer.setCase("Office Case", 3000);
    }

    @Override
    public void addAccessories() {
        computer.addAccessory("Mouse", 500);
        computer.addAccessory("Keyboard", 800);
    }

    @Override
    public Computer getComputer() {
        return computer;
    }
}

class BudgetComputerBuilder implements ComputerBuilder {
    private Computer computer;

    public BudgetComputerBuilder() {
        this.computer = new Computer();
    }

    @Override
    public void buildCpu() {
        computer.setCpu("AMD Ryzen 5 5600G", 12000);
    }

    @Override
    public void buildRam() {
        computer.setRam("8GB DDR4 3200MHz", 3000);
    }

    @Override
    public void buildStorage() {
        computer.setStorage("256GB SSD", 3500);
    }

    @Override
    public void buildGpu() {
        System.out.println("  [Builder] ⚠️ Budget computer does not include a graphics card");
    }

    @Override
    public void buildMotherboard() {
        computer.setMotherboard("Gigabyte A520", 7000);
    }

    @Override
    public void buildPowerSupply() {
        computer.setPowerSupply("450W", 2500);
    }

    @Override
    public void buildCase() {
        computer.setCase("Budget Case", 1500);
    }

    @Override
    public void addAccessories() {
        computer.addAccessory("Basic Mouse", 200);
    }

    @Override
    public Computer getComputer() {
        return computer;
    }
}

// =========================================================================
// Part 4: Director
// =========================================================================

class ComputerDirector {
    private ComputerBuilder builder;

    public ComputerDirector(ComputerBuilder builder) {
        this.builder = builder;
    }

    public void constructComputer() {
        builder.buildCpu();
        builder.buildMotherboard();
        builder.buildRam();
        builder.buildStorage();
        builder.buildGpu();
        builder.buildPowerSupply();
        builder.buildCase();
        builder.addAccessories();
    }

    public Computer getComputer() {
        return builder.getComputer();
    }
}

// =========================================================================
// Part 5: Main Class - With User Input
// =========================================================================

public class BuilderPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║         BUILDER PATTERN - Builder Design Pattern Demo            ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Select computer type:");
            System.out.println("1. Gaming Computer");
            System.out.println("2. Office Computer");
            System.out.println("3. Budget Computer");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            ComputerBuilder builder = null;
            String computerType = "";

            switch (choice) {
                case 1:
                    builder = new GamingComputerBuilder();
                    computerType = "Gaming Computer";
                    break;
                case 2:
                    builder = new OfficeComputerBuilder();
                    computerType = "Office Computer";
                    break;
                case 3:
                    builder = new BudgetComputerBuilder();
                    computerType = "Budget Computer";
                    break;
                case 4:
                    continueRunning = false;
                    System.out.println("\nThank you for using the system! Goodbye.");
                    continue;
                default:
                    System.out.println("Error: Please enter a valid choice (1-4)");
                    continue;
            }

            System.out.println("\n🔨 Building " + computerType + "...");

            ComputerDirector director = new ComputerDirector(builder);
            director.constructComputer();
            Computer computer = director.getComputer();

            computer.displayInfo();
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                Program terminated! Thank you                ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}