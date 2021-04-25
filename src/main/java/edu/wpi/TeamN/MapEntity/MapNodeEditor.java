package edu.wpi.TeamN.MapEntity;

import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.views.MapController;
import javafx.event.ActionEvent;
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

  public void commitChanges(ActionEvent actionEvent) {
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
      x = Double.parseDouble(mapController.getYCoord().getText());
      y = Double.parseDouble(mapController.getYCoord().getText());

      if (!(selectedNode == null)) {

        if (!id.equals(selectedNode.get_nodeID())) {
          if (!mapController.getDb().updateNode(id, x, y, f, b, nt, ln, sn)) {
            //                        messageLabel.setText("Invalid inputs");
          }
        } else {
          //                    messageLabel.setText("You cannot change the ID of an already made
          // node");
        }
      } else {
        id = mapController.getNodeId().getText();
        Node n = new Node(x, y, id, f, b, nt, ln, sn);
        if (mapController.getDb().addNode(n)) {
          // updateSelectedLabel(id);
        } else {
          // messageLabel.setText("Invalid inputs");
        }
      }

    } catch (Exception e) {
      // messageLabel.setText("Invalid type in field");
    }
  }
}
