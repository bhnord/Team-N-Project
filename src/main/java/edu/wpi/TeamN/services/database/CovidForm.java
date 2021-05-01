package edu.wpi.TeamN.services.database;

public class CovidForm {
  private int id, userId, assignedEmployeeId;
  private boolean answers[];
  private String extraInfo;

  public CovidForm(int userId, boolean answers[], String extraInfo) {
    this.userId = userId;
    this.answers = answers;
    this.extraInfo = extraInfo;
  }

  public CovidForm(
      int id, int userId, int assignedEmployeeId, boolean answers[], String extraInfo) {
    this.id = id;
    this.userId = userId;
    this.assignedEmployeeId = assignedEmployeeId;
    this.answers = answers;
    this.extraInfo = extraInfo;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getAssignedEmployeeId() {
    return assignedEmployeeId;
  }

  public void setAssignedEmployeeId(int assignedEmployeeId) {
    this.assignedEmployeeId = assignedEmployeeId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public boolean[] getAnswers() {
    return answers;
  }

  public void setAnswers(boolean[] answers) {
    this.answers = answers;
  }

  public String getExtraInfo() {
    return extraInfo;
  }

  public void setExtraInfo(String extra) {
    this.extraInfo = extraInfo;
  }
}
