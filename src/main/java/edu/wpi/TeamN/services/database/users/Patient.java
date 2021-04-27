package edu.wpi.TeamN.services.database.users;

public class Patient extends User {
  public Patient(String id, String username, UserPrefs userPrefs) {
    super(id, username, userPrefs);
  }

  @Override
  public UserType getType() {
    return UserType.PATIENT;
  }
}
