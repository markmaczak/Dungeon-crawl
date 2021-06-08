package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.ItemPositionsModel;
import com.google.gson.Gson;
import javafx.application.Application;
import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Giant;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Scorpion;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Main extends Application {
    String mapText = "/map.txt";
    GameMap map = MapLoader.loadMap(mapText);

    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Random rand = new Random();

    GameDatabaseManager dbManager;

    Label inventoryLabel = new Label();
    Inventory inventory = new Inventory();
    Stage primary;
    List<Scorpion> scorpions = map.getScorpions();
    List<Skeleton> skeletons = map.getSkeletons();
    List<Giant> giants = map.getGiants();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();

        GridPane ui = new GridPane();
        ui.setPrefWidth(280);
        ui.setPadding(new Insets(20));

        primary = primaryStage;
        ui.setVgap(10.0);
        ui.setHgap(1.0);

        ui.setStyle("-fx-background-color: #b73636; -fx-font-weight: bold; -fx-font-family: Arial");


        // Serialize
        Button importButton = new Button("Import");
        importButton.setFocusTraversable(false);
        importButton.setStyle("-fx-background-color: #6b323b; -fx-text-fill: white");
        Button exportButton = new Button("Export");
        exportButton.setFocusTraversable(false);
        exportButton.setStyle("-fx-background-color: #6b323b; -fx-text-fill: white");
        ui.add(importButton, 0, 0);
        ui.add(exportButton, 1, 0);

        importButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Gson gson = new Gson();
                Date date = new Date();

                PlayerModel playerModel = new PlayerModel(map.getPlayer());
                GameState gameState = dbManager.generateGameState(date.getYear(), date.getMonth(), date.getDay(), mapText, rand.nextInt(100));
                InventoryModel inventoryModel = dbManager.generateInventoryModel(inventory, rand.nextInt(100));
                ItemPositionsModel itemPositionsModel = dbManager.generateItemPositionsModel(inventory, rand.nextInt(100));
                
                try {
                    File myObj = new File("game.json");
                    if (myObj.createNewFile()) {
                        FileWriter myWriter = new FileWriter(myObj.getPath());
                        myWriter.write("[" + "\n" + gson.toJson(playerModel) + "," + "\n" + gson.toJson(gameState) + "," + "\n" + gson.toJson(inventoryModel) + "," + "\n" + gson.toJson(itemPositionsModel) + "\n" + "]");
                        myWriter.close();
                    }
                    else {
                      System.out.println("File already exists.");
                    }
                  }
                catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                  }
            }
        });

        exportButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("export button pressed");
                // create object with gameState
                // export object to a new file

            }
        });

        // 1. Header
        ui.add(new Label("Save game: press 'S'"), 0, 2);

        // 2. For save game press 's' + load game
        Button loadButton = new Button("Load");
        loadButton.setFocusTraversable(false);
        loadButton.setStyle("-fx-background-color: #6b323b; -fx-text-fill: white");
        ui.add(loadButton, 0, 3);

        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadGamesDialogBox();
            }
        });
        
        // 3. Health
        ui.add(new Label("➖➖➖➖➖➖➖➖➖➖➖➖"), 0, 4);
        ui.add(new Label("➖➖➖➖➖➖"), 1, 4);
        ui.add(new Label("💀DUNGEON CRAWL💀"), 0, 5);
        ui.add(new Label("➖➖➖➖➖➖➖➖➖➖➖➖"), 0, 6);
        ui.add(new Label("➖➖➖➖➖➖"), 1, 6);

        // Gameplay
        Button pickUpButton = new Button("Pick up");
        pickUpButton.setFocusTraversable(false);
        pickUpButton.setStyle("-fx-background-color: #5f833d; -fx-text-fill: white");
        ui.add(pickUpButton, 1, 7);

        ui.add(new Label("Health: "), 0, 8);
        ui.add(healthLabel, 1, 8);

        // 5. Inventory
        ui.add(new Label("Inventory: "), 0, 9);
        ui.add(inventoryLabel, 0, 10);

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setStyle("-fx-background-color: #b73636; -fx-font-weight: bold; -fx-font-family: Arial");
        borderPane.setLeft(ui);
        borderPane.requestFocus();

        pickUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Cell cell = map.getPlayer().getCell();
                CellType playerCellType = map.getPlayer().getCell().getType();

                if (!playerCellType.equals(CellType.FLOOR) && !playerCellType.equals(CellType.PLAYER)) {
                    inventory.addItem(playerCellType);
                    inventory.addPositionX(cell.getX());
                    inventory.addPositionY(cell.getY());
                    cell.setType(CellType.FLOOR);
                    map.getPlayer().setHealth(map.getPlayer().getHealth() + 10);
                }

                if (inventory.getInventory().containsKey(CellType.GOLD) && inventory.getInventory().get(CellType.GOLD) == 10) {
                    String wonText = "🥇Congratulations!🥇\nYou've won the game!";
                    dialogBox(wonText);
                }

                refresh();
            }
        });

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        scene.setOnKeyReleased(this::onKeyReleased);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                newGame("/map2.txt");
                map.getPlayer().move(0, -1);
                giants.forEach(giant -> giant.attack(giant.getCell(), 7, map.getPlayer()));
                skeletons.forEach(skeleton -> skeleton.attack(skeleton.getCell(), 5, map.getPlayer()));
                scorpions.forEach(scorpion -> scorpion.attack(scorpion.getCell(), 3, map.getPlayer()));
                giants.forEach(giant -> giant.move());
                skeletons.forEach(skeleton -> skeleton.move());
                refresh();
                break;
            case DOWN:
                newGame("/map2.txt");
                map.getPlayer().move(0, 1);
                giants.forEach(giant -> giant.attack(giant.getCell(), 7, map.getPlayer()));
                skeletons.forEach(skeleton -> skeleton.attack(skeleton.getCell(), 5, map.getPlayer()));
                scorpions.forEach(scorpion -> scorpion.attack(scorpion.getCell(), 3, map.getPlayer()));
                giants.forEach(giant -> giant.move());
                skeletons.forEach(skeleton -> skeleton.move());
                refresh();
                break;
            case LEFT:
                newGame("/map2.txt");
                map.getPlayer().move(-1, 0);
                giants.forEach(giant -> giant.attack(giant.getCell(), 7, map.getPlayer()));
                skeletons.forEach(skeleton -> skeleton.attack(skeleton.getCell(), 5, map.getPlayer()));
                scorpions.forEach(scorpion -> scorpion.attack(scorpion.getCell(), 3, map.getPlayer()));
                giants.forEach(giant -> giant.move());
                skeletons.forEach(skeleton -> skeleton.move());
                refresh();
                break;
            case RIGHT:
                newGame("/map2.txt");
                map.getPlayer().move(1, 0);
                giants.forEach(giant -> giant.attack(giant.getCell(), 7, map.getPlayer()));
                skeletons.forEach(skeleton -> skeleton.attack(skeleton.getCell(), 5, map.getPlayer()));
                scorpions.forEach(scorpion -> scorpion.attack(scorpion.getCell(), 3, map.getPlayer()));
                giants.forEach(giant -> giant.move());
                skeletons.forEach(skeleton -> skeleton.move());
                refresh();
                break;
            case S:
                saveGameDialogBox();
        }
    }

    private void newGame(String textMap) {
        if (map.getPlayer().getCell().getX() == 24 && map.getPlayer().getCell().getY() == 18) {
            inventory.removeItem(CellType.KEY);
            mapText = textMap;
            map = MapLoader.loadMap(textMap);
            refresh();
        }
    }

    private void refresh() {
        showGameOverIfPlayerDead();
        searchForKey();
        drawMap();
        refreshHealth();
        refreshInventory();
    }

    private void searchForKey() {
        Cell door = map.getCell(24, 18);

        if (inventory.getInventory().containsKey(CellType.KEY)) {
            door.setType(CellType.OPENDOOR);
        }
    }

    private void refreshHealth() {
        healthLabel.setText("" + map.getPlayer().getHealth());
    }

    private void refreshInventory() {
        Map<CellType, Integer> inventory = this.inventory.getInventory();

        if (inventory.size() == 0) {
            inventoryLabel.setText("");
        } else {
            String labelText = "";

            for (CellType cellType : inventory.keySet()) {
                labelText += String.valueOf(cellType).toLowerCase() + ": " + inventory.get(cellType) + "\n";
            }

            inventoryLabel.setText(labelText);
        }
    }

    private void showGameOverIfPlayerDead() {
        if (map.getPlayer().alive()) {
            String lostText = "Game Over";
            dialogBox(lostText);
        }
    }

    private void dialogBox(String labelText) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primary);

        VBox dialogBox = new VBox(20);

        Button dialogButton = new Button("Ok");
        dialogButton.setFocusTraversable(false);

        dialogBox.getChildren().addAll(new Label(labelText), dialogButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(dialogBox);
        borderPane.setStyle("-fx-background-color: #b73636; -fx-font-weight: bold; -fx-font-family: Arial");

        Scene dialogScene = new Scene(borderPane, 200, 200);
        dialog.setScene(dialogScene);
        dialog.show();

        dialogButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        exit();
                    }
                }
        );
    }

    private void loadGamesDialogBox() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primary);

        VBox dialogBox = new VBox(20);

        Button dialogButton = new Button("Ok");
        dialogButton.setFocusTraversable(false);

        List<PlayerModel> players = dbManager.getAllPlayerModels();
        List<GameState> gameStates = dbManager.getAllGameStates();

        ObservableList<String> names = FXCollections.observableArrayList();
        players.forEach(player -> names.add(player.getPlayerName()));
        for (int i = 0; i < gameStates.size(); i++) {
            names.set(i, names.get(i) + " ".repeat(10) + gameStates.get(i).getSavedAt());
        }

        ListView list = new ListView();
        list.setItems(names);
        list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        dialogBox.getChildren().addAll(new Label("Select"), list, dialogButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(dialogBox);
        borderPane.setStyle("-fx-background-color: #b73636; -fx-font-weight: bold; -fx-font-family: Arial");

        Scene dialogScene = new Scene(borderPane, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();

        dialogButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        MultipleSelectionModel items = list.getSelectionModel();
                        String[] playerName = String.valueOf(items.getSelectedItem()).split(" ");
                        players.forEach(player -> {
                            if (player.getPlayerName().equals(playerName[0])) {
                                int id = player.getId();

                                GameState gameMap = dbManager.getGameState(id);
                                mapText = gameMap.getCurrentMap();
                                map = MapLoader.loadMap(gameMap.getCurrentMap());

                                Player actualPlayer = map.getPlayer();
                                actualPlayer.setName(player.getPlayerName());
                                actualPlayer.setHealth(player.getHp());
                                actualPlayer.setX(player.getX());
                                actualPlayer.setY(player.getY());
                                actualPlayer.move(0, 0);

                                List<InventoryModel> inventoryItems = dbManager.getInventoryModel(id);
                                inventory.clearInventory();
                                for (int i = 0; i < inventoryItems.size(); i++) {
                                    for (int j = 0; j < inventoryItems.get(i).getItems().length; j++) {
                                        switch (inventoryItems.get(i).getItems()[j]) {
                                            case "key":
                                                inventory.addItem(CellType.KEY);
                                                break;
                                            case "sword":
                                                inventory.addItem(CellType.SWORD);
                                                break;
                                            case "gold":
                                                inventory.addItem(CellType.GOLD);
                                                break;
                                        }
                                    }

                                }

                                ItemPositionsModel itemPositionsModel = dbManager.getItemPositionsModel(id);

                                Integer[] positionsX = itemPositionsModel.getPositionsX();
                                Integer[] positionsY = itemPositionsModel.getPositionsY();

                                for (int i = 0; i < positionsX.length; i++) {
                                    int x = positionsX[i];
                                    int y = positionsY[i];

                                    map.getCell(x, y).setType(CellType.FLOOR);
                                }

                                refresh();
                            }
                        });

                        dialog.close();
                    }
                }
        );
    }

    private void saveGameDialogBox() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primary);

        VBox dialogBox = new VBox(20);

        Button saveButton = new Button("Save");
        saveButton.setFocusTraversable(false);

        Button cancelButton = new Button("Cancel");
        cancelButton.setFocusTraversable(false);

        TextField nameField = new TextField();

        dialogBox.getChildren().addAll(new Label("Name:"), nameField, saveButton, cancelButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(dialogBox);
        borderPane.setStyle("-fx-background-color: #b73636; -fx-font-weight: bold; -fx-font-family: Arial");

        Scene dialogScene = new Scene(borderPane, 200, 200);
        dialog.setScene(dialogScene);
        dialog.show();

        List<PlayerModel> players = dbManager.getAllPlayerModels();

        saveButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Date date = new Date();
                        String nameFieldText = nameField.getText();
                        List<String> playerModelNames = new ArrayList<>();

                        players.forEach(playerModel -> playerModelNames.add(playerModel.getPlayerName()));

                        if (playerModelNames.contains(nameFieldText)) {
                            Stage dialog = new Stage();
                            dialog.initModality(Modality.APPLICATION_MODAL);
                            dialog.initOwner(primary);

                            VBox dialogBox = new VBox(20);

                            Button yesButton = new Button("Yes");
                            yesButton.setFocusTraversable(false);

                            Button noButton = new Button("No");
                            noButton.setFocusTraversable(false);

                            dialogBox.getChildren().addAll(new Label("This name is already taken!\nWould you like to overwrite it?"), yesButton, noButton);

                            BorderPane borderPane = new BorderPane();
                            borderPane.setCenter(dialogBox);
                            borderPane.setStyle("-fx-background-color: #b73636; -fx-font-weight: bold; -fx-font-family: Arial");

                            Scene dialogScene = new Scene(borderPane, 200, 200);
                            dialog.setScene(dialogScene);
                            dialog.show();

                            yesButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    int playerId = 0;
                                    for (int i = 0; i < players.size(); i++) {
                                        if (players.get(i).getPlayerName().equals(nameFieldText)) {
                                            playerId = players.get(i).getId();
                                        }
                                    }

                                    map.getPlayer().setName(nameFieldText);
                                    dbManager.updateGame(playerId, map.getPlayer(), mapText, date.getYear(), date.getMonth(), date.getDay(), inventory);

                                    dialog.close();
                                }
                            });

                            noButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    dialog.close();
                                }
                            });
                        } else {
                            map.getPlayer().setName(nameFieldText);
                            dbManager.save(map.getPlayer(), mapText, date.getYear(), date.getMonth(), date.getDay(), inventory);

                            dialog.close();
                        }

                        dialog.close();

                    }
                }
        );

        cancelButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        dialog.close();
                    }
                }
        );
    }

    private void drawMap() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
    }

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
