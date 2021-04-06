package edu.wpi.teamname.services.algo;

public class Cartesian extends Node<Cartesian>{
    float _x,_y;

    public Cartesian(float _x, float _y) {
        super();
        this.value = this;
        this._x = _x;
        this._y = _y;
    }

    @Override
    public float heuristic(Node<Cartesian> other){
        return (other.value._x - this._x) * (other.value._x - this._x) + (other.value._y - this._y) * (other.value._y - this._y);
    }
}

