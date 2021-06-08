package com.codecool.dungeoncrawl.logic;


import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import java.io.InputStream;
import java.util.Scanner;
import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.Gold;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.logic.items.Uzi;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap(String textFile) {
        InputStream is = MapLoader.class.getResourceAsStream(textFile);

        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);

        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '$':
                            cell.setType(CellType.WALL2);
                            break;
                        case 'ß':
                            cell.setType(CellType.WALL3);
                            break;
                        case '4':
                            cell.setType(CellType.WALL4);
                            break;
                        case 'h':
                            cell.setType(CellType.HOUSE);
                            break;
                        case 't':
                            cell.setType(CellType.TREE);
                            break;
                        case '8':
                            cell.setType(CellType.TRUCK);
                            break;
                        case '9':
                            cell.setType(CellType.TRUCK2);
                            break;
                        case 'O':
                            cell.setType(CellType.OPENDOOR);
                            break;
                        case 'C':
                            cell.setType(CellType.CLOSEDOOR);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.SKELETON);
                            Skeleton skeleton = new Skeleton(cell);
                            skeleton.setHealth(5);
                            map.setSkeleton(skeleton);
                            map.addSkeleton(skeleton);
                            break;
                        case 'k':
                            cell.setType(CellType.KEY);
                            Key key = new Key(cell);
                            map.setKey(key);
                            map.addKey(key);
                            break;
                        case 'w':
                            cell.setType(CellType.SWORD);
                            Sword sword = new Sword(cell);
                            map.setSword(sword);
                            map.addSword(sword);
                            break;
                        case 'u':
                            cell.setType(CellType.UZI);
                            Uzi uzi = new Uzi(cell);
                            map.setUzi(uzi);
                            map.addUzi(uzi);
                            break;
                        case '€':
                            cell.setType(CellType.GOLD);
                            Gold gold = new Gold(cell);
                            map.setGold(gold);
                            map.addGold(gold);
                            break;
                        case 'p':
                            cell.setType(CellType.SCORPION);
                            Scorpion scorpion = new Scorpion(cell);
                            scorpion.setHealth(3);
                            map.setScorpion(scorpion);
                            map.addScorpion(scorpion);
                            break;
                        case 'g':
                            cell.setType(CellType.GIANT);
                            Giant giant = new Giant(cell);
                            giant.setHealth(7);
                            map.setGiant(giant);
                            map.addGiant(giant);
                            break;
                        case '@':
                            cell.setType(CellType.PLAYER);
                            Player player = new Player(cell);
                            map.setPlayer(player);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
