package edu.wpi.teamname.views;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NameNode extends masterController implements Initializable {
  static Stage stage;
  static NameNode box;
  static MapController external;
  public JFXTextField floor;
  public JFXTextField buidling;
  public JFXTextField longName;
  public JFXTextField shortName;

  @FXML private JFXTextField idName;

  public void confirm(MapController mapController) throws IOException {

    external = mapController;

    FXMLLoader fxmlLoader = new FXMLLoader(NameNode.class.getResource("nameNode.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);
    box = fxmlLoader.getController();
    box.stage = new Stage();
    box.stage.setScene(scene);

    box.stage.initModality(
        Modality.APPLICATION_MODAL); // pop-up window won't close until you have dealt with it
    box.stage.setAlwaysOnTop(true);
    box.stage.setTitle("Name Node");

    box.stage.showAndWait();
  }

  @FXML
  public void cancel() throws IOException {
    external.setCancelOrSubmit(false);
    box.stage.close();
  }

  @FXML
  public void submit() {
    external.setCancelOrSubmit(true);
    external.setNodeProperties(
        idName.getText(),
        floor.getText(),
        buidling.getText(),
        longName.getText(),
        shortName.getText());
    box.stage.close();
  }
}
