package edu.wpi.teamname.services.database;

public class Patient extends User {
  public Patient(String id, String username) {
    super(id, username);
  }

  public String getType() {
    return "Patient";
  }
}
