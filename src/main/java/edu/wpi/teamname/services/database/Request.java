package edu.wpi.teamname.services.database;

public abstract class Request {
  int id, senderID, recieverID;
  String notes;

  public Request(int id, int senderID, int recieverID, String notes) {
    this.id = id;
    this.senderID = senderID;
    this.recieverID = recieverID;
    this.notes = notes;
  }

  public int getId() {
    return id;
  }

  public int getSenderID() {
    return senderID;
  }

  public int getRecieverID() {
    return recieverID;
  }

  public String getNotes() {
    return notes;
  }

  public abstract String[] getContent();
}
