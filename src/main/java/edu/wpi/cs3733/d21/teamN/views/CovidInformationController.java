package edu.wpi.cs3733.d21.teamN.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class CovidInformationController extends MasterController {

  @FXML private AnchorPane anchorPane;
  @FXML private Rectangle darkMode;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "CovidInformation");

    //    loadEmployeeDropdown(employeeDropdown);
  }
}
