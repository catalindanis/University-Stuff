package sgbd.proiect.repository;

import sgbd.proiect.domain.BankAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankAccountRepository implements Repository<Long, BankAccount> {
    private final String url = "jdbc:postgresql://localhost:5432/sgbd_lab2?user=postgres&password=dbcata05";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM bank_account";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(BankAccount entity) {
        String sql = "INSERT INTO bank_account (id, account_number, owner_name, balance) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getAccountNumber());
            ps.setString(3, entity.getOwnerName());
            ps.setDouble(4, entity.getBalance());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Connection conn, BankAccount entity) {
        String sql = "INSERT INTO bank_account (id, account_number, owner_name, balance) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getAccountNumber());
            ps.setString(3, entity.getOwnerName());
            ps.setDouble(4, entity.getBalance());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM bank_account WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Long id, BankAccount entity) {
        String sql = "UPDATE bank_account SET account_number = ?, owner_name = ?, balance = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getAccountNumber());
            ps.setString(2, entity.getOwnerName());
            ps.setDouble(3, entity.getBalance());
            ps.setLong(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BankAccount findOne(Long id) {
        String sql = "SELECT * FROM bank_account WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new BankAccount(
                            rs.getLong("id"),
                            rs.getString("account_number"),
                            rs.getString("owner_name"),
                            rs.getDouble("balance")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Iterable<BankAccount> findAll() {
        List<BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM bank_account";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accounts.add(new BankAccount(
                        rs.getLong("id"),
                        rs.getString("account_number"),
                        rs.getString("owner_name"),
                        rs.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    public void updateBalance(Connection conn, long id, double newBalance) {
        String sql = "UPDATE bank_account SET balance = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public double getBalance(Connection conn, long id) {
        String sql = "SELECT balance FROM bank_account WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                } else {
                    throw new RuntimeException("Bank account not found for id: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BankAccount> findAllWithBalanceGreaterThan(Connection conn, double minBalance) {
        List<BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM bank_account WHERE balance > ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, minBalance);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(new BankAccount(
                            rs.getLong("id"),
                            rs.getString("account_number"),
                            rs.getString("owner_name"),
                            rs.getDouble("balance")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    public void clearTable() {
        String sqlDelete = "DELETE FROM bank_account";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sqlDelete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
