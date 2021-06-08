package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Scorpion extends Actor {
    public Scorpion(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "scorpion";
    }
}