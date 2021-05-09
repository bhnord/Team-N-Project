package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.d21.teamN.form.Form;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FormEditorController extends MasterController implements Initializable {

  Form form;

  @FXML VBox elements;
  @FXML JFXListView<HBox> editor;
  @FXML Label title;
  @FXML AnchorPane anchorPane;
  @Inject private FXMLLoader loader;
  @Inject private DatabaseService db;
  private String ogTitle;

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
    ogTitle = form.getTitle();
    editor.setMinWidth(1000);
    form.editorBuild(editor);
  }

  public Label getTitle() {
    return title;
  }

  public void submit(ActionEvent actionEvent) throws IOException {
//    Parent root = loader.load(getClass().getResourceAsStream("Templateform.fxml"));
//    appPrimaryScene.setRoot(root);
//    FormController formController = loader.getController();
//    formController.setUp(form);
  }

  public void add(ActionEvent actionEvent) {
    form.add(editor);
  }
}
