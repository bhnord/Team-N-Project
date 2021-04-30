package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.map.*;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapEditor extends MapController implements Initializable {
  //  @FXML private JFXColorPicker nodeColor;
  //  @FXML private JFXColorPicker EXIT;
  //  @FXML private JFXColorPicker ELEV;
  //  @FXML private JFXColorPicker STAI;
  //  @FXML private JFXColorPicker pathColor;
  //  @FXML private JFXColorPicker selectedNodeColor;
  @FXML private JFXTextField nodeSize;
  @FXML private JFXTextField pathSize;
  @Inject FXMLLoader loader;
  @Inject HomeState state;

  @FXML private Label XLabel;
  @FXML private Label YLabel;
  //  @FXML private ImageView mapImageView;
  //  @FXML private AnchorPane mapAnchor;

  private Group current;
  private Node startNodePath;

  //  public final double downScale = 0.25;
  //  public final double upScale = 4;
  //  private int nodeCount = 0;

  //  private AdminMap adminMap;
  //  private MapDrawer mapDrawer;
  // needed
  private MapNodeEditor mapNodeEditor;
  private MapEdgeEditor mapEdgeEditor;
  private IActionHandling actionHandling;

  private Scene appPrimaryScene;
  ArrayList<Node.Link> path = new ArrayList<>();

  @FXML private JFXTextField nodeId;
  @FXML private JFXTextField building;
  @FXML private JFXTextField floor;
  @FXML private JFXTextField longName;
  @FXML private JFXTextField shortName;
  @FXML private JFXTextField nodeType;
  @FXML private JFXTextField XCoord;
  @FXML private JFXTextField YCoord;

  @FXML private JFXTextField edgeID;
  //  @FXML private JFXTextField startNode;
  //  @FXML private JFXTextField endNode;

  Boolean cancelOrSubmit = false;
  UserPreferences currentPrefs;

  //  /**
  //   * This method allows the tests to inject the scene at a later time, since it must be done on
  // the
  //   * JavaFX thread
  //   *
  //   * @param appPrimaryScene Primary scene of the app whose root will be changed
  //   */
  //  @Inject
  //  public void setAppPrimaryScene(Scene appPrimaryScene) {
  //    this.appPrimaryScene = appPrimaryScene;
  //  }

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    mapNodeEditor = new MapNodeEditor(this);
    mapEdgeEditor = new MapEdgeEditor(this);

    actionHandling = new NodeActionHandling(this, this.mapNodeEditor, this.mapEdgeEditor);
    nodeColor.setValue(Color.BLUE);
    EXIT.setValue(Color.RED);
    ELEV.setValue(Color.PINK);
    STAI.setValue(Color.ORANGE);
    pathColor.setValue(Color.BLACK);
    selectedNodeColor.setValue(Color.GREEN);
    nodeSize.setText("3.5");
    pathSize.setText("2.5");

    adminMap = new AdminMap(db);
    mapDrawer = new MapDrawer(this);
    mapImageView.setCursor(Cursor.CROSSHAIR);
    this.Load();
    mapDrawer.setUpZoom(mapImageView, mapAnchor);
    super.init();
  }
  //
  //  @FXML
  //  public void advanceHome() throws IOException {
  //    super.advanceHome(loader, appPrimaryScene);
  //  }

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
    if (mouseEvent.getButton() == MouseButton.PRIMARY) {
      placeNode(
          "node_" + adminMap.getNodeSet().values().size(), mouseEvent.getX(), mouseEvent.getY());
    }

    setCancelOrSubmit(false);
  }

  public void setCancelOrSubmit(Boolean sm) {
    cancelOrSubmit = sm;
  }

  public void mouseClick(MouseEvent mouseEvent) {
    super.mouseClick(mouseEvent);
    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
      //      System.out.println("start: " + mouseEvent.getX() + ", " + mouseEvent.getY());
      startNodePath = adminMap.get(mouseEvent.getX(), mouseEvent.getY(), mapDrawer.getCurrentMap());
      mapDrawer.startLine(startNodePath);
    }
  }

  //  public AnchorPane getMapAnchor() {
  //    return mapAnchor;
  //  }

  public void releaseMouse(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
      System.out.println("end: " + mouseEvent.getX() + ", " + mouseEvent.getY());
      Node other = adminMap.get(mouseEvent.getX(), mouseEvent.getY(), mapDrawer.getCurrentMap());
      if (other != startNodePath) {
        //        placeLink(startNodePath.get_nodeID() + "_" + other.get_nodeID(), startNodePath,
        // other);
        mapDrawer.endLine(other);
      }
    }
  }

  @FXML
  private void dragMouse(MouseEvent mouseEvent) {
    this.mapDrawer.captureMouseDrag(mouseEvent);
  }

  private void placeNode(String id, double x, double y) {
    Node n =
        new Node(
            x * getUpScale(),
            y * getUpScale(),
            id,
            mapDrawer.getCurrentMap(),
            getBuilding().getText(),
            "",
            getLongName().getText(),
            getShortName().getText());
    placeNode(n);
  }

  public Group placeNode(Node n) {
    Group root = super.placeNode(n);
    actionHandling.setNodeInfo(root);
    actionHandling.setNodeStartLink(root);
    actionHandling.setNodeEndLink(root);
    actionHandling.setNodeDrag((Circle) n.get_shape());
    if (!adminMap.getNodeSet().containsKey(n.get_nodeID())) {
      adminMap.addNode(n);
      mapNodeEditor.showNodeProperties(root);
    }
    return root;
  }

  //  /**
  //   * Places a line between two nodes
  //   *
  //   * @param node1 node id of first node
  //   * @param node2 node id of the second node
  //   */
  //  private void placeLink(String id, Node node1, Node node2) {
  //    Group root = mapDrawer.drawLine(id, node1, node2);
  //    adminMap.makeEdge(id, node1, node2, (Line) root.getChildren().get(0));
  //    actionHandling.setEdgeInfo(root);
  //    mapAnchor.getChildren().add(root);
  //    root.setCursor(Cursor.CROSSHAIR);
  //    if (!adminMap.getEdgeSet().containsKey(id)) {
  //      adminMap.addEdge(new Edge(id, node1.get_nodeID(), node2.get_nodeID()));
  //      mapEdgeEditor.showEdgeProperties(root);
  //    }
  //  }
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

  private void DeleteNodesFromMap() throws IOException {
    int i = 1;
    for (javafx.scene.Node root :
        mapAnchor.getChildren().subList(i, mapAnchor.getChildren().size())) {
      if (root.getId().equals(current.getId())) {
        mapAnchor.getChildren().remove(i);
        return;
      } else {
        i++;
      }
    }
  }

  public void DeleteObjectDataBase() throws IOException {
    String id = current.getId();
    if (adminMap.getNodeSet().containsKey(id)) {
      adminMap.deleteNode(id);
    } else if (adminMap.getEdgeSet().containsKey(id)) {
      adminMap.deleteEdge(id);
    } else {
      System.out.println("Object does not exist");
    }
  }

  //  public void mapFloor() {
  //    for (int i = 1; mapAnchor.getChildren().size() > i; i++) {
  //      if (adminMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
  //        mapAnchor
  //            .getChildren()
  //            .get(i)
  //            .setVisible(
  //                adminMap
  //                    .getNodeSet()
  //                    .get(mapAnchor.getChildren().get(i).getId())
  //                    .get_floor()
  //                    .equals(mapDrawer.getCurrentMap()));
  //      }
  //    }
  //  }

  //  private JFXColorPicker getType(String id) {
  //    Node node = adminMap.getNodeSet().get(id);
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

  // Loading from the database
  //  public void Load(ActionEvent actionEvent) {
  //    for (javafx.scene.Node root :
  //        mapAnchor.getChildren().subList(1, mapAnchor.getChildren().size())) {
  //      root.setVisible(false);
  //    }
  //    adminMap
  //        .getEdgeSet()
  //        .forEach(
  //            (key, value) -> {
  //              if (adminMap
  //                  .getNodeSet()
  //                  .get(value.getStartNode())
  //                  .get_floor()
  //                  .equals(mapDrawer.getCurrentMap())) {
  //                placeLink(
  //                    key,
  //                    adminMap.getNodeSet().get(value.getStartNode()),
  //                    adminMap.getNodeSet().get(value.getEndNode()));
  //              }
  //            });
  //    adminMap
  //        .getNodeSet()
  //        .forEach(
  //            (key, value) -> {
  //              if (value.get_floor().equals(mapDrawer.getCurrentMap())) {
  //                placeNode(key, value.get_x() * downScale, value.get_y() * downScale);
  //              }
  //            });
  //  }

  public void deleteCurrent(ActionEvent actionEvent) throws IOException, InterruptedException {
    DeleteNodesFromMap();
    DeleteObjectDataBase();
  }

  //  public void SetStartNode(ActionEvent actionEvent) {
  //    adminMap.SetStartNode(nodeId.getText());
  //  }
  //
  //  public void SetEndNode(ActionEvent actionEvent) {
  //    adminMap.SetEndNode(nodeId.getText());
  //  }
  //
  //  @FXML
  //  public void logOut() throws IOException {
  //    super.logOut(loader, appPrimaryScene);
  //  }
  //
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
  //  public void newColorNode(ActionEvent actionEvent) {
  //    JFXColorPicker a = ((JFXColorPicker) actionEvent.getSource());
  //    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
  //      if (adminMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
  //        if (adminMap
  //            .getNodeSet()
  //            .get(mapAnchor.getChildren().get(i).getId())
  //            .get_nodeType()
  //            .equals(a.getId()))
  //          ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
  //              .setFill(a.getValue());
  //      }
  //    }
  //  }

  //  public void newColorNodeaf(ActionEvent actionEvent) {
  //    JFXColorPicker a = ((JFXColorPicker) actionEvent.getSource());
  //    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
  //      if (adminMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
  //        if (!adminMap
  //                .getNodeSet()
  //                .get(mapAnchor.getChildren().get(i).getId())
  //                .get_nodeType()
  //                .contains("ELEV")
  //            && !adminMap
  //                .getNodeSet()
  //                .get(mapAnchor.getChildren().get(i).getId())
  //                .get_nodeType()
  //                .contains("EXIT")
  //            && !adminMap
  //                .getNodeSet()
  //                .get(mapAnchor.getChildren().get(i).getId())
  //                .get_nodeType()
  //                .contains("STAI"))
  //          ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
  //              .setFill(a.getValue());
  //      }
  //    }
  //  }
  //
  //  public void newColorPath(ActionEvent actionEvent) {
  //    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
  //      if (adminMap.getEdgeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
  //        ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
  //            .setFill(pathColor.getValue());
  //      }
  //    }
  // }

  //  public AdminMap getAdminMap() {
  //    return adminMap;
  //  }

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
    mapNodeEditor.commitChanges(current);
  }

  //  public void setCurrent(Group current) {
  //    this.current = current;
  //  }
  //
  //  public Group getCurrent() {
  //    return current;
  //  }
  //
  //  public void saveEdge(ActionEvent actionEvent) {
  //    mapEdgeEditor.saveEdge(current);
  //  }
  //
  //  public JFXTextField getEdgeID() {
  //    return edgeID;
  //  }
  //
  //  public JFXTextField getStartNode() {
  //    return startNode;
  //  }
  //
  //  public JFXTextField getEndNode() {
  //    return endNode;
  //  }
  //
  //  public void setEdgeID(String s) {
  //    edgeID.setText(s);
  //  }
  //
  //  public void setStartNode(String s) {
  //    startNode.setText(s);
  //  }
  //
  //  public void setEndNode(String s) {
  //    endNode.setText(s);
  //  }

  //  public ImageView getMapImageView() {
  //    return mapImageView;
  //  }
  //
  //  public void setMapImageView(ImageView mapImageView) {
  //    this.mapImageView = mapImageView;
  //  }
  //
  //  public JFXColorPicker getNodeColor() {
  //    return nodeColor;
  //  }
  //
  //  public void setNodeColor(JFXColorPicker nodeColor) {
  //    this.nodeColor = nodeColor;
  //  }
  //
  //  public JFXColorPicker getPathColor() {
  //    return pathColor;
  //  }
  //
  //  public void setPathColor(JFXColorPicker pathColor) {
  //    this.pathColor = pathColor;
  //  }
  //
  //  public JFXColorPicker getSelectedNodeColor() {
  //    return selectedNodeColor;
  //  }
  //
  //  public void setSelectedNodeColor(JFXColorPicker selectedNodeColor) {
  //    this.selectedNodeColor = selectedNodeColor;
  //  }
  //
  //  public void setNodeSize(JFXTextField nodeSize) {
  //    this.nodeSize = nodeSize;
  //  }
  //
  //  public void setPathSize(JFXTextField pathSize) {
  //    this.pathSize = pathSize;
  //  }
  //
  //  public void setMap(ActionEvent actionEvent) {
  //    mapDrawer.setMap(((Button) actionEvent.getSource()).getId());
  //    this.Load();
  //  }
  //
  //  @Override
  //  public double getNodeSize() {
  //    return Double.parseDouble(nodeSize.getText());
  //  }
  //
  //  @Override
  //  public double getPathSize() {
  //    return Double.parseDouble(pathSize.getText());
  //  }

  @Override
  public void correctFloor(Node.Link link) {
    ;
  }

  protected Group placeLink(String id, Node node1, Node node2) {
    Group root = super.placeLink(id, node1, node2);
    actionHandling.setEdgeInfo(root);
    if (!super.getAdminMap().getEdgeSet().containsKey(id)) {
      mapEdgeEditor.showEdgeProperties(root);
    }
    return root;
  }
}
