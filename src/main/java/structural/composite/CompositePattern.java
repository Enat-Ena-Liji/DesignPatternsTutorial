// File: CompositePattern.java
// This file demonstrates the Composite Pattern in detail
// The user can create and display an organizational structure

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * COMPOSITE PATTERN - The Composite Design Pattern
 * =====================================================================
 * This design pattern composes objects into tree structures to represent
 * part-whole hierarchies.
 */

// =========================================================================
// Part 1: Component Interface
// =========================================================================

/**
 * OrganizationComponent Interface - This is the component
 */
interface OrganizationComponent {
    String getName();
    String getDescription();
    double getBudget();
    void displayInfo();
    void displayHierarchy(String indentation);
}

// =========================================================================
// Part 2: Leaf - Individual Employee
// =========================================================================

/**
 * Employee Class - This is the leaf
 */
class Employee implements OrganizationComponent {
    private int id;
    private String name;
    private String position;
    private double salary;
    private String department;

    public Employee(int id, String name, String position, double salary, String department) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.department = department;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return position + " in " + department + " department";
    }

    @Override
    public double getBudget() {
        return salary;
    }

    @Override
    public void displayInfo() {
        System.out.println("  👤 Employee: " + name);
        System.out.println("     ID: " + id);
        System.out.println("     Position: " + position);
        System.out.println("     Department: " + department);
        System.out.println("     Salary: " + String.format("%,.2f", salary) + " Birr");
    }

    @Override
    public void displayHierarchy(String indentation) {
        System.out.println(indentation + "├─ 👤 " + name + " (" + position + ")");
    }

    public int getId() {
        return id;
    }
}

// =========================================================================
// Part 3: Composite - Department
// =========================================================================

/**
 * Department Class - This is the composite
 */
class Department implements OrganizationComponent {
    private int id;
    private String name;
    private String description;
    private double budget;
    private List<OrganizationComponent> components;

    public Department(int id, String name, String description, double budget) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.budget = budget;
        this.components = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getBudget() {
        double totalBudget = budget;
        for (OrganizationComponent comp : components) {
            totalBudget += comp.getBudget();
        }
        return totalBudget;
    }

    @Override
    public void displayInfo() {
        System.out.println("  🏢 Department: " + name);
        System.out.println("     ID: " + id);
        System.out.println("     Description: " + description);
        System.out.println("     Budget: " + String.format("%,.2f", budget) + " Birr");
        System.out.println("     Sub-departments/Employees: " + components.size());
        System.out.println("     Total Budget: " + String.format("%,.2f", getBudget()) + " Birr");
    }

    @Override
    public void displayHierarchy(String indentation) {
        System.out.println(indentation + "└─ 🏢 " + name + " (Budget: " + String.format("%,.2f", budget) + " Birr)");

        if (!components.isEmpty()) {
            String newIndentation = indentation + "    ";
            for (int i = 0; i < components.size(); i++) {
                OrganizationComponent comp = components.get(i);
                if (i == components.size() - 1) {
                    comp.displayHierarchy(newIndentation + "    ");
                } else {
                    comp.displayHierarchy(newIndentation + "    ");
                }
            }
        }
    }

    public void addComponent(OrganizationComponent component) {
        components.add(component);
        System.out.println("  [Department] ➕ " + component.getName() + " added to " + name + " department");
    }

    public void removeComponent(OrganizationComponent component) {
        components.remove(component);
        System.out.println("  [Department] ➖ " + component.getName() + " removed from " + name + " department");
    }

    public List<OrganizationComponent> getComponents() {
        return components;
    }

    public int getId() {
        return id;
    }
}

// =========================================================================
// Part 4: Organization Manager
// =========================================================================

/**
 * Organization Class - Represents the entire organization
 */
class Organization {
    private String name;
    private Department rootDepartment;
    private List<Department> allDepartments;
    private List<Employee> allEmployees;
    private int nextDeptId;
    private int nextEmpId;

    public Organization(String name) {
        this.name = name;
        this.allDepartments = new ArrayList<>();
        this.allEmployees = new ArrayList<>();
        this.nextDeptId = 1;
        this.nextEmpId = 1001;

        // Create root department
        this.rootDepartment = new Department(nextDeptId++, "Head Office", "Main department of the organization", 1000000);
        allDepartments.add(rootDepartment);

        System.out.println("\n==========================================");
        System.out.println("🏢 New organization created: " + name);
        System.out.println("==========================================");
    }

    public Department createDepartment(String name, String description, double budget) {
        Department dept = new Department(nextDeptId++, name, description, budget);
        allDepartments.add(dept);
        return dept;
    }

    public Employee createEmployee(String name, String position, double salary, String department) {
        Employee emp = new Employee(nextEmpId++, name, position, salary, department);
        allEmployees.add(emp);
        return emp;
    }

    public void addToRoot(OrganizationComponent component) {
        rootDepartment.addComponent(component);
    }

    public void addToDepartment(OrganizationComponent component, int departmentId) {
        for (Department dept : allDepartments) {
            if (dept.getId() == departmentId) {
                dept.addComponent(component);
                return;
            }
        }
        System.out.println("  [Organization] ⚠️ Department ID " + departmentId + " not found");
    }

    public Department findDepartmentById(int id) {
        for (Department dept : allDepartments) {
            if (dept.getId() == id) {
                return dept;
            }
        }
        return null;
    }

    public void displayOrganizationStructure() {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("📋 " + name + " Organization Structure");
        System.out.println("══════════════════════════════════════════");
        System.out.println("   Total Departments: " + allDepartments.size());
        System.out.println("   Total Employees: " + allEmployees.size());
        System.out.println("\n📊 Organization Tree:");
        rootDepartment.displayHierarchy("");
    }

    public void listAllDepartments() {
        System.out.println("\n📋 All Departments:");
        for (Department dept : allDepartments) {
            System.out.println("   " + dept.getId() + ". " + dept.getName() + " - " +
                    String.format("%,.2f", dept.getBudget()) + " Birr");
        }
    }

    public void showBudgetSummary() {
        double totalBudget = rootDepartment.getBudget();
        System.out.println("\n💰 Budget Summary:");
        System.out.println("   Total Organization Budget: " + String.format("%,.2f", totalBudget) + " Birr");
    }
}

// =========================================================================
// Part 5: Main Class - With User Input
// =========================================================================

public class CompositePattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║       COMPOSITE PATTERN - Composite Design Pattern Demo          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.print("Enter your organization name: ");
        String orgName = scanner.nextLine();

        Organization org = new Organization(orgName);

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Create new department");
            System.out.println("2. Create new employee");
            System.out.println("3. Add department to root");
            System.out.println("4. Add department to another department");
            System.out.println("5. Add employee to department");
            System.out.println("6. Display organization structure");
            System.out.println("7. View all departments");
            System.out.println("8. View budget summary");
            System.out.println("9. Exit");
            System.out.print("Enter your choice (1-9): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: // Create new department
                    System.out.print("Enter department name: ");
                    String deptName = scanner.nextLine();

                    System.out.print("Enter department description: ");
                    String deptDesc = scanner.nextLine();

                    System.out.print("Enter department budget: ");
                    double deptBudget = scanner.nextDouble();
                    scanner.nextLine();

                    Department newDept = org.createDepartment(deptName, deptDesc, deptBudget);
                    System.out.println("✅ New department created with ID: " + newDept.getId());
                    break;

                case 2: // Create new employee
                    System.out.print("Enter employee name: ");
                    String empName = scanner.nextLine();

                    System.out.print("Enter employee position: ");
                    String position = scanner.nextLine();

                    System.out.print("Enter employee salary: ");
                    double salary = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.print("Enter employee department: ");
                    String department = scanner.nextLine();

                    Employee newEmp = org.createEmployee(empName, position, salary, department);
                    System.out.println("✅ New employee created with ID: " + newEmp.getId());
                    break;

                case 3: // Add department to root
                    org.listAllDepartments();
                    System.out.print("\nEnter department ID to add: ");
                    int deptId = scanner.nextInt();
                    scanner.nextLine();

                    Department deptToAdd = org.findDepartmentById(deptId);
                    if (deptToAdd != null) {
                        org.addToRoot(deptToAdd);
                    } else {
                        System.out.println("❌ Department not found!");
                    }
                    break;

                case 4: // Add department to another department
                    org.listAllDepartments();
                    System.out.print("\nEnter child department ID: ");
                    int childDeptId = scanner.nextInt();

                    System.out.print("Enter parent department ID: ");
                    int parentDeptId = scanner.nextInt();
                    scanner.nextLine();

                    Department childDept = org.findDepartmentById(childDeptId);
                    Department parentDept = org.findDepartmentById(parentDeptId);

                    if (childDept != null && parentDept != null) {
                        parentDept.addComponent(childDept);
                    } else {
                        System.out.println("❌ Department not found!");
                    }
                    break;

                case 5: // Add employee to department
                    System.out.print("Enter employee ID: ");
                    int empId = scanner.nextInt();

                    System.out.print("Enter department ID to add to: ");
                    int targetDeptId = scanner.nextInt();
                    scanner.nextLine();

                    Department targetDept = org.findDepartmentById(targetDeptId);
                    // For demonstration, create a temporary employee
                    Employee tempEmp = new Employee(empId, "Employee", "Employee", 0, "");
                    if (targetDept != null) {
                        targetDept.addComponent(tempEmp);
                    } else {
                        System.out.println("❌ Department not found!");
                    }
                    break;

                case 6:
                    org.displayOrganizationStructure();
                    break;

                case 7:
                    org.listAllDepartments();
                    break;

                case 8:
                    org.showBudgetSummary();
                    break;

                case 9:
                    continueRunning = false;
                    System.out.println("\nThank you for using the system! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-9)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                Program terminated! Thank you                ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}