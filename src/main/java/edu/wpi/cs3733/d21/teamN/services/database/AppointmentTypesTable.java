package edu.wpi.cs3733.d21.teamN.services.database;

import com.google.inject.Inject;
import edu.wpi.cs3733.d21.teamN.form.Form;
import java.io.*;
import java.sql.*;
import java.util.HashSet;

public class AppointmentTypesTable {
  private final Connection connection;
  private Statement stmt;

  @Inject
  AppointmentTypesTable(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean addAppointmentType(String type, Form form) {
    try {
      Blob blob = connection.createBlob();
      blob.setBytes(1, toStream(form));
      PreparedStatement ps =
          connection.prepareStatement("INSERT INTO APPOINTMENTTYPES (TYPE, FORM) VALUES (?, ?)");
      ps.setString(1, type);
      ps.setBlob(2, blob);
      ps.execute();
      blob.free();
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

  public Form getAppointmentTypeForm(int id) {
    String str = "SELECT * FROM APPOINTMENTTYPES WHERE id = " + id;
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

  private HashSet<AppointmentType> resultSetToAppointmentTypes(ResultSet rs) throws SQLException {
    HashSet<AppointmentType> appointmentTypes = new HashSet<>();
    while (rs.next()) {
      Blob blob = rs.getBlob("FORM");
      Form form = fromStream(blob.getBytes(1, (int) blob.length()));
      appointmentTypes.add(new AppointmentType(rs.getInt("ID"), rs.getString("TYPE"), form));
    }
    return appointmentTypes;
  }
}
