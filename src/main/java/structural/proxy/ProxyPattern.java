// File: ProxyPattern.java
// This file demonstrates the Proxy Pattern in detail
// The user can read and modify documents

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * =====================================================================
 * PROXY PATTERN
 * =====================================================================
 * This design pattern creates a substitute or proxy for another object
 * and controls access to it.
 */

// =========================================================================
// Section 1: Document Interface
// =========================================================================

interface Document {
    String getTitle();
    String getContent(ProxyUser user);
    boolean updateContent(ProxyUser user, String newContent);
    void displayInfo();
}

// =========================================================================
// Section 2: User Class
// =========================================================================

class ProxyUser {
    private String username;
    private String role; // ADMIN, EDITOR, VIEWER, BLOCKED
    private String department;

    public ProxyUser(String username, String role, String department) {
        this.username = username;
        this.role = role;
        this.department = department;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getDepartment() { return department; }

    public boolean canRead() {
        return !role.equals("BLOCKED");
    }

    public boolean canWrite() {
        return role.equals("ADMIN") || role.equals("EDITOR");
    }

    @Override
    public String toString() {
        return username + " [" + role + "]";
    }
}

// =========================================================================
// Section 3: Real Document
// =========================================================================

class RealDocument implements Document {
    private String title;
    private String content;
    private String owner;
    private List<String> accessLog;

    public RealDocument(String title, String content, String owner) {
        System.out.println("  [RealDocument] ⚠️ Real document being created...");

        this.title = title;
        this.content = content;
        this.owner = owner;
        this.accessLog = new ArrayList<>();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("  [RealDocument] ✅ Real document created: " + title);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent(ProxyUser user) {
        String log = user.getUsername() + " read at " + System.currentTimeMillis();
        accessLog.add(log);
        return content;
    }

    @Override
    public boolean updateContent(ProxyUser user, String newContent) {
        if (user.getUsername().equals(owner) || user.getRole().equals("ADMIN")) {
            this.content = newContent;
            String log = user.getUsername() + " updated at " + System.currentTimeMillis();
            accessLog.add(log);
            return true;
        }
        return false;
    }

    @Override
    public void displayInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📄 Document: " + title);
        System.out.println("  │ Owner: " + owner);
        System.out.println("  │ Times read: " + accessLog.size());
        System.out.println("  │ Content: " + content.substring(0, Math.min(50, content.length())) + "...");
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Section 4: Document Proxy
// =========================================================================

class DocumentProxy implements Document {
    private String title;
    private String content;
    private String owner;
    private RealDocument realDocument;
    private Map<String, List<Long>> accessTimes;

    public DocumentProxy(String title, String content, String owner) {
        this.title = title;
        this.content = content;
        this.owner = owner;
        this.accessTimes = new HashMap<>();
        System.out.println("  [Proxy] 📄 Document proxy created: " + title);
    }

    private RealDocument getRealDocument() {
        if (realDocument == null) {
            realDocument = new RealDocument(title, content, owner);
        }
        return realDocument;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent(ProxyUser user) {
        System.out.println("\n  [Proxy] 🔍 " + user + " requested to read: " + title);

        if (!user.canRead()) {
            System.out.println("  [Proxy] ⛔ Access denied! User is blocked");
            return "⛔ Access denied!";
        }

        recordAccess(user.getUsername());
        return getRealDocument().getContent(user);
    }

    @Override
    public boolean updateContent(ProxyUser user, String newContent) {
        System.out.println("\n  [Proxy] ✏️ " + user + " requested to update: " + title);

        if (!user.canWrite()) {
            System.out.println("  [Proxy] ⛔ Cannot update! No permission");
            return false;
        }

        if (!user.getUsername().equals(owner) && !user.getRole().equals("ADMIN")) {
            System.out.println("  [Proxy] ⛔ Cannot update this document! Not the owner");
            return false;
        }

        recordAccess(user.getUsername() + " (update)");
        return getRealDocument().updateContent(user, newContent);
    }

    private void recordAccess(String username) {
        List<Long> times = accessTimes.getOrDefault(username, new ArrayList<>());
        times.add(System.currentTimeMillis());
        accessTimes.put(username, times);
    }

    @Override
    public void displayInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ [Proxy] Document: " + title);
        System.out.println("  │ Owner: " + owner);
        System.out.println("  │ Real document created: " + (realDocument != null ? "✅" : "❌"));
        System.out.println("  │ Access log:");
        for (Map.Entry<String, List<Long>> entry : accessTimes.entrySet()) {
            System.out.println("  │    " + entry.getKey() + ": " + entry.getValue().size() + " times");
        }
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Section 5: Document Manager
// =========================================================================

class DocumentManager {
    private List<DocumentProxy> documents;
    private List<ProxyUser> users;

    public DocumentManager() {
        this.documents = new ArrayList<>();
        this.users = new ArrayList<>();
        initializeData();
    }

    private void initializeData() {
        users.add(new ProxyUser("Abera", "ADMIN", "IT"));
        users.add(new ProxyUser("Samuel", "EDITOR", "Marketing"));
        users.add(new ProxyUser("Terefe", "VIEWER", "Sales"));
        users.add(new ProxyUser("Hana", "BLOCKED", "External"));

        documents.add(new DocumentProxy("Annual Report 2024",
                "This is the 2024 annual report. Sales have grown by 20%...", "Abera"));
        documents.add(new DocumentProxy("Sales Data Q1",
                "First quarter sales: 5 million Birr...", "Samuel"));
    }

    public ProxyUser findUser(String username) {
        for (ProxyUser user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public DocumentProxy findDocument(String title) {
        for (DocumentProxy doc : documents) {
            if (doc.getTitle().equals(title)) {
                return doc;
            }
        }
        return null;
    }

    public void listDocuments() {
        System.out.println("\n📋 Available documents:");
        for (DocumentProxy doc : documents) {
            System.out.println("   • " + doc.getTitle());
        }
    }

    public void listUsers() {
        System.out.println("\n👥 Registered users:");
        for (ProxyUser user : users) {
            System.out.println("   • " + user);
        }
    }
}

// =========================================================================
// Section 6: Main Class - With User Input
// =========================================================================

public class ProxyPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║              PROXY PATTERN DEMO                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        DocumentManager manager = new DocumentManager();
        ProxyUser currentUser = null;

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");

            if (currentUser == null) {
                System.out.println("🔐 Please login:");
                manager.listUsers();
                System.out.print("Enter username: ");
                String username = scanner.nextLine();

                currentUser = manager.findUser(username);
                if (currentUser == null) {
                    System.out.println("❌ Username not found!");
                    continue;
                }
                System.out.println("✅ Welcome " + currentUser);
            }

            System.out.println("\nAvailable options for " + currentUser + ":");
            System.out.println("1. View documents");
            System.out.println("2. Read document");
            System.out.println("3. Update document");
            System.out.println("4. View document info");
            System.out.println("5. Logout");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    manager.listDocuments();
                    break;

                case 2:
                    manager.listDocuments();
                    System.out.print("Enter document title: ");
                    String readTitle = scanner.nextLine();

                    DocumentProxy readDoc = manager.findDocument(readTitle);
                    if (readDoc != null) {
                        String content = readDoc.getContent(currentUser);
                        System.out.println("\n📄 Content: " + content);
                    } else {
                        System.out.println("❌ Document not found!");
                    }
                    break;

                case 3:
                    if (!currentUser.canWrite()) {
                        System.out.println("⛔ Cannot update! No permission");
                        break;
                    }

                    manager.listDocuments();
                    System.out.print("Enter document title: ");
                    String updateTitle = scanner.nextLine();

                    DocumentProxy updateDoc = manager.findDocument(updateTitle);
                    if (updateDoc != null) {
                        System.out.print("Enter new content: ");
                        String newContent = scanner.nextLine();

                        boolean success = updateDoc.updateContent(currentUser, newContent);
                        if (success) {
                            System.out.println("✅ Document updated successfully!");
                        }
                    } else {
                        System.out.println("❌ Document not found!");
                    }
                    break;

                case 4:
                    manager.listDocuments();
                    System.out.print("Enter document title: ");
                    String infoTitle = scanner.nextLine();

                    DocumentProxy infoDoc = manager.findDocument(infoTitle);
                    if (infoDoc != null) {
                        infoDoc.displayInfo();
                    } else {
                        System.out.println("❌ Document not found!");
                    }
                    break;

                case 5:
                    currentUser = null;
                    System.out.println("👋 Logged out successfully!");
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
        System.out.println("║              Program terminated! Thank you              ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}