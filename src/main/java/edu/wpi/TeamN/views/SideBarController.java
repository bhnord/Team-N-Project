package edu.wpi.TeamN.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.TeamN.services.database.DatabaseService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;

public class SideBarController extends MasterController implements Initializable {

  /** Buttons and tooltips */
  @FXML private JFXButton CovidForm;

  @FXML private Tooltip ttCovidForm;
  @FXML private JFXButton exit;
  @FXML private Tooltip ttExit;
  @FXML private JFXButton logOutButton;
  @FXML private Tooltip ttLogOutButton;

  public static Scene appScene;

  /** Groups for implementation on different pages */
  @FXML private Group groupExit;

  @FXML private Group groupLogOut;
  @FXML private Group groupCovid;
  @FXML private Group groupBack;
  @FXML private Group groupHome;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // log.debug(state.toString());
    CovidForm.setTooltip(ttCovidForm);
    logOutButton.setTooltip(ttLogOutButton);
    exit.setTooltip(ttExit);
  }

  @Override
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    super.setAppPrimaryScene(appPrimaryScene);
  }

  @Override
  public void setLoader(FXMLLoader loader) {
    super.setLoader(loader);
  }

  @Override
  public void setDB(DatabaseService db) {
    super.setDB(db);
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  @Override
  public void advanceViews(ActionEvent actionEvent) throws IOException {
    super.advanceViews(actionEvent);
  }

  @FXML
  public void advanceServiceRequest() throws IOException {
    super.advanceServiceRequest(loader, appPrimaryScene);
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void setType(String type) {
    groupExit.setVisible(true);
    groupExit.setManaged(true);
    groupLogOut.setVisible(true);
    groupLogOut.setManaged(true);
    groupCovid.setVisible(true);
    groupCovid.setManaged(true);
    groupBack.setVisible(true);
    groupBack.setManaged(true);
    groupHome.setVisible(true);
    groupHome.setManaged(true);
    if (type.equals("Home")) {
      groupBack.setVisible(false);
      groupBack.setManaged(false);
      groupHome.setVisible(false);
      groupHome.setManaged(false);
    } else if (type.equals("Map") || type.equals("Database")) {
      groupBack.setVisible(false);
      groupBack.setManaged(false);
    } else if (type.equals("Service Request")) {
      // groupCovid.setVisible(false);
      // groupCovid.setManaged(false);
    } else if (type.equals("Covid Form")) {
      groupCovid.setVisible(false);
      groupCovid.setManaged(false);
    } else if (type.equals("Login")) {
      groupLogOut.setVisible(false);
      groupLogOut.setManaged(false);
      groupCovid.setVisible(false);
      groupCovid.setManaged(false);
      groupBack.setVisible(false);
      groupBack.setManaged(false);
      groupHome.setVisible(false);
      groupHome.setManaged(false);
    } else if (type.equals("Register")) {
      groupLogOut.setVisible(false);
      groupLogOut.setManaged(false);
      groupCovid.setVisible(false);
      groupCovid.setManaged(false);
      groupBack.setVisible(false);
      groupBack.setManaged(false);
      groupHome.setVisible(false);
      groupHome.setManaged(false);
    }
  }
}
