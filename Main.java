package com.chunkminecraft;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chunk Minecraft");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            ChunkPanel chunk = new ChunkPanel(10, 10, 20);

            // 👉 Panel de controles arriba
            frame.add(chunk.createControls(), BorderLayout.NORTH);

            // 👉 Chunk en el centro
            frame.add(chunk, BorderLayout.CENTER);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
