package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.InventoryModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDaoJdbc implements InventoryDao {
    private DataSource dataSource;

    public InventoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(InventoryModel inventory) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO inventory (items, player_id) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            Array arrayItems = conn.createArrayOf("text", inventory.getItems());
            statement.setArray(1, arrayItems);
            statement.setInt(2, inventory.getPlayerId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            inventory.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(InventoryModel inventory) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE inventory SET items = ? WHERE player_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            Array arrayItems = conn.createArrayOf("text", inventory.getItems());
            st.setArray(1, arrayItems);
            st.setInt(2, inventory.getPlayerId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<InventoryModel> get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, items, player_id FROM inventory WHERE player_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            List<InventoryModel> result = new ArrayList<>();
            while (rs.next()) {
                Array itemArray = rs.getArray(2);
                String[] stringItemArray = (String[])itemArray.getArray();
                InventoryModel inventoryModel = new InventoryModel(stringItemArray, rs.getInt(3));
                inventoryModel.setId(id);
                result.add(inventoryModel);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading inventory with id: " + id, e);
        }
    }

    @Override
    public List<InventoryModel> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, items, player_id FROM inventory";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<InventoryModel> result = new ArrayList<>();
            while (rs.next()) {
                Array itemArray = rs.getArray(2);
                String[] stringItemArray = (String[])itemArray.getArray();
                InventoryModel inventoryModel = new InventoryModel(stringItemArray, rs.getInt(3));
                inventoryModel.setId(rs.getInt(1));
                result.add(inventoryModel);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all inventorys", e);
        }
    }
}
