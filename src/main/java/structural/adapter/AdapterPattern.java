// File: AdapterPattern.java
// This file demonstrates the Adapter Pattern in detail
// The user can play any media type and file they want

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * =====================================================================
 * ADAPTER PATTERN
 * =====================================================================
 * This design pattern allows classes with incompatible interfaces to work together.
 */

// =========================================================================
// Section 1: Target Interface
// =========================================================================

/**
 * MediaPlayer Interface - The interface expected by the client
 */
interface MediaPlayer {
    void play(String audioType, String fileName);
    List<String> getPlayHistory();
}

// =========================================================================
// Section 2: Adaptee Interface
// =========================================================================

/**
 * AdvancedMediaPlayer Interface - Advanced media player functions
 */
interface AdvancedMediaPlayer {
    void playMp4(String fileName);
    void playVlc(String fileName);
    void playAvi(String fileName);
    void playMkv(String fileName);
    List<String> getPlayHistory();
}

// =========================================================================
// Section 3: Concrete Adaptee Classes
// =========================================================================

/**
 * Mp4Player Class - Can play MP4 files
 */
class Mp4Player implements AdvancedMediaPlayer {
    private List<String> playHistory;
    private String playerName;

    public Mp4Player() {
        this.playHistory = new ArrayList<>();
        this.playerName = "MP4 Player";
        System.out.println("  [Mp4Player] 🎬 MP4 Player created");
    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("  [Mp4Player] ▶️ Playing MP4 file: " + fileName);
        System.out.println("  [Mp4Player]    Quality: 1080p, Audio: Stereo");
        playHistory.add("MP4: " + fileName);
    }

    @Override
    public void playVlc(String fileName) {
        System.out.println("  [Mp4Player] ⚠️ Mp4Player does not support VLC files: " + fileName);
    }

    @Override
    public void playAvi(String fileName) {
        System.out.println("  [Mp4Player] ⚠️ Mp4Player does not support AVI files: " + fileName);
    }

    @Override
    public void playMkv(String fileName) {
        System.out.println("  [Mp4Player] ⚠️ Mp4Player does not support MKV files: " + fileName);
    }

    @Override
    public List<String> getPlayHistory() {
        return new ArrayList<>(playHistory);
    }
}

/**
 * VlcPlayer Class - Can play VLC files
 */
class VlcPlayer implements AdvancedMediaPlayer {
    private List<String> playHistory;
    private String playerName;

    public VlcPlayer() {
        this.playHistory = new ArrayList<>();
        this.playerName = "VLC Player";
        System.out.println("  [VlcPlayer] 🎬 VLC Player created");
    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("  [VlcPlayer] ⚠️ VlcPlayer does not support MP4 files: " + fileName);
    }

    @Override
    public void playVlc(String fileName) {
        System.out.println("  [VlcPlayer] ▶️ Playing VLC file: " + fileName);
        System.out.println("  [VlcPlayer]    Quality: 720p, Audio: Mono");
        playHistory.add("VLC: " + fileName);
    }

    @Override
    public void playAvi(String fileName) {
        System.out.println("  [VlcPlayer] ⚠️ VlcPlayer does not support AVI files: " + fileName);
    }

    @Override
    public void playMkv(String fileName) {
        System.out.println("  [VlcPlayer] ⚠️ VlcPlayer does not support MKV files: " + fileName);
    }

    @Override
    public List<String> getPlayHistory() {
        return new ArrayList<>(playHistory);
    }
}

/**
 * AviPlayer Class - Can play AVI files
 */
class AviPlayer implements AdvancedMediaPlayer {
    private List<String> playHistory;
    private String playerName;

    public AviPlayer() {
        this.playHistory = new ArrayList<>();
        this.playerName = "AVI Player";
        System.out.println("  [AviPlayer] 🎬 AVI Player created");
    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("  [AviPlayer] ⚠️ AviPlayer does not support MP4 files: " + fileName);
    }

    @Override
    public void playVlc(String fileName) {
        System.out.println("  [AviPlayer] ⚠️ AviPlayer does not support VLC files: " + fileName);
    }

    @Override
    public void playAvi(String fileName) {
        System.out.println("  [AviPlayer] ▶️ Playing AVI file: " + fileName);
        System.out.println("  [AviPlayer]    Quality: 480p, Audio: Stereo");
        playHistory.add("AVI: " + fileName);
    }

    @Override
    public void playMkv(String fileName) {
        System.out.println("  [AviPlayer] ⚠️ AviPlayer does not support MKV files: " + fileName);
    }

    @Override
    public List<String> getPlayHistory() {
        return new ArrayList<>(playHistory);
    }
}

// =========================================================================
// Section 4: Adapter Class
// =========================================================================

/**
 * MediaAdapter Class - This is the adapter
 */
class MediaAdapter implements MediaPlayer {
    private AdvancedMediaPlayer advancedMediaPlayer;
    private String adapterType;
    private List<String> playHistory;

    public MediaAdapter(String audioType) {
        this.playHistory = new ArrayList<>();
        this.adapterType = "MediaAdapter-" + audioType;
        System.out.println("\n  MediaAdapter :🔧 Creating adapter for: " + audioType);

        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMediaPlayer = new VlcPlayer();
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMediaPlayer = new Mp4Player();
        } else if (audioType.equalsIgnoreCase("avi")) {
            advancedMediaPlayer = new AviPlayer();
        }
    }

    @Override
    public void play(String audioType, String fileName) {
        System.out.println("  [MediaAdapter] 🔄 Request received: " + audioType + " - " + fileName);
        playHistory.add("Played: " + audioType + " - " + fileName);

        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMediaPlayer.playVlc(fileName);
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMediaPlayer.playMp4(fileName);
        } else if (audioType.equalsIgnoreCase("avi")) {
            advancedMediaPlayer.playAvi(fileName);
        }
    }

    @Override
    public List<String> getPlayHistory() {
        List<String> fullHistory = new ArrayList<>(playHistory);
        if (advancedMediaPlayer != null) {
            fullHistory.addAll(advancedMediaPlayer.getPlayHistory());
        }
        return fullHistory;
    }
}

// =========================================================================
// Section 5: Client Class
// =========================================================================

/**
 * AudioPlayer Class - This is the client
 */
class AudioPlayer implements MediaPlayer {
    private MediaAdapter mediaAdapter;
    private List<String> playHistory;
    private String playerName;

    public AudioPlayer(String name) {
        this.playerName = name;
        this.playHistory = new ArrayList<>();
        System.out.println("\n==========================================");
        System.out.println("🎵 New AudioPlayer created: " + playerName);
        System.out.println("==========================================");
    }

    @Override
    public void play(String audioType, String fileName) {
        System.out.println("\n--- " + playerName + " started playing ---");

        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("  AudioPlayer:▶️ Playing MP3 file: " + fileName);
            System.out.println("  AudioPlayer:   Using built-in player");
            playHistory.add("MP3: " + fileName);
        }
        else if (audioType.equalsIgnoreCase("vlc") ||
                audioType.equalsIgnoreCase("mp4") ||
                audioType.equalsIgnoreCase("avi")) {

            System.out.println("  AudioPlayer: 🔄 Using adapter for: " + audioType);
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, fileName);
            playHistory.add(audioType.toUpperCase() + ": " + fileName + " (via adapter)");
        }
        else {
            System.out.println("  AudioPlayer: ❌ Unsupported media type: " + audioType);
            System.out.println("  AudioPlayer:    Supported types: mp3, mp4, vlc, avi");
        }

        System.out.println("--- " + playerName + " finished playing ---");
    }

    @Override
    public List<String> getPlayHistory() {
        List<String> fullHistory = new ArrayList<>(playHistory);
        if (mediaAdapter != null) {
            fullHistory.addAll(mediaAdapter.getPlayHistory());
        }
        return fullHistory;
    }

    public void showPlayHistory() {
        System.out.println("\n--- " + playerName + " Play History ---");
        List<String> history = getPlayHistory();
        if (history.isEmpty()) {
            System.out.println("  No files played yet");
        } else {
            for (int i = 0; i < history.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + history.get(i));
            }
        }
    }
}

// =========================================================================
// Section 6: Main Class - With User Input
// =========================================================================

public class AdapterPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║              ADAPTER PATTERN DEMO                      ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("This demo shows how the Adapter Pattern makes");
        System.out.println("incompatible interfaces work together. It allows");
        System.out.println("integrating new media formats into an existing system.\n");

        System.out.print("Enter your player name: ");
        String playerName = scanner.nextLine();

        AudioPlayer audioPlayer = new AudioPlayer(playerName);

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n──────────────────────────────────────────");
            System.out.println("Available Options:");
            System.out.println("1. Play file");
            System.out.println("2. View play history");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter media type (mp3, mp4, vlc, avi): ");
                    String mediaType = scanner.nextLine();

                    System.out.print("Enter file name (e.g., movie.mp4): ");
                    String fileName = scanner.nextLine();

                    audioPlayer.play(mediaType, fileName);
                    break;

                case 2:
                    audioPlayer.showPlayHistory();
                    break;

                case 3:
                    continueRunning = false;
                    System.out.println("\nThank you for using the program! Goodbye.");
                    break;

                default:
                    System.out.println("Error: Please enter a valid choice (1-3)");
            }
        }

        scanner.close();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("  ║              Program terminated! Thank you             ║");
        System.out.println("  ╚════════════════════════════════════════════════════════╝");
    }
}