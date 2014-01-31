package com.yakovshklarov.branchy;

import java.util.Objects;

// TODO use template for type (only support Comparable types)
public class Node implements Cloneable, Comparable<Node> {
    private final Integer value;
    
    public Node(Integer v) {
        value = v;
    }
    
    public Integer getValue() {
        return value;
    }
    
    public Node clone() {
        return this;  // Nodes are immutable.
    }
    
    public int compareTo (Node other) {
        return Integer.compare(value, other.value);
    }
    
    public boolean equals(Object otherOb) {
        if (this == otherOb) return true;
        if (otherOb == null) return false;
        if (getClass() != otherOb.getClass()) return false;
        Node other = (Node) otherOb;
        return value == other.value;
    }

    public int hashCode() {
        return Objects.hash(value);
    }
    
    public String toString() {
        //return "Node[" + value + "]";
        return value.toString();
    }
}
