package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The Connect class is responsible for establishing and managing the database connection.
 */
public class Connect {

    private String driver = "org.postgresql.Driver";
    private String host = "195.150.230.208";
    private String port = "5432"; // Required when it's not the default for the database
    private String dbname = "2023_ciura_damian";
    private String user = "2023_ciura_damian";
    private String pass = "35192";

    private String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
    private Connection connection;

    /**
     * Initializes the Connect object and establishes a database connection.
     */
    public Connect() {
        connection = makeConnection();
    }

    /**
     * Retrieves the established database connection.
     *
     * @return The database connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException sqle) {
            System.err.println("Error when closing the connection: " + sqle);
        }
    }

    /**
     * Establishes a connection to the database.
     *
     * @return The established database connection.
     */
    private Connection makeConnection() {
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, pass);
            return connection;
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Error loading the driver: " + cnfe);
            return null;
        } catch (SQLException sqle) {
            System.err.println("Error establishing the connection: " + sqle);
            return null;
        }
    }
}
