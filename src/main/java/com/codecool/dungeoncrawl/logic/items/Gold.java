package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Gold extends Item{
    public Gold(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "gold";
    }
}
