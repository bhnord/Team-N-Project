package edu.wpi.teamname.services.algo;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Node <T extends Node<T>> implements Comparable<Node<T>> {
    public class Link{
       float _distance;
       Node<T> _other;
    }
    T value;
    float _localGoal;
    float _globalGoal;
    boolean _seen;
    Node<T> _parent;
    List<Link> _neighbors;

    public Node(){
        _neighbors = new ArrayList<>();
        reset();
    }

    /**
     * resets all node parameters to default
     */
    public void reset(){
        this._localGoal = 0;
        this._globalGoal = 0;
        this._seen = false;
        this._parent = null;
    }
    abstract float heuristic(Node<T> other);
    @Override
    public int compareTo(@NonNull Node<T> Other){
        return (this._localGoal + this._globalGoal) > (this._localGoal + this._globalGoal) ? 1 : -1;
    }
}
