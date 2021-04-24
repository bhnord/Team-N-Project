package edu.wpi.TeamN.MapEntity;

import edu.wpi.TeamN.views.MapController;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class NodeActionHandling {

  public NodeActionHandling(Group root, MapController mapController) {
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapController.getCurrent().setText(root.getId());
          }
        });
    root.setOnMousePressed(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapController.startLink(event);
          }
        });
    root.setOnMouseReleased(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapController.releaseMouse(event);
          }
        });
  }
}
