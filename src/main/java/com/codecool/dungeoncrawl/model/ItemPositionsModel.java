package com.codecool.dungeoncrawl.model;

public class ItemPositionsModel extends BaseModel{
    private Integer[] positionsX;
    private Integer[] positionsY;
    private int playerId;

    public ItemPositionsModel(Integer[] positionsX, Integer[] positionsY, int playerId) {
        this.positionsX = positionsX;
        this.positionsY = positionsY;
        this.playerId = playerId;
    }

    public Integer[] getPositionsX() {
        return positionsX;
    }

    public void setPositionsX(Integer[] positions) {
        this.positionsX = positions;
    }

    public Integer[] getPositionsY() { return positionsY; }

    public void setPositionsY(Integer[] positionsY) { this.positionsY = positionsY; }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
