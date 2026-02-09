package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.Order;
import org.example.template.domain.OrderStatus;
import org.example.template.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBPagingOrdersRepository extends DBOrdersRepository implements PagingRepository<Integer, Order> {
    @Override
    public Iterable<Order> findPage(int pageNumber, int pageSize) {
        List<Order> orders = new ArrayList<>();

        String query = """
        SELECT * FROM orders
        LIMIT ? OFFSET ?
        """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, pageSize);
            stmt.setInt(2, pageNumber * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
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
            return orders;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find page:\n" + exception.getMessage());
        }
    }
}
