import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    // JDBC URL, username, and password of the MySQL server
    private static final String JDBC_URL = "your-value";
    private static final String JDBC_USER = "your-value";
    private static final String DATABASE_NAME = "your-value";
    private static final String JDBC_PASSWORD = "your-value";

    public static Connection establishConnection() {
        Connection conn = null;
        try {
            // Establishing a connection to the MySQL database
            conn = DriverManager.getConnection(JDBC_URL + DATABASE_NAME, JDBC_USER, JDBC_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
