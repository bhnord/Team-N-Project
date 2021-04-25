package edu.wpi.TeamN.MapEntity;

import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.views.MapController;
import javafx.scene.Group;

public class MapEdgeEditor {
  private final MapController mapController;

  public MapEdgeEditor(MapController mapController) {
    this.mapController = mapController;
  }

  public void showEdgeProperties(Group root) {
    Edge edge = mapController.getAdminMap().getEdgeSet().get(root.getId());
    mapController.setEdgeID(edge.getEdgeID());
    mapController.setEndNode(edge.getEndNode());
    mapController.setStartNode(edge.getStartNode());
  }

  public void saveEdge(Group root) {
    Edge edge = mapController.getAdminMap().getEdgeSet().get(mapController.getEdgeID().getText());
    String id = mapController.getEdgeID().getId();
    String sn = mapController.getStartNode().getText();
    String en = mapController.getEndNode().getText();

    try {
      String currentid = root.getId();
      if (mapController.getAdminMap().getEdgeSet().containsKey(currentid)) {
        root.setId(id);
        mapController.getAdminMap().deleteEdge(currentid);
        mapController.getAdminMap().addEdge(new Edge(id, sn, en));
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}
