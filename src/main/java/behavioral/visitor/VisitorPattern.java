// File: VisitorPattern.java
// This file demonstrates the Visitor Pattern in detail
// The user can visit different menu items

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * VISITOR PATTERN - The Visitor Design Pattern
 * =====================================================================
 * This design pattern allows adding new operations to object structures
 * without changing the classes of the objects.
 */

// =========================================================================
// Part 1: Element Interface
// =========================================================================

interface MenuItem {
    void accept(MenuItemVisitor visitor);
    String getName();
    double getPrice();
}

// =========================================================================
// Part 2: Visitor Interface
// =========================================================================

interface MenuItemVisitor {
    void visit(FoodItem food);
    void visit(DrinkItem drink);
    void visit(DessertItem dessert);
    String getVisitorName();
}

// =========================================================================
// Part 3: Concrete Elements
// =========================================================================

class FoodItem implements MenuItem {
    private String name;
    private double price;
    private boolean isSpicy;
    private boolean isVegetarian;
    private int calories;

    public FoodItem(String name, double price, boolean isSpicy, boolean isVegetarian, int calories) {
        this.name = name;
        this.price = price;
        this.isSpicy = isSpicy;
        this.isVegetarian = isVegetarian;
        this.calories = calories;
    }

    @Override
    public void accept(MenuItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() { return name; }

    @Override
    public double getPrice() { return price; }

    public boolean isSpicy() { return isSpicy; }
    public boolean isVegetarian() { return isVegetarian; }
    public int getCalories() { return calories; }
}

class DrinkItem implements MenuItem {
    private String name;
    private double price;
    private boolean isAlcoholic;
    private String size;
    private boolean isCarbonated;

    public DrinkItem(String name, double price, boolean isAlcoholic, String size, boolean isCarbonated) {
        this.name = name;
        this.price = price;
        this.isAlcoholic = isAlcoholic;
        this.size = size;
        this.isCarbonated = isCarbonated;
    }

    @Override
    public void accept(MenuItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() { return name; }

    @Override
    public double getPrice() { return price; }

    public boolean isAlcoholic() { return isAlcoholic; }
    public String getSize() { return size; }
    public boolean isCarbonated() { return isCarbonated; }
}

class DessertItem implements MenuItem {
    private String name;
    private double price;
    private boolean containsChocolate;
    private boolean containsNuts;
    private int sugarContent;

    public DessertItem(String name, double price, boolean containsChocolate, boolean containsNuts, int sugarContent) {
        this.name = name;
        this.price = price;
        this.containsChocolate = containsChocolate;
        this.containsNuts = containsNuts;
        this.sugarContent = sugarContent;
    }

    @Override
    public void accept(MenuItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() { return name; }

    @Override
    public double getPrice() { return price; }

    public boolean containsChocolate() { return containsChocolate; }
    public boolean containsNuts() { return containsNuts; }
    public int getSugarContent() { return sugarContent; }
}

// =========================================================================
// Part 4: Concrete Visitors
// =========================================================================

class PriceCalculatorVisitor implements MenuItemVisitor {
    private double totalOriginal;
    private double totalDiscounted;
    private List<String> calculations;

    public PriceCalculatorVisitor() {
        this.totalOriginal = 0;
        this.totalDiscounted = 0;
        this.calculations = new ArrayList<>();
    }

    @Override
    public void visit(FoodItem food) {
        double price = food.getPrice();
        double finalPrice = price;
        String note = "";

        if (food.isVegetarian()) {
            finalPrice *= 0.95; // 5% discount
            note += " Vegetarian -5%";
        }
        if (food.isSpicy()) {
            finalPrice *= 1.02; // 2% extra
            note += " Spicy +2%";
        }

        totalOriginal += price;
        totalDiscounted += finalPrice;
        calculations.add(String.format("  %s: %.2f → %.2f Birr%s",
                food.getName(), price, finalPrice, note));
    }

    @Override
    public void visit(DrinkItem drink) {
        double price = drink.getPrice();
        double finalPrice = price;
        String note = "";

        if (drink.getSize().equals("Large")) {
            finalPrice *= 1.3; // 30% extra
            note += " Large +30%";
        } else if (drink.getSize().equals("Small")) {
            finalPrice *= 0.8; // 20% discount
            note += " Small -20%";
        }

        if (drink.isAlcoholic()) {
            finalPrice *= 1.15; // 15% extra
            note += " Alcoholic +15%";
        }

        totalOriginal += price;
        totalDiscounted += finalPrice;
        calculations.add(String.format("  %s: %.2f → %.2f Birr%s",
                drink.getName(), price, finalPrice, note));
    }

    @Override
    public void visit(DessertItem dessert) {
        double price = dessert.getPrice();
        double finalPrice = price;
        String note = "";

        if (dessert.containsChocolate()) {
            finalPrice *= 1.1; // 10% extra
            note += " Chocolate +10%";
        }
        if (dessert.containsNuts()) {
            finalPrice *= 1.05; // 5% extra
            note += " Nuts +5%";
        }

        totalOriginal += price;
        totalDiscounted += finalPrice;
        calculations.add(String.format("  %s: %.2f → %.2f Birr%s",
                dessert.getName(), price, finalPrice, note));
    }

    @Override
    public String getVisitorName() {
        return "Price Calculator";
    }

    public void printReport() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 💰 Price Report");
        System.out.println("  ├─────────────────────────────────");
        for (String calc : calculations) {
            System.out.println("  │ " + calc);
        }
        System.out.println("  ├─────────────────────────────────");
        System.out.println("  │ Total Price: " + String.format("%.2f", totalOriginal) + " Birr");
        System.out.println("  │ After Discounts: " + String.format("%.2f", totalDiscounted) + " Birr");
        System.out.println("  │ Savings: " + String.format("%.2f", totalOriginal - totalDiscounted) + " Birr");
        System.out.println("  └─────────────────────────────────");
    }
}

class CalorieCounterVisitor implements MenuItemVisitor {
    private int totalCalories;
    private List<String> calorieInfo;

    public CalorieCounterVisitor() {
        this.totalCalories = 0;
        this.calorieInfo = new ArrayList<>();
    }

    @Override
    public void visit(FoodItem food) {
        calorieInfo.add(String.format("  %s: %d calories", food.getName(), food.getCalories()));
        totalCalories += food.getCalories();
    }

    @Override
    public void visit(DrinkItem drink) {
        int calories = 0;
        if (drink.isAlcoholic()) calories = 150;
        else if (drink.isCarbonated()) calories = 100;
        else calories = 50;

        if (drink.getSize().equals("Large")) calories *= 1.5;
        else if (drink.getSize().equals("Small")) calories *= 0.7;

        calorieInfo.add(String.format("  %s: %d calories (estimate)", drink.getName(), calories));
        totalCalories += calories;
    }

    @Override
    public void visit(DessertItem dessert) {
        int calories = dessert.getSugarContent() * 4;
        if (dessert.containsChocolate()) calories += 50;
        if (dessert.containsNuts()) calories += 30;

        calorieInfo.add(String.format("  %s: %d calories", dessert.getName(), calories));
        totalCalories += calories;
    }

    @Override
    public String getVisitorName() {
        return "Calorie Counter";
    }

    public void printReport() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 🔥 Calorie Report");
        System.out.println("  ├─────────────────────────────────");
        for (String info : calorieInfo) {
            System.out.println("  │ " + info);
        }
        System.out.println("  ├─────────────────────────────────");
        System.out.println("  │ Total Calories: " + totalCalories);
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Part 5: Menu Class
// =========================================================================

class Menu {
    private String restaurantName;
    private List<MenuItem> items;

    public Menu(String name) {
        this.restaurantName = name;
        this.items = new ArrayList<>();
        initializeMenu();

        System.out.println("\n==========================================");
        System.out.println("🍽️ " + restaurantName + " Menu Created");
        System.out.println("==========================================");
    }

    private void initializeMenu() {
        items.add(new FoodItem("Doro Wat", 250.00, true, false, 650));
        items.add(new FoodItem("Shiro Wat", 180.00, false, true, 450));
        items.add(new DrinkItem("Beer", 80.00, true, "Large", true));
        items.add(new DrinkItem("Coca Cola", 50.00, false, "Medium", true));
        items.add(new DessertItem("Chocolate Cake", 150.00, true, true, 45));
        items.add(new DessertItem("Fruit Salad", 120.00, false, false, 15));
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void accept(MenuItemVisitor visitor) {
        System.out.println("\n  [Menu] 🔄 " + visitor.getVisitorName() + " is visiting...");
        for (MenuItem item : items) {
            item.accept(visitor);
        }
    }

    public void displayMenu() {
        System.out.println("\n📋 " + restaurantName + " Menu:");
        for (MenuItem item : items) {
            String type = "";
            if (item instanceof FoodItem) type = "🍲";
            else if (item instanceof DrinkItem) type = "🥤";
            else if (item instanceof DessertItem) type = "🍰";

            System.out.println("   " + type + " " + item.getName() + " - " +
                    String.format("%.2f", item.getPrice()) + " Birr");
        }
    }
}

// =========================================================================
// Part 6: Main Class - With User Input
// =========================================================================

public class VisitorPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║         VISITOR PATTERN - Visitor Design Pattern Demo          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.print("Enter restaurant name: ");
        String restaurantName = scanner.nextLine();

        Menu menu = new Menu(restaurantName);

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Display Menu");
            System.out.println("2. Use Price Calculator Visitor");
            System.out.println("3. Use Calorie Counter Visitor");
            System.out.println("4. Add New Item to Menu");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    menu.displayMenu();
                    break;

                case 2:
                    PriceCalculatorVisitor priceVisitor = new PriceCalculatorVisitor();
                    menu.accept(priceVisitor);
                    priceVisitor.printReport();
                    break;

                case 3:
                    CalorieCounterVisitor calorieVisitor = new CalorieCounterVisitor();
                    menu.accept(calorieVisitor);
                    calorieVisitor.printReport();
                    break;

                case 4:
                    System.out.println("\nSelect item type:");
                    System.out.println("1. Food");
                    System.out.println("2. Drink");
                    System.out.println("3. Dessert");
                    System.out.print("Enter your choice: ");

                    int itemType = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();

                    switch (itemType) {
                        case 1:
                            System.out.print("Is it spicy? (true/false): ");
                            boolean spicy = scanner.nextBoolean();
                            System.out.print("Is it vegetarian? (true/false): ");
                            boolean veg = scanner.nextBoolean();
                            System.out.print("Enter calories: ");
                            int cal = scanner.nextInt();
                            scanner.nextLine();

                            menu.addItem(new FoodItem(name, price, spicy, veg, cal));
                            break;

                        case 2:
                            System.out.print("Is it alcoholic? (true/false): ");
                            boolean alc = scanner.nextBoolean();
                            scanner.nextLine();
                            System.out.print("Size (Small/Medium/Large): ");
                            String size = scanner.nextLine();
                            System.out.print("Is it carbonated? (true/false): ");
                            boolean carb = scanner.nextBoolean();
                            scanner.nextLine();

                            menu.addItem(new DrinkItem(name, price, alc, size, carb));
                            break;

                        case 3:
                            System.out.print("Contains chocolate? (true/false): ");
                            boolean choc = scanner.nextBoolean();
                            System.out.print("Contains nuts? (true/false): ");
                            boolean nuts = scanner.nextBoolean();
                            System.out.print("Sugar content (in grams): ");
                            int sugar = scanner.nextInt();
                            scanner.nextLine();

                            menu.addItem(new DessertItem(name, price, choc, nuts, sugar));
                            break;
                    }
                    System.out.println("✅ New item added!");
                    break;

                case 5:
                    continueRunning = false;
                    System.out.println("\nThank you for using the system! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-5)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                Program terminated! Thank you                ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}