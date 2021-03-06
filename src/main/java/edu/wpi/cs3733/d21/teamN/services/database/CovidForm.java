package edu.wpi.cs3733.d21.teamN.services.database;

public class CovidForm {
  private int id, userId, assignedEmployeeId;
  private boolean answers[], isOk, isProcessed, isCleared;
  private String extraInfo;

  public CovidForm(int userId, boolean answers[], String extraInfo) {
    this.userId = userId;
    this.answers = answers;
    this.extraInfo = extraInfo;
  }

  public CovidForm(
      int id,
      int userId,
      int assignedEmployeeId,
      boolean answers[],
      String extraInfo,
      boolean isOk,
      boolean isCleared,
      boolean isProcessed) {
    this.id = id;
    this.userId = userId;
    this.assignedEmployeeId = assignedEmployeeId;
    this.answers = answers;
    this.extraInfo = extraInfo;
    this.isOk = isOk;
    this.isCleared = isCleared;
    this.isProcessed = isProcessed;
  }

  public boolean isCleared() {
    return isCleared;
  }

  public int getId() {
    return id;
  }

  public int getAssignedEmployeeId() {
    return assignedEmployeeId;
  }

  public int getUserId() {
    return userId;
  }

  public boolean[] getAnswers() {
    return answers;
  }

  public String getExtraInfo() {
    return extraInfo;
  }

  public boolean isOk() {
    return isOk;
  }

  public boolean isProcessed() {
    return isProcessed;
  }

  public void setExtraInfo(String extraInfo) {
    this.extraInfo = extraInfo;
  }
}
