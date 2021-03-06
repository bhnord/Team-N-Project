package edu.wpi.cs3733.d21.teamN.services.database;

import edu.wpi.cs3733.d21.teamN.form.Form;
import java.io.*;
import java.sql.*;
import java.util.HashSet;

class AppointmentsTable {
  private Connection connection;
  private Statement stmt;
  private FormSerializer fs = new FormSerializer();

  public static AppointmentsTable getInstance() {
    return SingletonHelper.table;
  }

  private static class SingletonHelper {
    private static final AppointmentsTable table = new AppointmentsTable();
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Appointment addAppointment(Appointment appointment) {
    try {
      Blob blob = connection.createBlob();
      blob.setBytes(1, fs.toStream(appointment.getForm()));
      //      String str =
      //          ;
      PreparedStatement ps =
          connection.prepareStatement(
              "INSERT INTO APPOINTMENTS (APPOINTMENTTYPEID, PATIENTID, ASSIGNEDSTAFFID, FORM, TIMEOFAPPOINTMENT, CHECKINSTATUS, ASSOCIATEDROOMID) VALUES (?, ?, ?, ?, ?, ?, ?)",
              new String[] {"ID"});
      ps.setInt(1, appointment.getAppointmentTypeId());
      ps.setInt(2, appointment.getPatientId());
      ps.setInt(3, appointment.getAssignedStaffId());
      ps.setBlob(4, blob);
      ps.setTimestamp(5, appointment.getTimeOfAppointment());
      ps.setBoolean(6, false);
      ps.setString(7, appointment.getAssociatedRoomId());
      ps.executeUpdate();
      blob.free();
      ResultSet rs = ps.getGeneratedKeys();
      rs.next();
      return getAppointment(rs.getInt(1));
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean updateAppointment(Appointment appointment) {
    try {
      Blob blob = connection.createBlob();
      blob.setBytes(1, fs.toStream(appointment.getForm()));
      //      String str =
      //          ;
      PreparedStatement ps =
          connection.prepareStatement(
              "UPDATE APPOINTMENTS SET APPOINTMENTTYPEID = ?, PATIENTID = ?, ASSIGNEDSTAFFID = ?, FORM = ?, TIMEOFAPPOINTMENT = ?, CHECKINSTATUS = ?, ASSOCIATEDROOMID = ? WHERE Id = ?");
      ps.setInt(1, appointment.getAppointmentTypeId());
      ps.setInt(2, appointment.getPatientId());
      ps.setInt(3, appointment.getAssignedStaffId());
      ps.setBlob(4, blob);
      ps.setTimestamp(5, appointment.getTimeOfAppointment());
      ps.setBoolean(6, appointment.isCheckInStatus());
      ps.setString(7, appointment.getAssociatedRoomId());
      ps.setInt(8, appointment.getId());
      ps.executeUpdate();
      blob.free();
      ps.close();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteAppointment(int id) {
    String st = "DELETE FROM APPOINTMENTS WHERE Id = " + id + "";
    try {
      stmt.execute(st);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateAppointment(int appointmentId, Form formContent) {

    try {
      Blob blob = connection.createBlob();
      blob.setBytes(1, fs.toStream(formContent));
      String str = "UPDATE APPOINTMENTS SET FORM = ? WHERE Id = ?";
      PreparedStatement ps = connection.prepareStatement(str);
      ps.setBlob(1, blob);
      ps.setInt(2, appointmentId);
      ps.execute();
      blob.free();
      ps.close();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public HashSet<Appointment> getAllAppointments() {
    String str = "SELECT * FROM APPOINTMENTS";
    try {
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToAppointments(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public HashSet<Appointment> getAppointmentsByPatientId(int patientId) {
    String str = "SELECT * FROM APPOINTMENTS WHERE PatientId = " + patientId;
    try {
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToAppointments(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public HashSet<Appointment> getAppointmentsByAssignedStaffId(int assignedStaffId) {
    String str = "SELECT * FROM APPOINTMENTS WHERE AssignedStaffId = " + assignedStaffId;
    try {
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToAppointments(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Appointment getAppointment(int appointmentId) {
    String str = "SELECT * FROM APPOINTMENTS WHERE id = " + appointmentId;
    try {
      ResultSet rs = stmt.executeQuery(str);
      HashSet<Appointment> set = resultSetToAppointments(rs);
      if (set.size() > 0) {
        return (Appointment) set.toArray()[0];
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Form getAppointmentForm(int appointmentId) {
    String str = "SELECT * FROM APPOINTMENTS WHERE id = " + appointmentId;
    try {
      ResultSet rs = stmt.executeQuery(str);
      rs.next();
      Blob blob = rs.getBlob("FORM");
      return fs.fromStream(blob.getBytes(1, (int) blob.length()));
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private HashSet<Appointment> resultSetToAppointments(ResultSet rs) throws SQLException {
    HashSet<Appointment> appointments = new HashSet<>();
    while (rs.next()) {
      Blob blob = rs.getBlob("FORM");
      Form form = fs.fromStream(blob.getBytes(1, (int) blob.length()));
      appointments.add(
          new Appointment(
              rs.getInt("ID"),
              rs.getInt("APPOINTMENTTYPEID"),
              rs.getInt("PATIENTID"),
              rs.getInt("ASSIGNEDSTAFFID"),
              form,
              rs.getTimestamp("TIMEOFAPPOINTMENT"),
              rs.getBoolean("CHECKINSTATUS"),
              rs.getString("ASSOCIATEDROOMID")));
    }
    return appointments;
  }
}
