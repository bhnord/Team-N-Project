package edu.wpi.cs3733.d21.teamN.services.database;

import com.google.gson.Gson;
import edu.wpi.cs3733.d21.teamN.services.database.users.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import javax.imageio.ImageIO;

public class UsersTable {
  private Connection connection;
  private Statement stmt;

  public static UsersTable getInstance() {
    return SingletonHelper.table;
  }

  private static class SingletonHelper {
    private static final UsersTable table = new UsersTable();
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
    try {
      this.stmt = connection.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean addUser(String username, String password, UserType type) {
    Gson gson = new Gson();
    String str =
        "INSERT INTO USERS (USERNAME, PASSWORD, USERTYPE, PREFERENCES) VALUES ('"
            + username
            + "', '"
            + password
            + "', '"
            + type.toString()
            + "', '"
            + gson.toJson(new UserPrefs())
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

  public boolean updateUserImage(int id, BufferedImage image) {
    try {
      Blob blob = connection.createBlob();
      PreparedStatement ps;
      blob.setBytes(1, toByteArray(image, "PNG"));
      ps = connection.prepareStatement("UPDATE USERS SET FACEIMAGES = (?) WHERE ID = (?)");
      ps.setBlob(1, blob);
      ps.setInt(2, id);
      boolean result = ps.execute();
      blob.free();
      ps.close();
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public HashMap<BufferedImage, Integer> getAllFaces() {
    HashMap<BufferedImage, Integer> images = new HashMap<>();
    String str = "SELECT ID, FaceImages FROM USERS";
    try {
      ResultSet rs = stmt.executeQuery(str);
      while (rs.next()) {
        int id = rs.getInt(1);
        Blob photo = rs.getBlob(2);
        if (photo == null) continue;
        byte[] bytes = photo.getBytes(1, (int) photo.length());
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        images.put(image, id);
      }
      return images;
    } catch (Exception e) {
      e.printStackTrace();
      return images;
    }
  }

  private byte[] toByteArray(BufferedImage bi, String format) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(bi, format, baos);
    return baos.toByteArray();
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
}
