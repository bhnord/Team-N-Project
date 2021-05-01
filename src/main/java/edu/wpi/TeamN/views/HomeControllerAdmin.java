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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HomeControllerAdmin extends MasterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;

  @FXML private JFXButton mapPathfinder;
  @FXML private JFXButton mapEditor;
  @FXML private JFXButton ServiceRequests;
  @FXML private JFXButton EmployeeEditor;
  @FXML private JFXButton CurrentRequest;

  @FXML private VBox vBox1;
  @FXML private VBox vBox2;

  /** For sidebar nested FXML implementation */
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
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Home");
    switch (db.getCurrentUser().getType()) {
        // different login cases
      case ADMINISTRATOR:
        mapPathfinder.setPrefHeight(vBox1.getHeight() * 10);
        // mapEditor.setPrefHeight(vBox1.getMaxHeight() / 2);
        break;
      case EMPLOYEE:
        EmployeeEditor.setVisible(false);
        EmployeeEditor.setManaged(false);
        mapEditor.setManaged(false);
        mapEditor.setVisible(false);
        break;
      case PATIENT:
      case GUEST:
        EmployeeEditor.setVisible(false);
        EmployeeEditor.setManaged(false);
        CurrentRequest.setVisible(false);
        CurrentRequest.setManaged(false);
        mapEditor.setManaged(false);
        mapEditor.setVisible(false);
        break;
    }
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
}
