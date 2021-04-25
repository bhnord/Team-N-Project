package edu.wpi.TeamN.services.algo;

import java.util.*;

public class PathFinder {
  public interface Reduce {
    boolean isValid(Node.Link l);
  }
  /**
   * @param start node to start search from
   * @param end target node
   * @return a stack of nodes containing the path from the start to the end
   */
  public ArrayList<Node.Link> Astar(Node start, Node end) {
    return AstarFull(start, end, (l) -> true);
  }

  public ArrayList<Node.Link> AstarNoStairs(Node start, Node end) {
    return AstarFull(start, end, (l) -> !l._isStair);
  }

  public ArrayList<Node.Link> AstarFull(Node start, Node end, Reduce filter) {
    // Node.reset() should be called on every node accessible to start
    Node curNode;
    start.set_localGoal(0);
    start.set_globalGoal(start.heuristic(end));
    PriorityQueue<Node> open = new PriorityQueue<>();
    ArrayList<Node> used = new ArrayList<>();
    used.add(start);
    open.add(start);
    start.set_seen(true);
    do {
      curNode = open.remove();
      if (curNode == end) {
        break;
      }
      for (Node.Link l : curNode.get_neighbors()) {
        if (!filter.isValid(l)) {
          continue;
        }
        Node n = l._other;
        double local = curNode.get_localGoal() + l._distance;
        if (!n.is_seen() || n.get_localGoal() > local) {
          n.set_localGoal(local);
          n.set_globalGoal(n.heuristic(end));
          n.set_parent(l);
          n.set_seen(true);
          open.add(n);
          used.add(n);
        }
      }
    } while (!open.isEmpty());
    ArrayList<Node.Link> ret = rebuild(start, end);
    // reset manipulated nodes
    for (Node n : used) {
      n.reset();
    }
    return ret;
  }
  /**
   * @param start node to walk back to
   * @param curNode node to start walking back from
   * @return a stack of nodes where start is at the top and every next node's parent is the previous
   *     node in the stack
   */
  private ArrayList<Node.Link> rebuild(Node start, Node curNode) {
    ArrayList<Node.Link> ret = new ArrayList<>();
    while (curNode.get_parent() != null) {
      ret.add(curNode.get_parent());
      curNode = curNode.get_parent()._this;
    }
    return ret;
  }
}
