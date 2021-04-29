package edu.wpi.TeamN.mapEntity;

import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.views.MapController;
import javafx.scene.Group;

public class MapNodeEditor {
  private final MapController mapController;

  public MapNodeEditor(MapController mapController) {
    this.mapController = mapController;
  }

  public void showNodeProperties(Group root) {
    Node node = mapController.getAdminMap().getNodeSet().get(root.getId());
    mapController.setNodeId(node.get_nodeID());
    mapController.setBuilding(node.get_building());
    mapController.setNodeType(node.get_nodeType());
    mapController.setFloor(node.get_floor());
    mapController.setXCoord(Double.toString(node.get_x()));
    mapController.setYCoord(Double.toString(node.get_y()));
    mapController.setLongName(node.get_longName());
    mapController.setShortName(node.get_shortName());
  }

  public void commitChanges(Group root) {
    Node selectedNode =
        mapController.getAdminMap().getNodeSet().get(mapController.getNodeId().getText());
    String id = mapController.getNodeId().getText();
    double x, y;
    String f = mapController.getFloor().getText();
    String b = mapController.getBuilding().getText();
    String nt = mapController.getNodeType().getText();
    String ln = mapController.getLongName().getText();
    String sn = mapController.getShortName().getText();

    try {
      x = Double.parseDouble(mapController.getXCoord().getText());
      y = Double.parseDouble(mapController.getYCoord().getText());

      String currentID = root.getId();
      if (mapController.getAdminMap().getNodeSet().containsKey(currentID)) {
        root.setId(id);
        mapController.getAdminMap().deleteNode(currentID);
        mapController.getAdminMap().addNode(new Node(x, y, id, f, b, nt, ln, sn));
      } else {
        System.out.println("Node already Exists");
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}
