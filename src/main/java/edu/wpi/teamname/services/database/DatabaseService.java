package edu.wpi.teamname.services.database;

import com.google.inject.Inject;
import edu.wpi.teamname.services.algo.DataNode;
import edu.wpi.teamname.services.algo.Edge;
import java.sql.*;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseService {
  /*
   Database service class. This class will be loaded as a Singleton by Guice.
  */

  private final Connection connection;
  private Statement stmt;

  @Inject
  DatabaseService(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public HashSet<DataNode> getAllNodes() {
    String query = "SELECT * FROM NODES";
    try {
      ResultSet nodes = stmt.executeQuery(query);
      return resultSetToNodes(nodes);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public DataNode getNode(String nodeID) {
    String query = "SELECT * FROM NODES WHERE id = '" + nodeID + "'";
    try {
      ResultSet rs = stmt.executeQuery(query);
      return (DataNode) resultSetToNodes(rs).toArray()[0];
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean addNode(DataNode node) {
    String query =
        "INSERT INTO NODES VALUES ('"
            + node.getNodeID()
            + "', "
            + node.get_x()
            + ", "
            + node.get_y()
            + ", '"
            + node.getFloor()
            + "', '"
            + node.getBuilding()
            + "', '"
            + node.getNodeType()
            + "', '"
            + node.getLongName()
            + "', '"
            + node.getShortName()
            + "')";
    try {
      return stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean addEdge(Edge e) {
    String str =
        "INSERT INTO EDGES VALUES ('"
            + e.get_edgeID()
            + "', '"
            + e.get_startNode()
            + "', '"
            + e.get_endNode()
            + "')";
    try {
      return stmt.execute(str);
    } catch (SQLException exception) {
      exception.printStackTrace();
      return false;
    }
  }

  public HashSet<Edge> getAllEdges() {
    String query = "SELECT * FROM EDGES";
    try {
      ResultSet rs = stmt.executeQuery(query);
      return resultSetToEdges(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Edge getEdge(String edgeID) {
    String query = "SELECT * FROM EDGES WHERE id = '" + edgeID + "'";
    try {
      ResultSet rs = stmt.executeQuery(query);
      return (Edge) resultSetToEdges(rs).toArray()[0];
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void deleteEdge(String edgeID) {
    String st = "DELETE FROM EDGES WHERE id = '" + edgeID + "'";
    try {
      stmt.execute(st);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public HashSet<Edge> getEdgesFromStartNode(String startNode) {
    String query = "SELECT * FROM EDGES WHERE StartNodeID = '" + startNode + "'";
    try {
      ResultSet rs = stmt.executeQuery(query);
      return resultSetToEdges(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public HashSet<Edge> getEdgesFromEndNode(String endNode) {
    String query = "SELECT * FROM EDGES WHERE EndNodeID = '" + endNode + "'";
    try {
      ResultSet rs = stmt.executeQuery(query);
      return resultSetToEdges(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void deleteEdgeRows() {
    String str = "DELETE * FROM EDGES";
    try {
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * loads CSV files into Database.
   *
   * @param csvPath full path to CSV File. (needs .csv)
   * @param tableName table name to put CSV File in. --Note table name needs to be in all caps.--
   */
  public void loadCSVtoTable(String csvPath, String tableName) {
    String str =
        "CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
            + tableName
            + "', '"
            + csvPath
            + "', ',', '\"', 'UTF-8',0)";
    try {
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private HashSet<DataNode> resultSetToNodes(ResultSet rs) {
    HashSet<DataNode> nodeSet = new HashSet<>();
    try {
      while (rs.next()) {
        String nodeID = rs.getString("id");
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

  private HashSet<Edge> resultSetToEdges(ResultSet rs) {
    HashSet<Edge> edgeSet = new HashSet<>();
    try {
      while (rs.next()) {
        String edgeID = rs.getString("id");
        String startNode = rs.getString("StartNodeID");
        String endNode = rs.getString("EndNodeID");
        edgeSet.add(new Edge(edgeID, startNode, endNode));
      }
      return edgeSet;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
