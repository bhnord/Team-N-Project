package edu.wpi.cs3733.d21.teamN.appointments;

import com.google.inject.Inject;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.views.MasterController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class AppointmentController extends MasterController implements Initializable {
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  private Scene appPrimaryScene;

  @FXML private AnchorPane anchorPane;

  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @Inject
  public void setLoader(FXMLLoader loader) {
    this.loader = loader;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println(appPrimaryScene);
    System.out.println(loader);
    System.out.println(anchorPage);
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Login");
  }

  public void update(ActionEvent actionEvent) {}

  public void deleteAppointment(ActionEvent actionEvent) {}

  public void addApointment(ActionEvent actionEvent) {}
}
