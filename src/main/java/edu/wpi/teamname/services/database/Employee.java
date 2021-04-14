package edu.wpi.teamname.services.database;

public class Employee extends User {
  public Employee(String id, String username) {
    super(id, username);
  }

  @Override
  public String getType() {
    return "Employee";
  }
}
