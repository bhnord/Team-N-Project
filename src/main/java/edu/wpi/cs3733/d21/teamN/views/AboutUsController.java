package edu.wpi.cs3733.d21.teamN.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.state.HomeState;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javax.inject.Inject;

public class AboutUsController extends MasterController implements Initializable {
  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;

  @FXML private JFXButton AboutUs;

  @FXML private AnchorPane anchorPane;
  @FXML Rectangle darkMode;

  @FXML Label l1, l2;
  @FXML JFXTextArea tA;
  @FXML private Text HospitalStatement;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "About Us");
    darkMode.setVisible(db.getCurrentUser().getDarkMode());
    if (db.getCurrentUser().getDarkMode()) {
      l1.setStyle("-fx-text-fill: WHITE");
      l2.setStyle("-fx-text-fill: WHITE");
      tA.setStyle("-fx-text-fill: WHITE");
      HospitalStatement.setFill(Color.web("WHITE"));
    }
  }
}
