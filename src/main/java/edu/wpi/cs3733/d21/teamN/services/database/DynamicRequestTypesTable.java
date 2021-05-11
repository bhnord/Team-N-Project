package edu.wpi.cs3733.d21.teamN.services.database;

import java.sql.*;
import java.util.HashSet;

public class DynamicRequestTypesTable {
  private Connection connection;
  private Statement stmt;
  private FormSerializer fs = new FormSerializer();

  public static DynamicRequestTypesTable getInstance() {
    return SingletonHelper.table;
  }

  private static class SingletonHelper {
    private static final DynamicRequestTypesTable table = new DynamicRequestTypesTable();
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean addDynamicRequestType(String type, int formId) {
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "INSERT INTO DYNAMICREQUESTTYPES (TYPE, FORMID) VALUES (?, ?)");
      ps.setString(1, type);
      ps.setInt(2, formId);
      ps.execute();
      ps.close();
      return true;
      // String str = "INSERT INTO APPOINTMENTTYPES VALUES " + type + ", " + blob;
      // stmt.execute(str);
      // return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public HashSet<DynamicRequestType> getAllDynamicRequestTypes() {
    String str = "SELECT * FROM DYNAMICREQUESTTYPES";
    try {
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToDynamicRequestTypes(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public DynamicRequestType getDynamicRequestType(int id) {
    try {
      String str = "SELECT * FROM DYNAMICREQUESTTYPES WHERE id = " + id;
      ResultSet rs = stmt.executeQuery(str);
      HashSet<DynamicRequestType> set = resultSetToDynamicRequestTypes(rs);
      if (set.size() > 0) {
        return (DynamicRequestType) set.toArray()[0];
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private HashSet<DynamicRequestType> resultSetToDynamicRequestTypes(ResultSet rs)
      throws SQLException {
    HashSet<DynamicRequestType> set = new HashSet<>();
    while (rs.next()) {
      set.add(new DynamicRequestType(rs.getInt("ID"), rs.getString("TYPE"), rs.getInt("FORMID")));
    }
    return set;
  }
}
