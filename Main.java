package com.chunkminecraft;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Minecraft Chunk Generator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            
            // 🔥 CONFIGURAR PARA PANTALLA COMPLETA
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizada
            frame.setUndecorated(false); // Mantener bordes de ventana
            
            // Mostrar pantalla de inicio
            showMainMenu(frame);
            
            frame.setVisible(true);
        });
    }
    
    public static void showMainMenu(JFrame frame) {
        frame.getContentPane().removeAll();
        
        MainMenu mainMenu = new MainMenu(frame);
        frame.add(mainMenu, BorderLayout.CENTER);
        
        frame.revalidate();
        frame.repaint();
    }
    
    public static void showGame(JFrame frame) {
        frame.getContentPane().removeAll();
        
        ChunkPanel chunk = new ChunkPanel(10, 10, 20);
        
        frame.add(chunk.createControls(), BorderLayout.NORTH);
        frame.add(chunk, BorderLayout.CENTER);
        frame.add(createInfoPanel(), BorderLayout.SOUTH);
        
        frame.revalidate();
        frame.repaint();
    }
    
    private static JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        infoPanel.setBackground(new Color(240, 240, 240));
        
        JLabel infoLabel = new JLabel(
            "<html><center>" +
            "💡 <b>Consejos:</b> Haz clic en los bloques para minarlos • Usa la herramienta correcta para cada bloque • " +
            "Bedrock no se puede romper" +
            "</center></html>"
        );
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        infoLabel.setForeground(Color.DARK_GRAY);
        
        infoPanel.add(infoLabel);
        return infoPanel;
    }
}