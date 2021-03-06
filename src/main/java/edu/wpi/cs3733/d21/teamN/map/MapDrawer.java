package edu.wpi.cs3733.d21.teamN.map;

import edu.wpi.cs3733.d21.teamN.services.algo.Node;
import edu.wpi.cs3733.d21.teamN.views.MapController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class MapDrawer {
  private final MapController mapController;
  private final String[] maps = {"L1", "L2", "g", "1", "2", "3"};
  private String currentMap;
  final DoubleProperty zoomProperty = new SimpleDoubleProperty(1);
  private double pressedX, pressedY;
  private double offsetX, offsetY;
  private final double maxZoom = 5.6;
  private final double minZoom = 1;
  private double maxImgWidth;
  private double maxImgHeight;

  public Circle getDragging() {
    return dragging;
  }

  public void setDragging(Circle dragging) {
    this.dragging = dragging;
  }

  private Circle dragging;

  public boolean isDragging() {
    return this.draggedLine != null;
  }

  private Group draggedLineGroup;
  private Line draggedLine;

  private Group boundingBoxGroup;
  private Rectangle boundingBox;

  public MapDrawer(MapController mapControllerI) {
    this.mapController = mapControllerI;
    currentMap = maps[3];
  }

  public Group drawLine(String id, Node node1, Node node2) {
    Line simpleNode =
        new Line(
            node1.get_x() * mapController.getDownScale(),
            node1.get_y() * mapController.getDownScale(),
            node2.get_x() * mapController.getDownScale(),
            node2.get_y() * mapController.getDownScale());
    simpleNode.setTranslateZ(5);
    simpleNode.setStrokeWidth(mapController.getUserPrefs().getEdgeWidth());
    simpleNode.setStroke(mapController.getPathColor().getValue());
    Group root = new Group(simpleNode);
    root.setId(id);
    return root;
  }

  public Group drawBoundingBox(double x, double y) {
    Rectangle rectangle = new Rectangle(x, y, 0, 0);
    rectangle.setFill(Color.rgb(255, 255, 255, .5));
    Group root = new Group(rectangle);
    root.setId("b_" + x + ":" + y);
    mapController.getMapAnchor().getChildren().add(3, root);
    this.boundingBoxGroup = root;
    this.boundingBox = rectangle;
    return root;
  }

  public Rectangle endBoundingBox(double x, double y) {
    Rectangle temp = boundingBox;
    if (boundingBox != null) {
      mapController.getMapAnchor().getChildren().remove(boundingBoxGroup);
      boundingBox = null;
      boundingBoxGroup = null;
    }
    return temp;
  }

  public Group startLine(Node node1) {
    System.out.println("here");
    Line simpleNode =
        new Line(
            node1.get_x() * mapController.getDownScale(),
            node1.get_y() * mapController.getDownScale(),
            node1.get_x() * mapController.getDownScale(),
            node1.get_y() * mapController.getDownScale());
    simpleNode.setTranslateZ(5);
    simpleNode.setStrokeWidth(mapController.getUserPrefs().getEdgeWidth());
    simpleNode.setStroke(mapController.getPathColor().getValue());
    Group root = new Group(simpleNode);
    root.setId(node1.get_nodeID() + "_");
    this.draggedLineGroup = root;
    this.draggedLine = simpleNode;
    mapController.getMapAnchor().getChildren().add(3, root);
    return root;
  }

  public void cancelLine() {
    mapController.getMapAnchor().getChildren().remove(draggedLineGroup);
    this.draggedLine = null;
    this.draggedLineGroup = null;
  }

  public Group drawNode(String id, double x, double y, Color color) {
    Circle simpleNode = new Circle(x, y, mapController.getUserPrefs().getNodeSize());
    simpleNode.setTranslateZ(10);
    simpleNode.setFill(color);
    simpleNode.setId(id);
    Group root = new Group(simpleNode);
    root.setId(id);
    return root;
  }

  public void resetColors(HashMap<String, Node> nodeSet) {
    for (Node n : nodeSet.values()) {
      for (Node.Link l : n.get_neighbors()) {
        l._shape.setStroke(Color.BLACK);
        l._shape.getParent().setVisible(false);
      }
    }
  }

  public void colorPath(Color color, ArrayList<Node.Link> ret) {
    for (Node.Link c2 : ret) {
      Line simpleNode = c2._shape;
      if (c2._this.get_floor().equals(currentMap)) simpleNode.setVisible(true);
      simpleNode.setStroke(color);
      //      mapController.correctFloor(c2);
    }
  }

  public void captureClick(MouseEvent e) {
    pressedX = e.getX();
    pressedY = e.getY();
  }

  public void captureMouseDrag(MouseEvent event) {
    event.consume();
    if (event.isControlDown()) {
      if (boundingBox != null) {
        boundingBox.setX(Math.min(pressedX, event.getX()));
        boundingBox.setY(Math.min(pressedY, event.getY()));

        boundingBox.setWidth(Math.abs(event.getX() - pressedX));
        boundingBox.setHeight(Math.abs(event.getY() - pressedY));
      }
    }
    if (event.getButton() == MouseButton.SECONDARY) {
      if (draggedLine != null) {
        draggedLine.setEndX(event.getX());
        draggedLine.setEndY(event.getY());
      }
    }
    if (event.getButton() == MouseButton.PRIMARY && event.isShiftDown() && !event.isControlDown()) {
      mapController
          .getMapAnchor()
          .setTranslateX(mapController.getMapAnchor().getTranslateX() + event.getX() - pressedX);
      mapController
          .getMapAnchor()
          .setTranslateY(mapController.getMapAnchor().getTranslateY() + event.getY() - pressedY);
      updateOffset();
      Bounds bounds =
          mapController.getMapAnchor().localToScene(mapController.getMapAnchor().getLayoutBounds());
      //      System.out.println(offsetY / bounds.getMinY());
      offsetY = bounds.getMinY();
      correctImage(mapController.getMapAnchor());
    }
  }

  public String getCurrentMap() {
    return currentMap;
  }

  public void setMap(String floor) {
    mapController
        .getMapImageView()
        .setImage(
            new Image(
                Objects.requireNonNull(
                    ClassLoader.getSystemResourceAsStream("images/" + floor + ".png"))));
    currentMap = floor;
  }

  public void captureMouseScroll(ScrollEvent event) {
    double scale = 100 * zoomProperty.get();
    System.out.println(((event.getX()) / maxImgWidth));

    if (event.getDeltaY() > 0 && zoomProperty.get() <= maxZoom) {
      zoomProperty.set(zoomProperty.get() * 1.08);
      mapController
          .getMapAnchor()
          .setTranslateX(
              mapController.getMapAnchor().getTranslateX()
                  - scale * ((event.getX()) / maxImgWidth));
      mapController
          .getMapAnchor()
          .setTranslateY(
              mapController.getMapAnchor().getTranslateY()
                  - scale * ((event.getY()) / maxImgHeight));
    } else if (event.getDeltaY() < 0 && zoomProperty.get() >= minZoom) {
      zoomProperty.set(zoomProperty.get() / 1.08);
      mapController
          .getMapAnchor()
          .setTranslateX(
              mapController.getMapAnchor().getTranslateX()
                  + scale * ((event.getX()) / maxImgWidth));
      mapController
          .getMapAnchor()
          .setTranslateY(
              mapController.getMapAnchor().getTranslateY()
                  + scale * ((event.getY()) / maxImgHeight));
    }
    updateOffset();
    //    mapController
    //        .getMapAnchor()
    //        .setTranslateX(mapController.getMapAnchor().getTranslateX() - (newx - oldx));
    //    mapController
    //        .getMapAnchor()
    //        .setTranslateY(mapController.getMapAnchor().getTranslateY() - (newy - oldy));

    updateOffset();

    correctImage(mapController.getMapAnchor());
  }

  public double invTransformX(double input) {
    return input * zoomProperty.get() + offsetX;
  }

  public double invTransformY(double input) {
    return input * zoomProperty.get() + offsetY;
  }

  private double invOffsetX(double input) {
    return input + maxImgWidth * (zoomProperty.get() - 1) / 2;
  }

  private double invOffsetY(double input) {
    return input + (zoomProperty.get() - 1) * 700;
  }

  public double transformX(double input) {
    return ((input - offsetX) / zoomProperty.get());
  }

  public double transformY(double input) {
    return ((input - offsetY) / zoomProperty.get());
  }

  private void correctImage(AnchorPane mapContainer) {
    //    if (transformY(0) > -1 && transformY(0) < 1) {
    //      System.out.println(mapContainer.getTranslateY() / (zoomProperty.get() - 1));
    //    }
    //    if (transformX(0) < 0) {
    //      mapContainer.setTranslateX(invOffsetX(0));
    //    }
    //    if (transformY(0) < 0) {
    //      mapContainer.setTranslateY(invOffsetY(0));
    //    }
    //    if (transformX(maxImgWidth) > maxImgWidth) {
    //      mapContainer.setTranslateX(invOffsetX(-maxImgWidth * (zoomProperty.get() - 1)));
    //    }
    //    if (transformY(maxImgHeight) > maxImgHeight) {
    //      mapContainer.setTranslateY(invOffsetY(-maxImgHeight * (zoomProperty.get() - 1)));
    //    }
  }

  public void setUpZoom(ImageView imageView, AnchorPane mapContainer) {
    maxImgWidth = imageView.getFitWidth();
    maxImgHeight = imageView.getFitHeight();
    mapContainer.setManaged(false);
    zoomProperty.addListener(
        new InvalidationListener() {
          @Override
          public void invalidated(Observable arg0) {
            mapContainer.setScaleX(zoomProperty.get());
            mapContainer.setScaleY(zoomProperty.get());
          }
        });
    updateOffset();
  }

  private void updateOffset() {
    offsetX = mapController.getMapAnchor().getTranslateX();
    //    offsetX -= (maxImgWidth * (zoomProperty.get() - 1)) / 2;
    offsetY = mapController.getMapAnchor().getTranslateY();
    //    offsetY -= (maxImgHeight * (zoomProperty.get() - 1)) / 2;
  }
}
