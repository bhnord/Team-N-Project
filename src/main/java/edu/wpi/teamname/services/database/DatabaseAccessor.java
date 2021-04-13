package edu.wpi.teamname.services.database;

import edu.wpi.teamname.services.algo.DataNode;
import edu.wpi.teamname.services.algo.Edge;
import java.sql.*;
import java.util.HashSet;

public class DatabaseAccessor {

  private static DatabaseAccessor single_instance = null;
  private static String databaseUrl =
      "jdbc:derby:src/main/java/edu/wpi/teamname/services/database/DerbyDB;user=admin;password=admin;create=true";
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

  public static boolean addNode(DataNode node) {
    String query =
        "INSERT INTO NODES VALUES ('"
            + node.get_nodeID()
            + "', "
            + node.get_x()
            + ", "
            + node.get_y()
            + ", '"
            + node.get_floor()
            + "', '"
            + node.get_building()
            + "', '"
            + node.get_nodeType()
            + "', '"
            + node.get_longName()
            + "', '"
            + node.get_shortName()
            + "')";
    try {
      return stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static HashSet<Edge> getAllEdges() {
    String query = "SELECT * FROM EDGES";
    try {
      ResultSet rs = stmt.executeQuery(query);
      return resultSetToEdges(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static HashSet<Edge> getEdgesFromStartNode(String startNode) {
    String query = "SELECT * FROM EDGES WHERE STARTNODE = '" + startNode + "'";
    try {
      ResultSet rs = stmt.executeQuery(query);
      return resultSetToEdges(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static HashSet<Edge> getEdgesFromEndNode(String endNode) {
    String query = "SELECT * FROM EDGES WHERE EndNode = '" + endNode + "'";
    try {
      ResultSet rs = stmt.executeQuery(query);
      return resultSetToEdges(rs);
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

  private static HashSet<Edge> resultSetToEdges(ResultSet rs) {
    HashSet<Edge> edgeSet = new HashSet<>();
    try {
      while (rs.next()) {
        String edgeID = rs.getString("EDGESID");
        String startNode = rs.getString("STARTNODE");
        String endNode = rs.getString("ENDNODE");
        edgeSet.add(new Edge(edgeID, startNode, edgeID));
      }
      return edgeSet;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}

class test {
  public static void main(String[] args) {
    DatabaseAccessor db = DatabaseAccessor.getInstance();
    //        HashSet<DataNode> nodes = db.getAllNodes();
    //        nodes.forEach(node -> System.out.println(node));
    //        System.out.println();
    //
    //        System.out.println(db.getNode("CREST001L1"));
    //        System.out.println();
    //
    //        db.getAllEdges().forEach(edge -> System.out.println(edge));
    //        System.out.println();
    //
    //        db.getEdgesFromStartNode("CDEPT003L1").forEach(edge -> System.out.println(edge));
    //        System.out.println();
    //
    //    db.getEdgesFromEndNode("CLABS004L1").forEach(edge -> System.out.println(edge));

    String nodeID = "testid";
    String floor = "testfloor";
    String building = "testbuilding";
    String nodeType = "testtype";
    String longName = "testlname";
    String shortName = "testsname";
    double xPos = 0;
    double yPos = 1;
    db.addNode(new DataNode(xPos, yPos, nodeID, floor, building, nodeType, longName, shortName));
  }
}
