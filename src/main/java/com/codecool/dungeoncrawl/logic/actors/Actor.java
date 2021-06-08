package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;
import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.List;

public abstract class Actor implements Drawable {
    protected Cell cell;
    private int health = 15;
    private boolean alive = true;
    protected List<CellType> forbiddenCells = Arrays.asList(CellType.WALL, CellType.WALL2, CellType.WALL3, CellType.WALL4,
            CellType.HOUSE, CellType.CLOSEDOOR, CellType.SKELETON, CellType.SCORPION, CellType.GIANT, CellType.PLAYER);
    protected List<CellType> pickUpItems = Arrays.asList(CellType.KEY, CellType.SWORD, CellType.UZI, CellType.GOLD);

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;
    }

    public void attack(Cell monsterCell, int damageNumber, Player player) {
        if ((monsterCell.getNeighbor(1,0).getType().equals(CellType.PLAYER) ||
                monsterCell.getNeighbor(0,1).getType().equals(CellType.PLAYER) ||
                monsterCell.getNeighbor(-1,0).getType().equals(CellType.PLAYER)  ||
                monsterCell.getNeighbor(0,-1).getType().equals(CellType.PLAYER)) && getAlive())  {
            player.setHealth(player.getHealth() - damageNumber);

            monsterCell.setActor(null);
            monsterCell.setType(CellType.FLOOR);

            setAlive(false);
        }
    };

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public void setX(int x) { cell.setX(x); }

    public void setY(int y) { cell.setY(y); }

    public List<CellType> getForbiddenCells() {
        return forbiddenCells;
    }

    public List<CellType> getPickUpItems() {
        return pickUpItems;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean getAlive() {
        return this.alive;
    }

}
