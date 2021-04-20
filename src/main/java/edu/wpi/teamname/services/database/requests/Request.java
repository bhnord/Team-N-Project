package edu.wpi.teamname.services.database.requests;

public class Request {
  private int id, senderID, receiverID;
  private RequestType type;
  private String content, notes;

  public Request(RequestType type, int senderID, int receiverID, String content, String notes) {
    this.type = type;
    this.senderID = senderID;
    this.receiverID = receiverID;
    this.content = content;
    this.notes = notes;
  }

  public Request(
      RequestType type, int requestID, int senderID, int receiverID, String content, String notes) {
    this.type = type;
    this.id = requestID;
    this.senderID = senderID;
    this.receiverID = receiverID;
    this.content = content;
    this.notes = notes;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getSenderID() {
    return senderID;
  }

  public void setSenderID(int senderID) {
    this.senderID = senderID;
  }

  public int getReceiverID() {
    return receiverID;
  }

  public void setReceiverID(int receiverID) {
    this.receiverID = receiverID;
  }

  public RequestType getType() {
    return type;
  }

  public void setType(RequestType type) {
    this.type = type;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }
}
