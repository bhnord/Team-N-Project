package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import edu.wpi.TeamN.form.Form;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class FormController extends MasterController implements Initializable {

  Form form;

  @FXML VBox elements;
  @FXML Label title;
  @FXML AnchorPane anchorPane;
  @Inject private FXMLLoader loader;

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
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Service Request");
  }

  public void setUp(Form form) {
    this.form = form;
    form.build(this);
  }

  public VBox getElements() {
    return elements;
  }

  public Label getTitle() {
    return title;
  }

  public void submit(ActionEvent actionEvent) throws IOException {}
}
