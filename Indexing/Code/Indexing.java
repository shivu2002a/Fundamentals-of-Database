import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Indexing {
    // JDBC URL, username, and password of the MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    private static final String JDBC_USER = "root";
    private static final String TABLE_NAME = "dummydata";    
    private static final String DATABASE_NAME = "fundamentals-of-database";

    private static final String JDBC_PASSWORD = "localhost3306";

    public static void main(String[] args) {
        Connection conn = null;
        try {
            // Establishing a connection to the MySQL database
            conn = DriverManager.getConnection(JDBC_URL + DATABASE_NAME, JDBC_USER, JDBC_PASSWORD);
            if (conn != null) {
                System.out.println("Connected to the database!");
                // Step 2: Insert 11 million dummy records
                insertDummyRecords(conn);
                // Step 3: Query for a value and show execution time
                long startTime = System.currentTimeMillis();
                int value = queryValue(conn, "600000");
                long endTime = System.currentTimeMillis();
                System.out.println("Query execution time: " + (endTime - startTime) + " ms");
                System.out.println("Value retrieved: " + value);
                // Step 4: Create indices on table, query for a value, and show the difference in execution times
                createIndices(conn);
                startTime = System.currentTimeMillis();
                value = queryValue(conn, "700000");
                endTime = System.currentTimeMillis();
                System.out.println("Query with indices execution time: " + (endTime - startTime) + " ms");
                System.out.println("Value retrieved with indices: " + value);
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void insertDummyRecords(Connection conn) throws SQLException {
        String insertQuery = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(insertQuery);
        // Inserting 11 million records
        for (int i = 1; i <= 1000000; i++) {
            pstmt.setInt(1, i);
            pstmt.setString(2, generateRandomString());
            pstmt.setString(3, generateRandomString());
            pstmt.executeUpdate();
        }
        System.out.println("Inserted 11 million dummy records.");
    }

    public static String generateRandomString() {
        StringBuilder sb  = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = characters.length();
        for(int i = 0; i < 3; i++) {
            sb.append(characters.charAt(new Random().nextInt(length)));
        }
        return sb.toString();
    } 

    private static int queryValue(Connection conn, String valueToQuery) throws SQLException {
        // Creating a PreparedStatement for the query
        String query = "SELECT val1 FROM dummydata WHERE dummy_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, valueToQuery);
        // Executing the query and retrieving the value
        ResultSet rs = pstmt.executeQuery();
        int value = -1; // Change the datatype as per your column type
        if (rs.next()) {
            value = rs.getInt(1); // Change column name and datatype
        }
        rs.close();
        return value;
    }

    private static void createIndices(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE INDEX dummydata_index ON dummydata(dummy_id)");
        System.out.println("Created index on the table for the column 'dummy_id'.");
        stmt.close();
    }
}
