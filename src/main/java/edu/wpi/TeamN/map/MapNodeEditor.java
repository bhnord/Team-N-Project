package edu.wpi.TeamN.map;

import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.views.MapEditor;
import javafx.scene.Group;

public class MapNodeEditor {
  private final MapEditor mapEditor;

  public MapNodeEditor(MapEditor mapEditor) {
    this.mapEditor = mapEditor;
  }

  public void showNodeProperties(Group root) {
    Node node = mapEditor.getAdminMap().getNodeSet().get(root.getId());
    mapEditor.setNodeId(node.get_nodeID());
    mapEditor.setBuilding(node.get_building());
    mapEditor.setNodeType(node.get_nodeType());
    mapEditor.setFloor(node.get_floor());
    mapEditor.setXCoord(Double.toString(node.get_x()));
    mapEditor.setYCoord(Double.toString(node.get_y()));
    mapEditor.setLongName(node.get_longName());
    mapEditor.setShortName(node.get_shortName());
  }

  public void commitChanges(Group root) {
    Node selectedNode = mapEditor.getAdminMap().getNodeSet().get(mapEditor.getNodeId().getText());
    String id = mapEditor.getNodeId().getText();
    double x, y;
    String f = mapEditor.getFloor().getText();
    String b = mapEditor.getBuilding().getText();
    String nt = mapEditor.getNodeType().getText();
    String ln = mapEditor.getLongName().getText();
    String sn = mapEditor.getShortName().getText();

    try {
      x = Double.parseDouble(mapEditor.getXCoord().getText());
      y = Double.parseDouble(mapEditor.getYCoord().getText());

      String currentID = root.getId();
      if (mapEditor.getAdminMap().getNodeSet().containsKey(currentID)) {
        root.setId(id);
        mapEditor.getAdminMap().deleteNode(currentID);
        mapEditor.getAdminMap().addNode(new Node(x, y, id, f, b, nt, ln, sn));
      } else {
        System.out.println("Node already Exists");
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}
