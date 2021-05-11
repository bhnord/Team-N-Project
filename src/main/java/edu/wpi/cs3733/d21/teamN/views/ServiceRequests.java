package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import edu.wpi.cs3733.d21.teamN.services.database.NamedForm;
import edu.wpi.cs3733.d21.teamN.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceRequests extends MasterController implements Initializable {

  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private AnchorPane anchorPane;
  private Scene appPrimaryScene;
  @FXML Rectangle darkMode;
  @FXML private JFXListView<JFXButton> listView;

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
    darkMode.setVisible(db.getCurrentUser().getDarkMode());

    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Database");
    switch (db.getCurrentUser().getType()) {
        // different login cases to be implemented later
      case ADMINISTRATOR:
      case EMPLOYEE:
      case PATIENT:
        break;
    }
    loadRequestFromDB();
  }

  private void loadRequestFromDB() {
    for (NamedForm form : db.getAllForms()) {
      //            if (form.getForm().isRequest()) {
      JFXButton btn = new JFXButton(form.getName());
      btn.setId(String.valueOf(form.getId()));
      String style =
          "-fx-background-color: "
              + "#"
              + db.getCurrentUser().getAppColor().substring(2)
              + "; -fx-background-radius: 25; -fx-text-fill: WHITE; -fx-font-family: 'Roboto Black'; -fx-font-size: 26;";
      btn.setStyle(style);
      btn.setAlignment(Pos.TOP_LEFT);
      btn.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
      btn.setPrefSize(1000, 310);
      btn.setMaxSize(100000, 100000);
      btn.setOnMouseClicked(
          event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
              Parent root = null;
              try {
                root = loader.load(getClass().getResourceAsStream("Templateform.fxml"));
                appPrimaryScene.setRoot(root);
                FormController formController = loader.getController();
                formController.setUp(db.getForm(form.getId()).getForm());
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          });
      listView.getItems().add(btn);
      //            }
    }
  }

  /**
   * advance Takes from Service Request Page to separate service requests
   *
   * @param actionEvent Button click
   * @throws IOException
   */
  public void advance(ActionEvent actionEvent) throws IOException {
    String file = "Requests/" + ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }
}
