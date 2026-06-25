package sgbd.proiect.service;

import sgbd.proiect.domain.BankAccount;
import sgbd.proiect.repository.BankAccountRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.logging.Logger;

public class BankAccountService {
    private final BankAccountRepository repository;
    private static final Logger logger = Logger.getLogger(BankAccountService.class.getName());
    private final String DB_URL = "jdbc:postgresql://localhost:5432/sgbd_lab2?user=postgres&password=dbcata05";

    public BankAccountService() {
        this.repository = new BankAccountRepository();
    }

    // Solve: READ_COMMITED, REPEATABLE_READ, SERIALIZABLE
    public void simulateDirtyRead() {
        init();
        System.out.println("~~~~~~~~~~~~~~~Dirty Read (with threads)~~~~~~~~~~~~~~~");

        printFinalState();

        Thread t1 = new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                conn.setAutoCommit(false);
                System.out.println("T1: Update balance to 999.99");
                repository.updateBalance(conn, 1L, 999.99);
                Thread.sleep(2000);
                conn.rollback();
                System.out.println("T1: Update rollback.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    conn.setAutoCommit(false);
                    Thread.sleep(1000);
                    System.out.println("T2: Reading balance");
                    double balance = repository.getBalance(conn, 1L);
                    System.out.println("T2: Balance read: " + balance);
                    conn.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        try { t1.join(); t2.join(); } catch (InterruptedException e) { e.printStackTrace(); }

        printFinalState();
    }

    // Solve: REPEATABLE_READ, SERIALIZABLE
    public void simulateNonRepeatableRead() {
        init();
        System.out.println("~~~~~~~~~~~~~~~Non-Repeatable Read (with threads)~~~~~~~~~~~~~~~");

        printFinalState();

        final double[] firstRead = new double[1];

        Thread t1 = new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                conn.setAutoCommit(false);
                System.out.println("T1: Reading balance...");
                firstRead[0] = repository.getBalance(conn, 1L);
                System.out.println("T1: Balance: " + firstRead[0]);
                Thread.sleep(2000);
                System.out.println("T1: Reading balance again...");
                double secondRead = repository.getBalance(conn, 1L);
                System.out.println("T1: Balance: " + secondRead);
                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    conn.setAutoCommit(false);
                    System.out.println("T2: Adding 500 to balance and committing...");
                    repository.updateBalance(conn, 1L, firstRead[0] + 500.0);
                    conn.commit();
                    System.out.println("T2: Update committed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        try { t1.join(); t2.join(); } catch (InterruptedException e) { e.printStackTrace(); }

        printFinalState();
    }

    // Solve: SERIALIZABLE
    public void simulatePhantomRead() {
        init();
        System.out.println("~~~~~~~~~~~~~~~Phantom Read (with threads)~~~~~~~~~~~~~~~");

        printFinalState();

        Thread t1 = new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                conn.setAutoCommit(false);
                System.out.println("T1: Accounts with balance > 1000:");
                List<BankAccount> firstRead = repository.findAllWithBalanceGreaterThan(conn, 1000);
                firstRead.forEach(acc -> System.out.println("T1: " + acc));
                Thread.sleep(2000);
                System.out.println("T1: Accounts with balance > 1000 (again):");
                List<BankAccount> secondRead = repository.findAllWithBalanceGreaterThan(conn, 1000);
                secondRead.forEach(acc -> System.out.println("T1: " + acc));
                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    conn.setAutoCommit(false);
                    System.out.println("T2: Adding new account with balance 2000 and committing...");
                    BankAccount newAccount = new BankAccount(3L, "ACC999", "Phantom User", 2000.0);
                    repository.save(newAccount);
                    conn.commit();
                    System.out.println("T2: Insert committed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        try { t1.join(); t2.join(); } catch (InterruptedException e) { e.printStackTrace(); }

        printFinalState();
    }

    // Solve: SERIALIZABLE
    public void simulateLostUpdate() {
        init();
        System.out.println("~~~~~~~~~~~~~~~Lost Update (with threads)~~~~~~~~~~~~~~~");

        printFinalState();

        final double[] t1Read = new double[1];
        final double[] t2Read = new double[1];

        Thread t1 = new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                conn.setAutoCommit(false);
                System.out.println("T1: Reading balance...");
                t1Read[0] = repository.getBalance(conn, 1L);
                System.out.println("T1: Balance: " + t1Read[0]);
                Thread.sleep(2000);
                double newBalance = t1Read[0] + 100;
                System.out.println("T1: Updating balance to " + newBalance);
                repository.updateBalance(conn, 1L, newBalance);
                conn.commit();
                System.out.println("T1: Update committed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    conn.setAutoCommit(false);
                    System.out.println("T2: Reading balance...");
                    t2Read[0] = repository.getBalance(conn, 1L);
                    System.out.println("T2: Balance: " + t2Read[0]);
                    Thread.sleep(2000);
                    double newBalance = t2Read[0] + 200;
                    System.out.println("T2: Updating balance to " + newBalance);
                    repository.updateBalance(conn, 1L, newBalance);
                    conn.commit();
                    System.out.println("T2: Update committed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        try { t1.join(); t2.join(); } catch (InterruptedException e) { e.printStackTrace(); }

        printFinalState();
    }

    public void simulateDeadlock() {
        init();
        System.out.println("~~~~~~~~~~~~~~~Deadlock Simulation (with threads)~~~~~~~~~~~~~~~");

        Thread t1 = new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                conn.setAutoCommit(false);
                System.out.println("T1: Locking Account 1");
                repository.updateBalance(conn, 1L, repository.getBalance(conn, 1L) + 100);
                Thread.sleep(2000);
                System.out.println("T1: Trying to lock Account 2");
                repository.updateBalance(conn, 2L, repository.getBalance(conn, 2L) + 100);
                conn.commit();
                System.out.println("T1: Committed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                conn.setAutoCommit(false);
                System.out.println("T2: Locking Account 2");
                repository.updateBalance(conn, 2L, repository.getBalance(conn, 2L) + 200);
                Thread.sleep(2000);
                System.out.println("T2: Trying to lock Account 1");
                repository.updateBalance(conn, 1L, repository.getBalance(conn, 1L) + 200);
                conn.commit();
                System.out.println("T2: Committed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        try { t1.join(); t2.join(); } catch (InterruptedException e) { e.printStackTrace(); }

        printFinalState();
    }

    public void insertWithAutoCommit() {
        init();

        long start = System.currentTimeMillis();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            for (int i = 3; i <= 5002; i++) {
                BankAccount acc = new BankAccount(
                        (long) i,
                        "ACC" + i,
                        "User" + i,
                        1000.0 + i
                );
                repository.save(conn, acc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Auto-commit (o tranzacție per inserare): " + (end - start) + " ms");
    }

    public void insertWithBatchCommit() {
        init();

        long start = System.currentTimeMillis();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            for (int i = 3; i <= 5002; i++) {
                BankAccount acc = new BankAccount(
                        (long) i,
                        "ACC" + i,
                        "User" + i,
                        1000.0 + i
                );
                repository.save(conn, acc);

                if ((i - 2) % 100 == 0) {
                    conn.commit();
                }
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Batch commit (every 100 inserts): " + (end - start) + " ms");
    }

    public void insertWithOneCommit() {
        init();

        long start = System.currentTimeMillis();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            for (int i = 3; i <= 5002; i++) {
                BankAccount acc = new BankAccount(
                        (long) i,
                        "ACC" + i,
                        "User" + i,
                        1000.0 + i
                );
                repository.save(conn, acc);
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Single transaction (all inserts in one commit): " + (end - start) + " ms");
    }

    private void printFinalState() {
        System.out.println("=== bank_account table ===");
        for (BankAccount acc : repository.findAll()) {
            System.out.println(acc);
        }
        System.out.println("===========================================");
    }

    private void init() {
        repository.clearTable();

        BankAccount bankAccount = new BankAccount(
                1L,
                "151231",
                "Catalin",
                1400.0
        );

        repository.save(bankAccount);

        bankAccount = new BankAccount(
                2L,
                "311145",
                "Paul",
                750.0
        );

        repository.save(bankAccount);
    }
}
