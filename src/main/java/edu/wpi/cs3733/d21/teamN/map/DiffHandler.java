package edu.wpi.cs3733.d21.teamN.map;

import edu.wpi.cs3733.d21.teamN.services.algo.Node;
import edu.wpi.cs3733.d21.teamN.views.MapEditor;
import java.util.ArrayList;
import java.util.Arrays;

public class DiffHandler {
  private static class Diff {
    DiffType type;
    ArrayList<Node> nodes;
    Node node1;
    Node node2;
    String id;
    double[] dxs;
    double[] dys;
    double dx, dy;

    public Diff(DiffType type, Node node1, Node node2, String id) {
      this.type = type;
      this.node1 = node1;
      this.node2 = node2;
      this.id = id;
    }

    public Diff(DiffType type, ArrayList<Node> nodes) {
      this.type = type;
      this.nodes = nodes;
    }

    public Diff(DiffType type, ArrayList<Node> nodes, double dx, double dy) {
      this.type = type;
      this.nodes = nodes;
      this.dx = dx;
      this.dy = dy;
    }

    public Diff(DiffType type, ArrayList<Node> nodes, double[] dxs, double[] dys) {
      this.type = type;
      this.nodes = nodes;
      this.dxs = dxs;
      this.dys = dys;
    }
  }

  ArrayList<Diff> diffs;
  int index;
  MapNodeEditor nodeEditor;
  MapEditor mapEditor;

  public DiffHandler(MapNodeEditor nodeEditor, MapEditor mapEditor) {
    this.diffs = new ArrayList<>();
    this.nodeEditor = nodeEditor;
    this.index = 0;
    this.mapEditor = mapEditor;
  }

  public void create(ArrayList<Node> nodes) {
    (diffs.subList(index, diffs.size())).clear();
    diffs.add(new Diff(DiffType.CREATE, nodes));
    index++;
  }

  public void deleteEdge(String id, Node node1, Node node2) {
    (diffs.subList(index, diffs.size())).clear();
    diffs.add(new Diff(DiffType.DELETE_LINK, node1, node2, id));
    index++;
  }

  public void makeEdge(String id, Node node1, Node node2) {
    (diffs.subList(index, diffs.size())).clear();
    diffs.add(new Diff(DiffType.CREATE_LINK, node1, node2, id));
    index++;
  }

  public void align(ArrayList<Node> nodes, double[] x, double[] y) {
    (diffs.subList(index, diffs.size())).clear();
    diffs.add(new Diff(DiffType.MOVE, nodes, x, y));
    index++;
  }

  public void delete(ArrayList<Node> nodes) {
    (diffs.subList(index, diffs.size())).clear();
    diffs.add(new Diff(DiffType.DELETE, nodes));
    index++;
  }

  public boolean redo() {
    if (index == diffs.size()) {
      return false;
    }
    apply(diffs.get(index++));
    return true;
  }

  public boolean undo() {
    if (index == 0) {
      return false;
    }
    reverse(diffs.get(--index));
    return true;
  }

  private Diff apply(Diff diff) {
    switch (diff.type) {
      case CREATE:
        nodeEditor.createNodes(diff.nodes);
        return diff;
      case DELETE:
        nodeEditor.deleteNodes(diff.nodes);
        return diff;
      case MOVE:
        nodeEditor.moveSelection(diff.nodes, diff.dxs, diff.dys);
        return diff;
      case CREATE_LINK:
        mapEditor.placeLink(diff.id, diff.node1, diff.node2);
        return diff;
      case DELETE_LINK:
        mapEditor.deleteLink(diff.id, diff.node1, diff.node2);
        return diff;
    }
    return diff;
  }

  private Diff reverse(Diff diff) {
    switch (diff.type) {
      case CREATE:
        nodeEditor.deleteNodes(diff.nodes);
        return diff;
      case DELETE:
        nodeEditor.createNodes(diff.nodes);
        return diff;
      case MOVE:
        nodeEditor.moveSelection(
            diff.nodes,
            Arrays.stream(diff.dxs).map(v -> -v).toArray(),
            Arrays.stream(diff.dys).map(v -> -v).toArray());
        return diff;
      case CREATE_LINK:
        mapEditor.deleteLink(diff.id, diff.node1, diff.node2);
        return diff;
      case DELETE_LINK:
        mapEditor.placeLink(diff.id, diff.node1, diff.node2);
        return diff;
    }
    return diff;
  }
}
