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
    
    // Create a new orphan leaf.
    public Node() {
        this(null, null);
    }
    
    // Create a new orphan node with two child leaves.
    public Node(Integer value) {
        this(value, null);
    }
    
    // Create a new child leaf.
    public Node(Node parent) {
        this(null, parent);
    }
    
    // Create a new black child node, making leaf children if necessary.
    public Node(Integer value, Node parent) {
        this.value = value;
        this.parent = parent;
        if (value != null) {
            left = new Node();
            right = new Node();
        } else {
            left = null;
            right = null;
        }
        color = Color.BLACK;  // By default
    }        
    
    public Color getColor()   { return color;  }
    public Node getLeft()     { return left;   }
    public Node getRight()    { return right;  }
    public Node getParent()   { return parent; }
    public Integer getValue() { return value;  }

    public void setColor(Color color)   { this.color  = color;  }
    public void setLeft(Node left) {
        this.left = left;
        if (left != null)
            left.setParent(this);
    }
    public void setRight(Node right) {
        this.right = right;
        if (right != null)
            right.setParent(this);
    }
    public void setParent(Node parent)  { this.parent = parent; }
    // Set this node's value, creating leaf children if necessary.
    // If value is null, turn the node into a leaf, deleting any children.
    public void setValue(Integer value) {
        this.value  = value;
        if (value == null) {
            left = null;
            right = null;
            setColor(Color.BLACK);
        } else {
            if (left == null) left = new Node(this);
            if (right == null) right = new Node(this);
        }
    }
    
    public void makeBlack() {
        color = Color.BLACK;
    }

    public void makeRed() {
        color = Color.RED;
    }
    
    public Node getUncle() {
        Node g = getGrandparent();
        if (g != null) {
            if (parent == g.left)
                return g.right;
            else
                return g.left;
        }
        return null;
    }

    public Node getGrandparent() {
        if (parent != null)
            return parent.parent;
        else
            return null;
    }
    
    public Node getSibling() {
        if (parent == null)
            return null;
        if (this == parent.left)
            return parent.right;
        else
            return parent.left;
    }
    
    public boolean isLeaf() {
        return value == null;
    }
    
    public boolean isLeftChild() {
        return parent != null && this == parent.left;
    }

    public boolean isRightChild() {
        return parent != null && this == parent.right;
    }
    
    public boolean isRed() {
        return color == Color.RED;
    }

    public boolean isBlack() {
        return color == Color.BLACK;
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
