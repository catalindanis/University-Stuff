package Repository;
import java.sql.*;
import java.util.*;

public class ItemRepoPaged extends ItemRepo {
    public ItemRepoPaged(String url, String u, String p) { super(url, u, p); }

    public List<Object> findAllPaged(int limit, int offset) {
        List<Object> list = new ArrayList<>();
        String sql = "SELECT * FROM items LIMIT ? OFFSET ?";
        try (Connection con = DriverManager.getConnection(url, u, p);
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, limit);
            st.setInt(2, offset);
            ResultSet rs = st.executeQuery();
            while (rs.next()) { /* list.add(extract(rs)); */ }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}