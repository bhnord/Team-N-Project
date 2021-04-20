package edu.wpi.teamname.services.database;

import com.google.inject.Inject;
import edu.wpi.teamname.services.algo.Edge;
import edu.wpi.teamname.services.algo.Node;
import edu.wpi.teamname.services.database.requests.Request;
import edu.wpi.teamname.services.database.requests.RequestType;
import edu.wpi.teamname.services.database.users.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseService {
  /*
   Database service class. This class will be loaded as a Singleton by Guice.
  */
  User currentUser = new Employee("1", "username111");

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
            + " WHERE id ="
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

  public boolean addRequest(Request request) {
    String str =
        "INSERT INTO REQUESTS (TYPE, SENDERID, RECEIVERID, CONTENT, NOTES) VALUES ('"
            + request.getType()
            + "', "
            + currentUser.getId()
            + ", "
            + request.getReceiverID()
            + ", '"
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
    String str = "DELETE FROM REQUESTS WHERE id = '" + requestID + "'";
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public Request getReqeust(int requestID){
    String str = "SELECT * FROM REQUESTS WHERE id = " + requestID;
    try{
      ResultSet rs = stmt.executeQuery(str);
      return (Request) resultSetToRequest(rs).toArray()[0];
    } catch(SQLException e){
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

  public boolean addUser(User user, String password){
    String str = "INSERT INTO USERS VALUES ("
            + user.getId()
            + ", '"
            + user.getUsername()
            + "', '"
            + password
            + "', '"
            + user.getType().toString()
            + "')";
    try{
      stmt.execute(str);
      return true;
    } catch (SQLException e){
      e.printStackTrace();
      return false;
    }
  }

  public User getUserById(String id){
    String str = "SELECT * FROM USERS WHERE ID = " + id;
    try {
      ResultSet rs = stmt.executeQuery(str);
      return (User) resultSetToUsers(rs).toArray()[0];
    } catch (SQLException e){
      return null;
    }
  }

  public User getUserByUsername(String username){
    String str = "SELECT * FROM USERS WHERE USERNAME = " + username;
    try{
      ResultSet rs = stmt.executeQuery(str);
      return (User) resultSetToUsers(rs).toArray()[0];
    } catch (SQLException e){
      e.printStackTrace();
      return null;
    }
  }

  public boolean deleteUser(String username){
    String str = "DELETE USERS WHERE USERNAME = " + username;
    try{
      stmt.execute(str);
      return true;
    } catch (SQLException e){
      return false;
    }
  }









  private HashSet<User> resultSetToUsers(ResultSet rs) {
    HashSet<User>  users = new HashSet<>();
    try {
      while (rs.next()) {
        switch(rs.getString("USERTYPE")){
          case "Patient":
            users.add(new Patient(rs.getString("ID"), rs.getString("USERNAME")));
            break;
          case "Employee":
            users.add(new Employee(rs.getString("ID"), rs.getString("USERNAME")));
            break;
          case "Administrator":
            users.add(new Administrator(rs.getString("ID"), rs.getString("USERNAME")));
            break;
        }
      }
      return users;
    } catch (SQLException e){
      e.printStackTrace();
      return null;
    }
  }

  private HashSet<Request> resultSetToRequest(ResultSet rs) throws SQLException {
    HashSet<Request> requests = new HashSet<>();
    while (rs.next()) {
      requests.add(
          new Request(
              RequestType.valueOf(rs.getString("TYPE")),
              rs.getInt("ID"),
              rs.getInt("SENDERID"),
              rs.getInt("RECIEVERID"),
              rs.getString("CONTENT"),
              rs.getString("NOTES")));
    }
    return requests;
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
              + "Type varchar(30), "
              + "SenderID INT NOT NULL REFERENCES Users (id), "
              + "ReceiverID INT REFERENCES Users (id), "
              + "Room varchar(25) REFERENCES Nodes (id),"
              + "Content varchar(700), "
              + "Notes varchar(200), "
              + "CONSTRAINT chk_Type CHECK (Type IN "
              + "('AUDIO_VISUAL', 'COMPUTER_SERVICE', 'EXTERNAL_PATIENT_TRANSPORTATION', 'FLORAL', 'FOOD_DELIVERY', 'GIFT_DELIVERY', ' INTERNAL_PATIENT_TRANSPORTATION', 'LANGUAGE_INTERPRETER', "
              + "'LAUNDRY', 'MAINTENANCE', 'MEDICINE_DELIVERY', 'RELIGIOUS', "
              + "'SANITATION', 'SECURITY')),"
              + "PRIMARY KEY (id))";
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
