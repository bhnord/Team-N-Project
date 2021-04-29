package edu.wpi.TeamN.views;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;

public class SideBarController extends masterController implements Initializable {

  /** Buttons and tooltips */
  @FXML private JFXButton CovidForm;

  @FXML private Tooltip ttCovidForm;
  @FXML private JFXButton exit;
  @FXML private Tooltip ttExit;
  @FXML private JFXButton logOutButton;
  @FXML private Tooltip ttLogOutButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // log.debug(state.toString());
    CovidForm.setTooltip(ttCovidForm);
    logOutButton.setTooltip(ttLogOutButton);
    exit.setTooltip(ttExit);
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
