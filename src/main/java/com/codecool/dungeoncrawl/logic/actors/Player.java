package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

import com.codecool.dungeoncrawl.logic.CellType;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Player extends Actor {

    @Expose
    private String name;

    public Player(Cell cell) {
        super(cell);
    }

    public Player(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTileName() {
        return "player";
    }

    public boolean alive() {
        if (getHealth() < 1) {
            return true;
        }

        return false;
    }

    public void move(int dx, int dy) {
        if (pickUpItems.contains(cell.getType()) && !forbiddenCells.contains(cell.getNeighbor(dx, dy).getType())) {
            Cell nextCell = cell.getNeighbor(dx, dy);
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
        else if (!forbiddenCells.contains(cell.getNeighbor(dx, dy).getType()) && !pickUpItems.contains(cell.getNeighbor(dx, dy).getType())) {
            Cell nextCell = cell.getNeighbor(dx, dy);
            cell.setActor(null);
            cell.setType(CellType.FLOOR);
            nextCell.setActor(this);
            cell = nextCell;
            cell.setType(CellType.PLAYER);
        }
        else if (pickUpItems.contains(cell.getNeighbor(dx, dy).getType())) {
            Cell nextCell = cell.getNeighbor(dx, dy);
            cell.setActor(null);
            cell.setType(CellType.FLOOR);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "cell=" + cell +
                ", forbiddenCells=" + forbiddenCells +
                ", pickUpItems=" + pickUpItems +
                ", name='" + name + '\'' +
                '}';
    }
}
