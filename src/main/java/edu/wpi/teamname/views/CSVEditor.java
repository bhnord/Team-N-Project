package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXListView;
import edu.wpi.teamname.services.ServiceTwo;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
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

  @FXML private Button HomeView;

  @FXML private JFXListView<Label> listView;

  private Scene appPrimaryScene;

  JFileChooser fc =
      new JFileChooser() {
        @Override
        protected JDialog createDialog(Component parent) throws HeadlessException {
          // intercept the dialog created by JFileChooser
          JDialog dialog = super.createDialog(parent);
          dialog.setModal(true); // doesn't close pop-up until dealt with
          dialog.setAlwaysOnTop(true);
          FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILES", "csv");
          fc.setFileFilter(filter);
          return dialog;
        }
      };

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
  public void initialize(URL url, ResourceBundle rb) {
    // log.debug(state.toString());

    /** PLACEHOLDER TESTING FOR CSV NODES* */
    for (int i = 0; i < 4; i++) {
      try {
        Label lbl = new Label("Item" + i);
        listView.getItems().add(lbl);
      } catch (Exception ex) {
        Logger.getLogger(CSVEditor.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @FXML
  private void load(ActionEvent event) {

    if (!listView.isExpanded()) {
      listView.setExpanded(true);
      listView.depthProperty().set(1);
      listView.setExpanded(false);
      listView.depthProperty().set(1);
      listView.setExpanded(true);
      listView.depthProperty().set(1);
    } else {
      listView.setExpanded(false);
      listView.depthProperty().set(0);
    }
  }

  @FXML
  private void addNew(ActionEvent event) {
    listView.getItems().add(new Label("new node"));
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void openFile(ActionEvent actionEvent) throws IOException {
    // fc.setCurrentDirectory(new File("c:\\temp"));

    int returnValue = fc.showOpenDialog(fc);
    //    FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
    //    fc.setFileFilter(filter);
    if (returnValue == JFileChooser.APPROVE_OPTION) {

      File file = fc.getSelectedFile();
      // fc.addChoosableFileFilter(new FileNameExtensionFilter("CSV File", "csv"));
      loadSuccess.setText("File successfully loaded!");
      messageLabel.setText("" + file);
    } else {
      messageLabel.setText("no file chosen");
    }
  }
}