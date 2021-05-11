package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d21.teamN.form.Form;
import edu.wpi.cs3733.d21.teamN.services.database.Appointment;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FormController extends MasterController implements Initializable {

  Form form;

  @FXML Label title, text11;
  @FXML AnchorPane anchorPane;
  @Inject private FXMLLoader loader;
  @Inject private DatabaseService db;
  private Appointment appointment;
  @FXML Rectangle darkMode;
  @FXML private ListView<Node> listView;
  /**
   * This method allows the tests to inject the scene at a later time, since it must be done on the
   * JavaFX thread
   *
   * @param appPrimaryScene Primary scene of the app whose root will be changed
   */
  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Database");
    darkMode.setVisible(db.getCurrentUser().getDarkMode());
    updateStyle(db.getCurrentUser().getAppColor());
  }

  @FXML JFXButton helpSB;

  public void updateStyle(String color) {

    if (!db.getCurrentUser().getDarkMode()) {
      Color appC = Color.web(color);
      String s = appC.darker().darker().desaturate().toString();
      String style =
          "-fx-background-color: " + "#" + color.substring(2) + "; -fx-background-radius: 10;";
      Label[] lA = {text11, title};
      for (Label a : lA) a.setStyle(style);
    } else {
      Color appC = Color.web(color);
      String s = appC.darker().darker().desaturate().toString();
      String style =
          "-fx-background-color: " + "#" + s.substring(2) + "; -fx-background-radius: 10;";
      Label[] lA = {text11, title};
      for (Label a : lA) a.setStyle(style);
      style = "-fx-background-color: " + "#" + color.substring(2) + "; -fx-background-radius: 10;";
      helpSB.setStyle(style);
    }
  }

  public void setUp(Form form) {
    this.form = form;
    form.build(this, db);
  }

  public void setUp(Appointment a, Form form) {
    appointment = a;
    this.form = form;
    form.build(this, db);
  }

  public ListView<Node> getElements() {
    return listView;
  }

  public Label getTitle() {
    return title;
  }

  public void submit(ActionEvent actionEvent) throws IOException {
    if (!this.form.isRequest()) {
      this.appointment.setForm(this.form);
      db.updateAppointment(appointment);
    } else {
      //       db.add(this.form);
    }
  }
}
