package edu.wpi.cs3733.d21.teamN.services.database;

import com.google.inject.Inject;
import edu.wpi.cs3733.d21.teamN.services.database.requests.Request;
import edu.wpi.cs3733.d21.teamN.services.database.requests.RequestType;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

public class RequestsTable {
  private final Connection connection;
  private Statement stmt;

  @Inject
  RequestsTable(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean addRequest(Request request, User currentUser) {
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

  public Request getRequest(int requestID) {
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
}
