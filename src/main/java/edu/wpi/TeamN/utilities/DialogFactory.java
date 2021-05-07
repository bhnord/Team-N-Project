package edu.wpi.TeamN.utilities;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.services.database.CovidForm;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javax.swing.*;

public class DialogFactory {
  //  private final StackPane stackPane;
  private final JFXDialogLayout content = new JFXDialogLayout();
  private JFXDialog dialog;
  private Pane pane;

  public DialogFactory(Pane pane) {
    //    this.stackPane = stackPane;
    this.pane = pane;
    content.getStylesheets().add(getClass().getResource("/StyleSheet/Dialog.css").toExternalForm());

    Text headingText = new Text("");
    headingText.getStyleClass().add("heading-text");
    content.setHeading(headingText);

    Text bodyText = new Text("");
    bodyText.getStyleClass().add("body-text");
    content.setBody(bodyText);
  }

  /**
   * Creates a JFX Dialog with heading of heading and body of body with a single button of "Okay"
   * that closes the dialog.
   *
   * @param heading Heading of the dialog
   * @param body Body of the dialog
   */
  public void creatDialogOkay(String heading, String body) {
    creatDialogOkayWithAction(heading, body, event -> {});
  }

  /**
   * Creates a JFX Dialog with heading of heading and body of body with a single button of "Okay"
   * which must be assigned an action for on click, though it will close the dialog on click no
   * matter what
   *
   * @param heading
   * @param body
   * @param clickAction
   */
  public void creatDialogOkayWithAction(
      String heading, String body, EventHandler<? super MouseEvent> clickAction) {
    Text headingText = (Text) content.getHeading().get(0);
    headingText.setText(heading);
    content.setHeading(headingText);
    Text bodyText = (Text) content.getBody().get(0);
    bodyText.setText(body);
    content.setBody(bodyText);
    StackPane stackPane = new StackPane();
    //    stackPane.setMouseTransparent(true);
    dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
    JFXButton button = new JFXButton("Okay");
    button.setOnMouseClicked(clickAction);
    button.setOnMouseReleased(action -> dialog.close());
    content.setActions(button);
    stackPane.setPickOnBounds(false);
    pane.getChildren().add(stackPane);
    dialog.show();
  }

  /**
   * Creates a JFX Dialog with Heading of heading and Body of body with a cancel button and a
   * confirm button. The confirm button must be assigned an action for on click, though it will
   * close the dialog on click no matter what.
   *
   * @param heading Heading of the dialog
   * @param body Body of the dialog
   * @param clickAction Action to happen on click of the confirm button
   */
  public void creatDialogConfirmCancel(
      String heading, String body, EventHandler<? super MouseEvent> clickAction) {
    Text headingText = (Text) content.getHeading().get(0);
    headingText.setText(heading);
    content.setHeading(headingText);
    Text bodyText = (Text) content.getBody().get(0);
    bodyText.setText(body);
    content.setBody(bodyText);
    StackPane stackPane = new StackPane();
    //    stackPane.setMouseTransparent(true);
    dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
    JFXButton cancelButton = new JFXButton("Cancel");
    cancelButton.setOnAction(action -> dialog.close());
    JFXButton confirmButton = new JFXButton("Confirm");
    confirmButton.setOnMouseClicked(clickAction);
    confirmButton.setOnMouseReleased(action -> dialog.close());
    content.setActions(cancelButton, confirmButton);
    stackPane.setPickOnBounds(false);
    pane.getChildren().add(stackPane);
    dialog.show();
  }

  public void createDialog(
      String heading, String body, EventHandler<? super MouseEvent> clickAction) {
    Text headingText = (Text) content.getHeading().get(0);
    headingText.setText(heading);
    content.setHeading(headingText);
    Text bodyText = (Text) content.getBody().get(0);
    bodyText.setText(body);
    content.setBody(bodyText);
    StackPane stackPane = new StackPane();
    //    stackPane.setMouseTransparent(true);
    dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
    JFXButton button = new JFXButton("  ");
    button.setOnMouseClicked(clickAction);
    button.setOnMouseReleased(action -> dialog.close());
    content.setActions(button);
    stackPane.setPickOnBounds(false);
    pane.getChildren().add(stackPane);
    dialog.show();
  }

  public void close() {
    dialog.close();
  }

  public void covidFormTextInput(
      String heading, String body, CovidForm form, EventHandler<? super MouseEvent> clickAction) {

    Text headingText = (Text) content.getHeading().get(0);
    headingText.setText(heading);
    content.setHeading(headingText);
    Text bodyText = (Text) content.getBody().get(0);
    bodyText.setText(body);
    content.setBody(bodyText);
    StackPane stackPane = new StackPane();
    dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
    JFXTextField textField = new JFXTextField();
    JFXButton confirmButton = new JFXButton("Confirm");
    JFXButton text = new JFXButton("              ");
    confirmButton.setOnMouseClicked(clickAction);
    confirmButton.setOnMouseReleased(
        action -> {
          form.setExtraInfo(textField.getText());
          dialog.close();
        });
    content.setActions(textField, text, confirmButton);
    stackPane.setPickOnBounds(false);
    pane.getChildren().add(stackPane);
    dialog.show();
  }
}
