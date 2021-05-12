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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.javafx.FontIcon;

@Slf4j
public class ServiceRequests extends MasterController implements Initializable {

  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private AnchorPane anchorPane;
  private Scene appPrimaryScene;
  @FXML Rectangle darkMode;
  @FXML private JFXListView<HBox> listView;

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
      if (form.getForm().isRequest()) {
        HBox g = new HBox();
        g.setPadding(new Insets(10, 10, 10, 30));
        if (!form.getForm().getIcon().equals("")) {
          FontIcon fontIcon = new FontIcon();
          fontIcon.setIconLiteral(form.getForm().getIcon());
          fontIcon.setIconSize(45);
          fontIcon.setIconColor(Color.color(1, 1, 1, 1));
          g.getChildren().add(fontIcon);
        }
        JFXButton btn = new JFXButton(form.getName());
        btn.setId(String.valueOf(form.getId()));
        String style =
            "-fx-background-color: "
                + "#"
                + db.getCurrentUser().getAppColor().substring(2)
                + "; -fx-background-radius: 10; -fx-text-fill: WHITE; -fx-font-family: 'Roboto Black'; -fx-font-size: 33;";
        g.setStyle(style);
        g.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(style);
        g.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        g.setPrefSize(750, 115);
        g.setMaxSize(750, 100000);
        g.setOnMouseClicked(
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
        g.getChildren().add(btn);

        listView.getItems().add(g);
      }
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
