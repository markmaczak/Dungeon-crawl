package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.ItemPositionsModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemPositionsDaoJdbc implements ItemPositionsDao{
    private DataSource dataSource;

    public ItemPositionsDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(ItemPositionsModel itemPositionsModel) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO item_positions (positionsX, positionsY, player_id) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            Array arrayPositionsX = conn.createArrayOf("integer", itemPositionsModel.getPositionsX());
            statement.setArray(1, arrayPositionsX);
            Array arrayPositionsY = conn.createArrayOf("integer", itemPositionsModel.getPositionsY());
            statement.setArray(2, arrayPositionsY);
            statement.setInt(3, itemPositionsModel.getPlayerId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            itemPositionsModel.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(ItemPositionsModel itemPositionsModel) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE item_positions SET positionsX = ?, positionsY = ? WHERE player_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            Array arrayPositionsX = conn.createArrayOf("integer", itemPositionsModel.getPositionsX());
            st.setArray(1, arrayPositionsX);
            Array arrayPositionsY = conn.createArrayOf("integer", itemPositionsModel.getPositionsY());
            st.setArray(2, arrayPositionsY);
            st.setInt(3, itemPositionsModel.getPlayerId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemPositionsModel get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, positionsX, positionsY, player_id FROM item_positions WHERE player_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Array itemArrayX = rs.getArray(2);
            Integer[] intItemArrayX = (Integer[]) itemArrayX.getArray();
            Array itemArrayY = rs.getArray(3);
            Integer[] intItemArrayY = (Integer[]) itemArrayY.getArray();
            ItemPositionsModel itemPositionsModel = new ItemPositionsModel(intItemArrayX, intItemArrayY, rs.getInt(4));
            itemPositionsModel.setId(id);
            return itemPositionsModel;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading item_positions with id: " + id, e);
        }
    }

    @Override
    public List<ItemPositionsModel> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, positionsX, positionsY, player_id FROM item_positions";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<ItemPositionsModel> result = new ArrayList<>();
            while (rs.next()) {
                Array itemArrayX = rs.getArray(2);
                Integer[] intItemArrayX = (Integer[]) itemArrayX.getArray();
                Array itemArrayY = rs.getArray(3);
                Integer[] intItemArrayY = (Integer[]) itemArrayY.getArray();
                ItemPositionsModel itemPositionsModel = new ItemPositionsModel(intItemArrayX, intItemArrayY, rs.getInt(4));
                itemPositionsModel.setId(rs.getInt(1));
                result.add(itemPositionsModel);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all item_positions", e);
        }
    }
}
