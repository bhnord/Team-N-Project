package edu.wpi.TeamN.services.algo;

import java.util.*;

public class Astar implements PathFinderI {
  /**
   * @param start node to start search from
   * @param end target node
   * @return a stack of nodes containing the path from the start to the end
   */
  public ArrayList<Node.Link> pathfindFull(Node start, Node end, Reduce filter) {
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
    ArrayList<Node.Link> ret = PathFinder.rebuild(start, end);
    // reset manipulated nodes
    for (Node n : used) {
      n.reset();
    }
    return ret;
  }
}
