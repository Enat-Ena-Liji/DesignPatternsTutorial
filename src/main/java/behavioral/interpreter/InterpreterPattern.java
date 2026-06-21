// File: InterpreterPattern.java
// This file demonstrates the Interpreter Pattern in detail
// The user can evaluate mathematical expressions

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * =====================================================================
 * INTERPRETER PATTERN - The Interpreter Design Pattern
 * =====================================================================
 * This design pattern defines a representation for a language grammar and
 * uses this representation to interpret sentences written in that language.
 */

// =========================================================================
// Part 1: Context
// =========================================================================

class ExpressionContext {
    private Map<String, Integer> variables;
    private List<String> evaluationHistory;

    public ExpressionContext() {
        this.variables = new HashMap<>();
        this.evaluationHistory = new ArrayList<>();
    }

    public void setVariable(String name, int value) {
        variables.put(name, value);
        System.out.println("  [Context] 📝 Variable set: " + name + " = " + value);
    }

    public int getVariable(String name) {
        return variables.getOrDefault(name, 0);
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    public void addToHistory(String entry) {
        evaluationHistory.add(entry);
    }

    public void showHistory() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📋 Evaluation History:");
        for (String entry : evaluationHistory) {
            System.out.println("  │ " + entry);
        }
        System.out.println("  └─────────────────────────────────");
    }

    public void showVariables() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📊 Variables:");
        for (Map.Entry<String, Integer> entry : variables.entrySet()) {
            System.out.println("  │ " + entry.getKey() + " = " + entry.getValue());
        }
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Part 2: Expression Interface
// =========================================================================

interface Expression {
    int interpret(ExpressionContext context);
    String getRepresentation();
}

// =========================================================================
// Part 3: Terminal Expressions
// =========================================================================

class NumberExpression implements Expression {
    private int number;

    public NumberExpression(int number) {
        this.number = number;
    }

    @Override
    public int interpret(ExpressionContext context) {
        context.addToHistory("Number " + number);
        return number;
    }

    @Override
    public String getRepresentation() {
        return String.valueOf(number);
    }
}

class VariableExpression implements Expression {
    private String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public int interpret(ExpressionContext context) {
        int value = context.getVariable(name);
        context.addToHistory("Variable " + name + " = " + value);
        return value;
    }

    @Override
    public String getRepresentation() {
        return name;
    }
}

// =========================================================================
// Part 4: Operator Expressions
// =========================================================================

class AddExpression implements Expression {
    private Expression left;
    private Expression right;

    public AddExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret(ExpressionContext context) {
        int leftValue = left.interpret(context);
        int rightValue = right.interpret(context);
        int result = leftValue + rightValue;
        context.addToHistory(left.getRepresentation() + " + " + right.getRepresentation() + " = " + result);
        return result;
    }

    @Override
    public String getRepresentation() {
        return "(" + left.getRepresentation() + " + " + right.getRepresentation() + ")";
    }
}

class SubtractExpression implements Expression {
    private Expression left;
    private Expression right;

    public SubtractExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret(ExpressionContext context) {
        int leftValue = left.interpret(context);
        int rightValue = right.interpret(context);
        int result = leftValue - rightValue;
        context.addToHistory(left.getRepresentation() + " - " + right.getRepresentation() + " = " + result);
        return result;
    }

    @Override
    public String getRepresentation() {
        return "(" + left.getRepresentation() + " - " + right.getRepresentation() + ")";
    }
}

class MultiplyExpression implements Expression {
    private Expression left;
    private Expression right;

    public MultiplyExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret(ExpressionContext context) {
        int leftValue = left.interpret(context);
        int rightValue = right.interpret(context);
        int result = leftValue * rightValue;
        context.addToHistory(left.getRepresentation() + " * " + right.getRepresentation() + " = " + result);
        return result;
    }

    @Override
    public String getRepresentation() {
        return "(" + left.getRepresentation() + " * " + right.getRepresentation() + ")";
    }
}

class DivideExpression implements Expression {
    private Expression left;
    private Expression right;

    public DivideExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret(ExpressionContext context) {
        int leftValue = left.interpret(context);
        int rightValue = right.interpret(context);
        if (rightValue == 0) {
            throw new ArithmeticException("Cannot divide by zero!");
        }
        int result = leftValue / rightValue;
        context.addToHistory(left.getRepresentation() + " / " + right.getRepresentation() + " = " + result);
        return result;
    }

    @Override
    public String getRepresentation() {
        return "(" + left.getRepresentation() + " / " + right.getRepresentation() + ")";
    }
}

// =========================================================================
// Part 5: Expression Parser
// =========================================================================

class ExpressionParser {
    private ExpressionContext context;

    public ExpressionParser(ExpressionContext context) {
        this.context = context;
    }

    public Expression parse(String expression) {
        // Simple parser - expects space-separated tokens
        // Example: "3 4 +" means 3 + 4
        String[] tokens = expression.split(" ");
        Stack<Expression> stack = new Stack<>();

        for (String token : tokens) {
            if (token.matches("\\d+")) {
                stack.push(new NumberExpression(Integer.parseInt(token)));
            } else if (token.matches("[a-zA-Z]")) {
                if (!context.hasVariable(token)) {
                    context.setVariable(token, 0);
                }
                stack.push(new VariableExpression(token));
            } else {
                Expression right = stack.pop();
                Expression left = stack.pop();

                switch (token) {
                    case "+":
                        stack.push(new AddExpression(left, right));
                        break;
                    case "-":
                        stack.push(new SubtractExpression(left, right));
                        break;
                    case "*":
                        stack.push(new MultiplyExpression(left, right));
                        break;
                    case "/":
                        stack.push(new DivideExpression(left, right));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown operator: " + token);
                }
            }
        }

        return stack.pop();
    }
}

// =========================================================================
// Part 6: Main Class - With User Input
// =========================================================================

public class InterpreterPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║       INTERPRETER PATTERN - Interpreter Design Pattern Demo     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        ExpressionContext context = new ExpressionContext();
        ExpressionParser parser = new ExpressionParser(context);

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Evaluate Expression");
            System.out.println("2. Set Variable");
            System.out.println("3. View Variables");
            System.out.println("4. View History");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\nAvailable operators: + - * /");
                    System.out.println("Example: 3 4 + 5 *  (this means (3 + 4) * 5)");
                    System.out.println("You can use variables: x 5 +");
                    System.out.print("Enter expression: ");

                    String expr = scanner.nextLine();
                    try {
                        Expression expression = parser.parse(expr);
                        int result = expression.interpret(context);
                        System.out.println("\n✅ Result: " + result);
                    } catch (Exception e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Variable name (one letter): ");
                    String varName = scanner.nextLine();
                    System.out.print("Enter value: ");
                    int value = scanner.nextInt();
                    scanner.nextLine();

                    context.setVariable(varName, value);
                    break;

                case 3:
                    context.showVariables();
                    break;

                case 4:
                    context.showHistory();
                    break;

                case 5:
                    continueRunning = false;
                    System.out.println("\nThank you for using the interpreter! Goodbye.");
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