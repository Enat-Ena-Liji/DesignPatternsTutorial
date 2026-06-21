// File: FacadePattern.java
// This file demonstrates the Facade Pattern in detail
// The user can book hotel rooms and process payments

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * FACADE PATTERN - The Facade Design Pattern
 * =====================================================================
 * This design pattern creates a simple interface for a complex subsystem.
 */

// =========================================================================
// Part 1: Hotel Room Class (Subsystem Class)
// =========================================================================

class HotelRoom {
    private int roomNumber;
    private String roomType;
    private double pricePerNight;
    private boolean isAvailable;
    private boolean needsCleaning;
    private List<String> amenities;

    public HotelRoom(int roomNumber, String roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
        this.needsCleaning = false;
        this.amenities = new ArrayList<>();
        setupAmenities();
    }

    private void setupAmenities() {
        amenities.add("Comfortable bed");
        amenities.add("Television");
        amenities.add("Hot water");
        amenities.add("Free WiFi");

        if (roomType.equalsIgnoreCase("Double")) {
            amenities.add("Two beds");
            amenities.add("Refrigerator");
        } else if (roomType.equalsIgnoreCase("Suite")) {
            amenities.add("Living room");
            amenities.add("Jacuzzi");
            amenities.add("Mini bar");
        }
    }

    public boolean book() {
        if (isAvailable && !needsCleaning) {
            isAvailable = false;
            needsCleaning = true;
            return true;
        }
        return false;
    }

    public void checkOut() {
        if (!isAvailable) {
            needsCleaning = true;
        }
    }

    public void clean() {
        needsCleaning = false;
        isAvailable = true;
    }

    public String getInfo() {
        return "Room " + roomNumber + " [" + roomType + "] - " +
                (isAvailable ? "Available" : "Occupied") + " - " +
                String.format("%.2f", pricePerNight) + " Birr";
    }

    public int getRoomNumber() { return roomNumber; }
    public boolean isAvailable() { return isAvailable; }
    public double getPricePerNight() { return pricePerNight; }
}

// =========================================================================
// Part 2: Payment Processor Class (Subsystem Class)
// =========================================================================

class PaymentProcessor {
    private List<String> paymentHistory;

    public PaymentProcessor() {
        this.paymentHistory = new ArrayList<>();
    }

    public boolean processPayment(double amount, String paymentMethod, String customerName) {
        System.out.println("  [Payment] 💳 Processing payment...");
        System.out.println("     Method: " + paymentMethod);
        System.out.println("     Amount: " + String.format("%.2f", amount) + " Birr");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String record = customerName + " - " + paymentMethod + " - " + amount + " Birr";
        paymentHistory.add(record);

        System.out.println("  [Payment] ✅ Payment successful!");
        return true;
    }

    public void showPaymentHistory() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 💳 Payment History:");
        if (paymentHistory.isEmpty()) {
            System.out.println("  │    No payments processed");
        } else {
            for (String record : paymentHistory) {
                System.out.println("  │    • " + record);
            }
        }
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Part 3: Cleaning Service Class (Subsystem Class)
// =========================================================================

class CleaningService {
    private List<Integer> cleaningSchedule;

    public CleaningService() {
        this.cleaningSchedule = new ArrayList<>();
    }

    public void scheduleCleaning(int roomNumber) {
        cleaningSchedule.add(roomNumber);
        System.out.println("  [Cleaning] 🧹 Room " + roomNumber + " scheduled for cleaning");
    }

    public void cleanRoom(int roomNumber) {
        if (cleaningSchedule.contains(roomNumber)) {
            cleaningSchedule.remove((Integer) roomNumber);
            System.out.println("  [Cleaning] ✅ Room " + roomNumber + " cleaned");
        }
    }
}

// =========================================================================
// Part 4: Guest Class
// =========================================================================

class Guest {
    private String name;
    private String phone;
    private String email;

    public Guest(String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.email = "";
    }

    public String getName() { return name; }
    public void setEmail(String email) { this.email = email; }
}

// =========================================================================
// Part 5: Booking Class
// =========================================================================

class Booking {
    private int bookingId;
    private Guest guest;
    private int roomNumber;
    private int nights;
    private double totalCost;
    private boolean isPaid;
    private static int idCounter = 1000;

    public Booking(Guest guest, int roomNumber, int nights, double totalCost) {
        this.bookingId = idCounter++;
        this.guest = guest;
        this.roomNumber = roomNumber;
        this.nights = nights;
        this.totalCost = totalCost;
        this.isPaid = false;
    }

    public int getBookingId() { return bookingId; }
    public int getRoomNumber() { return roomNumber; }
    public double getTotalCost() { return totalCost; }
    public void markAsPaid() { this.isPaid = true; }
}

// =========================================================================
// Part 6: Facade Class (Facade)
// =========================================================================

class HotelFacade {
    private String hotelName;
    private List<HotelRoom> rooms;
    private PaymentProcessor paymentProcessor;
    private CleaningService cleaningService;
    private List<Guest> guests;
    private List<Booking> bookings;
    private int nextRoomNumber;

    public HotelFacade(String hotelName) {
        this.hotelName = hotelName;
        this.rooms = new ArrayList<>();
        this.paymentProcessor = new PaymentProcessor();
        this.cleaningService = new CleaningService();
        this.guests = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.nextRoomNumber = 101;

        initializeRooms();

        System.out.println("\n==========================================");
        System.out.println("🏨 Welcome to " + hotelName + "!");
        System.out.println("==========================================");
    }

    private void initializeRooms() {
        rooms.add(new HotelRoom(101, "Single", 1500.00));
        rooms.add(new HotelRoom(102, "Single", 1500.00));
        rooms.add(new HotelRoom(201, "Double", 2500.00));
        rooms.add(new HotelRoom(202, "Double", 2500.00));
        rooms.add(new HotelRoom(301, "Suite", 4500.00));
        System.out.println("  🏨 " + rooms.size() + " rooms created");
    }

    public List<HotelRoom> findAvailableRooms() {
        List<HotelRoom> available = new ArrayList<>();
        for (HotelRoom room : rooms) {
            if (room.isAvailable()) {
                available.add(room);
            }
        }
        return available;
    }

    public Guest registerGuest(String name, String phone) {
        Guest guest = new Guest(name, phone);
        guests.add(guest);
        System.out.println("  [Facade] 👤 Guest registered: " + name);
        return guest;
    }

    public Booking bookRoom(Guest guest, int roomNumber, int nights, String paymentMethod) {
        System.out.println("\n  [Facade] 🔄 Room booking process started");

        // Find the room
        HotelRoom selectedRoom = null;
        for (HotelRoom room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null || !selectedRoom.isAvailable()) {
            System.out.println("  [Facade] ❌ Room not found or not available!");
            return null;
        }

        // Create booking
        double totalCost = selectedRoom.getPricePerNight() * nights;
        Booking booking = new Booking(guest, roomNumber, nights, totalCost);

        // Process payment
        boolean paymentSuccess = paymentProcessor.processPayment(totalCost, paymentMethod, guest.getName());

        if (!paymentSuccess) {
            System.out.println("  [Facade] ❌ Payment failed!");
            return null;
        }

        // Book the room
        selectedRoom.book();
        booking.markAsPaid();
        bookings.add(booking);

        System.out.println("  [Facade] ✅ Room booking successful! Booking #" + booking.getBookingId());
        return booking;
    }

    public void checkOut(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId) {
                for (HotelRoom room : rooms) {
                    if (room.getRoomNumber() == booking.getRoomNumber()) {
                        room.checkOut();
                        cleaningService.scheduleCleaning(booking.getRoomNumber());
                        System.out.println("  [Facade] ✅ Check-out successful");
                        return;
                    }
                }
            }
        }
        System.out.println("  [Facade] ⚠️ Booking not found!");
    }

    public void showHotelStatus() {
        int available = 0, occupied = 0;
        for (HotelRoom room : rooms) {
            if (room.isAvailable()) available++;
            else occupied++;
        }

        System.out.println("\n══════════════════════════════════════════");
        System.out.println("📊 " + hotelName + " Hotel Status");
        System.out.println("══════════════════════════════════════════");
        System.out.println("   Total Rooms: " + rooms.size());
        System.out.println("   🟢 Available Rooms: " + available);
        System.out.println("   🔴 Occupied Rooms: " + occupied);
        System.out.println("   👥 Registered Guests: " + guests.size());
        System.out.println("   📋 Active Bookings: " + bookings.size());
    }
}

// =========================================================================
// Part 7: Main Class - With User Input
// =========================================================================

public class FacadePattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║         FACADE PATTERN - Facade Design Pattern Demo           ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.print("Enter hotel name: ");
        String hotelName = scanner.nextLine();

        HotelFacade hotel = new HotelFacade(hotelName);

        boolean continueRunning = true;
        Guest currentGuest = null;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. View available rooms");
            System.out.println("2. Register new guest");
            System.out.println("3. Book room");
            System.out.println("4. Check-out");
            System.out.println("5. View hotel status");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    List<HotelRoom> available = hotel.findAvailableRooms();
                    System.out.println("\n🟢 Available Rooms:");
                    for (HotelRoom room : available) {
                        System.out.println("   " + room.getInfo());
                    }
                    break;

                case 2:
                    System.out.print("Enter guest name: ");
                    String guestName = scanner.nextLine();

                    System.out.print("Enter guest phone: ");
                    String phone = scanner.nextLine();

                    currentGuest = hotel.registerGuest(guestName, phone);
                    break;

                case 3:
                    if (currentGuest == null) {
                        System.out.println("⚠️ Please register a guest first!");
                        break;
                    }

                    List<HotelRoom> availableRooms = hotel.findAvailableRooms();
                    System.out.println("\n🟢 Available Rooms:");
                    for (HotelRoom room : availableRooms) {
                        System.out.println("   " + room.getInfo());
                    }

                    System.out.print("\nEnter room number: ");
                    int roomNum = scanner.nextInt();

                    System.out.print("How many nights will you stay? ");
                    int nights = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Payment method (cash/card/mobile): ");
                    String paymentMethod = scanner.nextLine();

                    Booking booking = hotel.bookRoom(currentGuest, roomNum, nights, paymentMethod);
                    if (booking != null) {
                        System.out.println("✅ Booking ID: " + booking.getBookingId());
                    }
                    break;

                case 4:
                    System.out.print("Enter booking ID: ");
                    int bookingId = scanner.nextInt();
                    scanner.nextLine();

                    hotel.checkOut(bookingId);
                    break;

                case 5:
                    hotel.showHotelStatus();
                    break;

                case 6:
                    continueRunning = false;
                    System.out.println("\nThank you for using the system! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-6)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                Program terminated! Thank you                ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}