package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class AppointmentController extends MasterController implements Initializable {
  @FXML private JFXComboBox<Label> appointmentTypeDropdown;
  @FXML private JFXDatePicker datePicker;
  @FXML private JFXTimePicker timePicker;
  @FXML private JFXListView<HBox> listView;
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  private Scene appPrimaryScene;

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
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Login");
    listView.getStylesheets().add("StyleSheet/appointmentTable.css");
    for (User user : db.getAllUsers()) {
      appointmentTypeDropdown.getItems().add(new Label(user.getUsername()));
    }
  }

  public void update(ActionEvent actionEvent) {}

  public void deleteAppointment(ActionEvent actionEvent) {}

  public void addAppointment(ActionEvent actionEvent) {
    HBox box = new HBox();
    Label appointmentType =
        new Label(appointmentTypeDropdown.getSelectionModel().getSelectedItem().getText());
    Label date = new Label(datePicker.getEditor().getText());
    Label time = new Label(timePicker.getEditor().getText());
    time.setId("time");

    box.getStylesheets().add("StyleSheet/appointmentTable.css");
    box.getChildren().addAll(appointmentType, date, time);
    listView.getItems().add(box);
  }
}
