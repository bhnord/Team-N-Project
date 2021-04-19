package edu.wpi.teamname.services.database.requests;

public abstract class Request {
  private int id, senderID, receiverID;
  private String notes;

  public Request(int id, int senderID, int receiverID, String notes) {
    this.id = id;
    this.senderID = senderID;
    this.receiverID = receiverID;
    this.notes = notes;
  }

  public int getId() {
    return id;
  }

  public int getSenderID() {
    return senderID;
  }

  public int getReceiverID() {
    return receiverID;
  }

  public String getNotes() {
    return notes;
  }

  public abstract String[] getContent();
}
