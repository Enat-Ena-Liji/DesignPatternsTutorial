// File: MediatorPattern.java
// This file demonstrates the Mediator Pattern in detail
// The user can send messages in a chat room

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * =====================================================================
 * MEDIATOR PATTERN
 * =====================================================================
 * This pattern reduces communication complexity by placing
 * interactions between multiple objects in a central object.
 */

// =========================================================================
// Part 1: Mediator Interface
// =========================================================================

interface ChatMediator {
    void sendMessage(String message, ChatUser sender);
    void sendPrivateMessage(String message, ChatUser sender, String recipient);
    void addUser(ChatUser user);
    void removeUser(ChatUser user);
    List<String> getMessageHistory();
}

// =========================================================================
// Part 2: User Class
// =========================================================================

class ChatUser {
    private String username;
    private String role;
    private boolean isOnline;
    private ChatMediator mediator;
    private List<String> receivedMessages;

    public ChatUser(String username, String role, ChatMediator mediator) {
        this.username = username;
        this.role = role;
        this.mediator = mediator;
        this.isOnline = true;
        this.receivedMessages = new ArrayList<>();
        if (mediator != null) {
            mediator.addUser(this);
        }
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
    public boolean isOnline() { return isOnline; }

    public void setOnline(boolean online) {
        this.isOnline = online;
        System.out.println("  [User] " + username + " " + (online ? "is online" : "is offline"));
    }

    public void sendMessage(String message) {
        if (!isOnline) {
            System.out.println("  [User] ⚠️ " + username + " is offline!");
            return;
        }
        System.out.println("\n  [User] 📤 " + username + " sending to all: " + message);
        mediator.sendMessage(message, this);
    }

    public void sendPrivateMessage(String message, String recipient) {
        if (!isOnline) {
            System.out.println("  [User] ⚠️ " + username + " is offline!");
            return;
        }
        System.out.println("\n  [User] 🤫 " + username + " sending to " + recipient + ": " + message);
        mediator.sendPrivateMessage(message, this, recipient);
    }

    public void receiveMessage(String message, ChatUser sender) {
        if (!isOnline) return;

        String formatted = "From " + sender.getUsername() + ": " + message;
        receivedMessages.add(formatted);
        System.out.println("  [User] 📩 " + username + " received: " + formatted);
    }

    public void showReceivedMessages() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📋 Messages received by " + username + ":");
        if (receivedMessages.isEmpty()) {
            System.out.println("  │    No messages");
        } else {
            for (String msg : receivedMessages) {
                System.out.println("  │    • " + msg);
            }
        }
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Part 3: SystemMessageSender - Helper class for system messages
// =========================================================================

class SystemMessageSender extends ChatUser {
    public SystemMessageSender(ChatMediator mediator) {
        super("System", "system", mediator);
    }

    @Override
    public String getUsername() {
        return "System";
    }

    @Override
    public void receiveMessage(String message, ChatUser sender) {
        // System doesn't receive messages
    }

    @Override
    public void sendMessage(String message) {
        // System doesn't send messages through normal channels
    }

    @Override
    public void sendPrivateMessage(String message, String recipient) {
        // System doesn't send private messages
    }
}

// =========================================================================
// Part 4: Concrete Mediator
// =========================================================================

class ChatRoom implements ChatMediator {
    private String roomName;
    private List<ChatUser> users;
    private List<String> messageHistory;
    private SystemMessageSender systemSender;

    public ChatRoom(String name) {
        this.roomName = name;
        this.users = new ArrayList<>();
        this.messageHistory = new ArrayList<>();
        this.systemSender = new SystemMessageSender(this);

        System.out.println("\n==========================================");
        System.out.println("🏠 New chat room created: " + roomName);
        System.out.println("==========================================");
    }

    @Override
    public void addUser(ChatUser user) {
        users.add(user);
        System.out.println("  [Mediator] ✅ " + user.getUsername() + " joined the room");

        // Don't send notification for system user
        if (user.getUsername().equals("System")) {
            return;
        }

        String joinMsg = "👋 " + user.getUsername() + " joined the chat";
        messageHistory.add(joinMsg);

        // Send notification to all other users
        for (ChatUser u : users) {
            if (u != user && u.isOnline() && !u.getUsername().equals("System")) {
                u.receiveMessage(joinMsg, systemSender);
            }
        }
    }

    @Override
    public void removeUser(ChatUser user) {
        users.remove(user);
        System.out.println("  [Mediator] ❌ " + user.getUsername() + " left the room");

        // Don't send notification for system user
        if (user.getUsername().equals("System")) {
            return;
        }

        String leaveMsg = "👋 " + user.getUsername() + " left the chat";
        messageHistory.add(leaveMsg);

        // Send notification to all remaining users
        for (ChatUser u : users) {
            if (u.isOnline() && !u.getUsername().equals("System")) {
                u.receiveMessage(leaveMsg, systemSender);
            }
        }
    }

    @Override
    public void sendMessage(String message, ChatUser sender) {
        String formatted = "[" + sender.getUsername() + " to all]: " + message;
        messageHistory.add(formatted);

        for (ChatUser user : users) {
            if (user != sender && user.isOnline() && !user.getUsername().equals("System")) {
                user.receiveMessage(message, sender);
            }
        }
    }

    @Override
    public void sendPrivateMessage(String message, ChatUser sender, String recipientName) {
        for (ChatUser user : users) {
            if (user.getUsername().equals(recipientName) && user.isOnline() && !user.getUsername().equals("System")) {
                String formatted = "[From " + sender.getUsername() + " private]: " + message;
                messageHistory.add(formatted);
                user.receiveMessage("(Private) " + message, sender);
                return;
            }
        }
        System.out.println("  [Mediator] ⚠️ User '" + recipientName + "' not found or offline");
    }

    @Override
    public List<String> getMessageHistory() {
        return messageHistory;
    }

    public void showOnlineUsers() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 👥 Online Users:");
        boolean onlineFound = false;
        for (ChatUser user : users) {
            if (user.isOnline() && !user.getUsername().equals("System")) {
                System.out.println("  │    • " + user.getUsername() + " (" + user.getRole() + ")");
                onlineFound = true;
            }
        }
        if (!onlineFound) {
            System.out.println("  │    No users online");
        }
        System.out.println("  └─────────────────────────────────");
    }

    public void showAllUsers() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 👥 All Users (" + (users.size() - 1) + "):"); // Subtract system user
        boolean userFound = false;
        for (ChatUser user : users) {
            if (!user.getUsername().equals("System")) {
                System.out.println("  │    • " + user.getUsername() + " (" + user.getRole() +
                        ") - " + (user.isOnline() ? "🟢 Online" : "🔴 Offline"));
                userFound = true;
            }
        }
        if (!userFound) {
            System.out.println("  │    No users");
        }
        System.out.println("  └─────────────────────────────────");
    }

    public void showMessageHistory() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📋 Message History (" + messageHistory.size() + "):");
        if (messageHistory.isEmpty()) {
            System.out.println("  │    No messages");
        } else {
            for (String msg : messageHistory) {
                System.out.println("  │    • " + msg);
            }
        }
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Part 5: Main Class - With User Input
// =========================================================================

public class MediatorPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           MEDIATOR PATTERN DEMO                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.print("Enter chat room name: ");
        String roomName = scanner.nextLine();

        ChatRoom chatRoom = new ChatRoom(roomName);

        // Create default users
        System.out.println("\n--- Creating default users ---");
        ChatUser admin = new ChatUser("Abebe", "Admin", chatRoom);
        ChatUser mod = new ChatUser("Askale", "Moderator", chatRoom);
        ChatUser user1 = new ChatUser("Samuel", "Member", chatRoom);
        ChatUser user2 = new ChatUser("Hana", "Member", chatRoom);

        Map<String, ChatUser> userMap = new HashMap<>();
        userMap.put("Abebe", admin);
        userMap.put("Askale", mod);
        userMap.put("Samuel", user1);
        userMap.put("Hana", user2);

        ChatUser currentUser = admin;

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Current user: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            System.out.println("Available Options:");
            System.out.println("1. Change user");
            System.out.println("2. Send message to all");
            System.out.println("3. Send private message");
            System.out.println("4. View online users");
            System.out.println("5. View all users");
            System.out.println("6. View message history");
            System.out.println("7. View my received messages");
            System.out.println("8. Toggle online/offline status");
            System.out.println("9. Exit");
            System.out.print("Enter your choice (1-9): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Available users: Abebe, Askale, Samuel, Hana");
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    if (userMap.containsKey(username)) {
                        currentUser = userMap.get(username);
                        System.out.println("✅ You are now " + currentUser.getUsername());
                    } else {
                        System.out.println("❌ User not found!");
                    }
                    break;

                case 2:
                    System.out.print("Enter message: ");
                    String msg = scanner.nextLine();
                    currentUser.sendMessage(msg);
                    break;

                case 3:
                    System.out.print("Who do you want to send to? ");
                    String recipient = scanner.nextLine();
                    System.out.print("Enter message: ");
                    String privateMsg = scanner.nextLine();
                    currentUser.sendPrivateMessage(privateMsg, recipient);
                    break;

                case 4:
                    chatRoom.showOnlineUsers();
                    break;

                case 5:
                    chatRoom.showAllUsers();
                    break;

                case 6:
                    chatRoom.showMessageHistory();
                    break;

                case 7:
                    currentUser.showReceivedMessages();
                    break;

                case 8:
                    currentUser.setOnline(!currentUser.isOnline());
                    break;

                case 9:
                    continueRunning = false;
                    System.out.println("\nThank you for using the program! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-9)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                PROGRAM COMPLETED! THANK YOU            ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}