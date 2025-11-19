package com.chunkminecraft;

import java.util.Random;

public class WorldGenerator {
    private final int width, depth, height;
    private final Block[][][] world;
    private final int[][] topY;
    private final Random random = new Random();

    public WorldGenerator(int width, int depth, int height){
        this.width = width; 
        this.depth = depth; 
        this.height = height;
        world = new Block[width][depth][height + 1];
        topY = new int[width][depth];
        generate();
    }

    public void generate(){
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                // Inicializar todo con aire
                for (int y = 1; y <= height; y++) {
                    world[x][z][y] = Block.AIR;
                }
                
                // ALTURA VARIABLE por columna (entre 6 y 16 bloques)
                int columnHeight = 6 + random.nextInt(11); // 6-16 bloques de altura
                
                // GENERAR ESTRATOS SEGÚN REGLAS ORIGINALES PERO CON ALTURA VARIABLE
                
                // Capa 1: Bedrock exclusivo
                world[x][z][1] = Block.BEDROCK;
                
                // Capa 2: Transición Bedrock/Piedra
                world[x][z][2] = (random.nextDouble() < 0.7) ? Block.STONE : Block.BEDROCK;
                
                // Capas de piedra (3-6) - SIEMPRE 4 capas de piedra
                for (int y = 3; y <= 6; y++) {
                    if (y <= columnHeight) {
                        world[x][z][y] = Block.STONE;
                    }
                }
                
                // Capas de tierra (7-10) - SIEMPRE 4 capas de tierra
                for (int y = 7; y <= 10; y++) {
                    if (y <= columnHeight) {
                        world[x][z][y] = Block.DIRT;
                    }
                }
                
                // Capas de mezcla (11-14) - SIEMPRE 4 capas de mezcla
                for (int y = 11; y <= 14; y++) {
                    if (y <= columnHeight) {
                        double r = random.nextDouble();
                        if (r < 0.4) {
                            world[x][z][y] = Block.STONE;
                        } else if (r < 0.8) {
                            world[x][z][y] = Block.DIRT;
                        } else {
                            world[x][z][y] = Block.WOOD;
                        }
                    }
                }
                
                // Si la columna es más alta que 14, agregar más bloques aleatorios
                for (int y = 15; y <= columnHeight; y++) {
                    double r = random.nextDouble();
                    if (r < 0.3) {
                        world[x][z][y] = Block.STONE;
                    } else if (r < 0.6) {
                        world[x][z][y] = Block.DIRT;
                    } else if (r < 0.9) {
                        world[x][z][y] = Block.WOOD;
                    }
                    // else se queda como AIR (10% de chance de aire)
                }
                
                // Calcular topY
                int top = 0;
                for (int y = height; y >= 1; y--) {
                    if (world[x][z][y] != Block.AIR) { 
                        top = y; 
                        break; 
                    }
                }
                topY[x][z] = top;
            }
        }
    }

    public Block[][][] getWorld(){ return world; }
    public int[][] getTopY(){ return topY; }
}