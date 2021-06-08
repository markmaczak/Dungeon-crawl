package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

import java.util.Random;

public class Giant extends Actor {

    private Random rand = new Random();
    private int[] coordinatesX = {-1, 1};

    public Giant(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "giant";
    }

    public void move() {
        if (getAlive()) {
            Cell cell = getCell();

            int x = rand.nextInt(2);

            if (!getForbiddenCells().contains(cell.getNeighbor(coordinatesX[x] ,0).getType()) && !getPickUpItems().contains(cell.getNeighbor(coordinatesX[x],0).getType())) {
                Cell nextCell = cell.getNeighbor(coordinatesX[x], 0);
                cell.setActor(null);
                cell.setType(CellType.FLOOR);
                nextCell.setActor(this);
                cell = nextCell;
                setCell(cell);
            }
        }
    }
}