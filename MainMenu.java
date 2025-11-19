package com.chunkminecraft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel {
    private JFrame parentFrame;
    
    public MainMenu(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        createMenu();
    }
    
    private void createMenu() {
     
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
              
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(0, 0, 0);
                Color color2 = new Color(0, 50, 0);
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
               
                
          
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, getHeight()/4, 100));
        
    
        JButton startButton = createMenuButton("JUGAR");
        startButton.addActionListener(e -> {
            SoundManager.playClickSound();
            startGame();
        });
        
        // Botón Salir
        JButton exitButton = createMenuButton("SALIR");
        exitButton.addActionListener(e -> {
            SoundManager.playClickSound();
            System.exit(0);
        });
        
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
        
        mainPanel.add(buttonPanel);
        add(mainPanel, BorderLayout.CENTER);
    
        JLabel credits = new JLabel("by Aaron", SwingConstants.CENTER);
        credits.setForeground(Color.WHITE);
        credits.setFont(new Font("Arial", Font.ITALIC, 14));
        credits.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(credits, BorderLayout.SOUTH);
        
    
        JButton fullscreenButton = new JButton("⛶");
        fullscreenButton.setFont(new Font("Arial", Font.BOLD, 16));
        fullscreenButton.setBackground(new Color(60, 60, 60));
        fullscreenButton.setForeground(Color.WHITE);
        fullscreenButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        fullscreenButton.setFocusPainted(false);
        fullscreenButton.addActionListener(e -> toggleFullscreen());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(fullscreenButton);
        add(topPanel, BorderLayout.NORTH);
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
              
                if (getModel().isPressed()) {
                    g2.setColor(new Color(100, 100, 100));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(80, 80, 80));
                } else {
                    g2.setColor(new Color(60, 60, 60));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
              
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
                
                // Texto
                g2.setColor(Color.WHITE);
                int fontSize = Math.min(24, getWidth() / 15);
                g2.setFont(new Font("Arial", Font.BOLD, fontSize));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        
    
        button.setPreferredSize(new Dimension(400, 80));
        button.setMinimumSize(new Dimension(400, 80));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                button.repaint();
            }
        });
        
        return button;
    }
    
    private void startGame() {
        Main.showGame(parentFrame);
    }
    
    private void toggleFullscreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (parentFrame.isUndecorated()) {
         
            parentFrame.dispose();
            parentFrame.setUndecorated(false);
            parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            parentFrame.setVisible(true);
        } else {
            // Entrar en pantalla completa
            parentFrame.dispose();
            parentFrame.setUndecorated(true);
            gd.setFullScreenWindow(parentFrame);
        }
    }
}