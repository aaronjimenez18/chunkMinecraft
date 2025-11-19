package com.chunkminecraft;

import java.util.Random;

public class WorldGenerator {
    private final int width, depth, height;
    private final Block[][][] world;
    private final int[][] topY;
    private final Random random = new Random();

    public WorldGenerator(int width, int depth, int height){
        this.width = width; this.depth = depth; this.height = height;
        world = new Block[width][depth][height+1];
        topY = new int[width][depth];
        generate();
    }

    public void generate(){
        for (int x=0;x<width;x++){
            for (int z=0;z<depth;z++){
                for (int y=1;y<=height;y++) world[x][z][y] = Block.AIR;
                int heightTop = 2 + random.nextInt(13); // 2..14
                if (random.nextDouble() < 0.25) heightTop = Math.max(2, Math.min(14, heightTop + random.nextInt(3)-1));
                for (int y=1;y<=heightTop;y++){
                    if (y==1) world[x][z][y] = Block.BEDROCK;
                    else if (y==2) world[x][z][y] = (random.nextDouble() < 0.07) ? Block.BEDROCK : Block.STONE;
                    else if (y>=3 && y<=6) world[x][z][y] = Block.STONE;
                    else if (y>=7 && y<=10) world[x][z][y] = Block.DIRT;
                    else if (y>=11 && y<=14){
                        double r = random.nextDouble();
                        if (r < 0.45) world[x][z][y] = Block.STONE;
                        else if (r < 0.85) world[x][z][y] = Block.DIRT;
                        else world[x][z][y] = Block.WOOD;
                    } else world[x][z][y] = Block.AIR;
                }
                int top = 0;
                for (int y=height;y>=1;y--) if (world[x][z][y] != Block.AIR){ top = y; break; }
                topY[x][z] = top;
            }
        }
    }

    public Block[][][] getWorld(){ return world; }
    public int[][] getTopY(){ return topY; }
}