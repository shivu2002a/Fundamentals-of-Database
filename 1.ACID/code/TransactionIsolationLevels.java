import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionIsolationLevels {

    public static void main(String[] args) {
        isolationLevelSerializable();
        isolationlevelRepeatableRead();
    }

    public static void isolationLevelSerializable() {
        try {
            Connection conn = DatabaseConnection.establishConnection();

            // Set the transaction isolation level to SERIALIZABLE
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // Create a table if not exists
            try (Statement createStatement = conn.createStatement()) {
                createStatement
                        .executeUpdate("CREATE TABLE IF NOT EXISTS demo_table (id INT PRIMARY KEY, name VARCHAR(50))");
            }

            // Insert data in a transaction
            conn.setAutoCommit(false);
            try (PreparedStatement insertStatement = conn
                    .prepareStatement("INSERT INTO demo_table (id, name) VALUES (?, ?)")) {
                insertStatement.setInt(1, 1);
                insertStatement.setString(2, "Item 1");
                insertStatement.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }

            // Read data from another transaction
            conn.setAutoCommit(true); // Reset auto-commit for read operation
            try (Statement selectStatement = conn.createStatement()) {
                ResultSet resultSet = selectStatement.executeQuery("SELECT * FROM demo_table");
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // Close the connection
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void isolationlevelRepeatableRead() {
        try {
            Connection conn = DatabaseConnection.establishConnection();
            // Set the transaction isolation level to REPEATABLE READ
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            // Create a table if not exists
            try (Statement createStatement = conn.createStatement()) {
                createStatement.executeUpdate("CREATE TABLE IF NOT EXISTS demo_table (id INT PRIMARY KEY, name VARCHAR(50))");
            }

            // Insert data in a transaction
            conn.setAutoCommit(false);
            try (PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO demo_table (id, name) VALUES (?, ?)")) {
                insertStatement.setInt(1, 1);
                insertStatement.setString(2, "Item 1");
                insertStatement.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }

            // Read data from another transaction
            conn.setAutoCommit(true); // Reset auto-commit for read operation
            try (Statement selectStatement = conn.createStatement()) {
                ResultSet resultSet = selectStatement.executeQuery("SELECT * FROM demo_table");
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Attempt to insert conflicting data in a separate transaction
            conn.setAutoCommit(false);
            try (PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO demo_table (id, name) VALUES (?, ?)")) {
                insertStatement.setInt(1, 1); // Attempting to insert with the same ID
                insertStatement.setString(2, "New Item");
                insertStatement.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transaction failed: Data already exists.");
            }

            // Close the connection
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
