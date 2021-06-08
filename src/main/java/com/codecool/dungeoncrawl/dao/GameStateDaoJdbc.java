package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (current_map, player_id) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, state.getCurrentMap());
            statement.setInt(2, state.getPlayerId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            state.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE game_state SET current_map = ?, player_id = ? WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, state.getCurrentMap());
            st.setInt(2, state.getPlayerId());
            st.setInt(3, state.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, current_map , saved_at, player_id FROM game_state WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            GameState gameState = new GameState(rs.getString(2), rs.getDate(3), rs.getInt(4));
            gameState.setId(id);
            return gameState;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading game_state with id: " + id, e);
        }
    }

    @Override
    public List<GameState> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, current_map, saved_at, player_id FROM game_state";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<GameState> result = new ArrayList<>();
            while (rs.next()) {
                GameState gameState = new GameState(rs.getString(2), rs.getDate(3), rs.getInt(4));

                gameState.setId(rs.getInt(1));
                result.add(gameState);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all game_states", e);
        }
    }
}
