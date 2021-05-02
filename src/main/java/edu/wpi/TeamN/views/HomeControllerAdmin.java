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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HomeControllerAdmin extends MasterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;

  // all buttons for FXML page that can be hidden, hide in pairs
  @FXML private JFXButton mapPathfinder, BackMapPathfinder;
  @FXML private JFXButton mapEditor, BackMapEditor;
  @FXML private JFXButton ServiceRequests, BackServiceRequests;
  @FXML private JFXButton EmployeeEditor, BackEmployeeEditor;
  @FXML private JFXButton CurrentRequest, BackCurrentRequest;

  //For sidebar nested FXML implementation
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    log.debug(state.toString());
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Home");
    switch (db.getCurrentUser().getType()) {
        // different login cases
      case ADMINISTRATOR:
        break;
      case EMPLOYEE:
        makeInvisible(EmployeeEditor);
        makeInvisible(BackEmployeeEditor);
        makeInvisible(mapEditor);
        makeInvisible(BackMapEditor);
        break;
      case PATIENT:
      case GUEST:
        makeInvisible(EmployeeEditor);
        makeInvisible(BackEmployeeEditor);
        makeInvisible(CurrentRequest);
        makeInvisible(BackCurrentRequest);
        makeInvisible(mapEditor);
        makeInvisible(BackMapEditor);
        break;
    }
  }
  /**
   * makeInvisible
   *
   * @param button: a group to be taken out and managed in the sidebar
   */
  public void makeInvisible(JFXButton button) {
    button.setVisible(false);
    button.setManaged(false);
  }


  /**
   * advanceViews
   * Loads a new page *not for service requests*
   *
   * @param actionEvent Button press
   * @throws IOException
   */
  public void advanceViews(ActionEvent actionEvent) throws IOException {
    String file = ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }

  /**
   * map
   * advances to map FXML
   *
   * @param actionEvent Button Press
   * @throws IOException
   */
  public void map(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("MapAdmin2.fxml"));
    appPrimaryScene.setRoot(root);
  }

  /**
   * pathFind
   * advances to pathFinder FXML
   *
   * @param actionEvent Button Press
   * @throws IOException
   */
  public void pathFind(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("Pathfinder.fxml"));
    appPrimaryScene.setRoot(root);
  }
}
