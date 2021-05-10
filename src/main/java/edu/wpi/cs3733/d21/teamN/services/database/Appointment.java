package edu.wpi.cs3733.d21.teamN.services.database;

import edu.wpi.cs3733.d21.teamN.form.Form;
import java.sql.Timestamp;

public class Appointment {
  private int id, appointmentTypeId, patientId, assignedStaffId;

  public void setCheckInStatus(boolean checkInStatus) {
    this.checkInStatus = checkInStatus;
  }

  public void setAssociatedRoomId(String associatedRoomId) {
    this.associatedRoomId = associatedRoomId;
  }

  public void setAppointmentTypeId(int appointmentTypeId) {
    this.appointmentTypeId = appointmentTypeId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  public void setAssignedStaffId(int assignedStaffId) {
    this.assignedStaffId = assignedStaffId;
  }

  public void setTimeOfAppointment(Timestamp timeOfAppointment) {
    this.timeOfAppointment = timeOfAppointment;
  }

  private Timestamp timeOfAppointment;
  private boolean checkInStatus;
  private String associatedRoomId;
  Form form;

  public Appointment(
      int id,
      int appointmentTypeId,
      int patientId,
      int assignedStaffId,
      Form form,
      Timestamp timeOfAppointment,
      boolean checkInStatus,
      String associatedRoomId) {
    this.id = id;
    this.appointmentTypeId = appointmentTypeId;
    this.patientId = patientId;
    this.assignedStaffId = assignedStaffId;
    this.form = form;
    this.timeOfAppointment = timeOfAppointment;
    this.checkInStatus = checkInStatus;
    this.associatedRoomId = associatedRoomId;
  }

  public Appointment(
      int appointmentTypeId,
      int patientId,
      int assignedStaffId,
      Form form,
      Timestamp timeOfAppointment,
      String associatedRoomId) {
    this.appointmentTypeId = appointmentTypeId;
    this.patientId = patientId;
    this.assignedStaffId = assignedStaffId;
    this.form = form;
    this.timeOfAppointment = timeOfAppointment;
    this.associatedRoomId = associatedRoomId;
  }

  public int getId() {
    return id;
  }

  public int getPatientId() {
    return patientId;
  }

  public int getAppointmentTypeId() {
    return appointmentTypeId;
  }

  public int getAssignedStaffId() {
    return assignedStaffId;
  }

  public Form getForm() {
    return form;
  }

  public void setForm(Form form) {
    this.form = form;
  }

  public Timestamp getTimeOfAppointment() {
    return timeOfAppointment;
  }

  public boolean isCheckInStatus() {
    return checkInStatus;
  }

  public String getAssociatedRoomId() {
    return associatedRoomId;
  }
}
