package edu.wpi.TeamN.MapEntity;

import edu.wpi.TeamN.views.MapController;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class NodeActionHandling implements ActionHandlingI {

  private final MapController mapController;

  public NodeActionHandling(MapController mapController) {
    this.mapController = mapController;
  }

  @Override
  public void setNodeInfo(Group root) {
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapController.getCurrent().setText(root.getId());
          }
        });
  }

  @Override
  public void setEdgeInfo(Group root) {
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapController.getCurrent().setText(root.getId());
          }
        });
  }

  @Override
  public void setNodeStartLink(Group root) {
    root.setOnMousePressed(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapController.startLink(event);
          }
        });
  }

  @Override
  public void setNodeEndLink(Group root) {
    root.setOnMouseReleased(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapController.releaseMouse(event);
          }
        });
  }
}
