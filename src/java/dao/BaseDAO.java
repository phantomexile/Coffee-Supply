package dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Base DAO class that provides database connection functionality
 * All DAO classes should extend this class to get database access
 */
public abstract class BaseDAO {

    private final ResourceBundle bundle;

    public BaseDAO() {
        // Initialize the ResourceBundle to read from applications.properties
        this.bundle = ResourceBundle.getBundle("applications");
    }

    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    protected Connection getConnection() throws SQLException {
        try {
            // Load PostgreSQL JDBC driver
            Class.forName(bundle.getString("drivername"));

            // --- Database connection details from properties file ---
            String host = bundle.getString("url");
            String port = bundle.getString("port");
            String database = bundle.getString("database");
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            String sslmode = bundle.getString("sslmode");
            String options = bundle.getString("options");

            // --- Build the JDBC URL ---
            // The format for Supabase is: jdbc:postgresql://<host>:<port>/<database>
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, database);

            // --- Set connection properties ---
            Properties props = new Properties();
            props.setProperty("user", username);
            props.setProperty("password", password);
            // Explicit SSL flags for Pg JDBC + Supabase
            props.setProperty("ssl", "true");
            props.setProperty("sslmode", sslmode);

            // Supabase pooler requires the project option; do NOT URL-encode
            if (options != null && !options.trim().isEmpty()) {
                props.setProperty("options", options.trim());
            }
           
            // Get the connection using the URL and properties
            return DriverManager.getConnection(jdbcUrl, props);

        } catch (ClassNotFoundException ex) {
            System.out.println("Error: PostgreSQL JDBC driver not found.");
            throw new SQLException("JDBC Driver not found", ex);
        } catch (SQLException ex) {
            System.out.println("Error connecting to the database: " + ex.getMessage());
            // Re-throw the exception to be handled by the caller
            throw ex;
        }
    }

    /**
     * Helper method to get optional properties with default values
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default value
     */
    protected String getOptional(String key, String defaultValue) {
        try {
            String value = bundle.getString(key);
            return (value == null || value.trim().isEmpty()) ? defaultValue : value.trim();
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    /**
     * Test database connection (for debugging purposes)
     */
    public static void testConnection() {
        BaseDAO baseDAO = new BaseDAO() {
            // Anonymous implementation for testing
        };
        
        Connection conn = null;
        try {
            conn = baseDAO.getConnection();
            if (conn != null) {
                System.out.println("Connection established successfully!");
                System.out.println("Database Name: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Database Version: " + conn.getMetaData().getDatabaseProductVersion());
            } else {
                System.out.println("Failed to establish connection.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to establish connection due to an SQL error.");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
