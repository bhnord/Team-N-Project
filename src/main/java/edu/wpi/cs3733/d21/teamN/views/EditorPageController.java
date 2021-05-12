package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import edu.wpi.cs3733.d21.teamN.state.HomeState;
import edu.wpi.cs3733.d21.teamN.utilities.DialogFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class EditorPageController extends MasterController implements Initializable {
  public StackPane rootStackPane;
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;

  @FXML private JFXButton mapEditor, EmployeeEditor, FormEditor, AppointmentEditor;

  @FXML private AnchorPane anchorPane;
  private DialogFactory dialogFactory;
  private Scene appPrimaryScene;
  private User user;

  @FXML Rectangle darkMode;

  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  public void initialize(URL location, ResourceBundle resources) {

    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Map");

    user = db.getCurrentUser();
    if (user.getAppColor().equals("0x748cdc")) {
      updateStyle("0x748cdc");
    } else {
      updateStyle(user.getAppColor());
    }
  }

  /**
   * map advances to map FXML
   *
   * @param actionEvent Button Press
   * @throws IOException
   */
  public void map(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("MapAdmin2.fxml"));
    appPrimaryScene.setRoot(root);
  }

  public void formEditor(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("EditForms.fxml"));
    FormsEditorController controller = loader.getController();
    controller.setUp();
    appPrimaryScene.setRoot(root);
  }

  public void appointmentEdit(ActionEvent actionEvent) throws IOException {
    if (db.getAllFormsNotServiceRequest().isEmpty()) {
      dialogFactory = new DialogFactory(rootStackPane);
      dialogFactory.creatDialogOkay(
          "No Forms Available",
          "You must create a form before you can assign any appointments types");
      return;
    }
    Parent root = loader.load(getClass().getResourceAsStream("EditAppointments.fxml"));
    AppointmentsEditorController controller = loader.getController();
    controller.setUp();
    appPrimaryScene.setRoot(root);
  }
  /**
   * advanceViews Loads a new page *not for service requests*
   *
   * @param actionEvent Button press
   * @throws IOException
   */
  public void advanceViews(ActionEvent actionEvent) throws IOException {
    String file = ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void updateStyle(String color) {

    String style =
        "-fx-background-color: " + "#" + color.substring(2) + "; -fx-background-radius: 25;";
    if (db.getCurrentUser().getDarkMode()) {
      darkMode.setVisible(true);
    } else {
      darkMode.setVisible(false);
    }
    mapEditor.setStyle(style);
    EmployeeEditor.setStyle(style);
    FormEditor.setStyle(style);
    AppointmentEditor.setStyle(style);
  }
}
