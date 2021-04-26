package edu.wpi.TeamN.services.database.users;

public class Guest extends User {
  public Guest(String id, String username, UserPrefs userPrefs) {
    super(id, username, userPrefs);
  }

  @Override
  public UserType getType() {
    return UserType.GUEST;
  }
}
