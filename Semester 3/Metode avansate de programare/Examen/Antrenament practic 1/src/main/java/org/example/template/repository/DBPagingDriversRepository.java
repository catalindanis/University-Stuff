package org.example.template.repository;

import org.example.template.database.DBConnection;
import org.example.template.domain.Driver;
import org.example.template.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBPagingDriversRepository extends DBDriversRepository implements PagingRepository<Integer, Driver> {
    @Override
    public List<Driver> findPage(int pageNumber, int pageSize) {
        List<Driver> Drivers = new ArrayList<>();

        String query = """
        SELECT * FROM Drivers
        LIMIT ? OFFSET ?
        """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1, pageSize);
            stmt.setInt(2, pageNumber * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                Drivers.add(new Driver(rs.getInt("id"), rs.getString("name")));
            return Drivers;
        } catch (SQLException exception) {
            throw new RepositoryException("Could not find page:\n" + exception.getMessage());
        }
    }
}
