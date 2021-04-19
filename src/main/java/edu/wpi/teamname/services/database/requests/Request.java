package edu.wpi.teamname.services.database.requests;

public class Request {
  private int id, senderID, receiverID;
  private RequestType type;
  private String notes;

  public Request(RequestType type, int senderID, int receiverID, String notes) {
    this.senderID = senderID;
    this.receiverID = receiverID;
    this.notes = notes;
  }

  public Request(int requestID, int senderID, int receiverID, String notes) {
    this.id = requestID;
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

//  public abstract String getContent();
}
