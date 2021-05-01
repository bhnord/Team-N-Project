package edu.wpi.TeamN.views;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.map.AdminMap;
import edu.wpi.TeamN.map.MapDrawer;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.algo.PathFinder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PathFinderController extends MapController implements Initializable {
  ArrayList<String> path = new ArrayList<String>();
  ArrayList<Node.Link> nodePath = new ArrayList<Node.Link>();
  @FXML private AnchorPane anchorPane;
  private Scene appPrimaryScene;

  //  @FXML private AnchorPane mapAnchor;
  //  @FXML private ImageView mapImageView;
  //  @FXML private JFXColorPicker nodeColor;
  //  @FXML private JFXColorPicker EXIT;
  //  @FXML private JFXColorPicker ELEV;
  //  @FXML private JFXColorPicker STAI;
  //  @FXML private JFXColorPicker pathColor;
  //  @FXML private JFXColorPicker selectedNodeColor;
  //  @FXML private JFXTextField nodeSize;
  //  @FXML private JFXTextField pathSize;
  @FXML private JFXListView<Label> texutualDescription;

  //  public final double downScale = 0.25;
  //  public final double upScale = 4;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Map");

    nodeColor.setValue(Color.BLUE);
    EXIT.setValue(Color.RED);
    ELEV.setValue(Color.PINK);
    STAI.setValue(Color.ORANGE);
    pathColor.setValue(Color.BLUE);
    selectedNodeColor.setValue(Color.GREEN);
    nodeSize.setText("3.5");
    pathSize.setText("2.5");

    adminMap = new AdminMap(db);
    mapDrawer = new MapDrawer(this);
    mapImageView.setCursor(Cursor.CROSSHAIR);
    this.Load();
    mapDrawer.setUpZoom(mapImageView, mapAnchor);

    //    texutualDescription = new JFXListView<>();
    texutualDescription.setOnMouseClicked(
        event -> {
          Label selected = texutualDescription.getSelectionModel().getSelectedItem();
          if (event.getButton() == MouseButton.PRIMARY && selected != null) {
            ObservableList<Integer> seletedI =
                texutualDescription.getSelectionModel().getSelectedIndices();
            mapDrawer.colorPath(super.getPathColor().getValue(), nodePath);
            Node.Link link = nodePath.get(nodePath.size() - seletedI.get(0) - 1);
            mapDrawer.setMap(link._other.get_floor());
            mapFloor();
            link._shape.setStroke(Color.RED);
          }
        });
    super.init();
    //    load();
    //    mapFloor();
    //    mapDrawer.setUpZoom(super.getMapImageView(), mapAnchor);
  }

  private void updatePath() {
    StringBuilder str = new StringBuilder();
    for (String node : path) {
      if (str.toString().equals("")) str.append(node);
      else str.append(", ").append(node);
    }
  }

  //  @FXML
  //  private void exit(ActionEvent actionEvent) throws IOException {
  //    super.cancel(actionEvent);
  //  }
  //
  //  public void advanceViews(ActionEvent actionEvent) throws IOException {
  //    String file = ((Button) actionEvent.getSource()).getId() + ".fxml";
  //    Parent root = loader.load(getClass().getResourceAsStream(file));
  //    appPrimaryScene.setRoot(root);
  //  }
  //
  //  private void load() {
  //    pathFinderMap
  //        .getEdgeSet()
  //        .forEach(
  //            (key, value) -> {
  //              placeLink(
  //                  key,
  //                  pathFinderMap.getNodeSet().get(value.getStartNode()),
  //                  pathFinderMap.getNodeSet().get(value.getEndNode()));
  //            });
  //    pathFinderMap
  //        .getNodeSet()
  //        .forEach(
  //            (key, value) -> {
  //              placeNode(key, value.get_x() * downScale, value.get_y() * downScale);
  //            });
  //  }
  //
  public Group placeNode(Node n) {
    Group root = super.placeNode(n);
    root.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            System.out.println(path);
            if (event.getButton() == MouseButton.PRIMARY && !path.contains(n.get_nodeID()))
              path.add(n.get_nodeID());
            else if (event.getButton() == MouseButton.SECONDARY) path.remove(n.get_nodeID());
            updatePath();
            selectedNodeColor(new ActionEvent());
          }
        });
    return root;
  }

  @FXML
  private void dragMouse(MouseEvent mouseEvent) {
    this.mapDrawer.captureMouseDrag(mouseEvent);
  }
  //
  //  private JFXColorPicker getType(String id) {
  //    Node node = pathFinderMap.getNodeSet().get(id);
  //    if (node.get_nodeType().contains("ELEV")) {
  //      return ELEV;
  //    } else if (node.get_nodeType().contains("STAI")) {
  //      return STAI;
  //    } else if (node.get_nodeType().contains("EXIT")) {
  //      return EXIT;
  //    } else {
  //      return nodeColor;
  //    }
  //  }
  //
  //  private void placeLink(String id, Node node1, Node node2) {
  //    Group root = mapDrawer.drawLine(id, node1, node2);
  //    root.setId(id);
  //    mapAnchor.getChildren().add(root);
  //    pathFinderMap.makeEdge(id, node1, node2, (Line) root.getChildren().get(0));
  //    root.setCursor(Cursor.CROSSHAIR);
  //    root.setVisible(false);
  //  }
  //
  //  private double getUpScale() {
  //    return upScale;
  //  }
  //
  //  public void PathFind(ActionEvent actionEvent) {
  //    mapDrawer.resetColors(pathFinderMap.getNodeSet());
  //    for (int i = 0; path.size() - 1 > i; i++) {
  //      ArrayList<Node.Link> pathLink = pathFinderMap.pathfind(path.get(i), path.get(i + 1));
  //      // newColorPath(pathColor.getValue(), pathLink);
  //    }
  //  }

  private void reset() {
    for (Node n : getNodeSet().values()) {
      Circle c = (Circle) n.get_shape();
      c.setRadius(super.getNodeSize());
      n.get_shape().setVisible(mapDrawer.getCurrentMap().equals(n.get_floor()));
      n.get_shape().setStroke(Color.BLACK);
    }
    newColorNode(EXIT);
    newColorNode(ELEV);
    newColorNode(STAI);
    newColorNodeaf(new ActionEvent());
    nodePath.clear();
    texutualDescription.getItems().clear();
  }

  public void newColorPath(ActionEvent actionEvent) {
    for (int i = 0; path.size() - 1 > i; i++) {
      System.out.println(getNodeSet());
      ArrayList<Node.Link> pathLink =
          this.pathfind(getNodeSet().get(path.get(i)), getNodeSet().get(path.get(i + 1)));
      nodePath.addAll(pathLink);
      mapDrawer.colorPath(pathColor.getValue(), pathLink);
      PathFinder p = new PathFinder();
      ArrayList<String> s = p.getDescription(pathLink);
      for (String l : s) {
        texutualDescription.getItems().add(new Label(l));
      }
    }
  }

  public void newColorNode(ActionEvent actionEvent) {
    JFXColorPicker a = ((JFXColorPicker) actionEvent.getSource());
    for (Node n : getNodeSet().values()) {
      if (n.get_nodeType().equals(a.getId())) n.get_shape().setFill(a.getValue());
    }
  }

  public void newColorNode(JFXColorPicker a) {
    for (Node n : getNodeSet().values()) {
      if (n.get_nodeType().equals(a.getId())) n.get_shape().setFill(a.getValue());
    }
  }

  public void setMap(ActionEvent actionEvent) {
    mapDrawer.setMap(((Button) actionEvent.getSource()).getId());
    this.mapFloor();
  }

  public void mapFloor() {
    super.mapFloor();
    for (Node.Link link : nodePath) {
      correctFloor(link);
    }
  }

  public void newColorNodeaf(ActionEvent actionEvent) {
    JFXColorPicker a = nodeColor;
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        if (!this.getNodeSet()
                .get(mapAnchor.getChildren().get(i).getId())
                .get_nodeType()
                .contains("ELEV")
            && !this.getNodeSet()
                .get(mapAnchor.getChildren().get(i).getId())
                .get_nodeType()
                .contains("EXIT")
            && !this.getNodeSet()
                .get(mapAnchor.getChildren().get(i).getId())
                .get_nodeType()
                .contains("STAI"))
          ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setFill(a.getValue());
      }
    }
  }

  public void selectedNodeColor(ActionEvent actionEvent) {
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (this.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())
          && path.contains(mapAnchor.getChildren().get(i).getId())) {
        Circle l = ((Circle) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0));
        l.setFill(selectedNodeColor.getValue());
        l.setRadius(Double.parseDouble(nodeSize.getText()) + 1);
      }
    }
  }

  public void clearSelection(ActionEvent actionEvent) {
    reset();
    path = new ArrayList<String>();
  }

  public void newSize(ActionEvent actionEvent) {
    double nodeValue = Double.parseDouble(nodeSize.getText());
    double edgeValue = Double.parseDouble(pathSize.getText());
    if (((JFXTextField) actionEvent.getSource()).getId().equals("nodeSize")
        && nodeValue <= 5
        && nodeValue >= 3) {
      for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
        if (this.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
          ((Circle) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setRadius(nodeValue);
        }
      }
    } else if (((JFXTextField) actionEvent.getSource()).getId().equals("pathSize")
        && nodeValue <= 5
        && nodeValue >= 3) {
      for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
        if (this.getEdgeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
          ((Line) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setStrokeWidth(edgeValue);
        }
      }
    }
  }

  public void mousePress(MouseEvent mouseEvent) {
    super.mouseClick(mouseEvent);
  }
}
