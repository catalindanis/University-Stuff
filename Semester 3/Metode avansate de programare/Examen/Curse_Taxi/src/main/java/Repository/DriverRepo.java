package Repository;

import Domain.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverRepo {
    private String url, u, p;

    public DriverRepo(String url, String u, String p) {
        this.url = url;
        this.u = u;
        this.p = p;
    }

    public List<Driver> findAll(){
        List<Driver> drivers = new ArrayList<>();
        try(Connection con = DriverManager.getConnection(url,u,p);
            PreparedStatement st = con.prepareStatement("SELECT * FROM drivers");
            ResultSet rs = st.executeQuery()){
            while(rs.next()){
                drivers.add(new Driver(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return drivers;
    }

    public List<Driver> findEligibleDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT d.id, d.name, MAX(o.end_date) as last_end " +
                "FROM drivers d " +
                "LEFT JOIN orders o ON d.id = o.drivers_id " +
                "WHERE d.id NOT IN (SELECT drivers_id FROM orders WHERE status = 'IN_PROGRESS' AND drivers_id IS NOT NULL) " +
                "GROUP BY d.id, d.name " +
                "ORDER BY last_end ASC NULLS FIRST";

        try (Connection con = DriverManager.getConnection(url, u, p);
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                drivers.add(new Driver(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }



}
