package edu.wpi.teamname.entity.users;

public class Administrator extends User {

  public Administrator(String id, String username) {
    super(id, username);
  }

  public UserType getType() {
    return UserType.ADMINISTRATOR;
  }
}
