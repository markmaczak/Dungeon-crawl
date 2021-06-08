package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.actors.Giant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MonsterTest {

    GameMap gameMap;

    @BeforeEach
    void initMap() {
        gameMap = new GameMap(5, 5, CellType.FLOOR);
    }

    @Test
    void giantMovesRandomlyInOneRow() {
        Giant giant = new Giant(gameMap.getCell(2, 1));
        giant.move();

        assertEquals(1, giant.getX(), 3);
    }

    @Test
    void giantCannotMoveIntoWall() {
        gameMap.getCell(0, 1).setType(CellType.WALL);
        Giant giant = new Giant(gameMap.getCell(1, 1));
        giant.move();

        assertEquals(2, giant.getX(), 1);
    }

    @Test
    void skeletonMovesToNewRandomField() {
        gameMap = new GameMap(12, 8, CellType.FLOOR);
        Skeleton skeleton = new Skeleton(gameMap.getCell(1,1));
        skeleton.move();

        assertEquals("floor", gameMap.getCell(1,1).getTileName());
    }

}
