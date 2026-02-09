package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.Coin;
import org.example.template.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoinsRepository implements Repository<Integer, Coin> {
    @Override
    public void save(Coin entity) {
        String query = "INSERT INTO Coins (symbol, name, price) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getSymbol());
            stmt.setString(2, entity.getName());
            stmt.setDouble(3, entity.getPrice());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save Coin:\n" + exception.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        String query = "DELETE FROM Coins WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete Coin:\n" + exception.getMessage());
        }
    }

    @Override
    public void update(Integer ID, Coin entity) {
        String query = "UPDATE Coins SET symbol = ?, name = ?, price = ? WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getSymbol());
            stmt.setString(2, entity.getName());
            stmt.setDouble(3, entity.getPrice());
            stmt.setInt(4, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not update Coin:\n" + exception.getMessage());
        }
    }

    @Override
    public Coin findById(Integer ID) {
        String query = "SELECT * FROM Coins WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Coin(rs.getInt("id"), rs.getString("symbol"), rs.getString("name"), rs.getDouble("price"));
            }
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find Coin:\n" + exception.getMessage());
        }
    }

    @Override
    public List<Coin> findAll() {
        List<Coin> Coins = new ArrayList<>();

        String query = "SELECT * FROM Coins";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Coins.add(new Coin(rs.getInt("id"), rs.getString("symbol"), rs.getString("name"), rs.getDouble("price")));
            }
            return Coins;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not get Coins:\n" + exception.getMessage());
        }
    }
}
