package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d21.teamN.faceLogin.FaceEnroller;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class FacialRecognitionController extends MasterController implements Initializable {
  @Inject private DatabaseService db;
  @Inject private FXMLLoader loader;
  @Inject private Scene appPrimaryScene;

  @FXML private ImageView imageView;
  @FXML private JFXButton saveButton;
  @FXML private StackPane stackPane;
  FaceEnroller faceEnroller;

  @FXML AnchorPane anchorPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "FaceRec");

    faceEnroller = new FaceEnroller(db, imageView);
    //    db.setLoggedInUser(db.getUserByUsername("Michael"));
  }

  @FXML
  private void toggleSave() {
    faceEnroller.saveFace();
  }

  @FXML
  private void capture() {
    //    appPrimaryScene.setOnMouseClicked(System.out::println);
    stackPane.getScene().getWindow().setOnCloseRequest(event -> faceEnroller.releaseCamera());
    faceEnroller.startEnroller();
  }
}
