package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.Transaction;
import org.example.template.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionsRepository implements Repository<Integer, Transaction> {
    @Override
    public void save(Transaction entity) {
        String query = "INSERT INTO Transactions (userid, coinsymbol, type, price, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entity.getUserId());
            stmt.setString(2, entity.getCoinSymbol());
            stmt.setString(3, entity.getType());
            stmt.setDouble(4, entity.getPrice());
            stmt.setTimestamp(5, Timestamp.valueOf(entity.getDate()));
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save Transaction:\n" + exception.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        String query = "DELETE FROM Transactions WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete Transaction:\n" + exception.getMessage());
        }
    }

    @Override
    public void update(Integer ID, Transaction entity) {
        String query = "UPDATE Transactions SET Transaction = ? WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, entity.getTransaction());
            stmt.setInt(2, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not update Transaction:\n" + exception.getMessage());
        }
    }

    @Override
    public Transaction findById(Integer ID) {
        String query = "SELECT * FROM Transactions WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Transaction(rs.getInt("id"), rs.getInt("userid"), rs.getString("coinSymbol"), rs.getString("type"), rs.getDouble("price"), rs.getTimestamp("date").toLocalDateTime());
            }
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find Transaction:\n" + exception.getMessage());
        }
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> Transactions = new ArrayList<>();

        String query = "SELECT * FROM Transactions";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Transactions.add(new Transaction(rs.getInt("id"), rs.getInt("userid"), rs.getString("coinSymbol"), rs.getString("type"), rs.getDouble("price"), rs.getTimestamp("date").toLocalDateTime()));
            }
            return Transactions;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not get Transactions:\n" + exception.getMessage());
        }
    }
}
