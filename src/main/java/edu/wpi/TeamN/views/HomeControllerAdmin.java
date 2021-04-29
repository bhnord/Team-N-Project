package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HomeControllerAdmin extends MasterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;

  @FXML private JFXButton CovidForm;
  @FXML private Tooltip ttCovidForm;
  @FXML private JFXButton exit;
  @FXML private Tooltip ttExit;
  @FXML private JFXButton logOutButton;
  @FXML private Tooltip ttLogOutButton;

  /** For sidebar nested FXML implementation */
  @FXML private Window sideBar;

  @FXML private SideBarController sideBarController;
  @FXML private AnchorPane anchorPane;

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

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.debug(state.toString());
    AnchorPane pane = FXMLLoader.load(getClass().getResource("SideBar.fxml"));
    anchorPane.getChildren().setAll(pane);
  }

  public void advance(ActionEvent actionEvent) throws IOException {
    String file = "Requests/" + ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }

  public void advanceViews(ActionEvent actionEvent) throws IOException {
    String file = ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }

  public void map(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("MapAdmin2.fxml"));
    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    Stage stage = (Stage) appPrimaryScene.getWindow();
    // stage.setX(bounds.getMinX());
    // stage.setY(bounds.getMinY());
    // stage.setWidth(bounds.getWidth());
    // stage.setHeight(bounds.getHeight());
    appPrimaryScene.setRoot(root);
  }

  public void pathFind(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("Pathfinder.fxml"));
    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    Stage stage = (Stage) appPrimaryScene.getWindow();
    // stage.setX(bounds.getMinX());
    // stage.setY(bounds.getMinY());
    // stage.setWidth(bounds.getWidth());
    // stage.setHeight(bounds.getHeight());
    appPrimaryScene.setRoot(root);
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }
}
