package com.codecool.dungeoncrawl.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private Map<CellType, Integer> items = new HashMap<>();
    private List<Integer> positionsX = new ArrayList<>();
    private List<Integer> positionsY = new ArrayList<>();

    public Inventory() {};

    public void addItem(CellType item) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + 1);
        }
        else {
            items.put(item, 1);
        }
    }

    public void addPositionX(int x) { positionsX.add(x); }

    public void addPositionY(int y) { positionsY.add(y); }

    public Map<CellType, Integer> getInventory() {
        return items;
    }

    public void removeItem(CellType item) {
        items.remove(item);
    }

    public void clearInventory() {
        items.clear();
    }

    public List<Integer> getPositionsX() { return positionsX; }

    public List<Integer> getPositionsY() { return positionsY; }
}
