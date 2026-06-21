// File: CommandPattern.java
// This file demonstrates the Command Pattern in detail
// The user can execute various commands

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * =====================================================================
 * COMMAND PATTERN
 * =====================================================================
 * This pattern encapsulates a request as an object and passes it as a command.
 */

// =========================================================================
// Part 1: Receiver - Light
// =========================================================================

class Light {
    private String location;
    private boolean isOn;
    private int brightness;

    public Light(String location) {
        this.location = location;
        this.isOn = false;
        this.brightness = 0;
    }

    public void turnOn() {
        isOn = true;
        brightness = 100;
        System.out.println("  [Light] 💡 " + location + " light turned on");
    }

    public void turnOff() {
        isOn = false;
        brightness = 0;
        System.out.println("  [Light] 💡 " + location + " light turned off");
    }

    public void setBrightness(int level) {
        if (!isOn && level > 0) {
            isOn = true;
        }
        brightness = level;
        System.out.println("  [Light] 💡 " + location + " light brightness: " + brightness + "%");
    }

    // Getter methods - to access private properties
    public boolean isOn() {
        return isOn;
    }

    public int getBrightness() {
        return brightness;
    }

    public String getStatus() {
        return location + ": " + (isOn ? "On" : "Off") + " (" + brightness + "%)";
    }
}

// =========================================================================
// Part 2: Receiver - Fan
// =========================================================================

class Fan {
    private String location;
    private int speed; // 0-3

    public Fan(String location) {
        this.location = location;
        this.speed = 0;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        System.out.println("  [Fan] 🌬️ " + location + " fan speed: " + speed);
    }

    public void turnOff() {
        speed = 0;
        System.out.println("  [Fan] 🌬️ " + location + " fan turned off");
    }

    // Getter method - to access private property
    public int getSpeed() {
        return speed;
    }

    public String getStatus() {
        return location + ": Speed " + speed;
    }
}

// =========================================================================
// Part 3: Command Interface
// =========================================================================

interface Command {
    void execute();
    void undo();
    String getDescription();
}

// =========================================================================
// Part 4: Concrete Commands - For Light
// =========================================================================

class LightOnCommand implements Command {
    private Light light;
    private boolean previousState;
    private int previousBrightness;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        previousState = light.isOn();
        previousBrightness = light.getBrightness();
        light.turnOn();
    }

    @Override
    public void undo() {
        if (previousState) {
            light.turnOn();
            light.setBrightness(previousBrightness);
        } else {
            light.turnOff();
        }
    }

    @Override
    public String getDescription() {
        return "Turn on " + light.getStatus();
    }
}

class LightOffCommand implements Command {
    private Light light;
    private boolean previousState;
    private int previousBrightness;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        previousState = light.isOn();
        previousBrightness = light.getBrightness();
        light.turnOff();
    }

    @Override
    public void undo() {
        if (previousState) {
            light.turnOn();
            light.setBrightness(previousBrightness);
        }
    }

    @Override
    public String getDescription() {
        return "Turn off " + light.getStatus();
    }
}

class SetBrightnessCommand implements Command {
    private Light light;
    private int newBrightness;
    private int previousBrightness;
    private boolean previousState;

    public SetBrightnessCommand(Light light, int brightness) {
        this.light = light;
        this.newBrightness = brightness;
    }

    @Override
    public void execute() {
        previousState = light.isOn();
        previousBrightness = light.getBrightness();
        light.setBrightness(newBrightness);
    }

    @Override
    public void undo() {
        if (previousState) {
            light.turnOn();
            light.setBrightness(previousBrightness);
        } else {
            light.turnOff();
        }
    }

    @Override
    public String getDescription() {
        return "Set brightness to " + newBrightness + "%";
    }
}

// =========================================================================
// Part 5: Concrete Commands - For Fan
// =========================================================================

class FanSpeedCommand implements Command {
    private Fan fan;
    private int newSpeed;
    private int previousSpeed;

    public FanSpeedCommand(Fan fan, int speed) {
        this.fan = fan;
        this.newSpeed = speed;
    }

    @Override
    public void execute() {
        previousSpeed = fan.getSpeed();
        fan.setSpeed(newSpeed);
    }

    @Override
    public void undo() {
        fan.setSpeed(previousSpeed);
    }

    @Override
    public String getDescription() {
        return "Fan speed " + newSpeed;
    }
}

class FanOffCommand implements Command {
    private Fan fan;
    private int previousSpeed;

    public FanOffCommand(Fan fan) {
        this.fan = fan;
    }

    @Override
    public void execute() {
        previousSpeed = fan.getSpeed();
        fan.turnOff();
    }

    @Override
    public void undo() {
        fan.setSpeed(previousSpeed);
    }

    @Override
    public String getDescription() {
        return "Turn off fan";
    }
}

// =========================================================================
// Part 6: Invoker - Remote Control
// =========================================================================

class RemoteControl {
    private String name;
    private Stack<Command> commandHistory;
    private Stack<Command> redoStack;
    private List<Command> macroCommands;

    public RemoteControl(String name) {
        this.name = name;
        this.commandHistory = new Stack<>();
        this.redoStack = new Stack<>();
        this.macroCommands = new ArrayList<>();

        System.out.println("\n==========================================");
        System.out.println("🎮 " + name + " Remote Control Started");
        System.out.println("==========================================");
    }

    public void executeCommand(Command command) {
        System.out.println("\n  [Remote] 🎮 Command executed: " + command.getDescription());
        command.execute();
        commandHistory.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!commandHistory.isEmpty()) {
            Command command = commandHistory.pop();
            System.out.println("\n  [Remote] ↩️ Command undone: " + command.getDescription());
            command.undo();
            redoStack.push(command);
        } else {
            System.out.println("\n  [Remote] ⚠️ Nothing to undo");
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            System.out.println("\n  [Remote] 🔄 Command redone: " + command.getDescription());
            command.execute();
            commandHistory.push(command);
        } else {
            System.out.println("\n  [Remote] ⚠️ Nothing to redo");
        }
    }

    public void startMacro() {
        macroCommands.clear();
        System.out.println("\n  [Remote] 📝 Macro recording started");
    }

    public void addToMacro(Command command) {
        macroCommands.add(command);
        System.out.println("  [Remote] ➕ Command added to macro: " + command.getDescription());
    }

    public void executeMacro() {
        if (macroCommands.isEmpty()) {
            System.out.println("\n  [Remote] ⚠️ Macro is empty!");
            return;
        }

        System.out.println("\n  [Remote] 🔄 Executing macro (" + macroCommands.size() + " commands)");
        for (Command cmd : macroCommands) {
            cmd.execute();
        }
        macroCommands.clear();
    }

    public void showHistory() {
        System.out.println("\n  ┌─────────────────────────────────");
        System.out.println("  │ 📋 Command History (" + commandHistory.size() + "):");
        for (int i = 0; i < commandHistory.size(); i++) {
            System.out.println("  │ " + (i+1) + ". " + commandHistory.get(i).getDescription());
        }
        System.out.println("  └─────────────────────────────────");
    }
}

// =========================================================================
// Part 7: Main Class - With User Input
// =========================================================================

public class CommandPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║           COMMAND PATTERN DEMO                         ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        // Create devices
        Light livingRoomLight = new Light("Living Room");
        Light bedroomLight = new Light("Bedroom");
        Fan livingRoomFan = new Fan("Living Room");

        RemoteControl remote = new RemoteControl("Smart Home");

        boolean continueRunning = true;
        boolean macroRecording = false;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Turn on living room light");
            System.out.println("2. Turn off living room light");
            System.out.println("3. Change living room light brightness");
            System.out.println("4. Turn on bedroom light");
            System.out.println("5. Turn off bedroom light");
            System.out.println("6. Change living room fan speed");
            System.out.println("7. Turn off living room fan");
            System.out.println("8. Undo");
            System.out.println("9. Redo");
            System.out.println("10. Start macro recording");
            System.out.println("11. Add to macro");
            System.out.println("12. Execute macro");
            System.out.println("13. Show history");
            System.out.println("14. Exit");
            System.out.print("Enter your choice (1-14): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            Command command = null;

            switch (choice) {
                case 1:
                    command = new LightOnCommand(livingRoomLight);
                    break;
                case 2:
                    command = new LightOffCommand(livingRoomLight);
                    break;
                case 3:
                    System.out.print("Brightness level (0-100): ");
                    int brightness = scanner.nextInt();
                    scanner.nextLine();
                    command = new SetBrightnessCommand(livingRoomLight, brightness);
                    break;
                case 4:
                    command = new LightOnCommand(bedroomLight);
                    break;
                case 5:
                    command = new LightOffCommand(bedroomLight);
                    break;
                case 6:
                    System.out.print("Speed (0-3): ");
                    int speed = scanner.nextInt();
                    scanner.nextLine();
                    command = new FanSpeedCommand(livingRoomFan, speed);
                    break;
                case 7:
                    command = new FanOffCommand(livingRoomFan);
                    break;
                case 8:
                    remote.undo();
                    break;
                case 9:
                    remote.redo();
                    break;
                case 10:
                    remote.startMacro();
                    macroRecording = true;
                    break;
                case 11:
                    if (!macroRecording) {
                        System.out.println("⚠️ Start macro recording first!");
                    } else if (command != null) {
                        remote.addToMacro(command);
                    } else {
                        System.out.println("⚠️ Choose a command first!");
                    }
                    break;
                case 12:
                    remote.executeMacro();
                    macroRecording = false;
                    break;
                case 13:
                    remote.showHistory();
                    break;
                case 14:
                    continueRunning = false;
                    System.out.println("\nThank you for using the program! Goodbye.");
                    break;
                default:
                    System.out.println("Error: Please enter a valid choice (1-14)");
            }

            if (command != null && !macroRecording && choice <= 7) {
                remote.executeCommand(command);
            } else if (command != null && macroRecording && choice <= 7) {
                remote.addToMacro(command);
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                PROGRAM COMPLETED! THANK YOU            ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}