package edu.wpi.cs3733.d21.teamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d21.teamN.map.AdminMap;
import edu.wpi.cs3733.d21.teamN.map.MapDrawer;
import edu.wpi.cs3733.d21.teamN.services.algo.Edge;
import edu.wpi.cs3733.d21.teamN.services.algo.Node;
import edu.wpi.cs3733.d21.teamN.services.algo.PathFinder;
import edu.wpi.cs3733.d21.teamN.services.database.DatabaseService;
import edu.wpi.cs3733.d21.teamN.services.database.users.UserPrefs;
import edu.wpi.cs3733.d21.teamN.state.HomeState;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public abstract class MapController extends MasterController {

  public ImageView getMapImageView() {
    return this.mapImageView;
  }

  public AnchorPane getMapAnchor() {
    return this.mapAnchor;
  }

  @Inject DatabaseService db;

  private final double downScale = .25;
  private final double upScale = 4;

  private PathFinder pathFinder = PathFinder.getPathFinder();

  //  public MapController(DatabaseService db) {
  //    this.db = db;
  //    nodeSet = db.getAllNodesMap();
  //    edgeSet = db.getAllEdgesMap();
  //  }

  public HashMap<String, Node> getNodeSet() {
    return this.adminMap.getNodeSet();
  }

  public HashMap<String, Edge> getEdgeSet() {
    return this.adminMap.getEdgeSet();
  }

  public void addNode(Node node) {
    this.adminMap.addNode(node);
  }

  public void addEdge(Edge edge) {
    this.adminMap.addEdge(edge);
  }

  public void deleteNode(String id) {
    adminMap.deleteNode(id);
  }

  public void deleteEdge(String id) {
    adminMap.deleteEdge(id);
  }

  public Node get(double x, double y, String floor) {
    x *= upScale;
    y *= upScale;
    double min = Double.MAX_VALUE;
    Node closest = null;
    for (Node c : getNodeSet().values()) {
      if (c.get_floor().equals(floor)) {
        double distance = (c.get_x() - x) * (c.get_x() - x) + (c.get_y() - y) * (c.get_y() - y);
        if (distance < min) {
          closest = c;
          min = distance;
        }
      }
    }
    return closest;
  }

  public ArrayList<Node.Link> pathfind(Node node1, Node node2) {
    return pathFinder.pathfindNoStairs(node1, node2);
  }

  //  public void SetStartNode(String snp) { if (getNodeSet().containsKey(snp)) startNodePath = snp;
  //  }
  //
  //  public void SetEndNode(String enp) {
  //    if (getNodeSet().containsKey(enp)) endNodePath = enp;
  //  }

  @Inject FXMLLoader loader;
  @Inject HomeState state;

  protected MapDrawer mapDrawer;

  @FXML protected JFXColorPicker nodeColor;
  @FXML protected JFXColorPicker EXIT;
  @FXML protected JFXColorPicker ELEV;
  @FXML protected JFXColorPicker STAI;
  @FXML protected JFXColorPicker pathColor;
  @FXML protected JFXColorPicker selectedNodeColor;
  UserPrefs userPrefs;
  @FXML protected JFXTextField nodeSize;
  @FXML protected JFXTextField pathSize;
  @FXML protected AnchorPane anchorPane;
  @FXML private AnchorPane nodePane;

  //  @FXML private Label XLabel;
  //  @FXML private Label YLabel;
  @FXML protected ImageView mapImageView;
  @FXML protected AnchorPane mapAnchor;

  private Group current;

  protected AdminMap adminMap;
  //  private MapNodeEditor mapNodeEditor;
  //  private MapEdgeEditor mapEdgeEditor;

  @FXML private JFXTextField nodeId;
  //  @FXML private JFXTextField building;
  //  @FXML private JFXTextField floor;
  //  @FXML private JFXTextField longName;
  //  @FXML private JFXTextField shortName;
  //  @FXML private JFXTextField nodeType;
  //  @FXML private JFXTextField XCoord;
  //  @FXML private JFXTextField YCoord;

  @FXML private JFXTextField edgeID;
  @FXML private JFXTextField startNode;
  @FXML private JFXTextField endNode;

  @FXML private Circle nodeColor_L;
  @FXML private Circle EXIT_L;
  @FXML private Circle ELEV_L;
  @FXML private Circle STAI_L;
  @FXML private Circle pathColor_L;
  @FXML private Circle selectedNodeColor_L;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void init(Scene appPrimaryScene) {

    userPrefs = db.getCurrentUser().getUserPrefs();

    nodeColor.setValue(Color.web(userPrefs.getBasicNodeColor()));
    EXIT.setValue(Color.web(userPrefs.getExitColor()));
    ELEV.setValue(Color.web(userPrefs.getElevatorColor()));
    STAI.setValue(Color.web(userPrefs.getStairColor()));
    pathColor.setValue(Color.web(userPrefs.getPathfindingColor()));
    selectedNodeColor.setValue(Color.web(userPrefs.getHighlightColor()));
    nodeSize.setText(String.valueOf(userPrefs.getNodeSize()));
    pathSize.setText(String.valueOf(userPrefs.getEdgeWidth()));
    mapImageView.setOnScroll(event -> mapDrawer.captureMouseScroll(event));
    mapImageView.setOnMouseDragged(event -> mapDrawer.captureMouseDrag(event));
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void mouseClick(MouseEvent event) {
    this.mapDrawer.captureClick(event);
  }

  protected void Load() {
    //    for (javafx.scene.Node root :
    //        mapAnchor.getChildren().subList(1, mapAnchor.getChildren().size())) {
    //      root.setVisible(false);
    //    }
    for (Node n : getNodeSet().values()) {
      n.set_neighbors(new ArrayList<>());
    }
    adminMap
        .getEdgeSet()
        .forEach(
            (key, value) -> {
              placeLink(
                      key,
                      getNodeSet().get(value.getStartNode()),
                      getNodeSet().get(value.getEndNode()))
                  .getChildren()
                  .get(0)
                  .setVisible(
                      mapDrawer
                          .getCurrentMap()
                          .equals(getNodeSet().get(value.getStartNode()).get_floor()));
            });
    getAdminMap()
        .getNodeSet()
        .forEach(
            (key, value) -> {
              placeNode(value);
              value.get_shape().setVisible(mapDrawer.getCurrentMap().equals(value.get_floor()));
            });
  }

  public void releaseMouse(MouseEvent mouseEvent) {}

  protected Group placeNode(Node n) {
    Group root =
        mapDrawer.drawNode(
            n.get_nodeID(),
            n.get_x() * getDownScale(),
            n.get_y() * getDownScale(),
            nodeColor.getValue());
    mapAnchor.getChildren().add(root);
    n.set_shape((Circle) root.getChildren().get(0));
    root.setCursor(Cursor.CROSSHAIR);
    return root;
  }

  public Group createVisualLink(String id, Node node1, Node node2) {
    Group root = mapDrawer.drawLine(id, node1, node2);
    mapAnchor.getChildren().add(1, root);
    root.setCursor(Cursor.CROSSHAIR);
    root.setOnMouseDragged(event -> mapDrawer.captureMouseDrag(event));
    root.setOnScroll(event -> mapDrawer.captureMouseScroll(event));
    return root;
  }

  /**
   * Places a line between two nodes
   *
   * @param node1 node id of first node
   * @param node2 node id of the second node
   */
  public Group placeLink(String id, Node node1, Node node2) {
    Group root = createVisualLink(id, node1, node2);
    getAdminMap().makeEdge(id, node1, node2, (Line) root.getChildren().get(0));
    return root;
  }

  public void mapFloor() {
    for (Node n : getNodeSet().values()) {
      n.get_shape().setVisible(n.get_floor().equals(mapDrawer.getCurrentMap()));
      for (Node.Link l : n.get_neighbors()) {
        l._shape.setVisible(n.get_floor().equals(mapDrawer.getCurrentMap()));
      }
    }
  }

  public void correctFloor(Node.Link link) {
    link._shape.setVisible(link._this.get_floor().equals(mapDrawer.getCurrentMap()));
  }

  public void SetStartNode(ActionEvent actionEvent) {
    adminMap.SetStartNode(nodeId.getText());
  }

  public void SetEndNode(ActionEvent actionEvent) {
    adminMap.SetEndNode(nodeId.getText());
  }

  @FXML
  public void logOut() throws IOException {
    super.logOut(loader, appPrimaryScene);
  }

  @FXML
  private void exit(ActionEvent actionEvent) throws IOException {
    super.cancel(actionEvent);
  }

  public void advanceViews(ActionEvent actionEvent) throws IOException {
    String file = ((Button) actionEvent.getSource()).getId() + ".fxml";
    Parent root = loader.load(getClass().getResourceAsStream(file));
    appPrimaryScene.setRoot(root);
  }

  public void newColorNode(ActionEvent actionEvent) {
    JFXColorPicker a = ((JFXColorPicker) actionEvent.getSource());
    updateUserColors(a.getId(), a.getValue().toString());
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (adminMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        if (adminMap
            .getNodeSet()
            .get(mapAnchor.getChildren().get(i).getId())
            .get_nodeType()
            .equals(a.getId())) {
          ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setFill(a.getValue());
        }
      }
    }
  }

  void updateUserColors(String id, String value) {
    setUserPref(id, value);
    db.updateUserPrefs(db.getCurrentUser().getId(), userPrefs);
  }

  public void newColorNode(JFXColorPicker a) {
    updateUserColors(a.getId(), a.getValue().toString());
    for (Node n : getNodeSet().values()) {
      if (n.get_nodeType().equals(a.getId())) n.get_shape().setFill(a.getValue());
    }
  }

  private void setUserPref(String id, String value) {
    if (id.contains("ELEV")) {
      userPrefs.setElevatorColor(value);
    } else if (id.contains("STAI")) {
      userPrefs.setStairColor(value);
    } else if (id.contains("EXIT")) {
      userPrefs.setExitColor(value);
    } else if (id.contains("selectedNodeColor")) {
      userPrefs.setHighlightColor(value);
    } else if (id.contains("pathColor")) {
      userPrefs.setPathfindingColor(value);
    } else {
      userPrefs.setBasicNodeColor(value);
    }
  }

  public void newColorNodeaf(ActionEvent actionEvent) {
    JFXColorPicker a = ((JFXColorPicker) actionEvent.getSource());
    userPrefs.setBasicNodeColor(a.getValue().toString());
    db.updateUserPrefs(db.getCurrentUser().getId(), userPrefs);
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (adminMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        if (!adminMap
                .getNodeSet()
                .get(mapAnchor.getChildren().get(i).getId())
                .get_nodeType()
                .contains("ELEV")
            && !adminMap
                .getNodeSet()
                .get(mapAnchor.getChildren().get(i).getId())
                .get_nodeType()
                .contains("EXIT")
            && !adminMap
                .getNodeSet()
                .get(mapAnchor.getChildren().get(i).getId())
                .get_nodeType()
                .contains("STAI")) {
          ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setFill(a.getValue());
        }
      }
    }
  }

  public UserPrefs getUserPrefs() {
    return userPrefs;
  }

  public void newColorPath(ActionEvent actionEvent) {
    JFXColorPicker a = ((JFXColorPicker) actionEvent.getSource());
    userPrefs.setPathfindingColor(a.getValue().toString());
    db.updateUserPrefs(db.getCurrentUser().getId(), userPrefs);
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (adminMap.getEdgeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
            .setFill(pathColor.getValue());
      }
    }
  }

  public void newSize(ActionEvent actionEvent) {
    double nodeValue = Double.parseDouble(nodeSize.getText());
    double edgeValue = Double.parseDouble(pathSize.getText());
    if (((JFXTextField) actionEvent.getSource()).getId().equals("nodeSize")
        && nodeValue <= 5
        && nodeValue >= 3) {
      userPrefs.setNodeSize(nodeValue);
      for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
        if (this.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
          ((Circle) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setRadius(nodeValue);
        }
      }
    } else if (((JFXTextField) actionEvent.getSource()).getId().equals("pathSize")
        && nodeValue <= 5
        && nodeValue >= 3) {
      userPrefs.setEdgeWidth(edgeValue);
      for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
        if (this.getEdgeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
          ((Line) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setStrokeWidth(edgeValue);
        }
      }
    }
    db.updateUserPrefs(db.getCurrentUser().getId(), userPrefs);
  }

  public double getDownScale() {
    return downScale;
  }

  public double getUpScale() {
    return upScale;
  }

  public AdminMap getAdminMap() {
    return adminMap;
  }

  //  public void saveNode(ActionEvent actionEvent) {
  //    mapNodeEditor.commitChanges(current);
  //  }

  public void setCurrent(Group current) {
    this.current = current;
  }

  public Group getCurrent() {
    return current;
  }

  //  public void saveEdge(ActionEvent actionEvent) {
  //    mapEdgeEditor.saveEdge(current);
  //  }

  public JFXTextField getEdgeID() {
    return edgeID;
  }

  public JFXTextField getStartNode() {
    return startNode;
  }

  public JFXTextField getEndNode() {
    return endNode;
  }

  public void setEdgeID(String s) {
    edgeID.setText(s);
  }

  public void setStartNode(String s) {
    startNode.setText(s);
  }

  public void setEndNode(String s) {
    endNode.setText(s);
  }

  public void setMapImageView(ImageView mapImageView) {
    this.mapImageView = mapImageView;
  }

  public JFXColorPicker getNodeColor() {
    return nodeColor;
  }

  public void setNodeColor(JFXColorPicker nodeColor) {
    this.nodeColor = nodeColor;
  }

  public JFXColorPicker getPathColor() {
    return pathColor;
  }

  public void setPathColor(JFXColorPicker pathColor) {
    this.pathColor = pathColor;
  }

  public JFXColorPicker getSelectedNodeColor() {
    return selectedNodeColor;
  }

  public void setSelectedNodeColor(JFXColorPicker selectedNodeColor) {
    this.selectedNodeColor = selectedNodeColor;
  }

  public void setNodeSize(JFXTextField nodeSize) {
    this.nodeSize = nodeSize;
  }

  public void setPathSize(JFXTextField pathSize) {
    this.pathSize = pathSize;
  }

  public void setMap(ActionEvent actionEvent) {
    mapDrawer.setMap(((Button) actionEvent.getSource()).getId());
    this.mapFloor();
  }

  public double getNodeSize() {
    return Double.parseDouble(nodeSize.getText());
  }

  public double getPathSize() {
    return Double.parseDouble(pathSize.getText());
  }

  public MapDrawer getMapDrawer() {
    return mapDrawer;
  }
}
