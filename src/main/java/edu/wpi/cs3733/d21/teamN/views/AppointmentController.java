package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.*;
import edu.wpi.cs3733.d21.teamN.form.Form;
import edu.wpi.cs3733.d21.teamN.services.database.Appointment;
import edu.wpi.cs3733.d21.teamN.services.database.AppointmentType;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

public class AppointmentController extends MasterController implements Initializable {
  @FXML private JFXTextField username;
  @FXML private JFXTextField appointmentID;
  @FXML private JFXComboBox<Label> staffDropdown;
  @FXML private JFXComboBox<Label> locationDropdown;
  @FXML private JFXComboBox<Label> appointmentTypeDropdown;
  @FXML private JFXDatePicker datePicker;
  @FXML private JFXTimePicker timePicker;
  @FXML private JFXListView<Label> listView;
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  private Scene appPrimaryScene;
  private int appointmentsAdded;

  @FXML private AnchorPane anchorPane;

  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @Inject
  public void setLoader(FXMLLoader loader) {
    this.loader = loader;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Map");
    listView.getStylesheets().add("StyleSheet/appointmentTable.css");
    appointmentsAdded = db.getAllAppointments().size();
    loadDBAppointments();
    loadEmployeeDropdown(staffDropdown);
    loadRoomDropdown(locationDropdown, "hall");
    loadAppointmentType();

    listView.setOnMouseClicked(
        event -> {
          Label selected = listView.getSelectionModel().getSelectedItem();
          if (event.getButton() == MouseButton.PRIMARY && selected != null) {
            Integer id = Integer.parseInt(selected.getId());
            Appointment appointment = db.getAppointment(id);
            if (!(appointment == null)) {
              updateFields(appointment);
            } else {
              setEmptyFields();
            }
          }
        });
  }

  private void loadAppointmentType() {
    for (AppointmentType appointmentType : db.getAllAppointmentTypes()) {
      Label lbl = new Label(appointmentType.getType());
      lbl.setId(appointmentType.getType());
      appointmentTypeDropdown.getItems().add(lbl);
    }
  }

  private void setEmptyFields() {
    appointmentID.setText("");
    username.setText("");
    staffDropdown.getEditor().setText("");
    locationDropdown.getEditor().setText("");
    datePicker.getEditor().clear();
    timePicker.getEditor().clear();
    appointmentTypeDropdown.getEditor().setText("");
  }

  private void loadDBAppointments() {
    for (Appointment appointment : db.getAllAppointments()) {
      listView
          .getItems()
          .add(
              new Label(
                  db.getUserById(appointment.getPatientId()).getFirstname()
                      + "'s appointment"
                      + appointment.getId()));
    }
  }

  public void updateFields(Appointment appointment) {
    appointmentID.setText(String.valueOf(appointment.getId()));
    username.setText(String.valueOf(db.getUserById(appointment.getPatientId())));
    staffDropdown
        .getEditor()
        .setText(String.valueOf(db.getUserById(appointment.getAssignedStaffId())));
    locationDropdown.getEditor().setText(appointment.getAssociatedRoomId());
    datePicker.getEditor().setText(String.valueOf(appointment.getTimeOfAppointment().getTime()));
    timePicker.getEditor().setText(String.valueOf(appointment.getTimeOfAppointment().getTime()));
    ;
    appointmentTypeDropdown.getEditor().setText(String.valueOf(appointment.getAppointmentTypeId()));
  }

  public void deleteAppointment(ActionEvent actionEvent) {
    listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
  }

  public void addAppointment(ActionEvent actionEvent) {

    appointmentsAdded++;
    Label label = new Label("Appointment: " + appointmentsAdded);
    label.setId(String.valueOf(appointmentsAdded));

    listView.getItems().add(label);
  }

  public void save(ActionEvent actionEvent) {
    validateFields();
    Label curr = listView.getSelectionModel().getSelectedItem();

    Appointment appointment =
        new Appointment(
            Integer.parseInt(appointmentID.getText()),
            Integer.parseInt(appointmentTypeDropdown.getEditor().getText()),
            db.getUserByUsername(username.getText()).getId(),
            db.getUserByUsername(staffDropdown.getEditor().getText()).getId(),
            new Form("form"),
            new Timestamp(Long.parseLong(timePicker.getEditor().getText())),
            false,
            locationDropdown.getEditor().getText());
    db.addAppointment(appointment);
  }

  private boolean validateFields() {
    return (appointmentID.getText().isEmpty()
        && username.getText().isEmpty()
        && staffDropdown.getEditor().getText().isEmpty()
        && locationDropdown.getEditor().getText().isEmpty()
        && datePicker.getEditor().getText().isEmpty()
        && timePicker.getEditor().getText().isEmpty()
        && appointmentTypeDropdown.getEditor().getText().isEmpty());
  }
}
