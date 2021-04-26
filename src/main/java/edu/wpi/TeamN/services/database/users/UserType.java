package edu.wpi.TeamN.services.database.users;

public enum UserType {
  ADMINISTRATOR("Administrator"),
  EMPLOYEE("Employee"),
  PATIENT("Patient"),
  GUEST("Guest");

  String name;

  UserType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
