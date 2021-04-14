package edu.wpi.teamname.services.database;

public class Administrator extends User {

  public Administrator(String id, String username) {
    super(id, username);
  }

  public String getType() {
    return "Admin";
  }
}
