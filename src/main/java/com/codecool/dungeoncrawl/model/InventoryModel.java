package com.codecool.dungeoncrawl.model;

public class InventoryModel extends BaseModel {
    private String[] items;
    private int playerId;

    public InventoryModel(String[] items, int playerId) {
        this.items = items;
        this.playerId = playerId;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
