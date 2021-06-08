package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Uzi extends Item {

    public Uzi(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "uzi";
    }
}
