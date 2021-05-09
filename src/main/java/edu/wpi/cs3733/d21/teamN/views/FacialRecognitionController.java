package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d21.teamN.faceLogin.FaceEnroller;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class FacialRecognitionController extends MasterController implements Initializable {
  @Inject private DatabaseService db;
  @Inject private FXMLLoader loader;
  @Inject private Scene appPrimaryScene;

  @FXML private ImageView imageView;
  @FXML private JFXButton saveButton;
  @FXML private StackPane stackPane;
  private FaceEnroller faceEnroller;
  private SideBarController sideBarController;
  @FXML Rectangle darkMode;
  @FXML AnchorPane anchorPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    sideBarController = super.sideBarSetup(anchorPane, appPrimaryScene, loader, "FaceRec");
    darkMode.setVisible(db.getCurrentUser().getDarkMode());
    faceEnroller = new FaceEnroller(db, imageView);
    //    db.setLoggedInUser(db.getUserByUsername("Michael"));
  }

  @FXML
  private void toggleSave() {
    faceEnroller.saveFace();
  }

  @FXML
  private void capture() {
    stackPane.getScene().getWindow().setOnCloseRequest(event -> faceEnroller.releaseCamera());
    faceEnroller.startEnroller();
    sideBarController.setFaceEnroller(faceEnroller);
  }
}
