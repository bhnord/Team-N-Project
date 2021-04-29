package edu.wpi.TeamN.mapEntity;

import edu.wpi.TeamN.views.MapController;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class NodeActionHandling implements IActionHandling {

  private final MapController mapController;
  private final MapNodeEditor mapNodeEditor;
  private final MapEdgeEditor mapEdgeEditor;

  public NodeActionHandling(
      MapController mapController, MapNodeEditor mapNodeEditor, MapEdgeEditor mapEdgeEditor) {
    this.mapController = mapController;
    this.mapNodeEditor = mapNodeEditor;
    this.mapEdgeEditor = mapEdgeEditor;
  }

  @Override
  public void setNodeInfo(Group root) {
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapNodeEditor.showNodeProperties(root);
            mapController.setCurrent(root);
          }
        });
  }

  public void nodeHover(Group root) {
    //      root.on
  }

  @Override
  public void setEdgeInfo(Group root) {
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapEdgeEditor.showEdgeProperties(root);
            mapController.setCurrent(root);
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

  @Override
  public void addNodeToPath(Group node) {}

  @Override
  public void removeNodeToPath(Group node) {}
}
