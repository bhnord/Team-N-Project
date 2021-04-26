package edu.wpi.TeamN.services.database.users;

public enum UserType {
  ADMINISTRATOR {
    public String toString() {
      return "Administrator";
    }
  },
  EMPLOYEE {
    public String toString() {
      return "Employee";
    }
  },
  PATIENT {
    public String toString() {

      return "Patient";
    }
  }
}
