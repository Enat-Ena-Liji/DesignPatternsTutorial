// File: MementoPattern.java
// This file demonstrates the Memento Pattern in detail
// The user can undo changes in a text editor

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * =====================================================================
 * MEMENTO PATTERN
 * =====================================================================
 * This pattern allows restoring an object to its previous state
 * without exposing its internal structure.
 */

// =========================================================================
// Part 1: Originator - Text Editor
// =========================================================================

class TextEditor {
    private StringBuilder content;
    private String filename;
    private int cursorPosition;

    public TextEditor(String filename) {
        this.content = new StringBuilder();
        this.filename = filename;
        this.cursorPosition = 0;
        System.out.println("\n==========================================");
        System.out.println("📝 New file created: " + filename);
        System.out.println("==========================================");
    }

    public void write(String text) {
        content.append(text);
        cursorPosition = content.length();
        System.out.println("  [Editor] ✍️ Written: '" + text + "'");
    }

    public void deleteLast(int chars) {
        if (chars > content.length()) {
            chars = content.length();
        }
        if (chars > 0) {
            String deleted = content.substring(content.length() - chars);
            content.delete(content.length() - chars, content.length());
            cursorPosition = content.length();
            System.out.println("  [Editor] 🗑️ Deleted: '" + deleted + "'");
        }
    }

    public void setContent(String newContent) {
        this.content = new StringBuilder(newContent);
        this.cursorPosition = content.length();
    }

    public EditorMemento save() {
        System.out.println("  [Editor] 💾 State saved");
        return new EditorMemento(content.toString(), cursorPosition);
    }

    public void restore(EditorMemento memento) {
        this.content = new StringBuilder(memento.getContent());
        this.cursorPosition = memento.getCursorPosition();
        System.out.println("  [Editor] ↩️ Restored to previous state");
    }

    public void display() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📄 File: " + filename);
        System.out.println("  │ 📝 Content: " + content.toString());
        System.out.println("  │ 📍 Cursor Position: " + cursorPosition);
        System.out.println("  └─────────────────────────────────");
    }

    public String getContent() {
        return content.toString();
    }
}

// =========================================================================
// Part 2: Memento
// =========================================================================

class EditorMemento {
    private final String content;
    private final int cursorPosition;
    private final long timestamp;

    public EditorMemento(String content, int cursorPosition) {
        this.content = content;
        this.cursorPosition = cursorPosition;
        this.timestamp = System.currentTimeMillis();
    }

    public String getContent() { return content; }
    public int getCursorPosition() { return cursorPosition; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Memento @ " + new java.util.Date(timestamp) + " - Length: " + content.length();
    }
}

// =========================================================================
// Part 3: Caretaker
// =========================================================================

class History {
    private Stack<EditorMemento> undoStack;
    private Stack<EditorMemento> redoStack;
    private List<EditorMemento> allSnapshots;

    public History() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.allSnapshots = new ArrayList<>();
    }

    public void push(EditorMemento memento) {
        undoStack.push(memento);
        allSnapshots.add(memento);
        redoStack.clear();
        System.out.println("  [History] 📚 Added to history");
    }

    public EditorMemento undo() {
        if (undoStack.size() > 1) {
            EditorMemento current = undoStack.pop();
            redoStack.push(current);
            EditorMemento previous = undoStack.peek();
            System.out.println("  [History] ↩️ Undone to previous state");
            return previous;
        } else if (undoStack.size() == 1) {
            System.out.println("  [History] ⚠️ Already at initial state");
            return undoStack.peek();
        }
        System.out.println("  [History] ⚠️ No history available");
        return null;
    }

    public EditorMemento redo() {
        if (!redoStack.isEmpty()) {
            EditorMemento next = redoStack.pop();
            undoStack.push(next);
            System.out.println("  [History] 🔄 Redone to next state");
            return next;
        }
        System.out.println("  [History] ⚠️ Nothing to redo");
        return null;
    }

    public void showHistory() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📋 History List (" + allSnapshots.size() + "):");
        for (int i = 0; i < allSnapshots.size(); i++) {
            System.out.println("  │ " + i + ": " + allSnapshots.get(i));
        }
        System.out.println("  │");
        System.out.println("  │ ↩️ Undo Stack: " + undoStack.size());
        System.out.println("  │ 🔄 Redo Stack: " + redoStack.size());
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Part 4: Main Class - With User Input
// =========================================================================

public class MementoPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           MEMENTO PATTERN DEMO                         ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();

        TextEditor editor = new TextEditor(filename);
        History history = new History();

        // Save initial state
        history.push(editor.save());

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Write text");
            System.out.println("2. Delete last characters");
            System.out.println("3. Display content");
            System.out.println("4. Undo");
            System.out.println("5. Redo");
            System.out.println("6. Show history");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter text: ");
                    String text = scanner.nextLine();
                    editor.write(text);
                    history.push(editor.save());
                    break;

                case 2:
                    System.out.print("How many characters to delete? ");
                    int chars = scanner.nextInt();
                    scanner.nextLine();
                    editor.deleteLast(chars);
                    history.push(editor.save());
                    break;

                case 3:
                    editor.display();
                    break;

                case 4:
                    EditorMemento prev = history.undo();
                    if (prev != null) {
                        editor.restore(prev);
                    }
                    break;

                case 5:
                    EditorMemento next = history.redo();
                    if (next != null) {
                        editor.restore(next);
                    }
                    break;

                case 6:
                    history.showHistory();
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
}