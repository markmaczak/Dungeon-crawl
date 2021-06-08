package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Inventory;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.ItemPositionsModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private InventoryDao inventoryDao;
    private ItemPositionsDao itemPositionsDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
        inventoryDao = new InventoryDaoJdbc(dataSource);
        itemPositionsDao = new ItemPositionsDaoJdbc(dataSource);
    }

    public void save(Player player, String currentMap, int year, int month, int day, Inventory inventory) {
        PlayerModel model = new PlayerModel(player);
        playerDao.add(model);
      
        Date date = new Date(year, month, day);
        GameState gameState = new GameState(currentMap, date, model.getId());
      
        gameStateDao.add(gameState);

        List<String> items = new ArrayList<>();
        for (CellType item: inventory.getInventory().keySet()) {
            for (int i = 0; i < inventory.getInventory().get(item); i++) {
                items.add(item.getTileName());
            }
        }

        InventoryModel inventoryModel = new InventoryModel(items.toArray(new String[0]), model.getId());
        inventoryDao.add(inventoryModel);

        ItemPositionsModel itemPositionsModel = new ItemPositionsModel(inventory.getPositionsX().toArray(new Integer[0]), inventory.getPositionsY().toArray(new Integer[0]), model.getId());
        itemPositionsDao.add(itemPositionsModel);
    }

    public ItemPositionsModel generateItemPositionsModel(Inventory inventory, int playerId) {
        ItemPositionsModel itemPositionsModel = new ItemPositionsModel(inventory.getPositionsX().toArray(new Integer[0]), inventory.getPositionsY().toArray(new Integer[0]), playerId);

        return itemPositionsModel;
    }

    public InventoryModel generateInventoryModel(Inventory inventory, int playerId) {
        List<String> items = new ArrayList<>();

        for (CellType item: inventory.getInventory().keySet()) {
            for (int i = 0; i < inventory.getInventory().get(item); i++) {
                items.add(item.getTileName());
            }
        }

        InventoryModel inventoryModel = new InventoryModel(items.toArray(new String[0]), playerId);

        return inventoryModel;
    }

    public GameState generateGameState(int year, int month, int day, String currentMap, int playerId) {
        Date date = new Date(year, month, day);
        GameState gameState = new GameState(currentMap, date, playerId);
        return gameState;
    }

    public void updateGame(int playerId, Player player, String currentMap, int year, int month, int day, Inventory inventory) {
        PlayerModel model = new PlayerModel(player);
        model.setId(playerId);
        playerDao.update(model);

        Date date = new Date(year, month, day);
        GameState gameState = new GameState(currentMap, date, playerId);
        gameState.setId(playerId);
        gameStateDao.update(gameState);

        List<String> items = new ArrayList<>();
        for (CellType item: inventory.getInventory().keySet()) {
            for (int i = 0; i < inventory.getInventory().get(item); i++) {
                items.add(item.getTileName());
            }
        }

        InventoryModel inventoryModel = new InventoryModel(items.toArray(new String[0]), model.getId());
        inventoryDao.update(inventoryModel);

        ItemPositionsModel itemPositionsModel = new ItemPositionsModel(inventory.getPositionsX().toArray(new Integer[0]), inventory.getPositionsY().toArray(new Integer[0]), model.getId());
        itemPositionsDao.add(itemPositionsModel);
    }

    public List<PlayerModel> getAllPlayerModels() {
        return playerDao.getAll();

    }

    public PlayerModel getPlayerModel(int id) {
        return playerDao.get(id);
    }

    public List<GameState> getAllGameStates() {
        return gameStateDao.getAll();
    }

    public GameState getGameState(int id) {
        return gameStateDao.get(id);
    }

    public List<InventoryModel> getAllInvetonyModels() {
        return inventoryDao.getAll();
    }

    public List<InventoryModel> getInventoryModel(int id) { return inventoryDao.get(id); }

    public ItemPositionsModel getItemPositionsModel(int id) { return itemPositionsDao.get(id); }

    public List<ItemPositionsModel> getAllItemPositionsModel() { return itemPositionsDao.getAll(); }

    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("DATABASE");
        String user = System.getenv("USER");
        String password = System.getenv("PASSWORD");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
}
