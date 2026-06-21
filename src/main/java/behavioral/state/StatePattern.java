// File: StatePattern.java
// ይህ ፋይል State Pattern ን በዝርዝር ያሳያል
// ተጠቃሚው የትዕዛዝ ሁኔታዎችን መቀየር ይችላል

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * STATE PATTERN - የስቴት ንድፍ ቅጥ
 * =====================================================================
 * ይህ ንድፍ ቅጥ አንድ ነገር በውስጣዊ ሁኔታው መሰረት
 * ባህሪውን እንዲቀይር ያስችላል።
 */

// =========================================================================
// ክፍል 1: ስቴት በይነገጽ (State Interface)
// =========================================================================

interface OrderState {
    void next(Order order);
    void prev(Order order);
    void cancel(Order order);
    String getStatus();
    List<String> getAllowedActions();
}

// =========================================================================
// ክፍል 2: ኮንክሪት ስቴቶች (Concrete States)
// =========================================================================

class NewOrderState implements OrderState {

    @Override
    public void next(Order order) {
        System.out.println("  [State] ➡️ ወደ ክፍያ ማስኬጃ ሁኔታ ተለውጧል");
        order.setState(new ProcessingPaymentState());
    }

    @Override
    public void prev(Order order) {
        System.out.println("  [State] ⚠️ ከአዲስ ትዕዛዝ ወደ ኋላ መሄድ አይቻልም");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("  [State] ❌ ትዕዛዝ ተሰርዟል");
        order.setState(new CancelledOrderState());
    }

    @Override
    public String getStatus() {
        return "🆕 አዲስ ትዕዛዝ";
    }

    @Override
    public List<String> getAllowedActions() {
        List<String> actions = new ArrayList<>();
        actions.add("ወደ ክፍያ ሂድ");
        actions.add("ሰርዝ");
        return actions;
    }
}

class ProcessingPaymentState implements OrderState {

    @Override
    public void next(Order order) {
        System.out.println("  [State] ➡️ ወደ ተከፈለ ሁኔታ ተለውጧል");
        order.setState(new PaidOrderState());
    }

    @Override
    public void prev(Order order) {
        System.out.println("  [State] ⬅️ ወደ አዲስ ትዕዛዝ ተመለሰ");
        order.setState(new NewOrderState());
    }

    @Override
    public void cancel(Order order) {
        System.out.println("  [State] ❌ ክፍያ ሂደት ላይ እያለ መሰረዝ አይቻልም");
    }

    @Override
    public String getStatus() {
        return "💳 ክፍያ በመከናወን ላይ";
    }

    @Override
    public List<String> getAllowedActions() {
        List<String> actions = new ArrayList<>();
        actions.add("ክፍያ አጠናቅቅ");
        actions.add("ወደ ኋላ ተመለስ");
        return actions;
    }
}

class PaidOrderState implements OrderState {

    @Override
    public void next(Order order) {
        System.out.println("  [State] ➡️ ወደ ተላከ ሁኔታ ተለውጧል");
        order.setState(new ShippedOrderState());
    }

    @Override
    public void prev(Order order) {
        System.out.println("  [State] ⬅️ ወደ ክፍያ ማስኬጃ ተመለሰ");
        order.setState(new ProcessingPaymentState());
    }

    @Override
    public void cancel(Order order) {
        System.out.println("  [State] ❌ ትዕዛዝ ተሰርዟል (ገንዘብ ይመለሳል)");
        order.setState(new CancelledOrderState());
    }

    @Override
    public String getStatus() {
        return "✅ ተከፍሏል";
    }

    @Override
    public List<String> getAllowedActions() {
        List<String> actions = new ArrayList<>();
        actions.add("ላክ");
        actions.add("ሰርዝ (ገንዘብ ይመለሳል)");
        actions.add("ወደ ኋላ ተመለስ");
        return actions;
    }
}

class ShippedOrderState implements OrderState {

    @Override
    public void next(Order order) {
        System.out.println("  [State] ➡️ ወደ ደረሰ ሁኔታ ተለውጧል");
        order.setState(new DeliveredOrderState());
    }

    @Override
    public void prev(Order order) {
        System.out.println("  [State] ⬅️ ወደ ተከፈለ ሁኔታ ተመለሰ");
        order.setState(new PaidOrderState());
    }

    @Override
    public void cancel(Order order) {
        System.out.println("  [State] ❌ ከተላከ በኋላ መሰረዝ አይቻልም");
    }

    @Override
    public String getStatus() {
        return "📦 ተልኳል";
    }

    @Override
    public List<String> getAllowedActions() {
        List<String> actions = new ArrayList<>();
        actions.add("ደረሰ");
        actions.add("ወደ ኋላ ተመለስ");
        return actions;
    }
}

class DeliveredOrderState implements OrderState {

    @Override
    public void next(Order order) {
        System.out.println("  [State] ⚠️ ትዕዛዝ ደርሷል፣ ቀጣይ ሁኔታ የለም");
    }

    @Override
    public void prev(Order order) {
        System.out.println("  [State] ⬅️ ወደ ተላከ ሁኔታ ተመለሰ");
        order.setState(new ShippedOrderState());
    }

    @Override
    public void cancel(Order order) {
        System.out.println("  [State] ❌ ከደረሰ በኋላ መሰረዝ አይቻልም");
    }

    @Override
    public String getStatus() {
        return "📬 ደርሷል";
    }

    @Override
    public List<String> getAllowedActions() {
        List<String> actions = new ArrayList<>();
        actions.add("ወደ ኋላ ተመለስ");
        actions.add("አጠናቅቅ");
        return actions;
    }
}

class CancelledOrderState implements OrderState {

    @Override
    public void next(Order order) {
        System.out.println("  [State] ⚠️ የተሰረዘ ትዕዛዝ መቀየር አይቻልም");
    }

    @Override
    public void prev(Order order) {
        System.out.println("  [State] ⚠️ የተሰረዘ ትዕዛዝ መቀየር አይቻልም");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("  [State] ⚠️ ትዕዛዝ አስቀድሞ ተሰርዟል");
    }

    @Override
    public String getStatus() {
        return "❌ ተሰርዟል";
    }

    @Override
    public List<String> getAllowedActions() {
        List<String> actions = new ArrayList<>();
        actions.add("እንደገና አዘዝ");
        return actions;
    }
}

// =========================================================================
// ክፍል 3: አውድ (Context) - ትዕዛዝ
// =========================================================================

class Order {
    private int orderId;
    private String customerName;
    private List<String> items;
    private double total;
    private OrderState state;
    private List<String> stateHistory;
    private static int idCounter = 1000;

    public Order(String customerName) {
        this.orderId = idCounter++;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.state = new NewOrderState();
        this.stateHistory = new ArrayList<>();
        addToHistory("ትዕዛዝ ተፈጠረ");

        System.out.println("\n==========================================");
        System.out.println("🛒 አዲስ ትዕዛዝ #" + orderId + " ለ " + customerName);
        System.out.println("==========================================");
    }

    public void addItem(String item, double price) {
        items.add(item);
        total += price;
        System.out.println("  [Order] ➕ " + item + " - " + price + " ብር");
    }

    public void setState(OrderState newState) {
        String oldState = state.getStatus();
        this.state = newState;
        addToHistory("ከ " + oldState + " ወደ " + newState.getStatus());
    }

    private void addToHistory(String event) {
        stateHistory.add(java.time.LocalTime.now().toString().substring(0,8) + " - " + event);
    }

    public void nextState() {
        System.out.println("\n  [Order] 🔄 ወደ ቀጣይ ሁኔታ ለመሄድ መሞከር...");
        state.next(this);
    }

    public void prevState() {
        System.out.println("\n  [Order] 🔄 ወደ ቀድሞ ሁኔታ ለመመለስ መሞከር...");
        state.prev(this);
    }

    public void cancelOrder() {
        System.out.println("\n  [Order] 🔄 ትዕዛዝ ለመሰረዝ መሞከር...");
        state.cancel(this);
    }

    public void displayInfo() {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║  🧾 ትዕዛዝ #" + orderId);
        System.out.println("║  👤 ደንበኛ: " + customerName);
        System.out.println("║  📊 ሁኔታ: " + state.getStatus());
        System.out.println("║  💰 አጠቃላይ: " + total + " ብር");
        System.out.println("║  📦 ዕቃዎች: " + items.size());
        for (String item : items) {
            System.out.println("║     • " + item);
        }
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.println("║  ✅ የሚፈቀዱ ክዋኔዎች:");
        for (String action : state.getAllowedActions()) {
            System.out.println("║     • " + action);
        }
        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    public void showHistory() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📋 የትዕዛዝ ታሪክ:");
        for (String event : stateHistory) {
            System.out.println("  │ " + event);
        }
        System.out.println("  └─────────────────────────────────");
    }

    public int getOrderId() { return orderId; }
}

// =========================================================================
// ክፍል 4: ዋናው ክላስ - ከተጠቃሚ ግብዓት ጋር
// =========================================================================

public class StatePattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           STATE PATTERN - የስቴት ንድፍ ቅጥ ማሳያ            ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        Order currentOrder = null;
        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("የሚገኙ አማራጮች:");
            System.out.println("1. አዲስ ትዕዛዝ ፍጠር");
            System.out.println("2. ዕቃ ወደ ትዕዛዝ ጨምር");
            System.out.println("3. ወደ ቀጣይ ሁኔታ ሂድ");
            System.out.println("4. ወደ ቀድሞ ሁኔታ ተመለስ");
            System.out.println("5. ትዕዛዝ ሰርዝ");
            System.out.println("6. የትዕዛዝ መረጃ አሳይ");
            System.out.println("7. ታሪክ አሳይ");
            System.out.println("8. መውጣት");
            System.out.print("ምርጫዎን ያስገቡ (1-8): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("የደንበኛ ስም ያስገቡ: ");
                    String customer = scanner.nextLine();
                    currentOrder = new Order(customer);
                    break;

                case 2:
                    if (currentOrder == null) {
                        System.out.println("⚠️ መጀመሪያ ትዕዛዝ ይፍጠሩ!");
                        break;
                    }
                    System.out.print("የዕቃ ስም ያስገቡ: ");
                    String item = scanner.nextLine();
                    System.out.print("የዕቃ ዋጋ ያስገቡ: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();
                    currentOrder.addItem(item, price);
                    break;

                case 3:
                    if (currentOrder == null) {
                        System.out.println("⚠️ መጀመሪያ ትዕዛዝ ይፍጠሩ!");
                        break;
                    }
                    currentOrder.nextState();
                    break;

                case 4:
                    if (currentOrder == null) {
                        System.out.println("⚠️ መጀመሪያ ትዕዛዝ ይፍጠሩ!");
                        break;
                    }
                    currentOrder.prevState();
                    break;

                case 5:
                    if (currentOrder == null) {
                        System.out.println("⚠️ መጀመሪያ ትዕዛዝ ይፍጠሩ!");
                        break;
                    }
                    currentOrder.cancelOrder();
                    break;

                case 6:
                    if (currentOrder == null) {
                        System.out.println("⚠️ መጀመሪያ ትዕዛዝ ይፍጠሩ!");
                        break;
                    }
                    currentOrder.displayInfo();
                    break;

                case 7:
                    if (currentOrder == null) {
                        System.out.println("⚠️ መጀመሪያ ትዕዛዝ ይፍጠሩ!");
                        break;
                    }
                    currentOrder.showHistory();
                    break;

                case 8:
                    continueRunning = false;
                    System.out.println("\nእንደገና ለመጠቀም እንጠብቅዎታለን! ደህና ሁኑ።");
                    break;

                default:
                    System.out.println("ስህተት: እባክዎ ትክክለኛ ምርጫ ያስገቡ (1-8)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                ፕሮግራሙ ተጠናቋል! አመሰግናለሁ                ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}