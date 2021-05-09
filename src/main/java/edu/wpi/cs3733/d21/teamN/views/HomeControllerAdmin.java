package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.d21.teamN.faceLogin.FaceLogin;
import edu.wpi.cs3733.d21.teamN.form.Form;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.User;
import edu.wpi.cs3733.d21.teamN.state.HomeState;
import edu.wpi.cs3733.d21.teamN.utilities.DialogFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HomeControllerAdmin extends MasterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;

  // all buttons for FXML page that can be hidden, hide in pairs
  @FXML private JFXButton mapPathfinder;
  @FXML
  private Label BackMapPathfinder,
      BackMapEditor,
      BackServiceRequests,
      BackCurrentRequests,
      BackEmployeeEditor,
      BackFindUs,
      BackFormEditor;
  @FXML private JFXButton mapEditor;
  @FXML private JFXButton ServiceRequests;
  @FXML private JFXButton EmployeeEditor;
  @FXML private JFXButton CurrentRequests;
  @FXML private JFXButton FormEditor;
  @FXML private Label LogIn;
  @FXML private Group logInGroup;

  // Login implementation
  @FXML private JFXTextField usernameField;
  @FXML private JFXPasswordField passwordField;
  @FXML private JFXButton goToHomePage;
  @FXML private Label incorrectLogin;
  @FXML private StackPane rootStackPane;
  private String accountUsername = "";
  private String accountPassword = "";

  // For sidebar nested FXML implementation
  @FXML private AnchorPane anchorPane;
  private DialogFactory dialogFactory;
  private Scene appPrimaryScene;
  private User user;

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
    if (db.getCurrentUser() == null || db.getCurrentUser().getUsername().equals("guest")) {
      db.login("guest", "guest");
      super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Login");

      makeInvisible(EmployeeEditor);
      makeInvisible(BackEmployeeEditor);
      makeInvisible(CurrentRequests);
      makeInvisible(BackCurrentRequests);
      makeInvisible(mapEditor);
      makeInvisible(BackMapEditor);
      makeInvisible(ServiceRequests);
      makeInvisible(BackServiceRequests);
      makeInvisible(FormEditor);
      makeInvisible(BackFormEditor);

      updateStyle("0x748cdc");
      logInInit();
    } else {

      super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Home");

      user = db.getCurrentUser();
      if (user.getAppColor().equals("0x748cdc")) {
        updateStyle("0x748cdc");
      } else {
        updateStyle(user.getAppColor());
      }
      switch (db.getCurrentUser().getType()) {
          // different login cases
        case ADMINISTRATOR:
          LogIn.setVisible(false);
          LogIn.setManaged(false);
          logInGroup.setVisible(false);
          logInGroup.setManaged(false);
          break;
        case EMPLOYEE:
          makeInvisible(EmployeeEditor);
          makeInvisible(BackEmployeeEditor);
          makeInvisible(mapEditor);
          makeInvisible(BackMapEditor);
          LogIn.setVisible(false);
          LogIn.setManaged(false);
          logInGroup.setVisible(false);
          logInGroup.setManaged(false);

          makeInvisible(FormEditor);
          makeInvisible(BackFormEditor);
          break;
        case PATIENT:
          makeInvisible(EmployeeEditor);
          makeInvisible(BackEmployeeEditor);
          makeInvisible(CurrentRequests);
          makeInvisible(BackCurrentRequests);
          makeInvisible(mapEditor);
          makeInvisible(BackMapEditor);

          makeInvisible(FormEditor);
          makeInvisible(BackFormEditor);
          LogIn.setVisible(false);
          LogIn.setManaged(false);
          logInGroup.setVisible(false);
          logInGroup.setManaged(false);
          break;
      }
    }

    // if login matches the name of the person filling out covid form
    //    int idCovid = db.getCurrentUser().getId();
    //    if (db.getCovidFormByUserId(idCovid) != null) {
    //      // pop-up for main entrance
    //      if (db.getCovidFormByUserId(idCovid).isOk() == true) {
    //        dialogFactory.creatDialogOkayWithAction(
    //            "",
    //            "Your covid form has been processed! You can enter the hospital through the main
    // entrance.",
    //            event -> {
    //              try {
    //                advanceHome();
    //              } catch (IOException e) {
    //                e.printStackTrace();
    //              }
    //            });
    //      }
    //      // pop-up for emergency
    //      else {
    //        dialogFactory.creatDialogOkayWithAction(
    //            "",
    //            "Your covid form has been processed! Please enter the hospital through the
    // emergency entrance",
    //            event -> {
    //              try {
    //                advanceHome();
    //              } catch (IOException e) {
    //                e.printStackTrace();
    //              }
    //            });
    //      }
    //    }
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public String accountInfo() {
    if (db.getCurrentUser() != null) {
      String a = "user: " + db.getCurrentUser().getUsername();
      // String a = "                  jjjjjjjjj";
      // Label label = new Label(a);
      return a;
      // accountSettingsGroup.getChildren().add(label);
    } else {

      String a = "      user: guest";
      //  Label label = new Label(a);
      return a;
      // accountSettingsGroup.getChildren().add(label);
    }
  }

  /**
   * makeInvisible
   *
   * @param button: a group to be taken out and managed in the sidebar
   */
  public void makeInvisible(JFXButton button) {
    button.setVisible(false);
    button.setManaged(false);
  }

  public void makeInvisible(Label button) {
    button.setVisible(false);
    button.setManaged(false);
  }

  /**
   * advanceViews Loads a new page *not for service requests*
   *
   * @param actionEvent Button press
   * @throws IOException
   */
  public void advanceViews(ActionEvent actionEvent) throws IOException {
    String file = ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }

  /**
   * map advances to map FXML
   *
   * @param actionEvent Button Press
   * @throws IOException
   */
  public void map(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("MapAdmin2.fxml"));
    appPrimaryScene.setRoot(root);
  }

  /**
   * pathFind advances to pathFinder FXML
   *
   * @param actionEvent Button Press
   * @throws IOException
   */
  public void pathFind(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("Pathfinder.fxml"));
    appPrimaryScene.setRoot(root);
  }

  /**
   * findUs advances to findUs FXML
   *
   * @param actionEvent Button Press
   * @throws IOException
   */
  public void findUs(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("FindUs.fxml"));
    appPrimaryScene.setRoot(root);
  }

  public void formEditor(ActionEvent actionEvent) throws IOException {
    Parent root = loader.load(getClass().getResourceAsStream("EditForms.fxml"));
    FormsEditorController controller = loader.getController();
    controller.setUp();
    appPrimaryScene.setRoot(root);
  }

  /** LOGIN FUNCTIONS */
  public void logInInit() {
    // Login init
    /** Locking submit button to start* */
    goToHomePage.setDisable(true);
    goToHomePage.setDefaultButton(true);

    /** USERNAME input and password* */
    RequiredFieldValidator reqInputValid = new RequiredFieldValidator();
    reqInputValid.setMessage("Cannot be empty");
    usernameField.getValidators().add(reqInputValid);
    usernameField
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) usernameField.validate();
            });
    reqInputValid.setMessage("Cannot be empty");
    passwordField.getValidators().add(reqInputValid);
    passwordField
        .focusedProperty()
        .addListener(
            (o, oldVal, newVal) -> {
              if (!newVal) passwordField.validate();
            });
  }

  public String getUsername() {
    accountUsername = usernameField.getText();
    return usernameField.getText();
  }

  public String getPassword() {
    accountPassword = passwordField.getText();
    return passwordField.getText();
  }

  @FXML
  private void continueToHomePage() throws IOException {
    if (db.login(usernameField.getText(), passwordField.getText())) {
      super.advanceHome(loader, appPrimaryScene);
    } else {
      incorrectLogin.setText("INCORRECT USERNAME OR PASSWORD, TRY AGAIN");
      incorrectLogin.setAlignment(Pos.CENTER);
    }
  }

  @FXML
  public void register() throws IOException {
    super.register(loader, appPrimaryScene);
  }

  @FXML
  public void credits() throws IOException {
    super.credits(loader, appPrimaryScene);
  }

  @FXML
  private void validateButton() {
    if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
      goToHomePage.setDisable(false);
    } else {
      goToHomePage.setDisable(true);
    }
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  public void test(ActionEvent actionEvent) throws IOException {
    //    loader.setController(new FormController());
    Parent root = loader.load(getClass().getResourceAsStream("Templateform.fxml"));
    appPrimaryScene.setRoot(root);
    Form form = new Form();
    FormController formController = loader.getController();
    formController.setUp(form);
  }

  public void testEdit(ActionEvent actionEvent) throws IOException {
    //    loader.setController(new FormEditorController());
    //    Parent root = loader.load(getClass().getResourceAsStream("Template.fxml"));
    //    appPrimaryScene.setRoot(root);
    //    Form form = new Form();
    //    FormEditorController controller = loader.getController();
    //    controller.setUp(form);
  }

  public void updateStyle(String color) {
    String style =
        "-fx-background-color: " + "#" + color.substring(2) + "; -fx-background-radius: 25;";
    Label[] lA = {
      BackMapPathfinder,
      BackMapEditor,
      BackServiceRequests,
      BackCurrentRequests,
      BackEmployeeEditor,
      BackFindUs,
      BackFormEditor
    };
    for (Label a : lA) a.setStyle(style);

    if (db.getCurrentUser().getDarkMode()) {
      darkMode.setVisible(true);
    } else {
      darkMode.setVisible(false);
    }
  }

  @FXML
  private void loginWithFace() {
    FaceLogin facialRecognition = new FaceLogin(db);
    User user = facialRecognition.getUserFromFace();
    if (user != null) {
      db.setLoggedInUser(user);
      super.advanceHome(loader, appPrimaryScene);
    } else {
      dialogFactory = new DialogFactory(rootStackPane);
      dialogFactory.creatDialogOkay(
          "Couldn't Log You In",
          "Sorry we couldn't log you in"
              + " with FaceID. Please log in with your username and password.");
    }
  }
}
