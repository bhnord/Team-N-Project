package edu.wpi.TeamN.services.database.users;

public class Administrator extends User {

  public Administrator(String id, String username, UserPrefs userPrefs) {
    super(id, username, userPrefs);
  }

  public UserType getType() {
    return UserType.ADMINISTRATOR;
  }
}
