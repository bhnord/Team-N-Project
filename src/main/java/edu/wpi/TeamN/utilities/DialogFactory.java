package edu.wpi.TeamN.utilities;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DialogFactory {
  private final JFXDialogLayout content = new JFXDialogLayout();
  private final StackPane stackPane;
  private JFXDialog dialog;

  public DialogFactory(StackPane stackPane) {
    this.stackPane = stackPane;
  }

  public JFXDialog creatDialog(String heading, String body) {
    content.setHeading(new Text(heading));
    content.setBody(new Text(body));
    dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
    JFXButton button = new JFXButton("Okay");
    button.setOnAction(action -> dialog.close());
    content.setActions(button);
    dialog.show();
    System.out.println("showing");
    return dialog;
  }
}
