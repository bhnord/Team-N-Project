package edu.wpi.cs3733.d21.teamN.services.database;

import com.google.inject.Inject;
import edu.wpi.cs3733.d21.teamN.App;
import edu.wpi.cs3733.d21.teamN.form.Form;
import edu.wpi.cs3733.d21.teamN.services.database.users.*;
import java.io.*;
import java.sql.*;
import java.util.HashSet;

class AppointmentsTable {
  private final Connection connection;
  private Statement stmt;

  @Inject
  AppointmentsTable(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean addAppointment(
      int appointmentTypeId,
      int patientId,
      int assignedStaffId,
      Form formContent,
      Timestamp apptDateTime,
      String associatedRoomId) {
    try {

      Blob blob = connection.createBlob();
      blob.setBytes(1, toStream(formContent));
      String str =
          "INSERT INTO APPOINTMENTS (AppointmentTypeId, PatientId, AssignedStaffId, Form, TimeOfAppointment, CheckInStatus, AssociatedRoomId) VALUES (?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement ps = connection.prepareStatement(str);
      ps.setInt(1, appointmentTypeId);
      ps.setInt(2, patientId);
      ps.setInt(3, assignedStaffId);
      ps.setBlob(4, blob);
      ps.setTimestamp(5, apptDateTime);
      ps.setBoolean(6, false);
      ps.setString(7, associatedRoomId);
      ps.execute();
      blob.free();
      ps.close();
      return true;
      //      String str =
      //          "INSERT INTO APPOINTMENTS (AppointmentTypeId, PatientId, AssignedStaffId,
      // FormContent, TimeOfAppointment, CheckInStatus, AssociatedRoomId) VALUES ("
      //              + appointmentTypeId
      //              + ", "
      //              + patientId
      //              + ", "
      //              + assignedStaffId
      //              + ", "
      //              + blob
      //              + ", "
      //              + apptDateTime.toString()
      //              + ", "
      //              + false
      //              + ", '"
      //              + associatedRoomId
      //              + "')";
      //
      //      stmt.execute(str);
      // return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateAppointment(int appointmentId, Form formContent) {

    try {
      Blob blob = connection.createBlob();
      blob.setBytes(1, toStream(formContent));
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

  public HashSet<Appointment> getAppointmentsByPatientId(int patientId){
    String str = "SELECT * FROM APPOINTMENTS WHERE PatientId = " + patientId;
    try{
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToAppointments(rs);
    } catch (SQLException e){
      e.printStackTrace();
      return null;
    }
  }

  public HashSet<Appointment> getAppointmentsByAssignedStaffId(int assignedStaffId){
    String str = "SELECT * FROM APPOINTMENTS WHERE AssignedStaffId = " + assignedStaffId;
    try{
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToAppointments(rs);
    } catch (SQLException e){
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
      return fromStream(blob.getBytes(1, (int) blob.length()));
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private Form fromStream(byte[] stream) {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(stream);
      ObjectInputStream ois = new ObjectInputStream(bais);
      return (Form) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  private byte[] toStream(Form f) {
    byte[] stream = null;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(f);
      stream = baos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stream;
  }

  private HashSet<Appointment> resultSetToAppointments(ResultSet rs) throws SQLException {
    HashSet<Appointment> appointments = new HashSet<>();
    while (rs.next()) {
      Blob blob = rs.getBlob("FORM");
      Form form = fromStream(blob.getBytes(1, (int) blob.length()));
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
