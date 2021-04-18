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

  /**
   * loads CSV files into Database.
   *
   * @param csvPath full path to CSV File. (needs .csv)
   * @param tableName table name to put CSV File in. --Note table name needs to be in all caps.--
   */
  public boolean loadCSVtoTable(String csvPath, String tableName) {
    String str =
        "CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
            + tableName
            + "', '"
            + csvPath
            + "', ',', '\"', 'UTF-8',0)";
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
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

  public void initTables() {
    try {
      String str =
          "CREATE TABLE Nodes( "
              + "id varchar(25), "
              + "xcoord INT NOT NULL, "
              + "ycoord INT NOT NULL, "
              + "floor varchar(25), "
              + "building varchar(25), "
              + "nodeType varchar(25), "
              + "longName varchar(45), "
              + "shortName varchar(35), "
              + "PRIMARY KEY (id))";
      stmt.execute(str);
      str =
          "CREATE TABLE Edges( "
              + "id varchar(25), "
              + "startNodeID varchar(25) REFERENCES Nodes (id), "
              + "endNodeID varchar(25) REFERENCES Nodes (id), "
              + "PRIMARY KEY (id))";

      stmt.execute(str);
      str =
          "CREATE TABLE Users("
              + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
              + "Username varchar(40) NOT NULL UNIQUE, "
              + "Password varchar(40) NOT NULL,"
              + "UserType varchar(15),"
              + "CONSTRAINT chk_UserType CHECK (UserType IN ('Patient', 'Employee', 'Administrator')),"
              + "PRIMARY KEY (id))";
      stmt.execute(str);
      str =
          "CREATE TABLE Requests("
              + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
              + "Type varchar(30), "
              + "SenderID INT NOT NULL REFERENCES Users (id), "
              + "ReceiverID INT REFERENCES Users (id), "
              + "Content varchar(700), "
              + "Notes varchar(200), "
              + "CONSTRAINT chk_Type CHECK (Type IN "
              + "('Food Delivery', 'Language Interpreter', 'Sanitation', 'Laundry', 'Gift Delivery', 'Floral Delivery', 'Medicine Delivery', "
              + "'Religious Request', 'Internal Patient Transportation', 'External Patient Transportation', 'Security', 'Facilities Maintenance', "
              + "'Computer Service', 'Audio/Visual')),"
              + "PRIMARY KEY (id))";
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
