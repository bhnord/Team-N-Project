package edu.wpi.teamname.services.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Ndb {


    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("1 - Node Information\n2 - Update Node Information\n3 - Update Node Location Long Name");
            System.out.println("4 - Edge Information\n5 - Exit Program");
        } else {
            switch (args[0]) {
                case "1":
                    //TODO List nodes
                    break;
                case "2":
                    //TODO Update Node Coordinates
                    break;
                case "3":
                    //TODO Update Node Location Long Name
                    break;
                case "4":
                    //TODO Edge Information
                    break;
                default:
                    System.exit(0);
            }

            Connection connection;
            try {
                connection = DriverManager.getConnection("jdbc:derby:L1Nodes");
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }


        }
    }
    private void displayNodes() {

    }
    private void updateNode(){

        //scanner in w/ id and then prompt for new x and y
    }
    private void updateNodeLongName(){
        //enter ID and then prompt w/ new long name
    }
    private void getEdgeInfo(){
        //display list of edges w/ attributes
    }

}
