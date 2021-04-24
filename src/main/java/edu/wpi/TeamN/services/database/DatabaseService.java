package edu.wpi.TeamN.services.database;

import com.google.inject.Inject;
import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.database.requests.Request;
import edu.wpi.TeamN.services.database.requests.RequestType;
import edu.wpi.TeamN.services.database.users.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseService {
  /*
   Database service class. This class will be loaded as a Singleton by Guice.
  */
  User currentUser = new Employee("1", "username111"); // TODO IMPLEMENT WITH LOGIN PAGE

  private final Connection connection;
  private Statement stmt;
  @Inject UsersTable usersTable;
  @Inject NodesTable nodesTable;
  @Inject EdgesTable edgesTable;

  @Inject
  DatabaseService(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /// NODES
  public HashSet<Node> getAllNodes() {
    return nodesTable.getAllNodes();
  }

  public HashMap<String, Node> getAllNodesMap() {
    return nodesTable.getAllNodesMap();
  }

  public Node getNode(String nodeID) {
    return nodesTable.getNode(nodeID);
  }

  public boolean addNode(Node node) {
    return nodesTable.addNode(node);
  }

  public boolean updateNode(
      String id,
      double x,
      double y,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    return nodesTable.updateNode(id, x, y, floor, building, nodeType, longName, shortName);
  }

  public void deleteNodeRows() {
    nodesTable.deleteNodeRows();
  }

  public boolean deleteNode(String nodeID) {
    return nodesTable.deleteNode(nodeID);
  }

  /// EDGES
  public boolean addEdge(Edge e) {
    return edgesTable.addEdge(e);
  }

  public HashSet<Edge> getAllEdges() {
    return edgesTable.getAllEdges();
  }

  public HashMap<String, Edge> getAllEdgesMap() {
    return edgesTable.getAllEdgesMap();
  }

  public Edge getEdge(String edgeID) {
    return edgesTable.getEdge(edgeID);
  }

  public boolean updateEdge(String edgeID, String startNodeID, String endNodeID) {
    return edgesTable.updateEdge(edgeID, startNodeID, endNodeID);
  }

  public boolean deleteEdge(String edgeID) {
    return edgesTable.deleteEdge(edgeID);
  }

  public HashSet<Edge> getEdgesFromStartNode(String startNode) {
    return edgesTable.getEdgesFromStartNode(startNode);
  }

  public HashSet<Edge> getEdgesFromEndNode(String endNode) {
    return edgesTable.getEdgesFromEndNode(endNode);
  }

  public void deleteEdgeRows() {
    edgesTable.deleteEdgeRows();
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

  public boolean addRequest(Request request) {
    String str =
        "INSERT INTO REQUESTS (TYPE, SENDERID, RECEIVERID, ROOM, CONTENT, NOTES) VALUES ('"
            + request.getType()
            + "', "
            + currentUser.getId()
            + ", "
            + request.getReceiverID()
            + ", '"
            + request.getRoomNodeId()
            + "', '"
            + request.getContent()
            + "', '"
            + request.getNotes()
            + "')";
    try {
      return stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteRequest(int requestID) {
    String str = "DELETE FROM REQUESTS WHERE id = " + requestID;
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public HashSet<Request> getAllRequests() {
    String str = "SELECT * FROM REQUESTS";
    try {
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToRequest(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return new HashSet<Request>();
    }
  }

  public Request getReqeust(int requestID) {
    String str = "SELECT * FROM REQUESTS WHERE id = " + requestID;
    try {
      ResultSet rs = stmt.executeQuery(str);
      return (Request) resultSetToRequest(rs).toArray()[0];
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public HashSet<Request> getRequestBySender(int senderID) {
    String str = "SELECT * FROM REQUESTS WHERE SENDERID = " + senderID;
    try {
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToRequest(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return new HashSet<Request>();
    }
  }

  public HashSet<Request> getRequestByReceiver(int receiverID) {
    String str = "SELECT * FROM REQUESTS WHERE SENDERID = " + receiverID;
    try {
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToRequest(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return new HashSet<Request>();
    }
  }

  public boolean updateRequest(
      int requestID, RequestType type, int senderID, int receiverID, String content, String notes) {
    String str =
        "UPDATE REQUESTS SET TYPE = "
            + type.toString()
            + ", SENDERID = "
            + senderID
            + ", RECEIVERID = "
            + receiverID
            + ", CONTENT = "
            + content
            + ", NOTES = "
            + notes
            + " WHERE ID = "
            + requestID;
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  //////////////////////////
  boolean addUser(User user, String password) {
    return usersTable.addUser(user, password);
  }

  public User getUserById(String id) {
    return usersTable.getUserById(id);
  }

  public User getUserByUsername(String username) {
    return usersTable.getUserByUsername(username);
  }

  public HashMap<String, User> getUsersByType(UserType userType) {
    return usersTable.getUsersByType(userType);
  }

  public boolean deleteUser(String username) {
    return usersTable.deleteUser(username);
  }

  private HashSet<Request> resultSetToRequest(ResultSet rs) throws SQLException {
    HashSet<Request> requests = new HashSet<>();
    while (rs.next()) {
      requests.add(
          new Request(
              RequestType.valueOf(rs.getString("TYPE")),
              rs.getInt("ID"),
              rs.getInt("SENDERID"),
              rs.getInt("RECEIVERID"),
              rs.getString("ROOM"),
              rs.getString("CONTENT"),
              rs.getString("NOTES")));
    }
    return requests;
  }

  public void initTables() {
    try {
      String str =
          "CREATE TABLE Nodes( "
              + "id varchar(25), "
              + "xcoord DOUBLE NOT NULL, "
              + "ycoord DOUBLE NOT NULL, "
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
              + "startNodeID varchar(25) REFERENCES Nodes (id) ON DELETE CASCADE, "
              + "endNodeID varchar(25) REFERENCES Nodes (id) ON DELETE CASCADE, "
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
              + "Type varchar(35), "
              + "SenderID INT NOT NULL REFERENCES Users (id), "
              + "ReceiverID INT REFERENCES Users (id), "
              + "Room varchar(25) NOT NULL REFERENCES Nodes (id),"
              + "Content varchar(700), "
              + "Notes varchar(200), "
              + "CONSTRAINT chk_Type CHECK (Type IN "
              + "('AUDIO_VISUAL', 'COMPUTER_SERVICE', 'EXTERNAL_PATIENT_TRANSPORTATION', 'FLORAL', 'FOOD_DELIVERY', 'GIFT_DELIVERY', 'INTERNAL_PATIENT_TRANSPORTATION', 'LANGUAGE_INTERPRETER', "
              + "'LAUNDRY', 'MAINTENANCE', 'MEDICINE_DELIVERY', 'RELIGIOUS', "
              + "'SANITATION', 'SECURITY')),"
              + "PRIMARY KEY (id))";
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
