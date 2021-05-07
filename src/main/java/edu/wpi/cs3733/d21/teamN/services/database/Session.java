package edu.wpi.cs3733.d21.teamN.services.database;

import java.sql.*;

public class Session {
  private ResultSet _usrData = null; // TODO Change to Type USER
  private boolean _loggedIn = false;
  private final String _url =
      "jdbc:derby:src/main/java/edu/wpi/teamname/services/database/DerbyDB;user=admin;password=admin;create=true";
  private Connection connection;
  private Statement stmt;

  private static Session single_instance = null;

  private Session() {
    try {
      connection = DriverManager.getConnection(_url);
      stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /** inits user and sets up connection to database. Need to login() for any access. */
  public static Session getInstance() {
    // To ensure only one instance is created
    if (single_instance == null) {
      single_instance = new Session();
    }
    return single_instance;
  }

  /**
   * logs into database (sets permissions etc)
   *
   * @param username the given username of whomever logs in
   * @param password the given password of whomever log in
   * @return whether the user exists in the system
   */
  public boolean login(String username, String password) {
    String str =
        "SELECT * FROM Users WHERE username = '"
            + username
            + "' AND password = '"
            + password
            + "'"; // TODO deal with pw encryption
    ResultSet data = null;
    try {
      data = stmt.executeQuery(str);
      if (data.next()) {
        _usrData = data;
        _loggedIn = true;
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  void logout() {
    _usrData = null;
    _loggedIn = false;
  }
}
