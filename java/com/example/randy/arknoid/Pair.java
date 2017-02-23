package com.example.randy.arknoid;

public class Pair<A,B> {
    public A first;
    public B second;

    public Pair(A _first, B _second) {
        first = _first;
        second = _second;
    }

    public Boolean refEq(Pair<A,B> others) {
        if (others == null) return false;
        return (first == others.first) && (second == others.second);
    }

    public Boolean valEq(Pair<A,B> others) {
        if (others == null) return false;
        Boolean left,right;
        if (first == null) {
            left = others.first == null;
        } else {
            left = others.first == null ? false : first.equals(others.first);
        }
        if (second == null) {
            right = others.second == null;
        } else {
            right = others.second == null ? false : second.equals(others.second);
        }
        return left && right;
    }

    public String toString() {
        return "[" + first + ", " + second + "]";
    }
}
