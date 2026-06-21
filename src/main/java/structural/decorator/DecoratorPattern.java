// File: DecoratorPattern.java
// This file demonstrates the Decorator Pattern in detail
// The user can select coffee and add extras

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * DECORATOR PATTERN
 * =====================================================================
 * This pattern allows adding additional responsibilities dynamically to an object.
 */

// =========================================================================
// Part 1: Component Interface
// =========================================================================

interface Coffee {
    String getDescription();
    double getCost();
    List<String> getIngredients();
}

// =========================================================================
// Part 2: Concrete Component
// =========================================================================

class SimpleCoffee implements Coffee {
    private String description;
    private double cost;
    private List<String> ingredients;

    public SimpleCoffee() {
        this.description = "Simple Coffee";
        this.cost = 20.00;
        this.ingredients = new ArrayList<>();
        this.ingredients.add("Coffee");
        this.ingredients.add("Water");
        System.out.println("  [SimpleCoffee] ☕ Simple coffee prepared - " + cost + " Birr");
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public List<String> getIngredients() {
        return new ArrayList<>(ingredients);
    }
}

// =========================================================================
// Part 3: Decorator Abstract Class
// =========================================================================

abstract class CoffeeDecorator implements Coffee {
    protected Coffee decoratedCoffee;
    protected String extraName;
    protected double extraCost;
    protected String extraIngredient;

    public CoffeeDecorator(Coffee coffee, String extraName, double extraCost, String extraIngredient) {
        this.decoratedCoffee = coffee;
        this.extraName = extraName;
        this.extraCost = extraCost;
        this.extraIngredient = extraIngredient;
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription() + " + " + extraName;
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost() + extraCost;
    }

    @Override
    public List<String> getIngredients() {
        List<String> allIngredients = decoratedCoffee.getIngredients();
        allIngredients.add(extraIngredient);
        return allIngredients;
    }
}

// =========================================================================
// Part 4: Concrete Decorators
// =========================================================================

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee, "Milk", 5.00, "Milk");
        System.out.println("  [MilkDecorator] 🥛 Milk added (+5 Birr)");
    }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee, "Sugar", 2.00, "Sugar");
        System.out.println("  [SugarDecorator] 🍬 Sugar added (+2 Birr)");
    }
}

class WhippedCreamDecorator extends CoffeeDecorator {
    public WhippedCreamDecorator(Coffee coffee) {
        super(coffee, "Whipped Cream", 8.00, "Cream");
        System.out.println("  [WhippedCreamDecorator] 🍦 Whipped cream added (+8 Birr)");
    }
}

class ChocolateDecorator extends CoffeeDecorator {
    public ChocolateDecorator(Coffee coffee) {
        super(coffee, "Chocolate", 10.00, "Chocolate");
        System.out.println("  [ChocolateDecorator] 🍫 Chocolate added (+10 Birr)");
    }
}

class CaramelDecorator extends CoffeeDecorator {
    public CaramelDecorator(Coffee coffee) {
        super(coffee, "Caramel", 7.00, "Caramel");
        System.out.println("  [CaramelDecorator] 🍯 Caramel added (+7 Birr)");
    }
}

class CinnamonDecorator extends CoffeeDecorator {
    public CinnamonDecorator(Coffee coffee) {
        super(coffee, "Cinnamon", 3.00, "Cinnamon");
        System.out.println("  [CinnamonDecorator] 🌿 Cinnamon added (+3 Birr)");
    }
}

// =========================================================================
// Part 5: Coffee Shop Class
// =========================================================================

class CoffeeShop {
    private String shopName;
    private List<Coffee> orders;
    private double totalRevenue;

    public CoffeeShop(String name) {
        this.shopName = name;
        this.orders = new ArrayList<>();
        this.totalRevenue = 0.0;
        System.out.println("\n==========================================");
        System.out.println("☕ New coffee shop opened: " + shopName);
        System.out.println("==========================================");
    }

    public void placeOrder(Coffee coffee) {
        orders.add(coffee);
        totalRevenue += coffee.getCost();

        System.out.println("\n──────────────────────────────────────────");
        System.out.println("🛒 New order received:");
        System.out.println("   Description: " + coffee.getDescription());
        System.out.println("   Price: " + String.format("%.2f", coffee.getCost()) + " Birr");
        System.out.println("   Ingredients: " + coffee.getIngredients());
        System.out.println("──────────────────────────────────────────");
    }

    public void showDailySummary() {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("📊 " + shopName + " Coffee Shop Daily Summary");
        System.out.println("══════════════════════════════════════════");
        System.out.println("   Total Orders: " + orders.size());
        System.out.println("   Total Revenue: " + String.format("%,.2f", totalRevenue) + " Birr");

        System.out.println("\n   Coffee Sold:");
        for (int i = 0; i < orders.size(); i++) {
            Coffee coffee = orders.get(i);
            System.out.println("   " + (i + 1) + ". " + coffee.getDescription() +
                    " - " + String.format("%.2f", coffee.getCost()) + " Birr");
        }
    }
}

// =========================================================================
// Part 6: Main Class - With User Input
// =========================================================================

public class DecoratorPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           DECORATOR PATTERN DEMO                       ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.print("Enter your coffee shop name: ");
        String shopName = scanner.nextLine();

        CoffeeShop shop = new CoffeeShop(shopName);

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("☕ Order New Coffee");
            System.out.println("Available Extras:");
            System.out.println("1. Milk (+5 Birr)");
            System.out.println("2. Sugar (+2 Birr)");
            System.out.println("3. Whipped Cream (+8 Birr)");
            System.out.println("4. Chocolate (+10 Birr)");
            System.out.println("5. Caramel (+7 Birr)");
            System.out.println("6. Cinnamon (+3 Birr)");
            System.out.println("7. Complete Order");
            System.out.println("8. View Daily Summary");
            System.out.println("9. Exit");

            Coffee coffee = new SimpleCoffee();
            boolean ordering = true;

            while (ordering) {
                System.out.print("\nEnter your choice (1-9): ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        coffee = new MilkDecorator(coffee);
                        break;
                    case 2:
                        coffee = new SugarDecorator(coffee);
                        break;
                    case 3:
                        coffee = new WhippedCreamDecorator(coffee);
                        break;
                    case 4:
                        coffee = new ChocolateDecorator(coffee);
                        break;
                    case 5:
                        coffee = new CaramelDecorator(coffee);
                        break;
                    case 6:
                        coffee = new CinnamonDecorator(coffee);
                        break;
                    case 7:
                        shop.placeOrder(coffee);
                        ordering = false;
                        break;
                    case 8:
                        shop.showDailySummary();
                        ordering = false;
                        break;
                    case 9:
                        continueRunning = false;
                        ordering = false;
                        break;
                    default:
                        System.out.println("Error: Please enter a valid choice!");
                }

                if (ordering && choice >= 1 && choice <= 6) {
                    System.out.println("   Current coffee: " + coffee.getDescription());
                    System.out.println("   Current price: " + String.format("%.2f", coffee.getCost()) + " Birr");
                }
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                PROGRAM COMPLETED! THANK YOU            ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}