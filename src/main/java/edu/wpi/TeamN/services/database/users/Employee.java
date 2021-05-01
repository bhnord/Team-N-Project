package edu.wpi.TeamN.services.database.users;

public class Employee extends User {
  public Employee(int id, String username, UserPrefs userPrefs) {
    super(id, username, userPrefs);
  }

  @Override
  public UserType getType() {
    return UserType.EMPLOYEE;
  }
}
