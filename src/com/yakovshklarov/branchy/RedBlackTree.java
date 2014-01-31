package com.yakovshklarov.branchy;

import java.util.Objects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

// TODO add docstrings for everything
// TODO specify what type to store
// TODO this should extend a standard binary search tree...
public class RedBlackTree implements Cloneable {
    // If root != null then left and right should also not be null.
    // root == null always represents a leaf (ie. a null node.)
    private Node root;
    private Color color;
    private RedBlackTree left;
    private RedBlackTree right;
    private RedBlackTree parent;

    private static final String ANSI_NORMAL = "\u001B[0m";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";

    
    public RedBlackTree() {
        root = null;
        parent = null;
        color = Color.BLACK;
    }
    
    public RedBlackTree(Integer[] values) {
        this();
        insert(values);
    }
    
    public void insert(Integer[] values) {
        for (int v: values) insert(v);
    }
    
    public void insert(Integer value) {
        // TODO: do this the proper red-black way.
        int comp;
        
        if (root == null) {
            root = new Node(value);
            color = Color.RED;
            left = new RedBlackTree();
            right = new RedBlackTree();
        } else {
            comp = value.compareTo(root.getValue());
            if (comp < 0)
                left.insert(value);
            else if (comp > 0)
                right.insert(value);
            // else if (comp == 0): do nothing; duplicates are not allowed.
        }
    }
    
    public Node remove(Integer value) {
        Node temp;
        
        if (root == null)
            return null;

        // TODO: do this the proper red-black way.
        int comp;
        comp = value.compareTo(root.getValue());
        if (comp < 0)
            return left.remove(value);
        else if (comp > 0)
            return right.remove(value);

        // comp == 0: we found a match
        temp = root;
        if (left.root != null)
            root = left.remove(left.getRightmostNode().getValue());
        else if (right.root != null)
            root = right.remove(right.getLeftmostNode().getValue());
        else
            root = null;

        return temp;
    }
    
    private Node getRightmostNode() {
        if (right == null || right.root == null) return root;
        return right.getRightmostNode();
    }
    private Node getLeftmostNode() {
        if (left == null || left.root == null) return root;
        return left.getLeftmostNode();
    }
    
    public Node search(Integer value) {
        int comp = value.compareTo(root.getValue());
        if (comp == 0) {
            return root;
        } else if (comp < 0) {
            if (left == null) return null;
            return left.search(value);
        } else {  // comp > 0
            if (right == null) return null;
            return right.search(value);
        }
    }

    public int size() {
        int count;
        
        if (root == null) return 0;
        
        count = 1;
        if (left != null)  count +=  left.size();
        if (right != null) count += right.size();
        return count;
    }
    
    public Integer[] toArray() {
        ArrayList<Integer> list = new ArrayList<>();
        Integer[] array;
        Iterator<Integer> iter;

        if (left != null) list.addAll(Arrays.asList(left.toArray()));
        if (root != null) list.add(root.getValue());
        if (right != null) list.addAll(Arrays.asList(right.toArray()));
        
        array = new Integer[list.size()];
        iter = list.iterator();
        
        for (int i = 0; i < list.size(); i++) {
            array[i] = iter.next();
        }
        
        return array;
    }
    
    
    public void setRight(RedBlackTree t) { right = t; }
    public void setLeft(RedBlackTree t) { left = t; }

    public RedBlackTree getRight() { return right; }
    public RedBlackTree getLeft() { return left; }

    
    public RedBlackTree clone() {
        // Inefficient. TODO: clone more efficiently.
        return new RedBlackTree(toArray());
    }
    
    
    public boolean equals(Object otherOb) {
        if (this == otherOb) return true;
        if (otherOb == null) return false;
        if (getClass() != otherOb.getClass()) return false;
        RedBlackTree other = (RedBlackTree) otherOb;
        return color == other.color && root == other.root &&
            Objects.equals(left, other.left) && Objects.equals(right, other.right);
    }
    
    public int hashCode() {
        return Objects.hash(root, color, left, right);
    }
    
    public String toString() {
        // TODO make it display an actual, colorful tree
        String output = "Tree[";
        output += ANSI_BOLD;
        if (color == Color.BLACK)
            output += ANSI_BLUE;
        else if (color == Color.RED)
            output += ANSI_RED;
        
        output += root;
        output += ANSI_NORMAL;
        
        if (root != null)
            output += ", left: " + left + ", right: " + right;

        output += "]";

        return output;
    }
}
