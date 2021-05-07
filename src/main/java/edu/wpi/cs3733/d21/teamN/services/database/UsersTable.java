package edu.wpi.cs3733.d21.teamN.services.database;

import com.google.gson.Gson;
import com.google.inject.Inject;
import edu.wpi.cs3733.d21.teamN.services.database.users.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

public class UsersTable {
  private final Connection connection;
  private Statement stmt;

  @Inject
  UsersTable(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean addUser(String username, String password, UserType type, UserPrefs userPrefs) {
    Gson gson = new Gson();
    String str =
        "INSERT INTO USERS (USERNAME, PASSWORD, USERTYPE, PREFERENCES) VALUES ('"
            + username
            + "', '"
            + password
            + "', '"
            + type.toString()
            + "', '"
            + gson.toJson(userPrefs)
            + "')";
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateUser(
      int id, String username, String password, UserType type, UserPrefs userPrefs) {
    Gson gson = new Gson();
    String str =
        "UPDATE USERS SET USERNAME = '"
            + username
            + "', PASSWORD = '"
            + password
            + "', USERTYPE = '"
            + type.toString()
            + "', PREFERENCES = '"
            + gson.toJson(userPrefs)
            + "' WHERE ID = "
            + id;
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateUserPrefs(int id, UserPrefs userPrefs) {
    Gson gson = new Gson();
    String str = "UPDATE USERS SET PREFERENCES = '" + gson.toJson(userPrefs) + "' WHERE ID = " + id;
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public User getUserById(int id) {
    String str = "SELECT * FROM USERS WHERE ID = " + id;
    try {
      ResultSet rs = stmt.executeQuery(str);
      HashSet<User> u = resultSetToUsers(rs);
      if (u.size() > 0) {
        return (User) u.toArray()[0];
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public User getUserByUsername(String username) {
    String str = "SELECT * FROM USERS WHERE USERNAME = '" + username + "'";
    User returnUser;
    try {
      ResultSet rs = stmt.executeQuery(str);
      HashSet<User> u = resultSetToUsers(rs);
      if (u.size() > 0) {
        return (User) u.toArray()[0];
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  // TODO CHANGE
  public HashSet<User> getUsersByType(UserType userType) {
    String userTypeString =
        userType.toString().charAt(0) + userType.toString().substring(1).toLowerCase();
    String str = "SELECT * FROM USERS WHERE USERTYPE = '" + userTypeString + "'";
    try {
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToUsers(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public HashSet<User> getAllUsers() {
    String str = "SELECT * FROM USERS";
    try {
      ResultSet rs = stmt.executeQuery(str);
      return resultSetToUsers(rs);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean deleteUser(String username) {
    String str = "DELETE FROM USERS WHERE USERNAME = '" + username + "'";
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private HashSet<User> resultSetToUsers(ResultSet rs) {
    HashSet<User> users = new HashSet<>();
    Gson gson = new Gson();
    try {
      while (rs.next()) {
        UserPrefs userPrefs = gson.fromJson(rs.getString("PREFERENCES"), UserPrefs.class);
        switch (rs.getString("USERTYPE")) {
          case "Patient":
            users.add(new Patient(rs.getInt("ID"), rs.getString("USERNAME"), userPrefs));
            break;
          case "Employee":
            users.add(new Employee(rs.getInt("ID"), rs.getString("USERNAME"), userPrefs));
            break;
          case "Administrator":
            users.add(new Administrator(rs.getInt("ID"), rs.getString("USERNAME"), userPrefs));
            break;
        }
      }
      return users;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean updateUserUsernameType(int id, String username, UserType type) {
    String str =
        "UPDATE USERS SET USERNAME = '"
            + username
            + "', USERTYPE = '"
            + type.toString()
            + "' WHERE ID = "
            + id;
    try {
      stmt.execute(str);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
