package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.ItemPositionsModel;

import java.util.List;

public interface ItemPositionsDao {
    void add(ItemPositionsModel itemPositionsModel);
    void update(ItemPositionsModel itemPositionsModel);
    ItemPositionsModel get(int id);
    List<ItemPositionsModel> getAll();
}
