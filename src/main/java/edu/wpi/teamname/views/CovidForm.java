package edu.wpi.teamname.views;

import com.google.inject.Inject;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CovidForm extends masterController implements Initializable {

  @FXML private ChoiceBox q1 = new ChoiceBox();

  @FXML private ChoiceBox q2 = new ChoiceBox();

  @FXML private ChoiceBox q3 = new ChoiceBox();

  @FXML private ChoiceBox q4 = new ChoiceBox();

  @FXML private ChoiceBox q5 = new ChoiceBox();

  @FXML private ChoiceBox q6 = new ChoiceBox();

  ObservableList<String> answers = FXCollections.observableArrayList("yes", "no");

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;

  static Stage stage;

  private Scene appPrimaryScene;

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
    log.debug(state.toString());

    q1.setValue("select");
    q1.setItems(answers);
    q2.setValue("select");
    q2.setItems(answers);
    q3.setValue("select");
    q3.setItems(answers);
    q4.setValue("select");
    q4.setItems(answers);
    q5.setValue("select");
    q5.setItems(answers);
    q6.setValue("select");
    q6.setItems(answers);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void goToRequestPage(FXMLLoader childLoader, Scene ChildAppPrimaryScene)
      throws IOException {
    Parent root = childLoader.load(getClass().getResourceAsStream("ConfirmationPageCovid.fxml"));
    ChildAppPrimaryScene.setRoot(root);
  }

  @FXML
  public void continuePage() throws IOException {
    goToRequestPage(loader, appPrimaryScene);
  }
}
