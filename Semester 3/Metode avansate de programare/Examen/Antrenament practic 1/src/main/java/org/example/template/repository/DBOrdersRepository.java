package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.Order;
import org.example.template.domain.OrderFilters;
import org.example.template.domain.OrderStatus;
import org.example.template.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBOrdersRepository implements Repository<Integer, Order> {

    @Override
    public Integer save(Order entity) {
        String query = "INSERT INTO Orders (\"driverId\", status, \"startDate\", \"endDate\", \"pickupAdress\", \"destinationAdress\", \"clientName\") VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, entity.getDriverId(), Types.INTEGER);
            stmt.setString(2, entity.getStatus().toString());
            stmt.setTimestamp(3, Timestamp.valueOf(entity.getStartDate()));
            stmt.setTimestamp(4, entity.getEndDate() != null ? Timestamp.valueOf(entity.getEndDate()) : null);
            stmt.setString(5, entity.getPickupAdress());
            stmt.setString(6, entity.getDestinationAdress());
            stmt.setString(7, entity.getClientName());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next())
                return resultSet.getInt("id");
        } catch (SQLException exception) {
            throw new RepositoryException("Could not save Order:\n" + exception.getMessage());
        }

        return -1;
    }

    @Override
    public void delete(Integer ID) {
        String query = "DELETE FROM Orders WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not delete Order:\n" + exception.getMessage());
        }
    }

    @Override
    public void update(Integer ID, Order entity) {
        String query = "UPDATE Orders SET \"driverId\" = ?, status = ?, \"startDate\" = ?, \"endDate\" = ?, \"pickupAdress\" = ?, \"destinationAdress\" = ?, \"clientName\" = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entity.getDriverId());
            stmt.setString(2, entity.getStatus().toString());
            stmt.setTimestamp(3, Timestamp.valueOf(entity.getStartDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(entity.getEndDate()));
            stmt.setString(5, entity.getPickupAdress());
            stmt.setString(6, entity.getDestinationAdress());
            stmt.setString(7, entity.getClientName());
            stmt.setInt(8, ID);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RepositoryException("Could not update Order:\n" + exception.getMessage());
        }
    }

    @Override
    public Order findById(Integer ID) {
        String query = "SELECT * FROM Orders WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        rs.getInt("driverId"),
                        OrderStatus.fromString(rs.getString("status")),
                        rs.getTimestamp("startDate").toLocalDateTime(),
                        rs.getTimestamp("endDate") != null ? rs.getTimestamp("endDate").toLocalDateTime() : null,
                        rs.getString("pickupAdress"),
                        rs.getString("destinationAdress"),
                        rs.getString("clientName")
                );
            }
            return null;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find Order:\n" + exception.getMessage());
        }
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();

        String query = "SELECT * FROM Orders";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        rs.getInt("driverId"),
                        OrderStatus.fromString(rs.getString("status")),
                        rs.getTimestamp("startDate").toLocalDateTime(),
                        rs.getTimestamp("endDate") != null ? rs.getTimestamp("endDate").toLocalDateTime() : null,
                        rs.getString("pickupAdress"),
                        rs.getString("destinationAdress"),
                        rs.getString("clientName")
                ));
            }
            return orders;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not get Orders:\n" + exception.getMessage());
        }
    }

    public List<Order> findAllFiltered(OrderFilters filters) {
        List<Order> orders = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT * FROM Orders");

        List<String> filtersBuilder = new ArrayList<>();
        filters.driverId.ifPresent(integer -> filtersBuilder.add("\"driverId\" = " + integer));
        filters.status.ifPresent(orderStatus -> filtersBuilder.add("status LIKE '%" + orderStatus.toString() + "%'"));

        if(!filtersBuilder.isEmpty())
            query.append(" WHERE ");
        query.append(String.join(" AND ", filtersBuilder));
//        System.out.println(query);

        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        rs.getInt("driverId"),
                        OrderStatus.fromString(rs.getString("status")),
                        rs.getTimestamp("startDate").toLocalDateTime(),
                        rs.getTimestamp("endDate") != null ? rs.getTimestamp("endDate").toLocalDateTime() : null,
                        rs.getString("pickupAdress"),
                        rs.getString("destinationAdress"),
                        rs.getString("clientName")
                ));
            }
            return orders;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not get Orders:\n" + exception.getMessage());
        }
    }
}

