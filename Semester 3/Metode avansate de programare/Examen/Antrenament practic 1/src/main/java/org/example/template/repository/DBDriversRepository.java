package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.Driver;
import org.example.template.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBDriversRepository implements Repository<Integer, Driver> {
    @Override
    public Integer save(Driver entity) {
        String query = "INSERT INTO Drivers (name) VALUES (?) RETURNING id";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getName());
            ResultSet resultSet =  stmt.executeQuery();
            if(resultSet.next())
                return resultSet.getInt("id");
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save Driver:\n" + exception.getMessage());
        }

        return -1;
    }

    @Override
    public void delete(Integer ID) {
        String query = "DELETE FROM Drivers WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete Driver:\n" + exception.getMessage());
        }
    }

    @Override
    public void update(Integer ID, Driver entity) {
        String query = "UPDATE Drivers SET name = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getName());
            stmt.setInt(2, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not update Driver:\n" + exception.getMessage());
        }
    }

    @Override
    public Driver findById(Integer ID) {
        String query = "SELECT * FROM Drivers WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Driver(rs.getInt("id"), rs.getString("name"));
            }
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find Driver:\n" + exception.getMessage());
        }
    }

    @Override
    public List<Driver> findAll() {
        List<Driver> drivers = new ArrayList<>();

        String query = "SELECT * FROM Drivers";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                drivers.add(new Driver(rs.getInt("id"), rs.getString("name")));
            }
            return drivers;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not get Drivers:\n" + exception.getMessage());
        }
    }
}
