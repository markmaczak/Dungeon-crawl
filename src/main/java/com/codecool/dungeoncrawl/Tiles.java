package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();
    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        //Field
        tileMap.put("empty", new Tile(5, 0));
        tileMap.put("wall", new Tile(6, 13));
        tileMap.put("wall2", new Tile(6, 15));
        tileMap.put("wall3", new Tile(6, 12));
        tileMap.put("wall4", new Tile(5, 12));
        tileMap.put("floor", new Tile(2, 0));
        tileMap.put("house", new Tile(0,19));
        tileMap.put("tree", new Tile(4,2));
        tileMap.put("openDoor", new Tile(2,9));
        tileMap.put("closeDoor", new Tile(0,9));
        tileMap.put("truck", new Tile(12, 20));
        tileMap.put("truck2", new Tile(13, 20));
        tileMap.put("gold", new Tile(9,26));
        //Actors
        tileMap.put("player", new Tile(18, 7));
        tileMap.put("skeleton", new Tile(29, 6));
        tileMap.put("scorpion", new Tile(24, 5));
        tileMap.put("giant", new Tile(30,6));
        //Items
        tileMap.put("sword", new Tile(0, 30));
        tileMap.put("key", new Tile(16, 23));
        tileMap.put("uzi", new Tile(9,31));
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }
}
