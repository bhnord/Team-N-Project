package edu.wpi.TeamN.services.algo;

import java.util.*;

public class Bfs extends PathFinderAlgorithm implements IPathFinder {
  @Override
  public ArrayList<Node.Link> pathfindFull(Node start, Node end, Reduce filter) {
    LinkedList<Node> open = new LinkedList<>();
    ArrayList<Node> used = new ArrayList<>();
    Node curnode;
    used.add(start);
    open.push(start);

    while (!open.isEmpty()) {
      curnode = open.pop();
      if (curnode == end) {
        break;
      }
      for (Node.Link l : curnode.get_neighbors()) {
        Node n = l._this;
        if (!n.is_seen()) {
          used.add(n);
          open.push(n);
          n.set_seen(true);
          n.set_parent(l);
        }
      }
    }
    ArrayList<Node.Link> ret = PathFinder.rebuild(start, end);
    // reset manipulated nodes
    for (Node n : used) {
      n.reset();
    }
    return ret;
  }
}
