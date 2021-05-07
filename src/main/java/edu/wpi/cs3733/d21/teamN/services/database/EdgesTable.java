package edu.wpi.cs3733.d21.teamN.services.database;

import com.google.inject.Inject;
import edu.wpi.cs3733.d21.teamN.services.algo.Edge;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

public class EdgesTable {
  private final Connection connection;
  private Statement stmt;

  @Inject
  EdgesTable(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean addEdge(Edge e) {
    String str =
        "INSERT INTO EDGES VALUES ('"
            + e.getEdgeID()
            + "', '"
            + e.getStartNode()
            + "', '"
            + e.getEndNode()
            + "')";
    try {
      stmt.execute(str);
      return true;
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

  public HashMap<String, Edge> getAllEdgesMap() {
    HashSet<Edge> edgeSet = getAllEdges();
    HashMap<String, Edge> edgeMap = new HashMap<>();
    for (Edge edge : edgeSet) {
      edgeMap.put(edge.getEdgeID(), edge);
    }
    return edgeMap;
  }

  public Edge getEdge(String edgeID) {
    String query = "SELECT * FROM EDGES WHERE id = '" + edgeID + "'";
    try {
      ResultSet rs = stmt.executeQuery(query);
      HashSet<Edge> edge = resultSetToEdges(rs);
      if (edge.size() > 0) {
        return (Edge) edge.toArray()[0];
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean updateEdge(String edgeID, String startNodeID, String endNodeID) {
    String str =
        "UPDATE EDGES SET startNodeID = "
            + startNodeID
            + ", endNodeID = "
            + endNodeID
            + " WHERE edgeID = "
            + edgeID;
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteEdge(String edgeID) {
    String st = "DELETE FROM EDGES WHERE id = '" + edgeID + "'";
    try {
      stmt.execute(st);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
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
    String str = "DELETE FROM EDGES";
    try {
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
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
