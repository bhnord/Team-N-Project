package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MasterControllerRequest extends MasterController implements Initializable {

  @Inject DatabaseService db;
  @Inject FXMLLoader loader;
  @Inject HomeState state;
  @FXML private Label text;

  @FXML private AnchorPane anchorPane;
  @FXML private SideBarController sideBarController;
  protected Scene appPrimaryScene;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void MissingFieldsDialog(
      ActionEvent actionEvent, Button helpButton, StackPane myStackPane) {
    String title = "Missing Fields";
    JFXDialogLayout dialogContent = new JFXDialogLayout();
    dialogContent.setHeading(new Text(title));
    dialogContent.setBody(
        (new Text("* You must fill out all required fields of the request to continue\n")));
    JFXButton close = new JFXButton("close");
    close.setButtonType(JFXButton.ButtonType.RAISED);
    close.setStyle("-fx-background-color : #748cdc;");
    close.setTextFill(Paint.valueOf("#FFFFFF"));
    dialogContent.setActions(close);

    JFXDialog dialog = new JFXDialog(myStackPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);
    actionEvent.consume();
    close.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dialog.close();
            helpButton.setDisable(false);
          }
        });
    helpButton.setDisable(true);
    dialog.show();
  }
}
