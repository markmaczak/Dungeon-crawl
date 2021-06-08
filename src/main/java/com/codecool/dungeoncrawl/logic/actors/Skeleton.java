package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import java.util.Random;


public class Skeleton extends Actor {

    private Random rand = new Random();
    private final int min = 1;
    private final int maxX = 11;
    private final int maxY = 7;

    public Skeleton(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }


    public void move() {
        if (getAlive()) {
            Cell cell = getCell();
            int x = rand.nextInt((maxX - min) + 1) + min;
            int y = rand.nextInt((maxY - min) + 1) + min;

            Cell nextCell = cell.newCell(x, y);

            if (!getForbiddenCells().contains(nextCell.getType()) && !getPickUpItems().contains(nextCell.getType())) {
                cell.setActor(null);
                cell.setType(CellType.FLOOR);
                nextCell.setActor(this);
                cell = nextCell;
                setCell(cell);
            }
        }
    }
}
