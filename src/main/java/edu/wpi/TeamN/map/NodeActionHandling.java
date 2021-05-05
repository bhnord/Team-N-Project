package edu.wpi.TeamN.map;

import edu.wpi.TeamN.views.MapEditor;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

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
    root.setOnMouseExited(
        event -> {
          Circle c = mapEditor.getNodeSet().get(root.getId()).get_shape();
          c.setRadius(c.getRadius() / 1.3);
        });
    root.setOnMouseEntered(
        event -> {
          Circle c = mapEditor.getNodeSet().get(root.getId()).get_shape();
          c.setRadius(c.getRadius() * 1.3);
        });
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            mapEditor.getMapDrawer().captureClick(event);
            mapNodeEditor.setDragger(mapEditor.getNodeSet().get(root.getId()));
            mapNodeEditor.showNodeProperties(root);
            mapNodeEditor.clearSelection(mapEditor.getNodeSet().get(root.getId()));
            mapNodeEditor.addNode(mapEditor.getNodeSet().get(root.getId()));
            mapEditor.getEditor().getSelectionModel().select(0);
          }
        });
  }

  public void setNodeDrag(Circle root) {
    root.setOnMouseDragged(
        event -> {
          //          root.setCenterX(event.getX());
          //          root.setCenterY(event.getY());
          //          Node n = mapEditor.getNodeSet().get(root.getId());
          //          n.set_x(event.getX() * mapEditor.getUpScale());
          //          n.set_y(event.getY() * mapEditor.getUpScale());
          //          for (Node.Link l : n.get_neighbors()) {
          //            l._shape.setStartX(l._this.get_x() * mapEditor.getDownScale());
          //            l._shape.setStartY(l._this.get_y() * mapEditor.getDownScale());
          //
          //            l._shape.setEndX(l._other.get_x() * mapEditor.getDownScale());
          //            l._shape.setEndY(l._other.get_y() * mapEditor.getDownScale());
          //          }
          //            mapEditor.ma
        });
  }

  public void nodeHover(Group root) {}

  @Override
  public void setEdgeInfo(Group root) {
    root.setOnMousePressed(
        event -> {
          mapEditor.getMapDrawer().captureClick(event);
        });
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {

            mapEdgeEditor.showEdgeProperties(root);
            mapEditor.setCurrent(root);
            mapEditor.getEditor().getSelectionModel().select(1);
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
