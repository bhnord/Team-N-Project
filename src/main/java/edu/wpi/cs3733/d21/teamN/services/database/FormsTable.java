package edu.wpi.cs3733.d21.teamN.services.database;

import edu.wpi.cs3733.d21.teamN.form.Form;
import java.sql.*;
import java.util.HashSet;

public class FormsTable {

  private Connection connection;
  private Statement stmt;
  FormSerializer fs = new FormSerializer();

  public static FormsTable getInstance() {
    return SingletonHelper.table;
  }

  private static class SingletonHelper {
    private static final FormsTable table = new FormsTable();
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean addForm(NamedForm nForm) {
    try {
      Blob blob = connection.createBlob();
      blob.setBytes(1, fs.toStream(nForm.getForm()));
      String str = "INSERT INTO FORMS (NAME, FORM, ISSERVICEREQUEST) VALUES (?, ?, ?)";
      PreparedStatement ps = connection.prepareStatement(str);
      ps.setString(1, nForm.getName());
      ps.setBlob(2, blob);
      ps.setBoolean(3, nForm.isRequest());
      ps.executeUpdate();
      blob.free();
      ps.close();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateForm(NamedForm nForm) {
    try {
      Blob blob = connection.createBlob();
      blob.setBytes(1, fs.toStream(nForm.getForm()));
      String str = "UPDATE FORMS SET FORM = ?, NAME = ?, ISSERVICEREQUEST = ? WHERE id = ?";
      PreparedStatement ps = connection.prepareStatement(str);
      ps.setBlob(1, blob);
      ps.setString(2, nForm.getName());
      ps.setBoolean(3, nForm.isRequest());
      ps.setInt(4, nForm.getId());
      ps.executeUpdate();
      blob.free();
      ps.close();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteForm(int id) {
    String st = "DELETE FROM FORMS WHERE id = " + id + "";
    try {
      stmt.execute(st);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public HashSet<NamedForm> getAllForms() {
    try {
      String str = "SELECT * FROM FORMS";
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToNamedForms(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public HashSet<NamedForm> getAllFormsNotServiceRequest() {
    try {
      String str = "SELECT * FROM FORMS WHERE ISSERVICEREQUEST = FALSE";
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToNamedForms(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public NamedForm getForm(int id) {
    try {
      String str = "SELECT * FROM FORMS WHERE ID = " + id;
      ResultSet rs = stmt.executeQuery(str);
      return (NamedForm) resultSetToNamedForms(rs).toArray()[0];
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public NamedForm getFormByName(String name) {
    try {
      String str = "SELECT * FROM FORMS WHERE NAME = '" + name + "'";
      ResultSet rs = stmt.executeQuery(str);
      return (NamedForm) resultSetToNamedForms(rs).toArray()[0];
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private HashSet<NamedForm> resultSetToNamedForms(ResultSet rs) throws SQLException {
    HashSet<NamedForm> namedForms = new HashSet<>();
    while (rs.next()) {
      Blob blob = rs.getBlob("FORM");
      Form form = fs.fromStream(blob.getBytes(1, (int) blob.length()));
      form.setRequest(rs.getBoolean("ISSERVICEREQUEST"));
      blob.free();
      namedForms.add(new NamedForm(rs.getInt("id"), rs.getString("NAME"), form));
    }
    return namedForms;
  }
}
