package edu.wpi.cs3733.d21.teamN.services;

import edu.wpi.cs3733.d21.teamN.form.Form;
import edu.wpi.cs3733.d21.teamN.services.database.FormSerializer;
import java.sql.*;
import java.util.HashSet;

public class DynamicRequestsTable {
  private Connection connection;
  private Statement stmt;
  private FormSerializer fs = new FormSerializer();

  public static DynamicRequestsTable getInstance() {
    return SingletonHelper.table;
  }

  private static class SingletonHelper {
    private static final DynamicRequestsTable table = new DynamicRequestsTable();
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public HashSet<DynamicRequest> getAllDynamicRequests() {
    String query = "SELECT * FROM DynamicRequests";
    try {
      ResultSet rs = stmt.executeQuery(query);
      return resultSetToDynamicRequests(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public DynamicRequest addDynamicRequest(DynamicRequest request) {
    try {
      Blob blob = connection.createBlob();
      blob.setBytes(1, fs.toStream(request.getForm()));
      PreparedStatement ps =
          connection.prepareStatement(
              "INSERT INTO DYNAMICREQUESTS (DYNAMICREQUESTTYPEID, SENDERID, RECIEVERID, FORM) VALUES (?, ?, ?, ?)",
              new String[] {"ID"});
      ps.setInt(1, request.getDynamicRequestTypeId());
      ps.setInt(2, request.getSenderID());
      ps.setInt(3, request.getReceiverID());
      ps.setBlob(4, blob);
      ps.executeUpdate();
      blob.free();
      ResultSet rs = ps.getGeneratedKeys();
      rs.next();
      return getDynamicRequest(rs.getInt(1));
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public DynamicRequest getDynamicRequest(int id) {
    String str = "SELECT * FROM DYNAMICREQUESTS WHERE id = " + id;
    try {
      ResultSet rs = stmt.executeQuery(str);
      HashSet<DynamicRequest> set = resultSetToDynamicRequests(rs);
      if (set.size() > 0) {
        return (DynamicRequest) set.toArray()[0];
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private HashSet<DynamicRequest> resultSetToDynamicRequests(ResultSet rs) {
    HashSet<DynamicRequest> set = new HashSet<>();
    try {
      while (rs.next()) {
        Blob blob = rs.getBlob("FORM");
        Form form = fs.fromStream(blob.getBytes(1, (int) blob.length()));
        int id = rs.getInt("ID");
        int formId = rs.getInt("DYNAMICREQUESTTYPEID");
        int senderId = rs.getInt("SENDERID");
        int recieverId = rs.getInt("RECIEVERID");

        set.add(new DynamicRequest(id, formId, senderId, recieverId, form));
      }
      return set;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
