package com.chunkminecraft;

import javax.swing.*;
import java.awt.*;

public class ChunkFrame extends JFrame {
    public ChunkFrame() {
        super("Chunk Minecraft - Cartoon Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        ChunkPanel panel = new ChunkPanel(10,10,20);
        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }
}