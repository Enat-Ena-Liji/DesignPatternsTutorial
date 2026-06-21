// File: BridgePattern.java
// This file demonstrates the Bridge Pattern in detail
// The user can select different shapes and colors

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * BRIDGE PATTERN
 * =====================================================================
 * This pattern decouples abstraction from implementation.
 */

// =========================================================================
// Part 1: Implementation Interface
// =========================================================================

/**
 * Color Interface - This is the implementation part
 */
interface Color {
    String applyColor();
    String getColorName();
    String getHexCode();
    String getRgbValue();
}

// =========================================================================
// Part 2: Concrete Implementations
// =========================================================================

class RedColor implements Color {
    private String hexCode = "#FF0000";
    private String rgbValue = "rgb(255, 0, 0)";

    @Override
    public String applyColor() {
        return "Applying red color";
    }

    @Override
    public String getColorName() {
        return "Red";
    }

    @Override
    public String getHexCode() {
        return hexCode;
    }

    @Override
    public String getRgbValue() {
        return rgbValue;
    }
}

class BlueColor implements Color {
    private String hexCode = "#0000FF";
    private String rgbValue = "rgb(0, 0, 255)";

    @Override
    public String applyColor() {
        return "Applying blue color";
    }

    @Override
    public String getColorName() {
        return "Blue";
    }

    @Override
    public String getHexCode() {
        return hexCode;
    }

    @Override
    public String getRgbValue() {
        return rgbValue;
    }
}

class GreenColor implements Color {
    private String hexCode = "#00FF00";
    private String rgbValue = "rgb(0, 255, 0)";

    @Override
    public String applyColor() {
        return "Applying green color";
    }

    @Override
    public String getColorName() {
        return "Green";
    }

    @Override
    public String getHexCode() {
        return hexCode;
    }

    @Override
    public String getRgbValue() {
        return rgbValue;
    }
}

class YellowColor implements Color {
    private String hexCode = "#FFFF00";
    private String rgbValue = "rgb(255, 255, 0)";

    @Override
    public String applyColor() {
        return "Applying yellow color";
    }

    @Override
    public String getColorName() {
        return "Yellow";
    }

    @Override
    public String getHexCode() {
        return hexCode;
    }

    @Override
    public String getRgbValue() {
        return rgbValue;
    }
}

class BlackColor implements Color {
    private String hexCode = "#000000";
    private String rgbValue = "rgb(0, 0, 0)";

    @Override
    public String applyColor() {
        return "Applying black color";
    }

    @Override
    public String getColorName() {
        return "Black";
    }

    @Override
    public String getHexCode() {
        return hexCode;
    }

    @Override
    public String getRgbValue() {
        return rgbValue;
    }
}

// =========================================================================
// Part 3: Abstraction
// =========================================================================

/**
 * Shape Class - This is the abstraction
 */
abstract class Shape {
    protected Color color;
    protected String shapeName;
    protected static int shapeCount = 0;
    protected int shapeId;

    public Shape(Color color, String shapeName) {
        this.color = color;
        this.shapeName = shapeName;
        this.shapeId = ++shapeCount;
        System.out.println("  [Shape] 🎨 New " + shapeName + " created (ID: " + shapeId + ")");
    }

    public abstract void draw();
    public abstract double getArea();
    public abstract double getPerimeter();

    public void setColor(Color color) {
        System.out.println("  [Shape] 🔄 " + shapeName + " color changed from " +
                this.color.getColorName() + " to " + color.getColorName());
        this.color = color;
    }

    public String getInfo() {
        return shapeName + " [ID: " + shapeId + ", Color: " + color.getColorName() + "]";
    }

    public int getShapeId() {
        return shapeId;
    }
}

// =========================================================================
// Part 4: Refined Abstractions
// =========================================================================

class Circle extends Shape {
    private double radius;

    public Circle(Color color, double radius) {
        super(color, "Circle");
        this.radius = radius;
        System.out.println("  [Circle] ⚪ Radius: " + radius);
    }

    @Override
    public void draw() {
        System.out.println("  [Draw] 🎨 " + getInfo());
        System.out.println("  [Draw]    Drawing circle...");
        System.out.println("  [Draw]    Radius: " + radius);
        System.out.println("  [Draw]    Area: " + String.format("%.2f", getArea()));
        System.out.println("  [Draw]    Perimeter: " + String.format("%.2f", getPerimeter()));
        System.out.println("  [Draw]    Color: " + color.applyColor());
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    public double getRadius() {
        return radius;
    }
}

class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(Color color, double width, double height) {
        super(color, "Rectangle");
        this.width = width;
        this.height = height;
        System.out.println("  [Rectangle] 📐 Width: " + width + ", Height: " + height);
    }

    @Override
    public void draw() {
        System.out.println("  [Draw] 🎨 " + getInfo());
        System.out.println("  [Draw]    Drawing rectangle...");
        System.out.println("  [Draw]    Width: " + width + ", Height: " + height);
        System.out.println("  [Draw]    Area: " + String.format("%.2f", getArea()));
        System.out.println("  [Draw]    Perimeter: " + String.format("%.2f", getPerimeter()));
        System.out.println("  [Draw]    Color: " + color.applyColor());
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}

class Triangle extends Shape {
    private double sideA;
    private double sideB;
    private double sideC;

    public Triangle(Color color, double a, double b, double c) {
        super(color, "Triangle");
        this.sideA = a;
        this.sideB = b;
        this.sideC = c;
        System.out.println("  [Triangle] 🔺 Sides: " + a + ", " + b + ", " + c);
    }

    @Override
    public void draw() {
        System.out.println("  [Draw] 🎨 " + getInfo());
        System.out.println("  [Draw]    Drawing triangle...");
        System.out.println("  [Draw]    Sides: " + sideA + ", " + sideB + ", " + sideC);
        System.out.println("  [Draw]    Area: " + String.format("%.2f", getArea()));
        System.out.println("  [Draw]    Perimeter: " + String.format("%.2f", getPerimeter()));
        System.out.println("  [Draw]    Color: " + color.applyColor());
    }

    @Override
    public double getArea() {
        // Heron's formula
        double s = (sideA + sideB + sideC) / 2;
        return Math.sqrt(s * (s - sideA) * (s - sideB) * (s - sideC));
    }

    @Override
    public double getPerimeter() {
        return sideA + sideB + sideC;
    }
}

// =========================================================================
// Part 5: Shape Manager
// =========================================================================

class ShapeManager {
    private List<Shape> shapes;
    private String managerName;

    public ShapeManager(String name) {
        this.managerName = name;
        this.shapes = new ArrayList<>();
        System.out.println("\n==========================================");
        System.out.println("🎨 New ShapeManager created: " + managerName);
        System.out.println("==========================================");
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
        System.out.println("  [Manager] ➕ " + shape.getInfo() + " added");
    }

    public void drawAll() {
        System.out.println("\n--- " + managerName + " drawing all shapes ---");
        if (shapes.isEmpty()) {
            System.out.println("  No shapes available");
        } else {
            for (Shape shape : shapes) {
                shape.draw();
                System.out.println();
            }
        }
    }

    public void changeShapeColor(int shapeId, Color newColor) {
        for (Shape shape : shapes) {
            if (shape.getShapeId() == shapeId) {
                shape.setColor(newColor);
                return;
            }
        }
        System.out.println("  [Manager] ⚠️ Shape ID " + shapeId + " not found");
    }

    public void listShapes() {
        System.out.println("\n--- " + managerName + " Shape List ---");
        if (shapes.isEmpty()) {
            System.out.println("  No shapes available");
        } else {
            for (int i = 0; i < shapes.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + shapes.get(i).getInfo());
                System.out.println("     Area: " + String.format("%.2f", shapes.get(i).getArea()));
                System.out.println("     Perimeter: " + String.format("%.2f", shapes.get(i).getPerimeter()));
            }
        }
    }

    public Shape findShapeById(int id) {
        for (Shape shape : shapes) {
            if (shape.getShapeId() == id) {
                return shape;
            }
        }
        return null;
    }
}

// =========================================================================
// Part 6: Main Class - With User Input
// =========================================================================

public class BridgePattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           BRIDGE PATTERN DEMO                          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("This demo shows how Bridge Pattern decouples");
        System.out.println("Abstraction (Shapes) from Implementation (Colors).\n");

        System.out.print("Enter your manager name: ");
        String managerName = scanner.nextLine();

        ShapeManager manager = new ShapeManager(managerName);

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Create new Circle");
            System.out.println("2. Create new Rectangle");
            System.out.println("3. Create new Triangle");
            System.out.println("4. Draw all shapes");
            System.out.println("5. View shape list");
            System.out.println("6. Change shape color");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: // New Circle
                    System.out.println("\nAvailable colors: RED, BLUE, GREEN, YELLOW, BLACK");
                    System.out.print("Choose color: ");
                    String circleColor = scanner.nextLine();

                    System.out.print("Enter radius: ");
                    double radius = scanner.nextDouble();
                    scanner.nextLine();

                    Color color1 = getColorFromName(circleColor);
                    if (color1 != null) {
                        Circle circle = new Circle(color1, radius);
                        manager.addShape(circle);
                    } else {
                        System.out.println("❌ Unknown color!");
                    }
                    break;

                case 2: // New Rectangle
                    System.out.println("\nAvailable colors: RED, BLUE, GREEN, YELLOW, BLACK");
                    System.out.print("Choose color: ");
                    String rectColor = scanner.nextLine();

                    System.out.print("Enter width: ");
                    double width = scanner.nextDouble();

                    System.out.print("Enter height: ");
                    double height = scanner.nextDouble();
                    scanner.nextLine();

                    Color color2 = getColorFromName(rectColor);
                    if (color2 != null) {
                        Rectangle rect = new Rectangle(color2, width, height);
                        manager.addShape(rect);
                    } else {
                        System.out.println("❌ Unknown color!");
                    }
                    break;

                case 3: // New Triangle
                    System.out.println("\nAvailable colors: RED, BLUE, GREEN, YELLOW, BLACK");
                    System.out.print("Choose color: ");
                    String triColor = scanner.nextLine();

                    System.out.print("Enter side A: ");
                    double a = scanner.nextDouble();

                    System.out.print("Enter side B: ");
                    double b = scanner.nextDouble();

                    System.out.print("Enter side C: ");
                    double c = scanner.nextDouble();
                    scanner.nextLine();

                    // Check if valid triangle
                    if (a + b > c && a + c > b && b + c > a) {
                        Color color3 = getColorFromName(triColor);
                        if (color3 != null) {
                            Triangle triangle = new Triangle(color3, a, b, c);
                            manager.addShape(triangle);
                        } else {
                            System.out.println("❌ Unknown color!");
                        }
                    } else {
                        System.out.println("❌ The given sides do not form a valid triangle!");
                    }
                    break;

                case 4:
                    manager.drawAll();
                    break;

                case 5:
                    manager.listShapes();
                    break;

                case 6:
                    manager.listShapes();
                    System.out.print("\nEnter shape ID to change color: ");
                    int shapeId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Available colors: RED, BLUE, GREEN, YELLOW, BLACK");
                    System.out.print("Choose new color: ");
                    String newColorName = scanner.nextLine();

                    Color newColor = getColorFromName(newColorName);
                    if (newColor != null) {
                        manager.changeShapeColor(shapeId, newColor);
                    } else {
                        System.out.println("❌ Unknown color!");
                    }
                    break;

                case 7:
                    continueRunning = false;
                    System.out.println("\nThank you for using the program! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-7)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                PROGRAM COMPLETED! THANK YOU            ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }

    private static Color getColorFromName(String colorName) {
        switch (colorName.toUpperCase()) {
            case "RED": return new RedColor();
            case "BLUE": return new BlueColor();
            case "GREEN": return new GreenColor();
            case "YELLOW": return new YellowColor();
            case "BLACK": return new BlackColor();
            default: return null;
        }
    }
}