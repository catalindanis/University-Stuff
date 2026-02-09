package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.User;
import org.example.template.domain.UserRole;
import org.example.template.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersRepository implements Repository<Integer, User> {
    @Override
    public void save(User entity) {
        String query = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getUsername());
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getRole().toString());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save User:\n" + exception.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        String query = "DELETE FROM Users WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete User:\n" + exception.getMessage());
        }
    }

    @Override
    public void update(Integer ID, User entity) {
        String query = "UPDATE Users SET User = ? WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, entity.getUser());
            stmt.setInt(2, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not update User:\n" + exception.getMessage());
        }
    }

    @Override
    public User findById(Integer ID) {
        String query = "SELECT * FROM Users WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("ID"), rs.getString("username"), rs.getString("password"), UserRole.fromString(rs.getString("role")));
            }
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find User:\n" + exception.getMessage());
        }
    }

    public User findByCredentials(String username, String password) {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("ID"), rs.getString("username"), rs.getString("password"), UserRole.fromString(rs.getString("role")));
            }

            throw new RepositoryException("Could not find User!");
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find User:\n" + exception.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        List<User> Users = new ArrayList<>();

        String query = "SELECT * FROM Users";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Users.add(new User(rs.getInt("ID"), rs.getString("username"), rs.getString("password"), UserRole.fromString(rs.getString("role"))));
            }
            return Users;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not get Users:\n" + exception.getMessage());
        }
    }
}
