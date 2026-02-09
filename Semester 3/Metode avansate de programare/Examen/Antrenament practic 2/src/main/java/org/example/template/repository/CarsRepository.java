package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.Car;
import org.example.template.domain.CarStatus;
import org.example.template.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarsRepository implements Repository<Integer, Car> {
    @Override
    public void save(Car entity) {
        String query = "INSERT INTO Cars (name, description, price, status) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.setDouble(3, entity.getPrice());
            stmt.setString(4, entity.getStatus().toString());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save Car:\n" + exception.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        String query = "DELETE FROM Cars WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete Car:\n" + exception.getMessage());
        }
    }

    @Override
    public void update(Integer ID, Car entity) {
        String query = "UPDATE Cars SET status = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getStatus().toString());
            stmt.setInt(2, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not update Car:\n" + exception.getMessage());
        }
    }

    @Override
    public Car findById(Integer ID) {
        String query = "SELECT * FROM Cars WHERE ID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Car(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getDouble("price"), CarStatus.fromString(rs.getString("status")));
            }
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find Car:\n" + exception.getMessage());
        }
    }

    @Override
    public List<Car> findAll() {
        List<Car> Cars = new ArrayList<>();

        String query = "SELECT * FROM Cars";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Cars.add(new Car(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getDouble("price"), CarStatus.fromString(rs.getString("status"))));
            }
            return Cars;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not get Cars:\n" + exception.getMessage());
        }
    }
}
