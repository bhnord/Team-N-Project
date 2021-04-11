package edu.wpi.teamname.services.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseAccessor {

    private static DatabaseAccessor single_instance = null;
    private static String databaseUrl = "jdbc:derby:src/main/java/edu/wpi/teamname/services/database/NodeEdgeData;user=admin;password=admin;create=true";
    private static Connection connection;
    private static Statement stmt;

    private DatabaseAccessor() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); // Apache Derby
            connection =
                    DriverManager.getConnection(databaseUrl);
            stmt = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseAccessor DatabaseAccessor() {
        // To ensure only one instance is created
        if (single_instance == null) {
            single_instance = new DatabaseAccessor();
        }
        return single_instance;
    }

}
