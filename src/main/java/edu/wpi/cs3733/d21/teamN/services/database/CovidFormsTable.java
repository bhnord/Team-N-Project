package edu.wpi.cs3733.d21.teamN.services.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;

public class CovidFormsTable {
  private Connection connection;
  private Statement stmt;

  public static CovidFormsTable getInstance() {
    return SingletonHelper.table;
  }

  private static class SingletonHelper {
    private static final CovidFormsTable table = new CovidFormsTable();
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public HashSet<CovidForm> getAllCovidForms() {
    String query = "SELECT * FROM COVIDFORMS";
    try {
      ResultSet set = stmt.executeQuery(query);
      return resultSetToCovidForms(set);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public CovidForm getCovidForm(int formID) {
    String str = "SELECT * FROM COVIDFORMS WHERE id = " + formID;
    try {
      ResultSet rs = stmt.executeQuery(str);
      HashSet<CovidForm> set = resultSetToCovidForms(rs);
      if (set.size() > 0) {
        return (CovidForm) set.toArray()[0];
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * @param userId
   * @return
   */
  public CovidForm getCovidFormByUserId(int userId) {
    String str = "SELECT * FROM COVIDFORMS WHERE userId = " + userId;
    try {
      ResultSet rs = stmt.executeQuery(str);
      HashSet<CovidForm> set = resultSetToCovidForms(rs);
      if (set.size() > 0) {
        return (CovidForm) set.toArray()[0];
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public HashSet<CovidForm> getCovidFormsByEmployeeId(int employeeId) {
    String str = "SELECT * FROM COVIDFORMS WHERE assignedEmployee = '" + employeeId + "'";
    try {
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToCovidForms(rs);

    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  // TODO DECIDE HOW TO ASSIGN EMPLOYEE ID

  public boolean addCovidForm(CovidForm form, int employeeId) {

    boolean ans[] = form.getAnswers();
    String ansString = Arrays.toString(ans);
    ansString = ansString.substring(1, ansString.length() - 1); // cuts off [] from toString
    String str =
        "INSERT INTO COVIDFORMS (UserId, AssignedEmployee, Q1, Q2, Q3, Q4, Q5, Q6, ExtraInfo) VALUES ("
            + form.getUserId()
            + ", "
            + employeeId
            + ", "
            // EMPLOYEE ID HERE
            + ansString
            + ", '"
            + form.getExtraInfo()
            + "')";
    if (getCovidFormByUserId(form.getUserId()) != null) {
      deleteCovidFormByUserId(form.getUserId());
    }
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateCovidForm(int id, boolean isOk, boolean isCleared) {
    String str =
        "UPDATE COVIDFORMS SET IsOk = " + isOk + ", IsCleared = " + isCleared + " WHERE id = " + id;
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteCovidForm(int formID) {
    String str = "DELETE FROM COVIDFORMS WHERE id = " + formID;
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteCovidFormByUserId(int userID) {
    String str = "DELETE FROM COVIDFORMS WHERE userId = " + userID;
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private HashSet<CovidForm> resultSetToCovidForms(ResultSet rs) {
    HashSet<CovidForm> formSet = new HashSet<>();
    try {
      while (rs.next()) {
        int id = rs.getInt("id");
        int user = rs.getInt("UserId");
        int assignedEmployee = rs.getInt("AssignedEmployee");
        boolean ans[] = new boolean[6];
        for (int i = 0; i < 6; i++) {
          ans[i] = rs.getBoolean("Q" + (i + 1));
        }
        String extraInfo = rs.getString("extraInfo");
        boolean isOk = rs.getBoolean("IsOk");
        boolean isProcessed = rs.getBoolean("IsProcessed");
        boolean isCleared = rs.getBoolean("IsCleared");
        CovidForm form =
            new CovidForm(id, user, assignedEmployee, ans, extraInfo, isOk, isCleared, isProcessed);
        formSet.add(form);
      }
      return formSet;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean setCovidFormIsProcessed(int id, boolean isProcessed) {
    String str = "UPDATE COVIDFORMS SET isProcessed = " + isProcessed + " WHERE id = " + id;
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
