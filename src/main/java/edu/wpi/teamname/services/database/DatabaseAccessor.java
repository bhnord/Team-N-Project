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
      return resultSetToNodes(nodes);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static DataNode getNode(String nodeID) {
    String query = "SELECT * FROM NODES WHERE NODEID = '" + nodeID + "'";
    try {
      ResultSet rs = stmt.executeQuery(query);
      return (DataNode) resultSetToNodes(rs).toArray()[0];
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static HashSet<DataNode> resultSetToNodes(ResultSet rs) {
    HashSet<DataNode> nodeSet = new HashSet<>();
    try {
      while (rs.next()) {
        String nodeID = rs.getString("NODEID");
        String floor = rs.getString("FLOOR");
        String building = rs.getString("BUILDING");
        String nodeType = rs.getString("NODETYPE");
        String longName = rs.getString("LONGNAME");
        String shortName = rs.getString("SHORTNAME");
        double xPos = rs.getDouble("XCOORD");
        double yPos = rs.getDouble("YCOORD");
        nodeSet.add(
            new DataNode(xPos, yPos, nodeID, floor, building, nodeType, longName, shortName));
      }
      return nodeSet;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}

class test {
  public static void main(String[] args) {
    DatabaseAccessor db = DatabaseAccessor.getInstance();
    HashSet<DataNode> nodes = db.getAllNodes();
    //    nodes.forEach(node -> System.out.println(node));

    System.out.println(db.getNode("CREST001L1"));
  }
}
