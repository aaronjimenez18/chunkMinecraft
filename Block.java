package com.chunkminecraft;

public enum Block {
    BEDROCK("bedrock.JPG"),
    STONE("stone.png"),
    DIRT("dirt.png"),
    WOOD("wood.png"),
    AIR("air.png");

    private final String filename;
    Block(String filename){ this.filename = filename; }
    public String getFilename(){ return filename; }
}