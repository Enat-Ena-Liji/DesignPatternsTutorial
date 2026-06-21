// File: ChainOfResponsibilityPattern.java
// This file demonstrates the Chain of Responsibility Pattern in detail
// The user can submit different types of customer requests

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * CHAIN OF RESPONSIBILITY PATTERN - The Chain of Responsibility Design Pattern
 * =====================================================================
 * This design pattern decouples the sender of a request from its receiver.
 * The request is passed along a chain of handlers until it is processed.
 */

// =========================================================================
// Part 1: Request Class
// =========================================================================

class SupportRequest {
    private int requestId;
    private String customerName;
    private String issueType;
    private String description;
    private int priority; // 1-5 (1 low, 5 high)
    private String status;
    private static int idCounter = 1000;

    public SupportRequest(String customerName, String issueType, String description, int priority) {
        this.requestId = idCounter++;
        this.customerName = customerName;
        this.issueType = issueType;
        this.description = description;
        this.priority = priority;
        this.status = "New";
    }

    public int getRequestId() { return requestId; }
    public String getCustomerName() { return customerName; }
    public String getIssueType() { return issueType; }
    public String getDescription() { return description; }
    public int getPriority() { return priority; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public void displayInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 🆔 Request #" + requestId);
        System.out.println("  │ 👤 Customer: " + customerName);
        System.out.println("  │ 📋 Type: " + issueType);
        System.out.println("  │ 📝 Description: " + description);
        System.out.println("  │ ⚡ Priority: " + priority + "/5");
        System.out.println("  │ 📊 Status: " + status);
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Part 2: Handler Interface
// =========================================================================

interface SupportHandler {
    void handleRequest(SupportRequest request);
    void setNextHandler(SupportHandler next);
    String getHandlerName();
    boolean canHandle(SupportRequest request);
}

// =========================================================================
// Part 3: Abstract Handler
// =========================================================================

abstract class BaseSupportHandler implements SupportHandler {
    protected SupportHandler nextHandler;
    protected String handlerName;
    protected int minPriority;
    protected int maxPriority;
    protected List<String> supportedIssueTypes;

    public BaseSupportHandler(String name, int minPriority, int maxPriority) {
        this.handlerName = name;
        this.minPriority = minPriority;
        this.maxPriority = maxPriority;
        this.supportedIssueTypes = new ArrayList<>();
    }

    @Override
    public void setNextHandler(SupportHandler next) {
        this.nextHandler = next;
    }

    @Override
    public String getHandlerName() {
        return handlerName;
    }

    protected void addSupportedIssueType(String type) {
        supportedIssueTypes.add(type);
    }

    @Override
    public boolean canHandle(SupportRequest request) {
        if (request.getPriority() < minPriority || request.getPriority() > maxPriority) {
            return false;
        }

        if (!supportedIssueTypes.isEmpty() && !supportedIssueTypes.contains(request.getIssueType())) {
            return false;
        }

        return true;
    }

    protected void passToNext(SupportRequest request) {
        if (nextHandler != null) {
            System.out.println("  [Chain] ⏩ Request #" + request.getRequestId() +
                    " forwarded to " + nextHandler.getHandlerName());
            nextHandler.handleRequest(request);
        } else {
            System.out.println("  [Chain] ❌ Request #" + request.getRequestId() +
                    " could not be handled!");
            request.setStatus("Could not be handled");
        }
    }
}

// =========================================================================
// Part 4: Concrete Handlers
// =========================================================================

class CustomerServiceHandler extends BaseSupportHandler {

    public CustomerServiceHandler() {
        super("Customer Service", 1, 2);
        addSupportedIssueType("Information");
        addSupportedIssueType("Complaint");
    }

    @Override
    public void handleRequest(SupportRequest request) {
        System.out.println("\n  [Handler] 👤 " + handlerName + " checking request #" +
                request.getRequestId() + "...");

        if (canHandle(request)) {
            System.out.println("  ✅ " + handlerName + " can handle this request!");
            System.out.println("     Processing the request...");
            request.setStatus("Processed by Customer Service");
        } else {
            System.out.println("  ⚠️ " + handlerName + " cannot handle this request");
            passToNext(request);
        }
    }
}

class TechnicalSupportHandler extends BaseSupportHandler {

    public TechnicalSupportHandler() {
        super("Technical Support", 2, 4);
        addSupportedIssueType("Technical");
        addSupportedIssueType("Software");
        addSupportedIssueType("Hardware");
    }

    @Override
    public void handleRequest(SupportRequest request) {
        System.out.println("\n  [Handler] 🔧 " + handlerName + " checking request #" +
                request.getRequestId() + "...");

        if (canHandle(request)) {
            System.out.println("  ✅ " + handlerName + " can handle this request!");
            System.out.println("     Resolving technical issue...");
            request.setStatus("Processed by Technical Support");
        } else {
            System.out.println("  ⚠️ " + handlerName + " cannot handle this request");
            passToNext(request);
        }
    }
}

class BillingHandler extends BaseSupportHandler {

    public BillingHandler() {
        super("Billing Department", 3, 5);
        addSupportedIssueType("Payment");
        addSupportedIssueType("Account");
        addSupportedIssueType("Invoice");
    }

    @Override
    public void handleRequest(SupportRequest request) {
        System.out.println("\n  [Handler] 💰 " + handlerName + " checking request #" +
                request.getRequestId() + "...");

        if (canHandle(request)) {
            System.out.println("  ✅ " + handlerName + " can handle this request!");
            System.out.println("     Processing billing inquiry...");
            request.setStatus("Processed by Billing Department");
        } else {
            System.out.println("  ⚠️ " + handlerName + " cannot handle this request");
            passToNext(request);
        }
    }
}

class ManagerHandler extends BaseSupportHandler {

    public ManagerHandler() {
        super("Manager", 4, 5);
    }

    @Override
    public void handleRequest(SupportRequest request) {
        System.out.println("\n  [Handler] 👔 " + handlerName + " checking request #" +
                request.getRequestId() + "...");

        System.out.println("  ✅ " + handlerName + " can handle this request!");
        System.out.println("     Processing high priority request...");
        request.setStatus("Processed by Manager");
    }
}

// =========================================================================
// Part 5: Request Manager
// =========================================================================

class SupportSystem {
    private String systemName;
    private SupportHandler firstHandler;
    private List<SupportRequest> allRequests;

    public SupportSystem(String name) {
        this.systemName = name;
        this.allRequests = new ArrayList<>();
        buildChain();

        System.out.println("\n==========================================");
        System.out.println("📞 " + systemName + " Support System Started");
        System.out.println("==========================================");
    }

    private void buildChain() {
        SupportHandler customerService = new CustomerServiceHandler();
        SupportHandler technical = new TechnicalSupportHandler();
        SupportHandler billing = new BillingHandler();
        SupportHandler manager = new ManagerHandler();

        customerService.setNextHandler(technical);
        technical.setNextHandler(billing);
        billing.setNextHandler(manager);

        firstHandler = customerService;

        System.out.println("\n  [Chain] 🔗 Chain of Responsibility built:");
        System.out.println("     Customer Service → Technical Support → Billing Department → Manager");
    }

    public SupportRequest submitRequest(String customer, String type, String desc, int priority) {
        SupportRequest request = new SupportRequest(customer, type, desc, priority);
        allRequests.add(request);

        System.out.println("\n📨 New request submitted! (#" + request.getRequestId() + ")");
        request.displayInfo();

        System.out.println("\n  [System] 🔄 Passing request through the chain...");
        firstHandler.handleRequest(request);

        return request;
    }

    public void showAllRequests() {
        System.out.println("\n📋 All Requests (" + allRequests.size() + "):");
        for (SupportRequest req : allRequests) {
            System.out.println("   #" + req.getRequestId() + " - " + req.getCustomerName() +
                    " - " + req.getIssueType() + " - " + req.getStatus());
        }
    }

    public void showRequestDetails(int id) {
        for (SupportRequest req : allRequests) {
            if (req.getRequestId() == id) {
                req.displayInfo();
                return;
            }
        }
        System.out.println("❌ Request #" + id + " not found!");
    }
}

// =========================================================================
// Part 6: Main Class - With User Input
// =========================================================================

public class ChainOfResponsibilityPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║   CHAIN OF RESPONSIBILITY PATTERN - Chain of Responsibility Demo  ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        SupportSystem support = new SupportSystem("Marta Support Center");

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Submit new request");
            System.out.println("2. View all requests");
            System.out.println("3. View request details");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter customer name: ");
                    String customer = scanner.nextLine();

                    System.out.print("Issue type (Information/Complaint/Technical/Software/Hardware/Payment/Account/Invoice): ");
                    String type = scanner.nextLine();

                    System.out.print("Enter description: ");
                    String desc = scanner.nextLine();

                    System.out.print("Priority (1-5, 1 low 5 high): ");
                    int priority = scanner.nextInt();
                    scanner.nextLine();

                    if (priority < 1 || priority > 5) {
                        System.out.println("⚠️ Priority must be between 1-5. Setting to 3");
                        priority = 3;
                    }

                    support.submitRequest(customer, type, desc, priority);
                    break;

                case 2:
                    support.showAllRequests();
                    break;

                case 3:
                    System.out.print("Enter request ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    support.showRequestDetails(id);
                    break;

                case 4:
                    continueRunning = false;
                    System.out.println("\nThank you for using the system! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-4)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                Program terminated! Thank you                ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}