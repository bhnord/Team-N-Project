package edu.wpi.teamname.services.database;

import java.sql.*;
import java.util.Scanner;

// TODO Decide if we want to use Slf4j logger (@Slf4j)
public class Ndb {
  private static Connection connection;
  private static Statement stmt;

  public static void main(String[] args) {

    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); // Apache Derby
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    // TODO Find a better way to get connection?
    try {
      connection = DriverManager.getConnection("jdbc:derby:test;create=true");
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
          displayNodes();
          break;
        case "2":
          updateNode();
          break;
        case "3":
          // TODO Update Node Location Long Name
          break;
        case "4":
          getEdgeInfo();
          break;
        default:
          System.exit(0);
      }
    }
  }

  // displays nodes (on command line)
  private static void displayNodes() {
    try {
      String str = "SELECT * FROM Nodes";
      ResultSet r = stmt.executeQuery(str);
      printResultSet(r);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // scanner in w/ id and then prompt for new x and y and change
  private static void updateNode() { // TODO update col. names
    try {
      Scanner s = new Scanner(System.in);
      System.out.println("Enter Node ID");
      int id = Integer.parseInt(s.nextLine());
      System.out.println("Enter new X Y");
      String[] xy = s.nextLine().split(" ");
      String str = "UPDATE Nodes SET X = " + xy[0] + ", Y = " + xy[1] + " WHERE id = " + id;
      s.close();
      stmt.execute(str);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // enter ID and then prompt w/ new long name
  private static void updateNodeLongName() {
    // TODO implement method

  }

  // display list of edges w/ attributes
  private static void getEdgeInfo() {
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
    while (r.next()) {
      for (int i = 1; i <= colNum; i++)
        System.out.print(r.getString(i) + " "); // TODO Format output
      System.out.println(""); // newline
    }
  }

  private static void initTestTables() { // TODO Delete this / replace with CSV data
    try {
      String str =
          "CREATE TABLE Nodes( "
              + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
              + "X INT NOT NULL, "
              + "Y INT NOT NULL, "
              + "PRIMARY KEY (Id))";
      stmt.execute(str);

      for (int i = 0; i < 5; i++) {
        str = "INSERT INTO Nodes(X, Y) VALUES(" + i + ", " + i + ")";
        stmt.execute(str);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
