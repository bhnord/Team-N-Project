package edu.wpi.TeamN.services.database;

import com.google.inject.Inject;
import edu.wpi.TeamN.services.database.users.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

public class UsersTable {
    private static UsersTable table;
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


    public boolean addUser(User user, String password) {
        String str =
                "INSERT INTO USERS VALUES ("
                        + user.getId()
                        + ", '"
                        + user.getUsername()
                        + "', '"
                        + password
                        + "', '"
                        + user.getType().toString()
                        + "')";
        try {
            stmt.execute(str);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserById(String id) {
        String str = "SELECT * FROM USERS WHERE ID = " + id;
        try {
            ResultSet rs = stmt.executeQuery(str);
            return (User) resultSetToUsers(rs).toArray()[0];
        } catch (SQLException e) {
            return null;
        }
    }

    public User getUserByUsername(String username) {
        String str = "SELECT * FROM USERS WHERE USERNAME = " + username;
        try {
            ResultSet rs = stmt.executeQuery(str);
            return (User) resultSetToUsers(rs).toArray()[0];
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, User> getUsersByType(UserType userType) {
        String userTypeString =
                userType.toString().charAt(0) + userType.toString().substring(1).toLowerCase();
        String str = "SELECT * FROM USERS WHERE USERTYPE = '" + userTypeString + "'";
        HashMap<String, User> userMap = new HashMap<>();
        try {
            ResultSet rs = stmt.executeQuery(str);
            HashSet<User> usersSet = resultSetToUsers(rs);
            assert usersSet != null;
            for (User user : usersSet) {
                userMap.put(user.getId(), user);
            }
            return userMap;
        } catch (SQLException e) {
            e.printStackTrace();
            return userMap;
        }
    }

    public boolean deleteUser(String username) {
        String str = "DELETE USERS WHERE USERNAME = " + username;
        try {
            stmt.execute(str);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private HashSet<User> resultSetToUsers(ResultSet rs) {
        HashSet<User> users = new HashSet<>();
        try {
            while (rs.next()) {
                switch (rs.getString("USERTYPE")) {
                    case "Patient":
                        users.add(new Patient(rs.getString("ID"), rs.getString("USERNAME")));
                        break;
                    case "Employee":
                        users.add(new Employee(rs.getString("ID"), rs.getString("USERNAME")));
                        break;
                    case "Administrator":
                        users.add(new Administrator(rs.getString("ID"), rs.getString("USERNAME")));
                        break;
                }
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
