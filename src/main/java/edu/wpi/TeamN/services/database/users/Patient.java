package edu.wpi.TeamN.services.database.users;

public class Patient extends User {
  public Patient(String id, String username) {
    super(id, username);
  }

  public UserType getType() {
    return UserType.PATIENT;
  }
}
