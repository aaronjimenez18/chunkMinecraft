package com.chunkminecraft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.awt.image.BufferedImage;

public class ChunkPanel extends JPanel {

    private final int width, depth, height;

    private Block[][][] world;
    private int[][] topY;
    private Tool selectedTool = Tool.HAND;

    private final ImageIcon bedrockIcon, stoneIcon, dirtIcon, woodIcon, airIcon;
    private final ImageIcon handIcon, pickaxeIcon, shovelIcon, axeIcon;

    private JPanel gridPanel;

    public ChunkPanel(int width, int depth, int height) {
        this.width = width;
        this.depth = depth;
        this.height = height;

        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        gridPanel = new JPanel(new GridLayout(depth, width, 2, 2));
        gridPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                "Mundo Minecraft - Chunks",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(70, 70, 70)
            )
        ));
        gridPanel.setBackground(new Color(220, 220, 220));
        add(gridPanel, BorderLayout.CENTER);

      
        bedrockIcon = load("/textures/bedrock.JPG");
        stoneIcon = load("/textures/stone.png");
        dirtIcon = load("/textures/dirt.png");
        woodIcon = load("/textures/wood.png");
        airIcon = load("/textures/air.png");

     
        handIcon = loadToolIcon("/textures/hand.png");
        pickaxeIcon = loadToolIcon("/textures/pickaxe.png");
        shovelIcon = loadToolIcon("/textures/shovel.png");
        axeIcon = loadToolIcon("/textures/axe.png");

   
        SoundManager.playBackgroundMusic();
        
        regenerate();
    }

    public JPanel createControls() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createTitledBorder("Herramientas")
        ));
        p.setBackground(new Color(245, 245, 245));

        JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolsPanel.setBackground(new Color(230, 230, 230));
        toolsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton bHand = createToolButton(handIcon, "Mano - No rompe bloques", Tool.HAND);
        JButton bPick = createToolButton(pickaxeIcon, "Pico - Rompe piedra", Tool.PICKAXE);
        JButton bShovel = createToolButton(shovelIcon, "Pala - Rompe tierra", Tool.SHOVEL);
        JButton bAxe = createToolButton(axeIcon, "Hacha - Rompe madera", Tool.AXE);

        toolsPanel.add(new JLabel("Herramientas: "));
        toolsPanel.add(bHand);
        toolsPanel.add(bPick);
        toolsPanel.add(bShovel);
        toolsPanel.add(bAxe);

        // Botón Regenerar con sonido
        JButton bReg = new JButton("Regenerar Mundo");
        bReg.setFont(new Font("Arial", Font.BOLD, 12));
        bReg.setBackground(new Color(70, 130, 180));
        bReg.setForeground(Color.WHITE);
        bReg.setFocusPainted(false);
        bReg.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        bReg.addActionListener(e -> {
            SoundManager.playClickSound(); 
            regenerate();
            refreshGrid();
        });

       
        JButton bMenu = new JButton("Menú Principal");
        bMenu.setFont(new Font("Arial", Font.BOLD, 12));
        bMenu.setBackground(new Color(180, 70, 70));
        bMenu.setForeground(Color.WHITE);
        bMenu.setFocusPainted(false);
        bMenu.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        bMenu.addActionListener(e -> {
            SoundManager.playClickSound();
            volverAlMenu();
        });

        JLabel toolLabel = new JLabel(" Herramienta actual: MANO");
        toolLabel.setName("toolLabel");
        toolLabel.setFont(new Font("Arial", Font.BOLD, 12));
        toolLabel.setForeground(new Color(0, 100, 0));
        toolLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        p.add(toolsPanel);
        p.add(bReg);
        p.add(bMenu);
        p.add(toolLabel);

        return p;
    }

    private JButton createToolButton(ImageIcon icon, String tooltip, Tool tool) {
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(60, 60));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        button.setFocusPainted(false);
        
        button.addActionListener(e -> {
            SoundManager.playClickSound(); 
            setTool(tool);
        });
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 240, 255));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 100, 200), 2),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)
                ));
            }
            
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
        });
        
        return button;
    }

    private void setTool(Tool t) {
        selectedTool = t;
        updateToolLabel();
        
        
        switch (t) {
            case PICKAXE:
                SoundManager.playToolSwitchSound();
                break;
            case SHOVEL:
                SoundManager.playToolSwitchSound();
                break;
            case AXE:
                SoundManager.playToolSwitchSound();
                break;
            default:
                SoundManager.playClickSound();
        }
    }

    private void updateToolLabel() {
        Component[] comps = ((JPanel)getParent().getComponent(0)).getComponents();
        for (Component c : comps) {
            if (c.getName() != null && c.getName().equals("toolLabel")) {
                String toolName = "";
                switch (selectedTool) {
                    case HAND: toolName = "MANO"; break;
                    case PICKAXE: toolName = "PICO"; break;
                    case SHOVEL: toolName = "PALA"; break;
                    case AXE: toolName = "HACHA"; break;
                }
                ((JLabel)c).setText(" Herramienta actual: " + toolName);
                break;
            }
        }
    }

    private ImageIcon load(String path) {
        URL u = getClass().getResource(path);
        if (u != null) return new ImageIcon(u);

        // Fallback si no encuentra imagen
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, 64, 64);
        g.setColor(Color.BLACK);
        g.drawString("?", 25, 35);
        g.dispose();
        return new ImageIcon(img);
    }

    private ImageIcon loadToolIcon(String path) {
        URL u = getClass().getResource(path);
        if (u != null) {
            ImageIcon originalIcon = new ImageIcon(u);
            Image scaledImage = originalIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }

     
        BufferedImage img = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 48, 48);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(0, 0, 47, 47);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        
        String text = "TOOL";
        if (path.contains("hand")) text = "MANO";
        else if (path.contains("pickaxe")) text = "PICO";
        else if (path.contains("shovel")) text = "PALA";
        else if (path.contains("axe")) text = "HACHA";
        
        g.drawString(text, 10, 24);
        g.dispose();
        
        return new ImageIcon(img);
    }

    public void regenerate() {
        WorldGenerator gen = new WorldGenerator(width, depth, height);
        this.world = gen.getWorld();
        this.topY = gen.getTopY();
        refreshGrid();
        
        // Sonido al regenerar mundo
        SoundManager.playWorldRegenSound();
    }

    private void refreshGrid() {
        gridPanel.removeAll();

        // Calcular tamaño responsive
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int cellSize = Math.min(100, Math.max(60, screenSize.width / 20));

        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                int yTop = topY[x][z];
                Block b = (yTop >= 1 ? world[x][z][yTop] : Block.AIR);
                JPanel cell = createCell(x, z, yTop, b, cellSize);
                gridPanel.add(cell);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createCell(int x, int z, int yTop, Block block, int cellSize) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setPreferredSize(new Dimension(cellSize, cellSize));
        cell.setBackground(getCellBackgroundColor(yTop));
        
        cell.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel icon = new JLabel(getIconFor(block));
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setVerticalAlignment(SwingConstants.CENTER);

        JLabel yLabel = new JLabel(
            yTop <= 0 ? "AIR" : "Y:" + yTop,
            SwingConstants.CENTER
        );
        yLabel.setFont(new Font("Arial", Font.BOLD, 11));
        yLabel.setForeground(yTop <= 0 ? Color.GRAY : Color.BLACK);
        yLabel.setOpaque(true);
        yLabel.setBackground(new Color(255, 255, 255, 200));
        yLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        cell.setToolTipText(createTooltipText(x, z, yTop, block));

        cell.add(icon, BorderLayout.CENTER);
        cell.add(yLabel, BorderLayout.SOUTH);

        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cell.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 100, 200), 2),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)
                ));
                SoundManager.playHoverSound(); 
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                cell.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                attemptBreak(x, z);
            }
        });

        return cell;
    }

    private Color getCellBackgroundColor(int yTop) {
        if (yTop <= 0) return new Color(180, 210, 255); // Azul claro para aire
        if (yTop <= 2) return new Color(100, 100, 100); // Gris oscuro para bedrock
        if (yTop <= 6) return new Color(150, 150, 150); // Gris para piedra
        if (yTop <= 10) return new Color(160, 130, 100); // Marrón para tierra
        return new Color(200, 180, 150); // Marrón claro para capas superiores
    }

    private String createTooltipText(int x, int z, int yTop, Block block) {
        return String.format(
            "<html><b>Coordenadas:</b> X:%d, Z:%d<br><b>Altura:</b> %d<br><b>Bloque:</b> %s<br><b>Herramienta necesaria:</b> %s</html>",
            x, z, yTop, getBlockName(block), getRequiredTool(block)
        );
    }

    private String getBlockName(Block block) {
        switch (block) {
            case BEDROCK: return "Bedrock";
            case STONE: return "Piedra";
            case DIRT: return "Tierra";
            case WOOD: return "Madera";
            case AIR: return "Aire";
            default: return "Desconocido";
        }
    }

    private String getRequiredTool(Block block) {
        switch (block) {
            case BEDROCK: return "Ninguna (no se puede romper)";
            case STONE: return "Pico";
            case DIRT: return "Pala";
            case WOOD: return "Hacha";
            case AIR: return "Ninguna";
            default: return "Desconocida";
        }
    }

    private ImageIcon getIconFor(Block b) {
        switch (b) {
            case BEDROCK: return bedrockIcon;
            case STONE:   return stoneIcon;
            case DIRT:    return dirtIcon;
            case WOOD:    return woodIcon;
            case AIR:
            default:      return airIcon;
        }
    }

    private void attemptBreak(int x, int z) {
        int yTop = topY[x][z];

        if (yTop <= 0) {
            JOptionPane.showMessageDialog(this, "No hay bloque (aire)");
            SoundManager.playErrorSound();
            return;
        }

        Block b = world[x][z][yTop];
        boolean breakable = false;
        String reason = "";

        if (b == Block.BEDROCK) {
            reason = "Bedrock no se rompe";
            SoundManager.playErrorSound();
        }
        else if (b == Block.STONE && selectedTool == Tool.PICKAXE) breakable = true;
        else if (b == Block.STONE) {
            reason = "Necesitas pico";
            SoundManager.playWrongToolSound();
        }
        else if (b == Block.DIRT && selectedTool == Tool.SHOVEL) breakable = true;
        else if (b == Block.DIRT) {
            reason = "Necesitas pala";
            SoundManager.playWrongToolSound();
        }
        else if (b == Block.WOOD && selectedTool == Tool.AXE) breakable = true;
        else if (b == Block.WOOD) {
            reason = "Necesitas hacha";
            SoundManager.playWrongToolSound();
        }

        if (!breakable) {
            if (!reason.isEmpty()) {
                JOptionPane.showMessageDialog(this, reason);
            }
            return;
        }

        // 🔇 SONIDO DE ROMPER BLOQUE
        SoundManager.playBreakSound();
        
        world[x][z][yTop] = Block.AIR;

        int newTop = 0;
        for (int y = yTop - 1; y >= 1; y--) {
            if (world[x][z][y] != Block.AIR) {
                newTop = y;
                break;
            }
        }

        topY[x][z] = newTop;
        refreshGrid();
    }

    private void volverAlMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            SoundManager.playClickSound();
            Main.showMainMenu(frame);
        }
    }
}