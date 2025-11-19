package com.chunkminecraft;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;

public class SoundManager {
    public static void playBreakSound() {
        playSound("/sounds/break.wav");
    }
    
    public static void playClickSound() {
        playSound("/sounds/click.wav");
    }
    
    public static void playBackgroundMusic() {
        playSound("/sounds/background.wav");
    }
    
    public static void playToolSwitchSound() {
        playSound("/sounds/tool_switch.wav");
    }
    
    public static void playWorldRegenSound() {
        playSound("/sounds/world_regen.wav");
    }
    
    public static void playHoverSound() {
        playSound("/sounds/hover.wav");
    }
    
    public static void playErrorSound() {
        playSound("/sounds/error.wav");
    }
    
    public static void playWrongToolSound() {
        playSound("/sounds/wrong_tool.wav");
    }

public static void playCollectSound() {
    playSound("/sounds/collect.wav");
}

public static void playPlaceSound() {
    playSound("/sounds/place.wav");
}

public static void playTimeChangeSound() {
    playSound("/sounds/time_change.wav");
}
    
    private static void playSound(String soundFile) {
        new Thread(() -> {
            try {
                java.io.InputStream inputStream = SoundManager.class.getResourceAsStream(soundFile);
                if (inputStream == null) {
                    System.out.println("Sonido no encontrado: " + soundFile);
                    return;
                }
                
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(inputStream)
                );
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e) {
                System.out.println("Error reproduciendo sonido: " + soundFile);
                // No hacemos nada si no hay sonido
            }
        }).start();
    }
}