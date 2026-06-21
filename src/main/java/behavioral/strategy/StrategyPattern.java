// File: StrategyPattern.java
// This file demonstrates the Strategy Pattern in detail
// The user can choose different payment methods

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * STRATEGY PATTERN - The Strategy Design Pattern
 * =====================================================================
 * This design pattern defines algorithms that enable performing a task
 * in different ways.
 */

// =========================================================================
// Part 1: Strategy Interface
// =========================================================================

interface PaymentStrategy {
    boolean pay(double amount);
    String getMethodName();
    String getDetails();
}

// =========================================================================
// Part 2: Concrete Strategies
// =========================================================================

class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private String cvv;
    private double dailyLimit;
    private double usedToday;

    public CreditCardPayment(String cardNumber, String cardHolder, String expiryDate, String cvv) {
        this.cardNumber = maskCardNumber(cardNumber);
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.cvv = "***";
        this.dailyLimit = 50000.0;
        this.usedToday = 0.0;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() >= 4) {
            return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        }
        return "****";
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("\n  [CreditCard] 💳 Payment is being processed...");
        System.out.println("     Card: " + cardNumber);
        System.out.println("     Holder: " + cardHolder);
        System.out.println("     Amount: " + amount + " Birr");

        if (usedToday + amount > dailyLimit) {
            System.out.println("  [CreditCard] ❌ Daily limit exceeded!");
            return false;
        }

        usedToday += amount;
        System.out.println("  [CreditCard] ✅ Payment successful!");
        return true;
    }

    @Override
    public String getMethodName() {
        return "Credit Card";
    }

    @Override
    public String getDetails() {
        return String.format("Credit Card: %s (Limit: %.0f Birr)", cardNumber, dailyLimit);
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;
    private double balance;

    public PayPalPayment(String email, double initialBalance) {
        this.email = email;
        this.balance = initialBalance;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("\n  [PayPal] 📧 Payment is being processed...");
        System.out.println("     Email: " + email);
        System.out.println("     Amount: " + amount + " Birr");

        if (amount > balance) {
            System.out.println("  [PayPal] ❌ Insufficient funds! Balance: " + balance + " Birr");
            return false;
        }

        balance -= amount;
        System.out.println("  [PayPal] ✅ Payment successful!");
        System.out.println("     Remaining balance: " + balance + " Birr");
        return true;
    }

    @Override
    public String getMethodName() {
        return "PayPal";
    }

    @Override
    public String getDetails() {
        return String.format("PayPal: %s (Balance: %.2f Birr)", email, balance);
    }
}

class MobileMoneyPayment implements PaymentStrategy {
    private String phoneNumber;
    private String provider;

    public MobileMoneyPayment(String phoneNumber, String provider) {
        this.phoneNumber = phoneNumber;
        this.provider = provider;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("\n  [MobileMoney] 📱 Payment is being processed...");
        System.out.println("     Phone: " + phoneNumber);
        System.out.println("     Service Provider: " + provider);
        System.out.println("     Amount: " + amount + " Birr");

        // Simulate verification code
        String code = String.valueOf((int)(Math.random() * 9000) + 1000);
        System.out.println("     🔐 Verification Code: " + code);
        System.out.println("     (Waiting for confirmation...)");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("  [MobileMoney] ✅ Payment successful!");
        return true;
    }

    @Override
    public String getMethodName() {
        return provider + " Mobile Money";
    }

    @Override
    public String getDetails() {
        return String.format("%s: %s", provider, phoneNumber);
    }
}

class CashOnDeliveryPayment implements PaymentStrategy {
    private String address;

    public CashOnDeliveryPayment(String address) {
        this.address = address;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("\n  [CashOnDelivery] 💵 Payment will be made upon delivery...");
        System.out.println("     Address: " + address);
        System.out.println("     Amount: " + amount + " Birr");
        System.out.println("  [CashOnDelivery] ✅ Order placed! You will pay upon delivery");
        return true;
    }

    @Override
    public String getMethodName() {
        return "Cash on Delivery";
    }

    @Override
    public String getDetails() {
        return "Address: " + address;
    }
}

// =========================================================================
// Part 3: Context - Shopping Cart
// =========================================================================

class ShoppingCart {
    private List<CartItem> items;
    private PaymentStrategy paymentStrategy;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void addItem(String name, double price, int quantity) {
        items.add(new CartItem(name, price, quantity));
        System.out.println("  [Cart] ➕ " + name + " x" + quantity + " = " +
                (price * quantity) + " Birr");
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotal();
        }
        return total;
    }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
        System.out.println("\n  [Cart] 💳 Payment method selected: " + strategy.getMethodName());
    }

    public boolean checkout() {
        if (items.isEmpty()) {
            System.out.println("\n  [Cart] ❌ Your cart is empty!");
            return false;
        }

        if (paymentStrategy == null) {
            System.out.println("\n  [Cart] ❌ Please select a payment method!");
            return false;
        }

        System.out.println("\n  [Cart] 🛒 Processing payment...");
        System.out.println("     Total: " + getTotal() + " Birr");

        boolean success = paymentStrategy.pay(getTotal());

        if (success) {
            System.out.println("\n  [Cart] ✅ Payment successful! Thank you for shopping with us!");
            items.clear();
        } else {
            System.out.println("\n  [Cart] ❌ Payment failed! Please try another method");
        }

        return success;
    }

    public void showCart() {
        if (items.isEmpty()) {
            System.out.println("\n  🛒 Cart is empty");
            return;
        }

        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 🛒 Cart Contents:");
        for (CartItem item : items) {
            System.out.println("  │    • " + item);
        }
        System.out.println("  │    Total: " + getTotal() + " Birr");
        System.out.println("  └─────────────────────────────────");
    }
}

class CartItem {
    private String name;
    private double price;
    private int quantity;

    public CartItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public double getTotal() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return name + " - " + price + " Birr x" + quantity + " = " + getTotal() + " Birr";
    }
}

// =========================================================================
// Part 4: Main Class - With User Input
// =========================================================================

public class StrategyPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║         STRATEGY PATTERN - Strategy Design Pattern Demo        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        ShoppingCart cart = new ShoppingCart();

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Add item to cart");
            System.out.println("2. Show cart");
            System.out.println("3. Choose payment method");
            System.out.println("4. Process payment");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter item name: ");
                    String itemName = scanner.nextLine();

                    System.out.print("Enter item price: ");
                    double price = scanner.nextDouble();

                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    cart.addItem(itemName, price, quantity);
                    break;

                case 2:
                    cart.showCart();
                    break;

                case 3:
                    System.out.println("\nAvailable payment methods:");
                    System.out.println("1. Credit Card");
                    System.out.println("2. PayPal");
                    System.out.println("3. Mobile Money");
                    System.out.println("4. Cash on Delivery");
                    System.out.print("Enter your choice (1-4): ");

                    int methodChoice = scanner.nextInt();
                    scanner.nextLine();

                    PaymentStrategy strategy = null;

                    switch (methodChoice) {
                        case 1:
                            System.out.print("Enter card number: ");
                            String cardNum = scanner.nextLine();
                            System.out.print("Name on card: ");
                            String holder = scanner.nextLine();
                            System.out.print("Expiry date (MM/YY): ");
                            String expiry = scanner.nextLine();
                            System.out.print("CVV: ");
                            String cvv = scanner.nextLine();
                            strategy = new CreditCardPayment(cardNum, holder, expiry, cvv);
                            break;

                        case 2:
                            System.out.print("Enter PayPal email: ");
                            String email = scanner.nextLine();
                            System.out.print("Initial balance: ");
                            double balance = scanner.nextDouble();
                            scanner.nextLine();
                            strategy = new PayPalPayment(email, balance);
                            break;

                        case 3:
                            System.out.print("Enter phone number: ");
                            String phone = scanner.nextLine();
                            System.out.print("Service provider (M-Pesa/Airtel/MTN): ");
                            String provider = scanner.nextLine();
                            strategy = new MobileMoneyPayment(phone, provider);
                            break;

                        case 4:
                            System.out.print("Enter address: ");
                            String address = scanner.nextLine();
                            strategy = new CashOnDeliveryPayment(address);
                            break;

                        default:
                            System.out.println("❌ Invalid choice!");
                    }

                    if (strategy != null) {
                        cart.setPaymentStrategy(strategy);
                    }
                    break;

                case 4:
                    cart.checkout();
                    break;

                case 5:
                    continueRunning = false;
                    System.out.println("\nThank you for shopping! Goodbye.");
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