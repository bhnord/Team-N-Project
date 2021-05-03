package edu.wpi.TeamN.map;

import edu.wpi.TeamN.services.algo.Node;
import edu.wpi.TeamN.views.MapEditor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class MapNodeEditor {
  private final MapEditor mapEditor;
  private ArrayList<Node> selection;
  private Node dragger;

  public void clearSelection() {
    this.selection.clear();
  }

  public void addNode(Node n) {
    this.selection.add(n);
  }

  public void moveSelection(ArrayList<Node> selection, double[] dx, double[] dy) {
    for (int i = 0; i < selection.size(); i++) {
      Node s = selection.get(i);
      s.set_x(s.get_x() + dx[i]);
      s.set_y(s.get_y() + dy[i]);
      s.get_shape().setCenterX(s.get_x() * mapEditor.getDownScale());
      s.get_shape().setCenterY(s.get_y() * mapEditor.getDownScale());
      updateLinks(s);
    }
    finalize_change();
  }

  public void moveSelection(ArrayList<Node> selection, double dx, double dy) {
    for (Node s : selection) {
      s.set_x(s.get_x() + dx);
      s.set_y(s.get_y() + dy);
      s.get_shape().setCenterX(s.get_x() * mapEditor.getDownScale());
      s.get_shape().setCenterY(s.get_y() * mapEditor.getDownScale());
      updateLinks(s);
    }
    finalize_change();
  }

  public void handleDrag(MouseEvent e, Node n) {
    if (selection.size() == 0) {
      selection.add(n);
    } else if (!selection.contains(n)) {
      selection.clear();
      selection.add(n);
    }
    this.dragger = n;
    double diffX = e.getX() - n.get_x() * mapEditor.getDownScale();
    double diffY = e.getY() - n.get_y() * mapEditor.getDownScale();
    for (Node s : selection) {
      s.get_shape().setCenterX(s.get_x() * mapEditor.getDownScale() + diffX);
      s.get_shape().setCenterY(s.get_y() * mapEditor.getDownScale() + diffY);
      updateLinks(s);
    }
  }

  private void updateLinks(Node n) {
    for (Node.Link l : n.get_neighbors()) {
      l._shape.setStartX(l._this.get_shape().getCenterX());
      l._shape.setStartY(l._this.get_shape().getCenterY());

      l._shape.setEndX(l._other.get_shape().getCenterX());
      l._shape.setEndY(l._other.get_shape().getCenterY());
    }
  }

  public void finalize_change(DiffHandler diffHandler) {
    double[] dx = new double[selection.size()];
    double[] dy = new double[selection.size()];
    for (int i = 0; i < selection.size(); i++) {
      Node s = selection.get(i);
      dx[i] = s.get_x();
      dy[i] = s.get_y();
      s.set_x(s.get_shape().getCenterX() * mapEditor.getUpScale());
      s.set_y(s.get_shape().getCenterY() * mapEditor.getUpScale());
      dx[i] -= s.get_x();
      dy[i] -= s.get_y();
      dx[i] = -dx[i];
      dy[i] = -dy[i];
      updateLinks(s);
    }
    diffHandler.align(new ArrayList<>(selection), dx, dy);
  }

  public void finalize_change() {
    for (Node s : selection) {
      s.set_x(s.get_shape().getCenterX() * mapEditor.getUpScale());
      s.set_y(s.get_shape().getCenterY() * mapEditor.getUpScale());
      updateLinks(s);
    }
  }

  private boolean straightenPrelim() {
    if (selection.size() <= 1) {
      return false;
    }

    double lastx = selection.get(0).get_x();
    double lasty = selection.get(0).get_y();
    boolean xf = true;
    boolean yf = true;

    for (int i = 1; i < selection.size(); i++) {
      if (lastx != selection.get(i).get_x()) {
        xf = false;
      }
      if (lasty != selection.get(i).get_y()) {
        yf = false;
      }
    }

    if (xf || yf) {
      return false;
    }
    return true;
  }

  private double[] applyReg() {
    ArrayList<Double> xs = new ArrayList<>();
    ArrayList<Double> ys = new ArrayList<>();
    selection.forEach(
        n -> {
          xs.add(n.get_x());
          ys.add(n.get_y());
        });
    return regression(xs, ys);
  }

  public void straightenSelectionSnap() {
    if (!straightenPrelim()) {
      return;
    }
    double[] d = applyReg();
    if (Math.abs(d[0]) > 1) straightenSelectionVert();
    else straightenSelectionHoriz();
  }

  public void straightenSelectionVert() {
    if (!straightenPrelim()) {
      return;
    }
    double averageX = 0;
    for (Node n : selection) {
      averageX += n.get_x();
    }
    averageX /= selection.size();
    for (Node n : selection) {
      n.get_shape().setCenterX(averageX);
    }
  }

  public void straightenSelectionHoriz() {
    if (!straightenPrelim()) {
      return;
    }
    double averageY = 0;
    for (Node n : selection) {
      averageY += n.get_y();
    }
    averageY /= selection.size();
    for (Node n : selection) {
      n.get_shape().setCenterY(averageY);
    }
  }

  public void straightenSelection() {
    if (!straightenPrelim()) {
      return;
    }
    double[] d = applyReg();
    for (Node n : selection) {
      double[] p = closesPoint(d[0], d[1], n.get_x(), n.get_y());
      n.get_shape().setCenterX(p[0] * mapEditor.getDownScale());
      n.get_shape().setCenterY(p[1] * mapEditor.getDownScale());
    }
  }

  private double[] closesPoint(double m, double b, double x, double y) {
    double m2 = -1 / m;
    double b2 = y - (m2 * x);

    double s = (b2 - b) / (m - m2);
    double r_y = m * s + b;

    return new double[] {s, r_y};
  }

  private double[] regression(List<Double> l1, List<Double> l2) {
    double sxy = 0;
    double sx = 0;
    double sy = 0;
    double sx2 = 0;
    for (int i = 0; i < l1.size(); i++) {
      double x = l1.get(i);
      double y = l2.get(i);
      sxy += x * y;
      sx += x;
      sy += y;
      sx2 += x * x;
    }
    double m = (selection.size() * sxy - sx * sy) / (selection.size() * sx2 - (sx * sx));
    double b = (sy - m * sx) / selection.size();

    return new double[] {m, b};
  }

  public MapNodeEditor(MapEditor mapEditor) {
    this.selection = new ArrayList<>();
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

  public void createNodes(ArrayList<Node> nodes) {
    HashSet<String> placed = new HashSet<>();
    for (Node n : nodes) {
      mapEditor.placeNode(n);
      for (Node.Link l : n.get_neighbors()) {
        if (!placed.contains(l._id)) {
          mapEditor.placeLink(l._id, l._this, l._other);
          placed.add(l._id);
        }
      }
    }
  }

  public void deleteNodes(ArrayList<Node> nodes) {
    HashSet<String> placed = new HashSet<>();
    for (Node n : nodes) {
      mapEditor.removeNode(n);
    }
  }
}
