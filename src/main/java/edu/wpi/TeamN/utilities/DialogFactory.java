package edu.wpi.TeamN.utilities;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DialogFactory {
  //  private final StackPane stackPane;
  private final JFXDialogLayout content = new JFXDialogLayout();
  private JFXDialog dialog;
  private Pane pane;

  public DialogFactory(Pane pane) {
    //    this.stackPane = stackPane;
    this.pane = pane;
    content
        .getStylesheets()
        .add(getClass().getResource("/Fonts/Roboto-Light.css").toExternalForm());
  }

  /**
   * Creates a JFX Dialog with heading of heading and body of body with a single button of "Okay"
   * that closes the dialog.
   *
   * @param heading Heading of the dialog
   * @param body Body of the dialog
   */
  public void creatDialogOkay(String heading, String body) {
    content.setHeading(new Text(heading));
    content.setBody(new Text(body));
    StackPane stackPane = new StackPane();
    stackPane.setMouseTransparent(true);
    dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
    JFXButton button = new JFXButton("Okay");
    button.setOnAction(action -> dialog.close());
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
    content.setHeading(new Text(heading));
    content.setBody(new Text(body));
    StackPane stackPane = new StackPane();
    stackPane.setMouseTransparent(true);
    dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
    JFXButton cancelButton = new JFXButton("Cancel");
    cancelButton.setOnAction(action -> dialog.close());
    JFXButton confirmButton = new JFXButton("Confirm");
    confirmButton.setOnMousePressed(clickAction);
    confirmButton.setOnMouseReleased(action -> dialog.close());
    content.setActions(cancelButton, confirmButton);
    stackPane.setLayoutX(pane.getWidth() / 2);
    stackPane.setLayoutY(pane.getHeight() / 2);
    pane.getChildren().add(stackPane);
    dialog.show();
  }
}
