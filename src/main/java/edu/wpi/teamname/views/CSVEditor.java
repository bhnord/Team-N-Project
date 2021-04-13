package edu.wpi.teamname.views;

import com.google.inject.Inject;
import edu.wpi.teamname.services.ServiceTwo;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javax.swing.*;
import javax.swing.JFileChooser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVEditor extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject ServiceTwo graph;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;

  @FXML private Label messageLabel;
  @FXML private Label loadSuccess;

  private Scene appPrimaryScene;

  private final JFileChooser openFileChooser;
  private Component editor;

  public CSVEditor() {
    openFileChooser = new JFileChooser();
    openFileChooser.setCurrentDirectory(new File("c:\\temp"));
  }

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
    log.debug(state.toString());
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void openFile(ActionEvent actionEvent) throws IOException {
    int returnValue = openFileChooser.showOpenDialog(editor);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File file = openFileChooser.getSelectedFile();
      loadSuccess.setText("File successfully loaded!");
      messageLabel.setText("" + file);
    } else {
      messageLabel.setText("no file chosen");
    }
  }
}
