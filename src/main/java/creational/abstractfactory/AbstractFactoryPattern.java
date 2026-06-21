// File: AbstractFactoryPattern.java
// This file demonstrates the Abstract Factory Pattern in detail
// The user can select bank type and loan type

import java.util.Scanner;

/**
 * =====================================================================
 * ABSTRACT FACTORY PATTERN
 * =====================================================================
 * This design pattern defines an interface for creating families of
 * related or dependent objects.
 */

// =========================================================================
// Section 1: Bank Interface
// =========================================================================

interface Bank {
    String getBankName();
    String getBankType();
    String getAddress();
    void displayBankInfo();
}

// =========================================================================
// Section 2: Abstract Loan Class
// =========================================================================

abstract class Loan {
    protected double rate;
    protected String loanName;
    protected String description;

    public abstract void getInterestRate();

    public double calculateLoanPayment(double loanAmount, int years) {
        if (rate == 0) {
            getInterestRate();
        }
        // Simple interest calculation
        return loanAmount + (loanAmount * rate * years);
    }

    public void displayLoanInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 🏦 Loan: " + loanName);
        System.out.println("  │ Rate: " + (rate * 100) + "%");
        System.out.println("  │ Description: " + description);
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Section 3: Concrete Banks
// =========================================================================

class CommercialBankOfEthiopia implements Bank {
    private String bankName;
    private String bankType;
    private String address;

    public CommercialBankOfEthiopia() {
        this.bankName = "Commercial Bank of Ethiopia";
        this.bankType = "Commercial Bank";
        this.address = " Square, INJIBARA";
        System.out.println("  [Bank] 🏦 Commercial Bank of Ethiopia created");
    }

    @Override
    public String getBankName() { return bankName; }
    @Override
    public String getBankType() { return bankType; }
    @Override
    public String getAddress() { return address; }

    @Override
    public void displayBankInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 🏦 " + bankName);
        System.out.println("  │ Type: " + bankType);
        System.out.println("  │ Address: " + address);
        System.out.println("  └─────────────────────────────────");
    }
}

class DashenBank implements Bank {
    private String bankName;
    private String bankType;
    private String address;

    public DashenBank() {
        this.bankName = "Dashen Bank";
        this.bankType = "Commercial Bank";
        this.address = "Bahir Dar Road, Injibara";
        System.out.println("  [Bank] 🏦 Dashen Bank created");
    }

    @Override
    public String getBankName() { return bankName; }
    @Override
    public String getBankType() { return bankType; }
    @Override
    public String getAddress() { return address; }

    @Override
    public void displayBankInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 🏦 " + bankName);
        System.out.println("  │ Type: " + bankType);
        System.out.println("  │ Address: " + address);
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Section 4: Concrete Loans
// =========================================================================

class HomeLoan extends Loan {

    public HomeLoan() {
        this.loanName = "Home Loan";
        this.description = "Long-term loan for buying or building a house";
    }

    @Override
    public void getInterestRate() {
        rate = 0.09; // 9%
        System.out.println("  [Loan] 🏠 Home Loan rate: " + (rate * 100) + "%");
    }
}

class BusinessLoan extends Loan {

    public BusinessLoan() {
        this.loanName = "Business Loan";
        this.description = "Loan for expanding business or starting a new business";
    }

    @Override
    public void getInterestRate() {
        rate = 0.12; // 12%
        System.out.println("  [Loan] 🏢 Business Loan rate: " + (rate * 100) + "%");
    }
}

class EducationLoan extends Loan {

    public EducationLoan() {
        this.loanName = "Education Loan";
        this.description = "Loan for educational expenses";
    }

    @Override
    public void getInterestRate() {
        rate = 0.06; // 6%
        System.out.println("  [Loan] 📚 Education Loan rate: " + (rate * 100) + "%");
    }
}

// =========================================================================
// Section 5: Abstract Factory
// =========================================================================

abstract class FinancialFactory {
    public abstract Bank createBank(String bankName);
    public abstract Loan createLoan(String loanType);
    public abstract String getFactoryName();
}

// =========================================================================
// Section 6: Concrete Factories
// =========================================================================

class CommercialBankFactory extends FinancialFactory {

    public CommercialBankFactory() {
        System.out.println("  [Factory] 🏭 New Commercial Bank Factory created");
    }

    @Override
    public Bank createBank(String bankName) {
        System.out.println("  [Factory] 🏭 Preparing to create '" + bankName + "' bank...");

        if (bankName.equalsIgnoreCase("CBE") || bankName.equalsIgnoreCase("COMMERCIAL")) {
            return new CommercialBankOfEthiopia();
        } else if (bankName.equalsIgnoreCase("DASHEN") || bankName.equalsIgnoreCase("DASHEN")) {
            return new DashenBank();
        }

        System.out.println("  [Factory] ❌ Unknown commercial bank: " + bankName);
        return null;
    }

    @Override
    public Loan createLoan(String loanType) {
        System.out.println("  [Factory] 🏭 Preparing to create '" + loanType + "' loan...");

        if (loanType.equalsIgnoreCase("BUSINESS") || loanType.equalsIgnoreCase("BUSINESS")) {
            return new BusinessLoan();
        } else if (loanType.equalsIgnoreCase("HOME") || loanType.equalsIgnoreCase("HOME")) {
            return new HomeLoan();
        }

        System.out.println("  [Factory] ❌ Unknown commercial loan: " + loanType);
        return null;
    }

    @Override
    public String getFactoryName() {
        return "Commercial Bank Factory";
    }
}

class DevelopmentBankFactory extends FinancialFactory {

    public DevelopmentBankFactory() {
        System.out.println("  [Factory] 🏭 New Development Bank Factory created");
    }

    @Override
    public Bank createBank(String bankName) {
        System.out.println("  [Factory] 🏭 Preparing to create '" + bankName + "' bank...");

        if (bankName.equalsIgnoreCase("DBE") || bankName.equalsIgnoreCase("DEVELOPMENT")) {
            return new CommercialBankOfEthiopia(); // For demonstration
        }

        System.out.println("  [Factory] ❌ Unknown development bank: " + bankName);
        return null;
    }

    @Override
    public Loan createLoan(String loanType) {
        System.out.println("  [Factory] 🏭 Preparing to create '" + loanType + "' loan...");

        if (loanType.equalsIgnoreCase("EDUCATION") || loanType.equalsIgnoreCase("EDUCATION")) {
            return new EducationLoan();
        }

        System.out.println("  [Factory] ❌ Unknown development loan: " + loanType);
        return null;
    }

    @Override
    public String getFactoryName() {
        return "Development Bank Factory";
    }
}

// =========================================================================
// Section 7: Factory Creator
// =========================================================================

class FactoryCreator {

    public static FinancialFactory getFactory(String choice) {
        System.out.println("  [Creator] 🔧 Looking for appropriate factory: '" + choice + "'");

        if (choice == null || choice.isEmpty()) {
            return null;
        }

        if (choice.equalsIgnoreCase("COMMERCIAL")) {
            System.out.println("  [Creator] ✅ Commercial Bank Factory selected");
            return new CommercialBankFactory();
        } else if (choice.equalsIgnoreCase("DEVELOPMENT")) {
            System.out.println("  [Creator] ✅ Development Bank Factory selected");
            return new DevelopmentBankFactory();
        }

        System.out.println("  [Creator] ❌ Unknown factory choice: '" + choice + "'");
        return null;
    }
}

// =========================================================================
// Section 8: Main Class - With User Input
// =========================================================================

public class AbstractFactoryPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║          ABSTRACT FACTORY PATTERN DEMO                 ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Use Commercial Bank Factory");
            System.out.println("2. Use Development Bank Factory");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            FinancialFactory factory = null;

            switch (choice) {
                case 1:
                    factory = FactoryCreator.getFactory("COMMERCIAL");
                    break;
                case 2:
                    factory = FactoryCreator.getFactory("DEVELOPMENT");
                    break;
                case 3:
                    continueRunning = false;
                    System.out.println("\nThank you for using the program! Goodbye.");
                    continue;
                default:
                    System.out.println("Error: Please enter a valid choice (1-3)");
                    continue;
            }

            if (factory != null) {
                System.out.println("\n--- " + factory.getFactoryName() + " selected ---");

                System.out.print("\nEnter bank name (CBE/DASHEN/DBE): ");
                String bankName = scanner.nextLine();

                Bank bank = factory.createBank(bankName);
                if (bank != null) {
                    bank.displayBankInfo();
                }

                System.out.print("\nEnter loan type (HOME/BUSINESS/EDUCATION): ");
                String loanType = scanner.nextLine();

                Loan loan = factory.createLoan(loanType);
                if (loan != null) {
                    System.out.print("Enter loan amount: ");
                    double amount = scanner.nextDouble();

                    System.out.print("Enter repayment period (in years): ");
                    int years = scanner.nextInt();
                    scanner.nextLine();

                    double totalPayment = loan.calculateLoanPayment(amount, years);

                    loan.displayLoanInfo();
                    System.out.println("\n  💰 Total payment: " + String.format("%.2f", totalPayment) + " Birr");
                }
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              Program terminated! Thank you              ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}