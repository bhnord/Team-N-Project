package edu.wpi.teamname.services.algo;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;

public abstract class Node<T extends Node<T>> implements Comparable<Node<T>> {
  public class Link {
    int _distance;
    Node<T> _other;

    public Link(Node<T> other, int distance) {
      this._distance = distance;
      this._other = other;
    }
  }

  T value;
  int _localGoal;
  int _globalGoal;
  boolean _seen;
  Node<T> _parent;
  List<Link> _neighbors;

  public Node() {
    _neighbors = new ArrayList<>();
    reset();
  }

  /** resets all node parameters to default */
  public void reset() {
    this._localGoal = 0;
    this._globalGoal = 0;
    this._seen = false;
    this._parent = null;
  }

  public void addNeighbor(Node<T> other, int distance) {
    this._neighbors.add(new Link(other, distance));
  }

  abstract int heuristic(Node<T> other);

  @Override
  public int compareTo(@NonNull Node<T> Other) {
    return (this._localGoal + this._globalGoal) > (Other._localGoal + Other._globalGoal) ? 1 : -1;
  }
}
