package edu.wpi.TeamN.map;

import edu.wpi.TeamN.services.algo.Edge;
import edu.wpi.TeamN.views.MapEditor;
import javafx.scene.Group;

public class MapEdgeEditor {
  private final MapEditor mapEditor;

  public MapEdgeEditor(MapEditor mapEditor) {
    this.mapEditor = mapEditor;
  }

  public void showEdgeProperties(Group root) {
    Edge edge = mapEditor.getAdminMap().getEdgeSet().get(root.getId());
    mapEditor.setEdgeID(edge.getEdgeID());
    mapEditor.setEndNode(edge.getEndNode());
    mapEditor.setStartNode(edge.getStartNode());
  }

  public void saveEdge(Group root) {
    //    Edge edge = mapEditor.getAdminMap().getEdgeSet().get(mapEditor.getEdgeID().getText());
    String id = mapEditor.getEdgeID().getText();
    String sn = mapEditor.getStartNode().getText();
    String en = mapEditor.getEndNode().getText();

    try {
      String currentid = root.getId();
      if (mapEditor.getAdminMap().getEdgeSet().containsKey(currentid)) {
        root.setId(id);
        mapEditor.getAdminMap().deleteEdge(currentid);
        mapEditor.getAdminMap().addEdge(new Edge(id, sn, en));
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
  }
}
