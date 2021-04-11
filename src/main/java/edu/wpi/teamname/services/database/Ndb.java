// package edu.wpi.teamname.services.database;
//
// import java.sql.*;
// import java.util.Scanner;
//
//// TODO Decide if we want to use Slf4j logger (@Slf4j)
// public class Ndb {
//  private static Connection connection;
//  private static Statement stmt;
//
//  public static void main(String[] args) {
//
//    try {
//      Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); // Apache Derby
//    } catch (ClassNotFoundException e) {
//      e.printStackTrace();
//    }
//
//    // TODO Find a better way to get connection?
//    try {
//      connection =
//          DriverManager.getConnection(
//
// "jdbc:derby:src/main/java/edu/wpi/teamname/services/database/NodeEdgeData;user=admin;password=admin;create=true");
//      stmt = connection.createStatement();
//    } catch (SQLException e) {
//      e.printStackTrace();
//      return;
//    }
//
//    if (args.length == 0) {
//      System.out.println(
//          "1 - Node Information\n2 - Update Node Information\n3 - Update Node Location Long
// Name");
//      System.out.println("4 - Edge Information\n5 - Exit Program");
//    } else {
//      switch (args[0]) {
//        case "1":
//          displayNodeInfo();
//          break;
//        case "2":
//          updateNode();
//          break;
//        case "3":
//          // TODO Update Node Location Long Name
//          break;
//        case "4":
//          displayEdgeInfo();
//          break;
//        default:
//          System.exit(0);
//      }
//    }
//  }
//
//  // displays nodes (on command line)
//  private static void displayNodeInfo() {
//    try {
//      String str = "SELECT * FROM Nodes";
//      ResultSet r = stmt.executeQuery(str);
//      printResultSet(r);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }
//
//  // scanner in w/ id and then prompt for new x and y and change
//  private static void updateNode() { // TODO update col. names
//    try {
//      Scanner s = new Scanner(System.in);
//      System.out.println("Enter Node ID");
//      String id = s.nextLine();
//      System.out.println("Enter new X Y");
//      String[] xy = s.nextLine().split(" ");
//      String str =
//          "UPDATE Nodes SET xcoord = " + xy[0] + ", ycoord = " + xy[1] + " WHERE nodeID = " + id;
//      s.close();
//      stmt.execute(str);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }
//
//  // enter ID and then prompt w/ new long name
//  private static void updateNodeLongName() {
//    try {
//      Scanner s = new Scanner(System.in);
//      System.out.println("Enter Node ID");
//      String id = s.nextLine();
//      System.out.println("Enter new longName");
//      String ln = s.nextLine();
//      String str = "UPDATE Nodes SET longName = " + ln + " WHERE nodeID = " + id;
//      s.close();
//      stmt.execute(str);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }
//
//  // display list of edges w/ attributes
//  private static void displayEdgeInfo() {
//    try {
//      String str = "SELECT * FROM Edges";
//      ResultSet r = stmt.executeQuery(str);
//      printResultSet(r);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }
//
//  private static void printResultSet(ResultSet r) throws SQLException {
//    ResultSetMetaData rm = r.getMetaData();
//    int colNum = rm.getColumnCount();
//    while (r.next()) {
//      for (int i = 1; i <= colNum; i++)
//        System.out.print(r.getString(i) + " "); // TODO Format output
//      System.out.println(""); // newline
//    }
//  }
//
//  private static void initTables() { // TODO Delete this
//    try {
//      String str =
//          "CREATE TABLE Edges( "
//              + "edgesID varchar(25), "
//              + "startNode varchar(25), "
//              + "endNode varchar(25), "
//              + "PRIMARY KEY (edgesID))";
//      stmt.execute(str);
//      str =
//          "CREATE TABLE Nodes( "
//              + "nodeID varchar(25), "
//              + "xcoord varchar(25), "
//              + "ycoord varchar(25), "
//              + "floor varchar(25), "
//              + "building varchar(25), "
//              + "nodeType varchar(25), "
//              + "longName varchar(25), "
//              + "shortName varchar(25), "
//              + "PRIMARY KEY (nodeID))";
//      stmt.execute(str);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }
// }
