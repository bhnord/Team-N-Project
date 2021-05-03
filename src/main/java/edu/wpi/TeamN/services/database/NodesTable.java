package edu.wpi.TeamN.services.database;

import com.google.inject.Inject;
import edu.wpi.TeamN.services.algo.Node;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

public class NodesTable {

  private final Connection connection;
  private Statement stmt;

  @Inject
  NodesTable(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public HashSet<Node> getAllNodes() {
    String query = "SELECT * FROM NODES";
    try {
      ResultSet nodes = stmt.executeQuery(query);
      return resultSetToNodes(nodes);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public HashMap<String, Node> getAllNodesMap() {
    HashSet<Node> nodeSet = getAllNodes();
    HashMap<String, Node> nodeMap = new HashMap<>();
    for (Node node : nodeSet) {
      nodeMap.put(node.get_nodeID(), node);
    }
    return nodeMap;
  }

  /**
   * gets node from the NODES table
   *
   * @param nodeID
   * @return node from table
   */
  public Node getNode(String nodeID) {
    String query = "SELECT * FROM NODES WHERE id = '" + nodeID + "'";
    try {
      ResultSet rs = stmt.executeQuery(query);
      HashSet<Node> set = resultSetToNodes(rs);
      if (set.size() > 0) {
        return (Node) set.toArray()[0];
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * adds given node to the NODES table
   *
   * @param node the node to add to table
   * @return whether operation was carried out successfully
   */
  public boolean addNode(Node node) {
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
      stmt.execute(query);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * updates selected node data (id stays the same.)
   *
   * @param id
   * @param x
   * @param y
   * @param floor
   * @param building
   * @param nodeType
   * @param longName
   * @param shortName
   * @return
   */
  public boolean updateNode(
      String id,
      double x,
      double y,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    String str =
        "UPDATE NODES SET xcoord = "
            + x
            + ",ycoord = "
            + y
            + ", floor = "
            + floor
            + ", building = "
            + building
            + ", nodeType = "
            + nodeType
            + ", longName = "
            + longName
            + ", shortName = "
            + shortName
            + ", WHERE id ="
            + id;
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /** deletes all rows from NODES table */
  public void deleteNodeRows() {
    String str = "DELETE FROM NODES";
    try {
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * deletes node from DB - WILL ALSO DELETE ANY ASSOCIATED EDGES -
   *
   * @param nodeID node's id
   * @return whether the operation was carried out successfully
   */
  public boolean deleteNode(String nodeID) {
    String st = "DELETE FROM NODES WHERE id = '" + nodeID + "'";
    try {
      stmt.execute(st);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private HashSet<Node> resultSetToNodes(ResultSet rs) {
    HashSet<Node> nodeSet = new HashSet<>();
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
        nodeSet.add(new Node(xPos, yPos, nodeID, floor, building, nodeType, longName, shortName));
      }
      return nodeSet;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
