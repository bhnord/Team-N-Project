package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.NamedForm;
import java.awt.*;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FormEditorController extends MasterController implements Initializable {

  NamedForm form;

  @FXML VBox elements;
  @FXML JFXListView<HBox> editor;
  @FXML AnchorPane anchorPane;
  @FXML JFXTextField titleEditor;
  @Inject private FXMLLoader loader;
  @Inject private DatabaseService db;
  private int id;
  @FXML Rectangle darkMode;

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
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Editor");
    titleEditor.setOnKeyReleased(
        e -> {
          this.form.getForm().setTitle(titleEditor.getText());
        });
    darkMode.setVisible(db.getCurrentUser().getDarkMode());
    updateStyle(db.getCurrentUser().getAppColor());
  }

  @FXML Label l1, text111;
  @FXML JFXButton submit;

  public void updateStyle(String color) {
    Color appC = Color.web(color);
    String s = appC.darker().darker().desaturate().toString();
    String style = "-fx-background-color: " + "#" + s.substring(2) + "; -fx-background-radius: 25;";
    text111.setStyle(style);
    submit.setStyle(style);
    style = "-fx-background-color: " + "#" + color.substring(2) + "; -fx-background-radius: 25;";
    if (db.getCurrentUser().getDarkMode()) {
      l1.setStyle(style);
    }
    if (db.getCurrentUser().getDarkMode()) {
      titleEditor.setFocusColor(Color.web("WHITE"));
      titleEditor.setUnFocusColor(Color.web("WHITE"));
      titleEditor.setStyle("-fx-text-inner-color: WHITE;");
    } else {
      titleEditor.setFocusColor(Color.web("BLACK"));
      titleEditor.setUnFocusColor(Color.web("BLACK"));
      titleEditor.setStyle("-fx-text-inner-color: BLACK;");
    }
  }

  public void setUp(NamedForm form) {
    this.form = form;

    System.out.println(form.getId());
    titleEditor.setText(this.form.getForm().getTitle());
    editor.setMinWidth(1000);
    form.getForm().editorBuild(editor);
  }

  //  public Label getTitle() {
  //    return form.getForm().getTitle();
  //  }

  public void submit(ActionEvent actionEvent) throws IOException {
    //    Parent root = loader.load(getClass().getResourceAsStream("Templateform.fxml"));
    //    appPrimaryScene.setRoot(root);
    //    FormController formController = loader.getController();
    //    formController.setUp(form);
    db.updateForm(form);
  }

  public void add(ActionEvent actionEvent) {
    form.getForm().add(editor);
  }
}
