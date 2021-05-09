package edu.wpi.cs3733.d21.teamN.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.state.HomeState;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javax.inject.Inject;

public class AboutUsController extends MasterController implements Initializable {
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;

  @FXML private JFXButton AboutUs;
  @FXML private Text HospitalStatement;
  @FXML private AnchorPane anchorPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "About Us");

    //    loadEmployeeDropdown(employeeDropdown);
  }
}
