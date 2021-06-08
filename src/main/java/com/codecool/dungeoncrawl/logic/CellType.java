package com.codecool.dungeoncrawl.logic;

public enum CellType {
    //Field
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    WALL2("wall2"),
    WALL3("wall3"),
    WALL4("wall4"),
    HOUSE("house"),
    TREE("tree"),
    TRUCK("truck"),
    TRUCK2("truck2"),
    OPENDOOR("openDoor"),
    CLOSEDOOR("closeDoor"),
    GOLD("gold"),
    //Actors
    PLAYER("player"),
    SKELETON("skeleton"),
    SCORPION("scorpion"),
    GIANT("giant"),
    //Items
    SWORD("sword"),
    KEY("key"),
    UZI("uzi");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
