package edu.wpi.cs3733.d21.teamN.services.database.users;

public class Administrator extends User {

  public Administrator(int id, String username, UserPrefs userPrefs) {
    super(id, username, userPrefs);
  }

  @Override
  public UserType getType() {
    return UserType.ADMINISTRATOR;
  }
}
