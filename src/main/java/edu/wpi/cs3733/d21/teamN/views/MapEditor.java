package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d21.teamN.map.*;
import edu.wpi.cs3733.d21.teamN.services.algo.*;
import edu.wpi.cs3733.d21.teamN.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapEditor extends MapController implements Initializable {
  @FXML private JFXComboBox<Label> algorithm;
  @FXML private JFXTabPane editor;
  @FXML private JFXTextField nodeSize;
  @FXML private JFXTextField pathSize;
  @Inject FXMLLoader loader;

  @Inject HomeState state;

  @FXML private Label XLabel;
  @FXML private Label YLabel;
  private Node startNodePath;

  private MapNodeEditor mapNodeEditor;
  private MapEdgeEditor mapEdgeEditor;
  private IActionHandling actionHandling;

  @FXML private Group ControlsGroup;
  @FXML private JFXButton buttonUp, buttonDown;

  private Scene appPrimaryScene;
  ArrayList<Node.Link> path = new ArrayList<>();

  @FXML private JFXTextField nodeId, startNode, endNode;
  @FXML private JFXTextField building;
  @FXML private JFXTextField floor;
  @FXML private JFXTextField longName;
  @FXML private JFXTextField shortName;
  @FXML private JFXTextField nodeType;
  @FXML private JFXTextField XCoord;
  @FXML private JFXTextField YCoord;
  @FXML protected AnchorPane mapAnchor;
  // @FXML protected AnchorPane anchorPane;

  @FXML private JFXTextField edgeID;
  //  @FXML private JFXTextField startNode;
  //  @FXML private JFXTextField endNode;

  Boolean cancelOrSubmit = false;
  UserPreferences currentPrefs;
  DiffHandler diffHandler;

  private int numNodes;

  /**
   * This method allows the tests to inject the scene at a later time, since it must be done on the
   * JavaFX thread
   */
  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  @Inject
  public void setLoader(FXMLLoader loader) {
    this.loader = loader;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    mapNodeEditor = new MapNodeEditor(this);
    mapEdgeEditor = new MapEdgeEditor(this);
    diffHandler = new DiffHandler(mapNodeEditor, this);

    actionHandling = new NodeActionHandling(this, this.mapNodeEditor, this.mapEdgeEditor);

    adminMap = new AdminMap(db);
    mapDrawer = new MapDrawer(this);
    mapImageView.setCursor(Cursor.CROSSHAIR);
    mapDrawer.setUpZoom(mapImageView, mapAnchor);
    algorithm
        .getItems()
        .addAll(
            new Label("Astar"),
            new Label("Depth First"),
            new Label("Breadth First"),
            new Label("Dijkstra"));
    algorithm.getSelectionModel().select(0);

    KeyCombination kc = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
    Runnable undo =
        () -> {
          diffHandler.undo();
          mapNodeEditor.clearSelection();
        };
    appPrimaryScene.getAccelerators().put(kc, undo);
    KeyCombination ky = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
    appPrimaryScene.getAccelerators().put(kc, undo);
    Runnable redo =
        () -> {
          diffHandler.redo();
          mapNodeEditor.clearSelection();
        };
    appPrimaryScene.getAccelerators().put(ky, redo);

    super.init(appPrimaryScene);
    this.Load();
    this.numNodes = adminMap.getNodeSet().size() + 1;

    updateStyle(db.getCurrentUser().getAppColor());
  }

  @FXML JFXButton Vert, Horiz, Snap, Reg, L2, L1, G, F1, F2, F3, dN1, sN1, dE1, sE1;
  @FXML Rectangle rectangle1, rectangle2, rectangle3, rectangle4, rectangle5, rectangle6;
  @FXML
  Label l1,
      l2,
      l3,
      l4,
      l5,
      l6,
      l7,
      l8,
      l9,
      l10,
      l11,
      l12,
      l13,
      l14,
      l15,
      l16,
      l17,
      l18,
      l19,
      l20,
      l21,
      l22,
      l23,
      backTab;
  @FXML TextArea tArea;

  public void updateStyle(String color) {

    if (!db.getCurrentUser().getDarkMode()) {
      Color appC = Color.web(color);
      String s = appC.darker().darker().desaturate().toString();
      String style =
          "-fx-background-color: " + "#" + s.substring(2) + "; -fx-background-radius: 15;";

      JFXButton[] lA = {Vert, Horiz, Snap, Reg, L2, L1, G, F1, F2, F3, dN1, sN1, dE1, sE1};
      for (JFXButton a : lA) a.setStyle(style);

      style = "-fx-background-color: " + "#" + color.substring(2) + "; -fx-background-radius: 25;";
    } else {
      String style =
          "-fx-background-color: " + "#" + color.substring(2) + "; -fx-background-radius: 25;";

      JFXButton[] lA = {Vert, Horiz, Snap, Reg, L2, L1, G, F1, F2, F3, dN1, sN1, dE1, sE1};
      for (JFXButton a : lA) a.setStyle(style);

      Color appC = Color.web(color);
      String s = appC.darker().darker().desaturate().toString();
      Color c = Color.web(s);
      style = "-fx-background-color: " + "#" + s.substring(2) + ";-fx-background-radius: 25;";
      backTab.setStyle(style);
      Rectangle[] rA = {rectangle1, rectangle2, rectangle3, rectangle4, rectangle5, rectangle6};
      for (Rectangle a : rA) a.setFill(c);
      Label[] labelA = {
        l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14, l15, l16, l17, l18, l19, l20,
        l21, l22, l23, XLabel, YLabel
      };
      for (Label a : labelA) a.setTextFill(Color.web("WHITE"));

      JFXTextField[] tA = {
        nodeSize, pathSize, shortName, longName, building, floor, YCoord, XCoord, nodeId, edgeID,
        startNode, endNode, nodeType
      };
      for (JFXTextField a : tA) {
        a.setFocusColor(Color.web("WHITE"));
        a.setUnFocusColor(Color.web("WHITE"));
        a.setStyle("-fx-text-inner-color: WHITE;");
      }

      algorithm.setFocusColor(Color.web("WHITE"));
      algorithm.setUnFocusColor(Color.web("WHITE"));
      algorithm.setStyle("-fx-text-fill: WHITE;");
      buttonDown.setTextFill(Color.web("WHITE"));
      buttonUp.setTextFill(Color.web("WHITE"));
      tArea.setStyle(
          "-fx-text-fill: WHITE; -fx-control-inner-background:"
              + "#"
              + s.substring(2)
              + ";-fx-text-box-border: transparent;-fx-focus-color: transparent;");
    }
  }

  /**
   * Prints the x and y values of the cursor on the bottom left of the screen
   *
   * @param mouseDragEvent
   */
  public void xyPrint(MouseEvent mouseDragEvent) {
    XLabel.setText(String.valueOf(mouseDragEvent.getX()));
    YLabel.setText(String.valueOf(mouseDragEvent.getY()));
  }

  /**
   * on mouse click on the map places a node with no id but when clicked it returns the node FX:ID
   *
   * @param mouseEvent
   */
  public void placeNodeClick(MouseEvent mouseEvent) throws IOException {
    setCancelOrSubmit(false);
  }

  public void setCancelOrSubmit(Boolean sm) {
    cancelOrSubmit = sm;
  }

  public void mouseClick(MouseEvent mouseEvent) {
    super.mouseClick(mouseEvent);
    if (mouseEvent.isControlDown()) {
      mapDrawer.drawBoundingBox(mouseEvent.getX(), mouseEvent.getY());
    }
    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
      startNodePath = adminMap.get(mouseEvent.getX(), mouseEvent.getY(), mapDrawer.getCurrentMap());
      mapDrawer.startLine(startNodePath);
    }
  }

  public void releaseMouse(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
      Rectangle r = mapDrawer.endBoundingBox(mouseEvent.getX(), mouseEvent.getY());
      if (r != null) {
        if (!mouseEvent.isShiftDown()) mapNodeEditor.clearSelection();
        for (Node n : this.getNodeSet().values()) {
          if (n.get_floor().equals(mapDrawer.getCurrentMap())
              && n.get_x() * getDownScale() > r.getX()
              && n.get_x() * getDownScale() < r.getX() + r.getWidth()
              && n.get_y() * getDownScale() > r.getY()
              && n.get_y() * getDownScale() < r.getY() + r.getHeight()) {
            mapNodeEditor.addNode(n);
          }
        }
      } else if (!mouseEvent.isShiftDown()) {
        mapNodeEditor.clearSelection();
        while (getNodeSet().containsKey("node_" + numNodes++)) {}
        placeNode("node_" + --numNodes, mouseEvent.getX(), mouseEvent.getY());
      }
    }
    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
      mapNodeEditor.clearSelection();
      Node other = adminMap.get(mouseEvent.getX(), mouseEvent.getY(), mapDrawer.getCurrentMap());
      if (other != startNodePath) {
        mapDrawer.cancelLine();
        String id = startNodePath.get_nodeID() + "_" + other.get_nodeID();
        placeLink(id, startNodePath, other);
        diffHandler.makeEdge(id, startNodePath, other);
      } else {
        mapDrawer.cancelLine();
      }
    }
  }

  @FXML
  private void dragMouse(MouseEvent mouseEvent) {
    this.mapDrawer.captureMouseDrag(mouseEvent);
  }

  private void placeNode(String id, double x, double y) {
    Node closest = adminMap.get(x, y, mapDrawer.getCurrentMap());
    Node n =
        new Node(
            x * getUpScale(),
            y * getUpScale(),
            id,
            mapDrawer.getCurrentMap(),
            closest.get_building(),
            closest.get_nodeType(),
            closest.get_longName(),
            closest.get_shortName());
    placeNode(n);
    diffHandler.create(new ArrayList<>(Collections.singleton(n)));
  }

  public void removeNode(Node n) {
    for (int i = 0; i < mapAnchor.getChildren().size(); i++) {
      for (Node.Link l : n.get_neighbors()) {
        if (mapAnchor.getChildren().get(i).getId().equals(l._id)) {
          mapAnchor.getChildren().remove(i);
          i--;
        }
      }
      if ((mapAnchor.getChildren().get(i)).getId().equals(n.get_nodeID())) {
        mapAnchor.getChildren().remove(i);
        break;
      }
    }
    adminMap.deleteNode(n.get_nodeID());
    for (Node.Link l : n.get_neighbors()) {
      l._other.removeNeightbor(n);
      adminMap.deleteEdge(l._id);
    }
  }

  public Group placeNode(Node n) {
    Group root = super.placeNode(n);
    actionHandling.setNodeInfo(root);
    actionHandling.setNodeStartLink(root);
    actionHandling.setNodeEndLink(root);
    root.setOnScroll(event -> mapDrawer.captureMouseScroll(event));
    //    actionHandling.setNodeDrag((Circle) n.get_shape());
    root.setOnMouseDragged(
        event -> {
          mapDrawer.captureMouseDrag(event);
          if (event.isPrimaryButtonDown())
            mapNodeEditor.handleDrag(event, getNodeSet().get(root.getId()));
        });
    root.setOnMouseReleased(
        event -> {
          if (mapDrawer.isDragging()) releaseMouse(event);
          else mapNodeEditor.finalize_change(diffHandler);
        });
    if (!adminMap.getNodeSet().containsKey(n.get_nodeID())) {
      adminMap.addNode(n);
      mapNodeEditor.showNodeProperties(root);
    }
    root.setOnMousePressed(
        event -> {
          if (event.isSecondaryButtonDown()) mouseClick(event);
        });
    return root;
  }

  public void deleteCurrent(ActionEvent actionEvent) throws IOException, InterruptedException {
    mapNodeEditor.deleteSelection(diffHandler);
  }

  public void setNodeId(String nodeId) {
    this.nodeId.setText(nodeId);
  }

  public void setBuilding(String building) {
    this.building.setText(building);
  }

  public void setFloor(String floor) {
    this.floor.setText(floor);
  }

  public void setLongName(String longName) {
    this.longName.setText(longName);
  }

  public void setShortName(String shortName) {
    this.shortName.setText(shortName);
  }

  public void setNodeType(String nodeType) {
    this.nodeType.setText(nodeType);
  }

  public void setXCoord(String XCoord) {
    this.XCoord.setText(XCoord);
  }

  public void setYCoord(String YCoord) {
    this.YCoord.setText(YCoord);
  }

  public JFXTextField getNodeId() {
    return nodeId;
  }

  public JFXTextField getBuilding() {
    return building;
  }

  public JFXTextField getFloor() {
    return floor;
  }

  public JFXTextField getLongName() {
    return longName;
  }

  public JFXTextField getShortName() {
    return shortName;
  }

  public JFXTextField getNodeType() {
    return nodeType;
  }

  public JFXTextField getXCoord() {
    return XCoord;
  }

  public JFXTextField getYCoord() {
    return YCoord;
  }

  public void saveNode(ActionEvent actionEvent) {
    mapNodeEditor.commitChanges(getCurrent());
  }

  @Override
  public void correctFloor(Node.Link link) {
    ;
  }

  public Group placeLink(String id, Node node1, Node node2) {
    Group root = super.placeLink(id, node1, node2);
    actionHandling.setEdgeInfo(root);
    root.setOnMouseReleased(this::releaseMouse);
    if (!super.getAdminMap().getEdgeSet().containsKey(id)) {
      Edge edge = new Edge(id, node1.get_nodeID(), node2.get_nodeID());
      adminMap.addEdge(edge);
      mapEdgeEditor.showEdgeProperties(root);
    }
    return root;
  }

  public void deleteLink(String id, Node node1, Node node2) {
    Node.Link link = null;
    for (Node.Link l : node1.get_neighbors()) {
      if (l._id.equals(id)) {
        link = l;
      }
    }
    if (link != null) node1.get_neighbors().remove(link);

    link = null;
    for (Node.Link l : node2.get_neighbors()) {
      if (l._id.equals(id)) {
        link = l;
      }
    }
    if (link != null) {
      node2.get_neighbors().remove(link);
      mapAnchor.getChildren().remove(link._shape);
    }
    //    link._shape.setVisible(false);
    System.out.println(id);
    adminMap.deleteEdge(id);
    for (int i = 0; i < mapAnchor.getChildren().size(); i++) {
      System.out.println(mapAnchor.getChildren().get(i).getId());
      if (mapAnchor.getChildren().get(i).getId().equals(id)) {
        mapAnchor.getChildren().remove(i);
        break;
      }
    }
  }

  protected void Load() {
    super.Load();
  }

  public void alignH(ActionEvent actionEvent) {
    mapNodeEditor.straightenSelectionHoriz();
    mapNodeEditor.finalize_change(diffHandler);
  }

  public void alignV(ActionEvent actionEvent) {
    mapNodeEditor.straightenSelectionVert();
    mapNodeEditor.finalize_change(diffHandler);
  }

  public void alignR(ActionEvent actionEvent) {
    mapNodeEditor.straightenSelection();
    mapNodeEditor.finalize_change(diffHandler);
  }

  public void alignS(ActionEvent actionEvent) {
    mapNodeEditor.straightenSelectionSnap();
    mapNodeEditor.finalize_change(diffHandler);
  }

  public JFXTabPane getEditor() {
    return editor;
  }

  public void updateAlgorithm(ActionEvent actionEvent) {
    switch (algorithm.getSelectionModel().getSelectedItem().getText()) {
      case "Astar":
        PathFinder.setImpl(new Astar());
        return;
      case "Depth First":
        PathFinder.setImpl(new Dfs());
        return;
      case "Breadth First":
        PathFinder.setImpl(new Bfs());
        return;
      case "Dijkstra":
        PathFinder.setImpl(new Dijkstra());
        return;
    }
  }

  @FXML
  public void groupUp() {
    // accountSettingsGroup.setTranslateX(100);
    TranslateTransition tt = new TranslateTransition();
    tt.setNode(ControlsGroup);
    tt.setDuration(new Duration(1000));
    // tt.setFromX(0);
    tt.setToY(-300);
    tt.setAutoReverse(true);
    buttonDown.setVisible(true);
    buttonUp.setVisible(false);
    tt.play();
  }

  @FXML
  public void groupDown() {
    TranslateTransition tt = new TranslateTransition();
    tt.setNode(ControlsGroup);
    tt.setDuration(new Duration(1000));
    // tt.setFromX(100);
    tt.setToY(0);
    tt.setAutoReverse(true);
    buttonDown.setVisible(false);
    buttonUp.setVisible(true);
    tt.play();
  }
}
