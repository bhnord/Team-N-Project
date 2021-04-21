package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CovidForm extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;

  static Stage primaryStage;
  private Scene appPrimaryScene;
  @FXML private JFXButton submit;
  int count = 0;

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

  @FXML JFXComboBox<String> comboBox = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox2 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox3 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox4 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox6 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox5 = new JFXComboBox<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    comboBox.getItems().add("yes");
    comboBox.getItems().add("no");

    comboBox2.getItems().add("yes");
    comboBox2.getItems().add("no");

    comboBox3.getItems().add("yes");
    comboBox3.getItems().add("no");

    comboBox4.getItems().add("yes");
    comboBox4.getItems().add("no");

    comboBox5.getItems().add("yes");
    comboBox5.getItems().add("no");

    comboBox6.getItems().add("yes");
    comboBox6.getItems().add("no");
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  public void goToRequestPage(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("ConfirmationPageCovid.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void continuePage(ActionEvent actionEvent) throws IOException {
    if (count == 0) {
      submit.setDisable(true);
    } else goToRequestPage(loader, appPrimaryScene);
  }

  @FXML
  private void validateButton() {
    if (comboBox.getValue() == null
        || comboBox2.getValue() == null
        || comboBox3.getValue() == null
        || comboBox4.getValue() == null
        || comboBox5.getValue() == null
        || comboBox6.getValue() == null) {
      submit.setDisable(true);
    } else {
      count += 1;
      submit.setDisable(false);
    }
  }
}
