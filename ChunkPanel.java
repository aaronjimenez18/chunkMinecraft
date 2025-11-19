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

    private JPanel gridPanel; // <- grid separado

    public ChunkPanel(int width, int depth, int height) {
        this.width = width;
        this.depth = depth;
        this.height = height;

        setLayout(new BorderLayout());

        // Panel del grid
        gridPanel = new JPanel(new GridLayout(depth, width, 4, 4));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(gridPanel, BorderLayout.CENTER);

        // Cargar texturas
        bedrockIcon = load("/textures/" + Block.BEDROCK.getFilename());
        stoneIcon = load("/textures/" + Block.STONE.getFilename());
        dirtIcon = load("/textures/" + Block.DIRT.getFilename());
        woodIcon = load("/textures/" + Block.WOOD.getFilename());
        airIcon = load("/textures/" + Block.AIR.getFilename());

        regenerate();
    }

    // 👉 Ahora es PUBLICO para Main.java
    public JPanel createControls() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));

        p.add(new JLabel("Herramienta: "));

        JButton bHand = new JButton("Mano");
        bHand.addActionListener(e -> setTool(Tool.HAND));
        p.add(bHand);

        JButton bPick = new JButton("Pico");
        bPick.addActionListener(e -> setTool(Tool.PICKAXE));
        p.add(bPick);

        JButton bShovel = new JButton("Pala");
        bShovel.addActionListener(e -> setTool(Tool.SHOVEL));
        p.add(bShovel);

        JButton bAxe = new JButton("Hacha");
        bAxe.addActionListener(e -> setTool(Tool.AXE));
        p.add(bAxe);

        JButton bReg = new JButton("Regenerar");
        bReg.addActionListener(e -> {
            regenerate();
            refreshGrid();
        });
        p.add(bReg);

        JLabel toolLabel = new JLabel(" [HAND]");
        toolLabel.setName("toolLabel");
        p.add(toolLabel);

        return p;
    }

    private void setTool(Tool t) {
        selectedTool = t;
    }

    private ImageIcon load(String path) {
        URL u = getClass().getResource(path);
        if (u != null) return new ImageIcon(u);

        // Fallback si no encuentra imagen
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, 64, 64);
        g.dispose();
        return new ImageIcon(img);
    }

    public void regenerate() {
        WorldGenerator gen = new WorldGenerator(width, depth, height);
        this.world = gen.getWorld();
        this.topY = gen.getTopY();
        refreshGrid();
    }

    private void refreshGrid() {
        gridPanel.removeAll();

        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {

                int yTop = topY[x][z];
                Block b = (yTop >= 1 ? world[x][z][yTop] : Block.AIR);

                JPanel cell = new JPanel(new BorderLayout());
                cell.setPreferredSize(new Dimension(72, 72));

                JLabel icon = new JLabel(getIconFor(b));
                icon.setHorizontalAlignment(SwingConstants.CENTER);

                JLabel yLabel = new JLabel(
                        yTop <= 0 ? "air" : String.valueOf(yTop),
                        SwingConstants.CENTER
                );
                yLabel.setFont(yLabel.getFont().deriveFont(Font.BOLD, 12f));

                cell.add(icon, BorderLayout.CENTER);
                cell.add(yLabel, BorderLayout.SOUTH);
                cell.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                int cx = x, cz = z;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        attemptBreak(cx, cz);
                    }
                });

                gridPanel.add(cell);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
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
            JOptionPane.showMessageDialog(this, "No hay bloque (aire).");
            return;
        }

        Block b = world[x][z][yTop];
        boolean breakable = false;
        String reason = "";

        if (b == Block.BEDROCK) reason = "Bedrock no se rompe.";
        else if (b == Block.STONE && selectedTool == Tool.PICKAXE) breakable = true;
        else if (b == Block.STONE) reason = "Necesitas Pico.";
        else if (b == Block.DIRT && selectedTool == Tool.SHOVEL) breakable = true;
        else if (b == Block.DIRT) reason = "Necesitas Pala.";
        else if (b == Block.WOOD && selectedTool == Tool.AXE) breakable = true;
        else if (b == Block.WOOD) reason = "Necesitas Hacha.";

        if (!breakable) {
            JOptionPane.showMessageDialog(this, reason);
            return;
        }

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
}
