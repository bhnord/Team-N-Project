package edu.wpi.teamname.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXListView;
import edu.wpi.teamname.services.ServiceTwo;
import edu.wpi.teamname.services.database.DatabaseService;
import edu.wpi.teamname.state.HomeState;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVEditor extends masterController implements Initializable {

  @Inject DatabaseService db;
  @Inject ServiceTwo graph;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;


  @FXML
  private Button HomeView;

  @FXML
  private JFXListView<Label> listView;


  private Scene appPrimaryScene;

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
    log.debug(state.toString());
    for(int i=0; i<4; i++){
      try{
        Label lbl = new Label("Item" + i);
        listView.getItems().add(lbl);
      } catch (Exception ex){
        Logger.getLogger(CSVEditor.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @FXML
  private void load(ActionEvent event){

  }

  @FXML
  private void addNew(ActionEvent event){

  }


  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

}
