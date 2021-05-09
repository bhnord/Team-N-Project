package edu.wpi.cs3733.d21.teamN.services.database;

import com.google.inject.Inject;
import edu.wpi.cs3733.d21.teamN.form.Form;
import java.sql.*;
import java.util.HashSet;

public class FormsTable {

  private final Connection connection;
  private Statement stmt;
  FormSerializer fs = new FormSerializer();

  @Inject
  FormsTable(Connection connection) {
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
      String str = "INSERT INTO FORMS (NAME, FORM) VALUES (?, ?)";
      PreparedStatement ps = connection.prepareStatement(str);
      ps.setString(1, nForm.getName());
      ps.setBlob(2, blob);
      ps.executeUpdate();
      blob.free();
      ps.close();
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
      blob.free();
      namedForms.add(new NamedForm(rs.getInt("ID"), rs.getString("NAME"), form));
    }
    return namedForms;
  }
}
