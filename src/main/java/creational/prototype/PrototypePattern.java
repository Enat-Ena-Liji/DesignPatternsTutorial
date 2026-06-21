// File: PrototypePattern.java
// This file demonstrates the Prototype Pattern in detail
// The user can clone existing objects

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * =====================================================================
 * PROTOTYPE PATTERN
 * =====================================================================
 * This design pattern clones existing objects instead of creating new ones.
 */

// =========================================================================
// Section 1: Prototype Interface
// =========================================================================

/**
 * Prototype Interface - All cloneable classes implement this
 */
interface Prototype {
    /**
     * Method to clone the object
     * @return Cloned object
     */
    Prototype clone();

    /**
     * Display object information
     */
    void displayInfo();
}

// =========================================================================
// Section 2: Concrete Prototype - Employee Information
// =========================================================================

/**
 * PrototypeEmployee Class - This is a concrete prototype
 * Name changed to avoid conflict with CompositePattern
 */
class PrototypeEmployee implements Prototype {
    private int id;
    private String name;
    private String position;
    private double salary;
    private String department;
    private String address;
    private String phone;
    private String email;

    /**
     * Constructor - Takes complete information
     */
    public PrototypeEmployee(int id, String name, String position, double salary,
                             String department, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.department = department;
        this.address = address;
        this.phone = phone;
        this.email = email;

        System.out.println("  [Employee] 👤 New employee created: " + name + " (ID: " + id + ")");
    }

    /**
     * Constructor for cloning - Private
     * @param source Source to clone from
     */
    private PrototypeEmployee(PrototypeEmployee source) {
        this.id = source.id + 1500; // New ID for the cloned one
        this.name = source.name;
        this.position = source.position;
        this.salary = source.salary;
        this.department = source.department;
        this.address = source.address;
        this.phone = source.phone;
        this.email = source.email;
    }

    /**
     * Cloning method - From Prototype interface
     * @return New cloned employee
     */
    @Override
    public Prototype clone() {
        System.out.println("  [Employee] 🔄 " + name + " is being cloned...");
        return new PrototypeEmployee(this);
    }

    // ========== Getter Methods ==========
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public double getSalary() { return salary; }
    public String getDepartment() { return department; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    // ========== Setter Methods - To modify the cloned object ==========
    public void setName(String name) {
        System.out.println("  [Employee] ✏️ Name changed: " + this.name + " → " + name);
        this.name = name;
    }

    public void setPosition(String position) {
        System.out.println("  [Employee] ✏️ Position changed: " + this.position + " → " + position);
        this.position = position;
    }

    public void setSalary(double salary) {
        System.out.println("  [Employee] ✏️ Salary changed: " + this.salary + " → " + salary);
        this.salary = salary;
    }

    public void setDepartment(String department) {
        System.out.println("  [Employee] ✏️ Department changed: " + this.department + " → " + department);
        this.department = department;
    }

    public void setAddress(String address) {
        System.out.println("  [Employee] ✏️ Address changed: " + this.address + " → " + address);
        this.address = address;
    }

    public void setPhone(String phone) {
        System.out.println("  [Employee] ✏️ Phone changed: " + this.phone + " → " + phone);
        this.phone = phone;
    }

    public void setEmail(String email) {
        System.out.println("  [Employee] ✏️ Email changed: " + this.email + " → " + email);
        this.email = email;
    }

    /**
     * Display employee information
     */
    @Override
    public void displayInfo() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 👤 Employee Information");
        System.out.println("  │ ID: " + id);
        System.out.println("  │ Name: " + name);
        System.out.println("  │ Position: " + position);
        System.out.println("  │ Salary: " + String.format("%,.2f", salary) + " Birr");
        System.out.println("  │ Department: " + department);
        System.out.println("  │ Address: " + address);
        System.out.println("  │ Phone: " + phone);
        System.out.println("  │ Email: " + email);
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Section 3: Employee Cache
// =========================================================================

/**
 * PrototypeCache Class - Stores created employees in cache
 */
class PrototypeCache {
    private Map<Integer, PrototypeEmployee> cache;
    private int nextId;

    public PrototypeCache() {
        this.cache = new HashMap<>();
        this.nextId = 1001;

        // Create some default employees
        initializeCache();
    }

    /**
     * Initialize cache - Create default employees
     */
    private void initializeCache() {
        PrototypeEmployee emp1 = new PrototypeEmployee(1001, "Abera Bekele", "Software Developer", 25000,
                "IT", "Bole, Addis Ababa", "0912345678", "abera@email.com");
        cache.put(1001, emp1);

        PrototypeEmployee emp2 = new PrototypeEmployee(1002, "Asnakech Tilahun", "Project Manager", 35000,
                "Management", "Megenagna, Addis Ababa", "0923456789", "asnakech@email.com");
        cache.put(1002, emp2);

        PrototypeEmployee emp3 = new PrototypeEmployee(1003, "Samuel Haile", "Database Administrator", 28000,
                "IT", "Lideta, Addis Ababa", "0934567890", "samuel@email.com");
        cache.put(1003, emp3);

        nextId = 1004;
        System.out.println("  [Cache] 📦 " + cache.size() + " employees added to cache");
    }

    /**
     * Get employee by ID
     * @param id Employee ID
     * @return Employee or null
     */
    public PrototypeEmployee getEmployee(int id) {
        return cache.get(id);
    }

    /**
     * Clone an employee
     * @param id ID of employee to clone
     * @return Cloned employee or null
     */
    public PrototypeEmployee cloneEmployee(int id) {
        PrototypeEmployee original = cache.get(id);
        if (original != null) {
            PrototypeEmployee cloned = (PrototypeEmployee) original.clone();
            return cloned;
        }
        return null;
    }

    /**
     * Add new employee to cache
     * @param emp Employee to add
     */
    public void addEmployee(PrototypeEmployee emp) {
        cache.put(emp.getId(), emp);
        System.out.println("  [Cache] ➕ Employee added to cache: " + emp.getName());
    }

    /**
     * Get next available ID
     * @return New ID
     */
    public int getNextId() {
        return nextId++;
    }

    /**
     * Display all employees in cache
     */
    public void displayAllEmployees() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📋 Employees in cache (" + cache.size() + "):");
        for (PrototypeEmployee emp : cache.values()) {
            System.out.println("  │   " + emp.getId() + ": " + emp.getName() + " - " + emp.getPosition());
        }
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Section 4: Main Class - With User Input
// =========================================================================

public class PrototypePattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║              PROTOTYPE PATTERN DEMO                     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("This demo shows how the Prototype Pattern allows");
        System.out.println("cloning existing objects.\n");

        PrototypeCache cache = new PrototypeCache();

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. View all employees");
            System.out.println("2. Clone an employee");
            System.out.println("3. Create new employee");
            System.out.println("4. Display employee details");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    cache.displayAllEmployees();
                    break;

                case 2:
                    cache.displayAllEmployees();
                    System.out.print("Enter employee ID to clone: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    PrototypeEmployee cloned = cache.cloneEmployee(id);
                    if (cloned != null) {
                        System.out.println("\n✅ Employee cloned successfully!");
                        System.out.println("   New ID: " + cloned.getId());

                        System.out.println("\nCloned employee information (before modification):");
                        cloned.displayInfo();

                        System.out.println("\nNow you can modify the cloned employee information:");

                        System.out.print("Enter new name (press enter to skip): ");
                        String newName = scanner.nextLine();
                        if (!newName.isEmpty()) {
                            cloned.setName(newName);
                        }

                        System.out.print("Enter new position (press enter to skip): ");
                        String newPosition = scanner.nextLine();
                        if (!newPosition.isEmpty()) {
                            cloned.setPosition(newPosition);
                        }

                        System.out.print("Enter new department (press enter to skip): ");
                        String newDept = scanner.nextLine();
                        if (!newDept.isEmpty()) {
                            cloned.setDepartment(newDept);
                        }

                        System.out.print("Enter new salary (press 0 to skip): ");
                        double newSalary = scanner.nextDouble();
                        scanner.nextLine();
                        if (newSalary > 0) {
                            cloned.setSalary(newSalary);
                        }

                        System.out.println("\nCloned employee information after modification:");
                        cloned.displayInfo();

                        cache.addEmployee(cloned);
                    } else {
                        System.out.println("❌ Employee not found!");
                    }
                    break;

                case 3:
                    int newId = cache.getNextId();

                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter position: ");
                    String position = scanner.nextLine();

                    System.out.print("Enter salary: ");
                    double salary = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.print("Enter department: ");
                    String department = scanner.nextLine();

                    System.out.print("Enter address: ");
                    String address = scanner.nextLine();

                    System.out.print("Enter phone: ");
                    String phone = scanner.nextLine();

                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();

                    PrototypeEmployee newEmp = new PrototypeEmployee(newId, name, position, salary,
                            department, address, phone, email);
                    cache.addEmployee(newEmp);
                    System.out.println("✅ New employee created with ID: " + newId);
                    break;

                case 4:
                    cache.displayAllEmployees();
                    System.out.print("Enter employee ID to display: ");
                    int showId = scanner.nextInt();
                    scanner.nextLine();

                    PrototypeEmployee emp = cache.getEmployee(showId);
                    if (emp != null) {
                        emp.displayInfo();
                    } else {
                        System.out.println("❌ Employee not found!");
                    }
                    break;

                case 5:
                    continueRunning = false;
                    System.out.println("\nThank you for using the program! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-5)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              Program terminated! Thank you              ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}