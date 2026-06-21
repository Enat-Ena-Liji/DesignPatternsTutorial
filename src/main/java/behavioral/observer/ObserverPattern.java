// File: ObserverPattern.java
// This file demonstrates the Observer Pattern in detail
// The user can track stock price changes

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * =====================================================================
 * OBSERVER PATTERN
 * =====================================================================
 * This pattern creates a one-to-many relationship between objects.
 * When one object changes state, all its observers are notified.
 */

// =========================================================================
// Part 1: Observer Interface
// =========================================================================

interface StockObserver {
    void update(String symbol, double oldPrice, double newPrice);
    String getObserverName();
}

// =========================================================================
// Part 2: Subject - Stock
// =========================================================================

class Stock {
    private String symbol;
    private String companyName;
    private double price;
    private List<StockObserver> observers;
    private Map<String, Double> priceHistory;

    public Stock(String symbol, String companyName, double initialPrice) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = initialPrice;
        this.observers = new ArrayList<>();
        this.priceHistory = new HashMap<>();
        priceHistory.put(java.time.LocalDate.now().toString(), initialPrice);
    }

    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }

    public void attach(StockObserver observer) {
        observers.add(observer);
        System.out.println("  Stock: " + observer.getObserverName() + " registered");
    }

    public void detach(StockObserver observer) {
        observers.remove(observer);
        System.out.println("  Stock: " + observer.getObserverName() + " removed");
    }

    private void notifyObservers(double oldPrice) {
        System.out.println("\n  Stock:  Notifying observers...");
        for (StockObserver observer : observers) {
            observer.update(symbol, oldPrice, price);
        }
    }

    public void setPrice(double newPrice) {
        if (newPrice != price) {
            double oldPrice = price;
            this.price = newPrice;
            priceHistory.put(java.time.LocalDate.now().toString(), newPrice);
            System.out.println("\n  Stock: " + symbol + " price changed: " +
                    oldPrice + " → " + newPrice);
            notifyObservers(oldPrice);
        }
    }

    public void displayInfo() {
        System.out.println("\n┌─────────────────────────────────");
        System.out.println("  │  " + symbol + " - " + companyName);
        System.out.println("  │  Price: " + price + " Birr");
        System.out.println("  │  Observers: " + observers.size());
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Part 3: Concrete Observers
// =========================================================================

class Investor implements StockObserver {
    private String name;
    private double budget;
    private Map<String, Integer> portfolio;
    private List<String> notifications;

    public Investor(String name, double budget) {
        this.name = name;
        this.budget = budget;
        this.portfolio = new HashMap<>();
        this.notifications = new ArrayList<>();
    }

    @Override
    public void update(String symbol, double oldPrice, double newPrice) {
        double change = ((newPrice - oldPrice) / oldPrice) * 100;
        String notif = symbol + ": " + oldPrice + " → " + newPrice +
                " (" + String.format("%+.2f", change) + "%)";
        notifications.add(notif);

        System.out.println("  Investor: " + name + " received: " + notif);

        // React based on price change
        if (change < -5) {
            System.out.println("     " + name + ": Price dropped significantly - time to buy!");
        } else if (change > 5) {
            System.out.println("     " + name + ": Price increased significantly - time to sell!");
        }
    }

    @Override
    public String getObserverName() {
        return name + " (Investor)";
    }

    public void buyStock(String symbol, int shares, double price) {
        double cost = shares * price;
        if (cost <= budget) {
            budget -= cost;
            portfolio.put(symbol, portfolio.getOrDefault(symbol, 0) + shares);
            System.out.println("  Investor: " + name + " bought " + shares +
                    " of " + symbol + " at " + price + " Birr");
        } else {
            System.out.println("  Investor: " + name + " insufficient funds!");
        }
    }

    public void showPortfolio() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("    │  " + name + " Portfolio:");
        System.out.println("    │  Remaining Budget: " + budget + " Birr");
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            System.out.println("│    " + entry.getKey() + ": " + entry.getValue() + " shares");
        }
        System.out.println("    │  Notifications: " + notifications.size());
        System.out.println("    └─────────────────────────────────");
    }
}

class Broker implements StockObserver {
    private String name;
    private String firm;
    private List<String> recommendations;

    public Broker(String name, String firm) {
        this.name = name;
        this.firm = firm;
        this.recommendations = new ArrayList<>();
    }

    @Override
    public void update(String symbol, double oldPrice, double newPrice) {
        double change = ((newPrice - oldPrice) / oldPrice) * 100;

        String recommendation;
        if (change > 2) {
            recommendation = "Positive - Good time to buy";
        } else if (change < -2) {
            recommendation = "Negative - Good time to sell";
        } else {
            recommendation = "Neutral - Better to wait";
        }

        String rec = symbol + ": " + recommendation + " (Change: " +
                String.format("%+.2f", change) + "%)";
        recommendations.add(rec);

        System.out.println("  [Broker]  " + name + " from " + firm + " advice: " + rec);
    }

    @Override
    public String getObserverName() {
        return name + " (Broker)";
    }

    public void showRecommendations() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("    │  " + name + " Recommendations:");
        for (String rec : recommendations) {
            System.out.println("│    • " + rec);
        }
        System.out.println("    └─────────────────────────────────");
    }
}

// =========================================================================
// Part 4: Stock Market Manager
// =========================================================================

class StockMarket {
    private String name;
    private Map<String, Stock> stocks;
    private List<StockObserver> observers;

    public StockMarket(String name) {
        this.name = name;
        this.stocks = new HashMap<>();
        this.observers = new ArrayList<>(); 

        System.out.println("\n==========================================");
        System.out.println(" " + name + " Stock Market Opened");
        System.out.println("==========================================");
    }

    public void addStock(Stock stock) {
        stocks.put(stock.getSymbol(), stock);
        System.out.println("  Market:  " + stock.getSymbol() + " added to market");
    }

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void subscribeToAll(StockObserver observer) {
        for (Stock stock : stocks.values()) {
            stock.attach(observer);
        }
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public void listStocks() {
        System.out.println("\n Stocks available in market:");
        for (Stock stock : stocks.values()) {
            System.out.println("   • " + stock.getSymbol() + " - " + stock.getPrice() + " Birr");
        }
    }

    public void listObservers() {
        System.out.println("\n Registered Observers:");
        for (StockObserver obs : observers) {
            System.out.println("   • " + obs.getObserverName());
        }
    }
}

// =========================================================================
// Part 5: Main Class - With User Input
// =========================================================================

public class ObserverPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           OBSERVER PATTERN DEMO                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        StockMarket market = new StockMarket("Ethio Stock Market");

        // Create stocks
        Stock Laptop = new Stock("Laptop", "Ethio Telecom", 15000.00);
        Stock Mobile = new Stock("Mobile", "Commercial Bank", 8500.50);
        Stock Tablet = new Stock("Tablet", "Tablet Bank", 50000.75);

        market.addStock(Laptop);
        market.addStock(Mobile);
        market.addStock(Tablet);

        // Create observers
        Investor inv1 = new Investor("Daniel", 50000);
        Investor inv2 = new Investor("Yared", 100000);
        Broker broker = new Broker(  "Bezawit", "Ethio Brokers");

        market.addObserver(inv1);
        market.addObserver(inv2);
        market.addObserver(broker);

        market.subscribeToAll(inv1);
        market.subscribeToAll(inv2);
        market.subscribeToAll(broker);

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Change stock price");
            System.out.println("2. View stocks");
            System.out.println("3. View observers");
            System.out.println("4. View investor portfolio");
            System.out.println("5. View broker recommendations");
            System.out.println("6. Buy stock");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    market.listStocks();
                    System.out.print("Enter stock symbol (Laptop/Mobile/Tablet): ");
                    String symbol = scanner.nextLine();

                    Stock stock = market.getStock(symbol);
                    if (stock != null) {
                        System.out.print("Enter new price: ");
                        double newPrice = scanner.nextDouble();
                        scanner.nextLine();
                        stock.setPrice(newPrice);
                    } else {
                        System.out.println(" Unknown stock!");
                    }
                    break;

                case 2:
                    market.listStocks();
                    break;

                case 3:
                    market.listObservers();
                    break;

                case 4: 
                    System.out.println("Available investors: Daniel, Yared");
                    System.out.print("Enter investor name: ");
                    String invName = scanner.nextLine();

                    if (invName.equals("Daniel")) {
                        inv1.showPortfolio();
                    } else if (invName.equals("Yared")) {
                        inv2.showPortfolio();
                    } else {
                        System.out.println(" Investor not found!");
                    }
                    break;

                case 5:
                    broker.showRecommendations();
                    break;

                case 6:
                    market.listStocks();
                    System.out.print("Enter stock symbol: ");
                    String buySymbol = scanner.nextLine();

                    Stock buyStock = market.getStock(buySymbol);
                    if (buyStock != null) {
                        System.out.print("How many shares do you want to buy? ");
                        int shares = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Enter investor name: ");
                        String buyer = scanner.nextLine();

                        if (buyer.equals("Daniel")) {
                            inv1.buyStock(buySymbol, shares, buyStock.getPrice());
                        } else if (buyer.equals("Yared")) {
                            inv2.buyStock(buySymbol, shares, buyStock.getPrice());
                        } else {
                            System.out.println(" Investor not found!");
                        }
                    } else {
                        System.out.println(" Unknown stock!");
                    }
                    break;

                case 7:
                    continueRunning = false;
                    System.out.println("\nThank you for using the program! GooTabletye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-7)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║                PROGRAM COMPLETED! THANK YOU            ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");
    }
}