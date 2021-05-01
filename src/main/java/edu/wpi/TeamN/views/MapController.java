package edu.wpi.TeamN.views;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamN.map.*;
import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.services.algo.PathFinder;
import edu.wpi.TeamN.services.database.DatabaseService;
import edu.wpi.TeamN.state.HomeState;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

  private PathFinder pathFinder = new PathFinder();

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
  @FXML protected JFXTextField nodeSize;
  @FXML protected JFXTextField pathSize;
  @FXML protected AnchorPane anchorPane;

  //  @FXML private Label XLabel;
  //  @FXML private Label YLabel;
  @FXML protected ImageView mapImageView;
  @FXML protected AnchorPane mapAnchor;

  private Group current;

  protected AdminMap adminMap;
  //  private MapNodeEditor mapNodeEditor;
  //  private MapEdgeEditor mapEdgeEditor;

  private Scene appPrimaryScene;

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

  //  Boolean cancelOrSubmit = false;

  @Inject
  public void setAppPrimaryScene(Scene appPrimaryScene) {
    this.appPrimaryScene = appPrimaryScene;
  }

  public void init() {
    super.sideBarSetup(anchorPane, appPrimaryScene, loader, "Map");
    //    log.debug(state.toString());
  }

  @FXML
  public void advanceHome() throws IOException {
    super.advanceHome(loader, appPrimaryScene);
  }

  public void mouseClick(MouseEvent event) {
    this.mapDrawer.captureClick(event);
  }

  protected void Load() {
    for (javafx.scene.Node root :
        mapAnchor.getChildren().subList(1, mapAnchor.getChildren().size())) {
      root.setVisible(false);
    }
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
                  .setVisible(
                      mapDrawer
                          .getCurrentMap()
                          .equals(getNodeSet().get(value.getStartNode()).get_floor()));
            });
    adminMap
        .getNodeSet()
        .forEach(
            (key, value) -> {
              placeNode(value).setVisible(mapDrawer.getCurrentMap().equals(value.get_floor()));
            });
  }

  public void releaseMouse(MouseEvent mouseEvent) {
    //    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
    //      System.out.println("end: " + mouseEvent.getX() + ", " + mouseEvent.getY());
    //      Node other = adminMap.get(mouseEvent.getX(), mouseEvent.getY(),
    // mapDrawer.getCurrentMap());
    //      if (other != startNodeDrag) {
    //        placeLink(startNodeDrag.get_nodeID() + "_" + other.get_nodeID(), startNodeDrag,
    // other);
    //      }
    //    }
  }

  protected Group placeNode(Node n) {
    Group root =
        mapDrawer.drawNode(
            n.get_nodeID(),
            n.get_x() * getDownScale(),
            n.get_y() * getDownScale(),
            nodeColor.getValue());
    mapAnchor.getChildren().add(root);
    n.set_shape((Shape) root.getChildren().get(0));
    root.setCursor(Cursor.CROSSHAIR);
    return root;
  }

  /**
   * Places a line between two nodes
   *
   * @param node1 node id of first node
   * @param node2 node id of the second node
   */
  protected Group placeLink(String id, Node node1, Node node2) {
    Group root = mapDrawer.drawLine(id, node1, node2);
    //    adminMap.makeEdge(id, node1, node2, (Line) root.getChildren().get(0));
    mapAnchor.getChildren().add(root);
    root.setCursor(Cursor.CROSSHAIR);
    getAdminMap().makeEdge(id, node1, node2, (Line) root.getChildren().get(0));
    return root;
  }

  //  private void DeleteNodesFromMap() throws IOException {
  //    int i = 1;
  //    for (javafx.scene.Node root :
  //            mapAnchor.getChildren().subList(i, mapAnchor.getChildren().size())) {
  //      if (root.getId().equals(current.getId())) {
  //        mapAnchor.getChildren().remove(i);
  //        return;
  //      } else {
  //        i++;
  //      }
  //    }
  //  }

  //  public void DeleteObjectDataBase() throws IOException {
  //    String id = current.getId();
  //    if (adminMap.getNodeSet().containsKey(id)) {
  //      adminMap.deleteNode(id);
  //    } else if (adminMap.getEdgeSet().containsKey(id)) {
  //      adminMap.deleteEdge(id);
  //    } else {
  //      System.out.println("Object does not exist");
  //    }
  //  }

  public void mapFloor() {
    for (Node n : getNodeSet().values()) {
      n.get_shape().setVisible(n.get_floor().equals(mapDrawer.getCurrentMap()));
    }
  }

  public void correctFloor(Node.Link link) {
    if (getEdgeSet().containsKey(link._shape.getParent().getId())) {
      Edge a = getEdgeSet().get(link._shape.getParent().getId());
      if (getNodeSet().containsKey(a.getStartNode()) || getNodeSet().containsKey(a.getEndNode())) {
        link._shape
            .getParent()
            .setVisible(
                this.getNodeSet()
                    .get(a.getStartNode())
                    .get_floor()
                    .equals(mapDrawer.getCurrentMap()));
      }
    }
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
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (adminMap.getNodeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        if (adminMap
            .getNodeSet()
            .get(mapAnchor.getChildren().get(i).getId())
            .get_nodeType()
            .equals(a.getId()))
          ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setFill(a.getValue());
      }
    }
  }

  private JFXColorPicker getType(String id) {
    Node node = adminMap.getNodeSet().get(id);
    if (node.get_nodeType().contains("ELEV")) {
      return ELEV;
    } else if (node.get_nodeType().contains("STAI")) {
      return STAI;
    } else if (node.get_nodeType().contains("EXIT")) {
      return EXIT;
    } else {
      return nodeColor;
    }
  }

  public void newColorNodeaf(ActionEvent actionEvent) {
    JFXColorPicker a = ((JFXColorPicker) actionEvent.getSource());
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
                .contains("STAI"))
          ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
              .setFill(a.getValue());
      }
    }
  }

  public void newColorPath(ActionEvent actionEvent) {
    for (int i = 1; mapAnchor.getChildren().size() - 1 > i; i++) {
      if (adminMap.getEdgeSet().containsKey(mapAnchor.getChildren().get(i).getId())) {
        ((Shape) ((Group) mapAnchor.getChildren().get(i)).getChildren().get(0))
            .setFill(pathColor.getValue());
      }
    }
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
    this.Load();
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
