package edu.wpi.teamname.services.database;

import edu.wpi.teamname.services.algo.DataNode;
import java.sql.*;
import java.util.HashSet;

public class DatabaseAccessor {

  private static DatabaseAccessor single_instance = null;
  private static String databaseUrl =
      "jdbc:derby:src/main/java/edu/wpi/teamname/services/database/NodeEdgeData;user=admin;password=admin;create=true";
  private static Connection connection;
  private static Statement stmt;

  private DatabaseAccessor() {
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); // Apache Derby
      connection = DriverManager.getConnection(databaseUrl);
      stmt = connection.createStatement();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static DatabaseAccessor getInstance() {
    // To ensure only one instance is created
    if (single_instance == null) {
      single_instance = new DatabaseAccessor();
    }
    return single_instance;
  }

  public static HashSet<DataNode> getAllNodes() {
    HashSet<DataNode> nodeSet = new HashSet<>();
    String query = "SELECT * FROM NODES";
    try {
      ResultSet nodes = stmt.executeQuery(query);
      while (nodes.next()) {
        String nodeID = nodes.getString("NODEID");
        String floor = nodes.getString("FLOOR");
        String building = nodes.getString("BUILDING");
        String nodeType = nodes.getString("NODETYPE");
        String longName = nodes.getString("LONGNAME");
        String shortName = nodes.getString("SHORTNAME");
        double xPos = nodes.getDouble("XCOORD");
        double yPos = nodes.getDouble("YCOORD");
        nodeSet.add(
            new DataNode(xPos, yPos, nodeID, floor, building, nodeType, longName, shortName));
      }

    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    return nodeSet;
  }
}

class test {
  public static void main(String[] args) {
    DatabaseAccessor db = DatabaseAccessor.getInstance();

    HashSet<DataNode> nodes = db.getAllNodes();
    nodes.forEach(node -> System.out.println(node));
  }
}
