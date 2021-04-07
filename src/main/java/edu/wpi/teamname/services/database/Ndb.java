package edu.wpi.teamname.services.database;

import java.sql.*;
import java.util.Scanner;

public class Ndb {
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
              "jdbc:derby:src/main/java/edu/wpi/teamname/services/database/NodeEdgeData;user=admin;password=admin;create=true");
      stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    }

    if (args.length == 0) {
      System.out.println(
          "1 - Node Information\n2 - Update Node Information\n3 - Update Node Location Long Name");
      System.out.println("4 - Edge Information\n5 - Exit Program");
    } else {
      switch (args[0]) {
        case "1":
          displayNodeInfo();
          break;
        case "2":
          updateNode();
          break;
        case "3":
          updateNodeLongName();
          break;
        case "4":
          displayEdgeInfo();
          break;
        default:
          System.exit(0);
      }
    }
  }

  // displays nodes (on command line)

  private static void displayNodeInfo() {
    try {
      String str = "SELECT * FROM Nodes";
      ResultSet r = stmt.executeQuery(str);
      printResultSet(r);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // scanner in w/ id and then prompt for new x and y and change
  private static void updateNode() {
    try {
      Scanner s = new Scanner(System.in);
      System.out.print("Enter Node ID: ");
      String id = s.nextLine();
      System.out.print("Enter new X Y (ex. 100 200): ");
      String[] xy = s.nextLine().split(" ");
      String str =
          "UPDATE Nodes SET xcoord = "
              + xy[0]
              + ", ycoord = "
              + xy[1]
              + " WHERE nodeID = '"
              + id
              + "'";
      s.close();
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // enter ID and then prompt w/ new long name
  private static void updateNodeLongName() {
    try {
      Scanner s = new Scanner(System.in);
      System.out.print("Enter Node ID: ");
      String id = s.nextLine();
      System.out.print("Enter new longName: ");
      String ln = s.nextLine();
      String str = "UPDATE Nodes SET longName = '" + ln + "' WHERE nodeID = '" + id + "'";
      s.close();
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // display list of edges w/ attributes
  private static void displayEdgeInfo() {
    try {
      String str = "SELECT * FROM Edges";
      ResultSet r = stmt.executeQuery(str);
      printResultSet(r);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void printResultSet(ResultSet r) throws SQLException {
    ResultSetMetaData rm = r.getMetaData();
    int colNum = rm.getColumnCount();
    for (int i = 1; i <= colNum; i++) {
      System.out.printf("%-45s", rm.getColumnLabel(i));
    }
    System.out.println(); // newline
    while (r.next()) {
      for (int i = 1; i <= colNum; i++) System.out.printf("%-45s", r.getString(i));
      System.out.println(); // newline
    }
  }

  private static void initTables() {
    try {
      String str =
          "CREATE TABLE Edges( "
              + "edgesID varchar(25), "
              + "startNode varchar(25), "
              + "endNode varchar(25), "
              + "PRIMARY KEY (edgesID))";
      stmt.execute(str);
      str =
          "CREATE TABLE Nodes( "
              + "nodeID varchar(25), "
              + "xcoord INT NOT NULL, "
              + "ycoord INT NOT NULL, "
              + "floor varchar(25), "
              + "building varchar(25), "
              + "nodeType varchar(25), "
              + "longName varchar(45), "
              + "shortName varchar(25), "
              + "PRIMARY KEY (nodeID))";
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
