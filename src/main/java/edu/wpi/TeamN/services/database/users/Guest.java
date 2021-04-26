package edu.wpi.TeamN.services.database.users;

public class Guest extends User {
  public Guest(String id, String username) {
    super(id, username);
  }

  @Override
  public UserType getType() {
    return UserType.GUEST;
  }
}
