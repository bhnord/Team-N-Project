package edu.wpi.TeamN.views;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // log.debug(state.toString());
    CovidForm.setTooltip(ttCovidForm);
    logOutButton.setTooltip(ttLogOutButton);
    exit.setTooltip(ttExit);

    //    groupLogOut.setVisible(false);
    //    groupLogOut.setManaged(false);
  }

  @Override
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    super.setAppPrimaryScene(appPrimaryScene);
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
}
