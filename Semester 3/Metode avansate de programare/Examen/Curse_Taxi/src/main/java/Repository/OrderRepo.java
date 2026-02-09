package Repository;

import Domain.Order;
import Domain.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepo {
    private String url, u, p;

    public OrderRepo(String url, String u, String p) {
        this.url = url;
        this.u = u;
        this.p = p;
    }

    public List<Order> findAllActiveByDriver(int driverId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE drivers_id = ? AND status ='IN_PROGRESS'";
        try (Connection con = DriverManager.getConnection(url, u, p);
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, driverId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                orders.add(extractOrder(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }

    public Order save(Order o) {
        String sql = "insert into orders (status,start_date,pickup_adress,destination_adress,client_name) values (?,?,?,?,?)";
        try (Connection con = DriverManager.getConnection(url, u, p);
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, o.getStatus().name());
            st.setTimestamp(2, Timestamp.valueOf(o.getStartDate()));
            st.setString(3, o.getPickupAddress());
            st.setString(4, o.getDestinationAddress());
            st.setString(5, o.getClientName());
            st.executeUpdate();

            ResultSet keys = st.getGeneratedKeys();
            if (keys.next()) {
                o.setId(keys.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return o;
    }


    public void update(int orderId, int driverId, String status, Timestamp end) {
        String sql = "update orders set drivers_id = ?, end_date = ?, status = ? where id = ?";
        try (Connection con = DriverManager.getConnection(url, u, p);
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, driverId);
            if (end != null) {
                st.setTimestamp(2, end);
            } else {
                st.setNull(2, Types.TIMESTAMP);
            }
            st.setString(3, status);
            st.setInt(4, orderId);
            st.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    private Order extractOrder(ResultSet rs) throws SQLException {
        Timestamp endTs = rs.getTimestamp("end_date");
        return new Order(
                rs.getInt("id"),
                rs.getInt("drivers_id"),
                OrderStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("start_date").toLocalDateTime(),
                endTs != null ? endTs.toLocalDateTime() : null,
                rs.getString("pickup_adress"),
                rs.getString("destination_adress"),
                rs.getString("client_name")
        );
    }

}