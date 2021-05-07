package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.TeamN.services.database.CovidForm;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import edu.wpi.TeamN.utilities.DialogFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CovidFormController extends MasterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;
  private Scene appPrimaryScene;
  @FXML private JFXButton submit = new JFXButton();
  @FXML private AnchorPane anchorPage;
  @FXML private StackPane myStackPane;
  @FXML private AnchorPane anchorPane;
  @FXML private StackPane rootStackPane;
  private DialogFactory dialogFactory;

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

  @FXML JFXComboBox<String> comboBox = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox2 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox3 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox4 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox6 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox5 = new JFXComboBox<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    dialogFactory = new DialogFactory(rootStackPane);
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Covid Form");

    // submit.setDisable(true);
    comboBox.getItems().add("yes");
    comboBox.getItems().add("no");

    comboBox2.getItems().add("yes");
    comboBox2.getItems().add("no");

    comboBox3.getItems().add("yes");
    comboBox3.getItems().add("no");

    comboBox4.getItems().add("yes");
    comboBox4.getItems().add("no");

    comboBox5.getItems().add("yes");
    comboBox5.getItems().add("no");

    comboBox6.getItems().add("yes");
    comboBox6.getItems().add("no");
  }

  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void advanceMap() throws IOException {
    super.advanceMap(loader, appPrimaryScene);
  }

  @FXML
  public void back() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  @FXML
  public void submit() throws IOException {
    if (comboBox.getValue() == null
        || comboBox2.getValue() == null
        || comboBox3.getValue() == null
        || comboBox4.getValue() == null
        || comboBox5.getValue() == null
        || comboBox6.getValue() == null) {
      dialogFactory.creatDialogOkay(
          "Missing Fields", "* You must fill out all required fields of the request to continue\n");
    } else {
      boolean ans[] = new boolean[6];
      ans[0] = comboBox.getValue() == "yes";
      ans[1] = comboBox2.getValue() == "yes";
      ans[2] = comboBox3.getValue() == "yes";
      ans[3] = comboBox4.getValue() == "yes";
      ans[5] = comboBox5.getValue() == "yes";

      CovidForm form = new CovidForm(db.getCurrentUser().getId(), ans, "");
      if (comboBox.getValue() == "yes"
          || comboBox3.getValue() == "yes"
          || comboBox4.getValue() == "yes"
          || comboBox5.getValue() == "yes"
          || comboBox6.getValue() == "yes") {
        dialogFactory.covidFormTextInput(
            " ",
            "Please Elaborate on your symptoms\n",
            form,
            event -> {
              processingPopup();
              db.addCovidForm(form);
            });
        // db.addCovidForm(form);

      } else {
        dialogFactory.creatDialogConfirmCancel(
            "",
            "Are you sure the information you have provided is correct?",
            event -> {
              db.addCovidForm(form);
              processingPopup();
            });
      }
    }
  }

  private void processingPopup() {

    //    int id = db.getCurrentUser().getId();
    //    if (db.getCovidForm(id).isProcessed() == true) {
    //      dialogFactory.close();
    //      // advanceHomePopup();
    //    }

    dialogFactory.createDialog(
        "",
        "Your survey is being processed. Please wait to enter the hospital.",
        event -> {
          // processingPopup();
        });
  }

  private void advanceHomePopup() {

    dialogFactory.creatDialogOkayWithAction(
        "",
        "Your covid form has been processed! You can enter the hospital.",
        event -> {
          try {
            advanceHome();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }
}
