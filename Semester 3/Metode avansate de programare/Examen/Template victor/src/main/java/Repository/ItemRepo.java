package Repository;

import Domain.Entity; // Inlocuieste cu clasa ta

import java.sql.*;
import java.util.*;

public class ItemRepo {
    protected String url, u, p;

    public ItemRepo(String url, String u, String p) {
        this.url = url;
        this.u = u;
        this.p = p;
    }

    public List<Object> findAll() { // Inlocuieste Object cu clasa ta
        List<Object> list = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, u, p);
             PreparedStatement st = con.prepareStatement("SELECT * FROM items")) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) { /* list.add(extract(rs)); */ }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void save(Object entity) {
        try (Connection con = DriverManager.getConnection(url, u, p);
             PreparedStatement st = con.prepareStatement("INSERT INTO items...")) {
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}