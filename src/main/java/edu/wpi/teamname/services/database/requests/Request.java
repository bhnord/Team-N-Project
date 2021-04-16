package edu.wpi.teamname.services.database.requests;

public abstract class Request {
  int id, senderID, receiverID;
  String notes;

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
