package com.yakovshklarov.branchy;

import java.util.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

// TODO use template for type (only support Comparable types)
public class Node implements Comparable<Node> {
    // If value != null then left and right should also not be null (but they
    // may have null values.)
    // value == null always represents a leaf (ie. a null node.)
    private Integer value;
    private Color color;
    private Node left;
    private Node right;
    private Node parent;
    
    public Node() {
        this(null, null);
    }
    
    public Node(Integer value) {
        this(value, null);
    }

    public Node(Node parent) {
        this(null, parent);
    }
    
    public Node(Integer value, Node parent) {
        this.value = value;
        this.parent = parent;
        left = null;
        right = null;
        color = Color.BLACK;
    }        
    
    public Color getColor()   { return color;  }
    public Node getLeft()     { return left;   }
    public Node getRight()    { return right;  }
    public Node getParent()   { return parent; }
    public Integer getValue() { return value;  }

    public void setColor(Color color)   { this.color  = color;  }
    public void setLeft(Node left)      { this.left   = left;   }
    public void setRight(Node right)    { this.right  = right;  }
    public void setParent(Node parent)  { this.parent = parent; }
    public void setValue(Integer value) {
        this.value  = value;
        if (value != null) {
            if (left == null) left = new Node(this);
            if (right == null) right = new Node(this);
        }
    }
    
    public boolean isLeaf() {
        return value == null;
    }
    
    public Node getRightmost() {
        if (right == null || right.value == null) return this;
        return right.getRightmost();
    }
    
    public Node getLeftmost() {
        if (left == null || left.value == null) return this;
        return left.getLeftmost();
    }
    
    public int size() {
        if (isLeaf()) return 0;
        return 1 + left.size() + right.size();
    }
    
    public int compareTo (Node other) {
        return Integer.compare(value, other.value);
    }

    
    public boolean equals(Object otherOb) {
        if (this == otherOb) return true;
        if (otherOb == null) return false;
        if (getClass() != otherOb.getClass()) return false;

        Node other = (Node) otherOb;

        if (parent == null && other.parent == null) {
            return value.equals(other.value) && color == other.color &&
                left.equals(other.left) && right.equals(other.right);
        } else if (parent == null || other.parent == null) {
            return false;
        } else {
            return parent.equals(other.parent);
        }
    }

    public int hashCode() {
        if (parent == null)
            return Objects.hash(value, color, left, right);
        return parent.hashCode();
    }
    
    public String toString() {
        //return "Node[" + value + "]";
        if (value == null) return "L";
        return value.toString();
    }
}
