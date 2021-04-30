package edu.wpi.TeamN.map;

import edu.wpi.TeamN.views.MapEditor;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class NodeActionHandling implements IActionHandling {

  private final MapEditor mapEditor;
  private final MapNodeEditor mapNodeEditor;
  private final MapEdgeEditor mapEdgeEditor;

  public NodeActionHandling(
      MapEditor mapEditor, MapNodeEditor mapNodeEditor, MapEdgeEditor mapEdgeEditor) {
    this.mapEditor = mapEditor;
    this.mapNodeEditor = mapNodeEditor;
    this.mapEdgeEditor = mapEdgeEditor;
  }

  @Override
  public void setNodeInfo(Group root) {
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            System.out.println("asdfasdf");
            mapNodeEditor.showNodeProperties(root);
            mapEditor.setCurrent(root);
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
            mapEditor.setCurrent(root);
          }
        });
  }

  @Override
  public void setNodeStartLink(Group root) {
    root.setOnMousePressed(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapEditor.mouseClick(event);
          }
        });
  }

  @Override
  public void setNodeEndLink(Group root) {
    root.setOnMouseReleased(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapEditor.releaseMouse(event);
          }
        });
  }

  @Override
  public void addNodeToPath(Group node) {}

  @Override
  public void removeNodeToPath(Group node) {}
}
