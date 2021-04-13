package edu.wpi.teamname.services.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTesting {
  private static Connection connection;
  private static Statement stmt;

  public static void main(String[] args) {

    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); // Apache Derby
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    try {
      connection =
          DriverManager.getConnection(
              "jdbc:derby:src/main/java/edu/wpi/teamname/services/database/TESTDB;user=admin;password=admin;create=true");
      stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    }

    loadCSVtoTable("C:\\Users\\Bernhard\\Desktop\\L1Nodes.csv", "NODES");
    loadCSVtoTable("C:\\Users\\Bernhard\\Desktop\\L1Edges.csv", "EDGES");
  }

  /**
   * loads CSV files into Database.
   *
   * @param csvPath full path to CSV File.
   * @param tableName table name to put CSV File in.
   */
  public static void loadCSVtoTable(String csvPath, String tableName) {
    String str =
        "CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null, '"
            + tableName
            + "', '"
            + csvPath
            + "', ',', '\"', 'UTF-8',0)";
    try {
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void initTables() {
    try {

      String str =
          "CREATE TABLE Nodes( "
              + "id varchar(25), "
              + "xcoord INT NOT NULL, "
              + "ycoord INT NOT NULL, "
              + "floor varchar(25), "
              + "building varchar(25), "
              + "nodeType varchar(25), "
              + "longName varchar(45), "
              + "shortName varchar(35), "
              + "PRIMARY KEY (id))";
      stmt.execute(str);
      str =
          "CREATE TABLE Edges( "
              + "id varchar(25), "
              + "startNodeID varchar(25) REFERENCES Nodes (id), "
              + "endNodeID varchar(25) REFERENCES Nodes (id), "
              + "PRIMARY KEY (id))";

      stmt.execute(str);
      str =
          "CREATE TABLE Users("
              + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
              + "Username varchar(40) NOT NULL UNIQUE, "
              + "Password varchar(40) NOT NULL,"
              + "UserType varchar(15),"
              + "CONSTRAINT chk_UserType CHECK (UserType IN ('Patient', 'Employee', 'Administrator')),"
              + "PRIMARY KEY (id))";
      stmt.execute(str);
      str =
          "CREATE TABLE Requests("
              + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
              + "Type varchar(30), "
              + "SenderID INT NOT NULL REFERENCES Users (id), "
              + "ReceiverID INT REFERENCES Users (id), "
              + "Content varchar(700), "
              + "Notes varchar(200), "
              + "CONSTRAINT chk_Type CHECK (Type IN "
              + "('Food Delivery', 'Language Interpreter', 'Sanitation', 'Laundry', 'Gift Delivery', 'Floral Delivery', 'Medicine Delivery', "
              + "'Religious Request', 'Internal Patient Transportation', 'External Patient Transportation', 'Security', 'Facilities Maintenance', "
              + "'Computer Service', 'Audio/Visual')),"
              + "PRIMARY KEY (id))";
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
