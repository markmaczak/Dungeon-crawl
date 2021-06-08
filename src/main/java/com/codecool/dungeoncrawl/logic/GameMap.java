package com.codecool.dungeoncrawl.logic;


import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.Gold;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.logic.items.Uzi;
import java.util.ArrayList;
import java.util.List;


public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;

    //Lists
    private List<Skeleton> skeletons = new ArrayList<>();
    private List<Scorpion> scorpions = new ArrayList<>();
    private List<Giant> giants = new ArrayList<>();
    private List<Sword> swords = new ArrayList<>();
    private List<Uzi> uzis = new ArrayList<>();
    private List<Key> keys = new ArrayList<>();
    private List<Gold> golds = new ArrayList<>();

    //Actors
    private Player player;
    private Skeleton skeleton;
    private Giant giant;
    private Scorpion scorpion;

    //Items
    private Sword sword;
    private Uzi uzi;
    private Key key;
    private Gold gold;


    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSkeleton(Skeleton skeleton) { this.skeleton = skeleton; }

    public void setGiant(Giant giant) { this.giant = giant; }

    public void setScorpion(Scorpion scorpion) { this.scorpion = scorpion; }

    public List<Skeleton> getSkeletons() { return skeletons; }

    public void addSkeleton(Skeleton skeleton) { skeletons.add(skeleton); }

    public List<Scorpion> getScorpions() { return scorpions; }

    public void addScorpion(Scorpion scorpion) { scorpions.add(scorpion); }

    public List<Giant> getGiants() { return giants; }

    public void addGiant(Giant giant) { giants.add(giant); }

    public void setSword(Sword sword) { this.sword = sword; }

    public void setUzi(Uzi uzi) { this.uzi = uzi; }

    public void setKey(Key key) { this.key = key; }

    public void setGold(Gold gold) { this.gold = gold; }

    public void addSword (Sword sword) { swords.add(sword); }

    public void addUzi (Uzi uzi) { uzis.add(uzi); }

    public void addKey (Key key) { keys.add(key); }

    public void addGold (Gold gold) { golds.add(gold); }

}
