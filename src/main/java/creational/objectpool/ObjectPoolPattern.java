// File: ObjectPoolPattern.java
// This file demonstrates the Object Pool Pattern in detail
// The user can get and return database connections from the pool

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * =====================================================================
 * OBJECT POOL PATTERN
 * =====================================================================
 * This pattern allows reusing objects that are expensive to create.
 */

// =========================================================================
// Part 1: Database Connection Class (Poolable Object)
// =========================================================================

class DatabaseConnection {
    private String connectionId;
    private boolean inUse;
    private long creationTime;
    private long lastUsedTime;
    private int queryCount;

    public DatabaseConnection() {
        this.connectionId = UUID.randomUUID().toString().substring(0, 8);
        this.inUse = false;
        this.creationTime = System.currentTimeMillis();
        this.lastUsedTime = this.creationTime;
        this.queryCount = 0;

        System.out.println("  [Connection] 🔌 New database connection created: " + connectionId);
    }

    public void executeQuery(String query) {
        if (!inUse) {
            System.out.println("  [Connection] ⚠️ Connection is not in use!");
            return;
        }

        queryCount++;
        lastUsedTime = System.currentTimeMillis();
        System.out.println("  [Connection] 📊 Executing query [" + connectionId + "]: " + query);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        inUse = false;
        System.out.println("  [Connection] 🔌 Connection returned to pool [" + connectionId + "]");
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
        if (inUse) {
            this.lastUsedTime = System.currentTimeMillis();
        }
    }

    public boolean isInUse() { return inUse; }
    public String getConnectionId() { return connectionId; }
    public long getAge() { return System.currentTimeMillis() - creationTime; }
    public int getQueryCount() { return queryCount; }

    @Override
    public String toString() {
        return "DBConn{" + connectionId +
                ", inUse=" + inUse +
                ", queries=" + queryCount + "}";
    }
}

// =========================================================================
// Part 2: Connection Pool Class (Object Pool)
// =========================================================================

class ConnectionPool {
    private static ConnectionPool instance;
    private List<DatabaseConnection> available;
    private List<DatabaseConnection> inUse;
    private int maxSize;
    private int createdCount;

    private ConnectionPool(int initialSize, int maxSize) {
        this.available = new ArrayList<>();
        this.inUse = new ArrayList<>();
        this.maxSize = maxSize;
        this.createdCount = 0;

        System.out.println("\n  [Pool] 🏊 New connection pool created (Max: " + maxSize + ")");

        for (int i = 0; i < initialSize; i++) {
            available.add(new DatabaseConnection());
            createdCount++;
        }

        System.out.println("  [Pool] 📊 " + initialSize + " connections created");
    }

    public static synchronized ConnectionPool getInstance(int initial, int max) {
        if (instance == null) {
            instance = new ConnectionPool(initial, max);
        }
        return instance;
    }

    public synchronized DatabaseConnection getConnection() {
        System.out.println("\n  [Pool] 🔍 Looking for a connection...");

        if (!available.isEmpty()) {
            DatabaseConnection conn = available.remove(0);
            conn.setInUse(true);
            inUse.add(conn);
            System.out.println("  [Pool] ✅ Existing connection provided: " + conn.getConnectionId());
            return conn;
        }

        if (createdCount < maxSize) {
            DatabaseConnection conn = new DatabaseConnection();
            conn.setInUse(true);
            inUse.add(conn);
            createdCount++;
            System.out.println("  [Pool] ✅ New connection created and provided: " + conn.getConnectionId());
            return conn;
        }

        System.out.println("  [Pool] ❌ All connections are in use!");
        return null;
    }

    public synchronized void releaseConnection(DatabaseConnection conn) {
        if (conn == null) return;

        if (inUse.remove(conn)) {
            conn.setInUse(false);
            conn.close();
            available.add(conn);
            System.out.println("  [Pool] ↩️ Connection returned: " + conn.getConnectionId());
        }
    }

    public void showPoolStatus() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📊 Connection Pool Status");
        System.out.println("  │ Available: " + available.size());
        System.out.println("  │ In Use: " + inUse.size());
        System.out.println("  │ Total Created: " + createdCount);
        System.out.println("  │ Max Size: " + maxSize);
        System.out.println("  └─────────────────────────────────");

        if (!available.isEmpty()) {
            System.out.println("  Available Connections:");
            for (DatabaseConnection conn : available) {
                System.out.println("    • " + conn);
            }
        }

        if (!inUse.isEmpty()) {
            System.out.println("  Connections In Use:");
            for (DatabaseConnection conn : inUse) {
                System.out.println("    • " + conn);
            }
        }
    }

    public int getAvailableCount() { return available.size(); }
    public int getInUseCount() { return inUse.size(); }
}

// =========================================================================
// Part 3: Main Class - With User Input
// =========================================================================

public class ObjectPoolPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           OBJECT POOL PATTERN DEMO                     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        // Create connection pool (2 initial, max 5)
        ConnectionPool pool = ConnectionPool.getInstance(2, 5);

        // Store acquired connections
        List<DatabaseConnection> connections = new ArrayList<>();

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Get new connection");
            System.out.println("2. Execute query");
            System.out.println("3. Return connection to pool");
            System.out.println("4. View pool status");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    DatabaseConnection conn = pool.getConnection();
                    if (conn != null) {
                        connections.add(conn);
                        System.out.println("  [Main] ✅ Connection acquired: " + conn.getConnectionId());
                    }
                    break;

                case 2:
                    if (connections.isEmpty()) {
                        System.out.println("  [Main] ⚠️ No active connections!");
                        break;
                    }

                    System.out.println("\nActive Connections:");
                    for (int i = 0; i < connections.size(); i++) {
                        System.out.println("  " + (i+1) + ". " + connections.get(i));
                    }

                    System.out.print("Enter connection number: ");
                    int connIndex = scanner.nextInt() - 1;
                    scanner.nextLine();

                    if (connIndex >= 0 && connIndex < connections.size()) {
                        System.out.print("Enter query (e.g., SELECT * FROM users): ");
                        String query = scanner.nextLine();
                        connections.get(connIndex).executeQuery(query);
                    }
                    break;

                case 3:
                    if (connections.isEmpty()) {
                        System.out.println("  [Main] ⚠️ No connections to return!");
                        break;
                    }

                    System.out.println("\nActive Connections:");
                    for (int i = 0; i < connections.size(); i++) {
                        System.out.println("  " + (i+1) + ". " + connections.get(i));
                    }

                    System.out.print("Enter connection number to return: ");
                    int releaseIndex = scanner.nextInt() - 1;
                    scanner.nextLine();

                    if (releaseIndex >= 0 && releaseIndex < connections.size()) {
                        pool.releaseConnection(connections.remove(releaseIndex));
                    }
                    break;

                case 4:
                    pool.showPoolStatus();
                    break;

                case 5:
                    continueRunning = false;

                    // Return all connections to pool
                    for (DatabaseConnection conn2 : connections) {
                        pool.releaseConnection(conn2);
                    }
                    connections.clear();

                    System.out.println("\nThank you for using the program! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-5)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                PROGRAM COMPLETED! THANK YOU            ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}