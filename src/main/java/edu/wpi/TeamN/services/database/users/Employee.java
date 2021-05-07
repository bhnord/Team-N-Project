package edu.wpi.TeamN.services.database.users;

import edu.wpi.TeamN.services.database.requests.RequestType;

public class Employee extends User {
  private RequestType occupation;

  public Employee(int id, String username, UserPrefs userPrefs) {
    super(id, username, userPrefs);
  }

  public Employee(int id, String username, RequestType occupation, UserPrefs userPrefs) {
    super(id, username, userPrefs);
    this.occupation = occupation;
  }

  public RequestType getOccupation() {
    return occupation;
  }

  @Override
  public UserType getType() {
    return UserType.EMPLOYEE;
  }
}
