package edu.wpi.cs3733.d21.teamN.services.database;

import java.io.*;
import java.sql.*;
import java.util.HashSet;

public class AppointmentTypesTable {
  private Connection connection;
  private Statement stmt;
  private FormSerializer fs = new FormSerializer();

  public static AppointmentTypesTable getInstance() {
    return SingletonHelper.table;
  }

  private static class SingletonHelper {
    private static final AppointmentTypesTable table = new AppointmentTypesTable();
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean deleteAppointmentType(int id) {
    String st = "DELETE FROM APPOINTMENTTYPES WHERE id = " + id + "";
    try {
      stmt.execute(st);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateAppointmentType(AppointmentType appointmentType) {
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "UPDATE APPOINTMENTTYPES SET TYPE = ?, FORMID = ? WHERE id = ?");
      ps.setString(1, appointmentType.getType());
      ps.setInt(2, appointmentType.getFormId());
      ps.setInt(3, appointmentType.getId());
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

  public boolean addAppointmentType(String type, int formId) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("INSERT INTO APPOINTMENTTYPES (TYPE, FORMID) VALUES (?, ?)");
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

  public HashSet<AppointmentType> getAllAppointmentTypes() {
    String str = "SELECT * FROM APPOINTMENTTYPES";
    try {
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToAppointmentTypes(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public AppointmentType getAppointmentType(int id) {
    try {
      String str = "SELECT * FROM APPOINTMENTTYPES WHERE id = " + id;
      ResultSet rs = stmt.executeQuery(str);
      HashSet<AppointmentType> set = resultSetToAppointmentTypes(rs);
      if (set.size() > 0) {
        return (AppointmentType) set.toArray()[0];
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public AppointmentType getAppointmentTypeByType(String type) {
    try {
      String str = "SELECT * FROM APPOINTMENTTYPES WHERE TYPE = '" + type + "'";
      ResultSet rs = stmt.executeQuery(str);
      HashSet<AppointmentType> set = resultSetToAppointmentTypes(rs);
      if (set.size() > 0) {
        return (AppointmentType) set.toArray()[0];
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private HashSet<AppointmentType> resultSetToAppointmentTypes(ResultSet rs) throws SQLException {
    HashSet<AppointmentType> appointmentTypes = new HashSet<>();
    while (rs.next()) {
      appointmentTypes.add(
          new AppointmentType(rs.getInt("ID"), rs.getString("TYPE"), rs.getInt("FORMID")));
    }
    return appointmentTypes;
  }
}
