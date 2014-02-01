package com.yakovshklarov.branchy;

import java.util.Objects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

// TODO add docstrings for everything
// TODO use template
// TODO this should extend a standard binary search tree...
public class RedBlackTree implements Cloneable {
    private Node root;

    private static final String ANSI_NORMAL = "\u001B[0m";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";
    
    public RedBlackTree() {
        root = new Node();
        root.setColor(Color.BLACK);
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
        Node where, parent;
        where = root;
        
        while (where.getValue() != null) {
            comp = value.compareTo(where.getValue());
            if (comp < 0)
                where = where.getLeft();
            else if (comp > 0)
                where = where.getRight();
            else if (comp == 0)
                return;  // No duplicates allowed in our tree.
        }
        
        where.setValue(value);
        where.setColor(Color.RED);
    }
    
    public Node remove(Integer value) {
        // TODO: do this the proper red-black way.
        // TODO: fix ugly.
        int comp;
        Node toRemove, replacement, oldLeft, oldRight, oldParent;
        
        toRemove = search(value);
        if (toRemove == null) return null;
        
        oldLeft = toRemove.getLeft();
        oldRight = toRemove.getRight();
        oldParent = toRemove.getParent();
        
        if (!toRemove.getLeft().isLeaf()) {
            replacement = toRemove.getLeft().getRightmost();
            if (replacement == toRemove.getLeft()) {
                oldLeft = replacement.getLeft();
            } else {
                replacement.getParent().setRight(replacement.getLeft());
                replacement.getLeft().setParent(replacement.getParent());
            }
        } else if (!toRemove.getRight().isLeaf()) {
            replacement = toRemove.getRight().getLeftmost();
            if (replacement == toRemove.getRight()) {
                oldRight = replacement.getRight();
            } else {
                replacement.getParent().setLeft(replacement.getRight());
                replacement.getRight().setParent(replacement.getParent());
            }
        } else {
            replacement = new Node(); // leaf
        }
        
        if (!replacement.isLeaf()) {
            replacement.setLeft(oldLeft);
            replacement.setRight(oldRight);
            oldLeft.setParent(replacement);
            oldRight.setParent(replacement);
        }
        
        if (oldParent != null) {
            replacement.setParent(oldParent);
            comp = oldParent.compareTo(toRemove);
            if (comp < 0) {
                oldParent.setRight(replacement);
            } else {
                oldParent.setLeft(replacement);
            }
        }
        
        toRemove.setLeft(null);
        toRemove.setRight(null);
        toRemove.setParent(null);
        if (toRemove == root) {
            root = replacement;
            root.setParent(null);
        }
        
        return toRemove;
    }
    
    public Node search(Integer value) {
        int comp;
        Node where = root;
        
        while (!where.isLeaf()) {
            comp = value.compareTo(where.getValue());
            if (comp < 0)
                where = where.getLeft();
            else if (comp > 0)
                where = where.getRight();
            else if (comp == 0) {  // found a match
                return where;
            }
        }
        
        return null;  // No match.
    }

    public int size() {
        return root.size();
    }
    
    public Integer[] toArray() {
        return root.toArray();
    }
    
    public RedBlackTree clone() {
        // TODO: clone properly, preserving structure.
        return new RedBlackTree(toArray());
    }
    
    public boolean verify() {
        return root.getParent() == null && verify(root);
    }
    
    private boolean verify(Node n) {
        if (n.isLeaf()) return true;
        if (!n.getLeft().isLeaf())
            return n == n.getLeft().getParent() && verify(n.getLeft());
        if (!n.getRight().isLeaf())
            return n == n.getRight().getParent() && verify(n.getRight());
        return true;
    }
    
    public boolean equals(Object otherOb) {
        if (this == otherOb) return true;
        if (otherOb == null) return false;
        if (getClass() != otherOb.getClass()) return false;
        RedBlackTree other = (RedBlackTree) otherOb;
        return Objects.equals(root, other.root);
    }
    
    public int hashCode() {
        return Objects.hash(root);
    }
    
    public String toString() {
        return "Tree " + toString(root, "", "     ");
    }
    
    public String toString(Node n, String prefix1, String prefix2) {
        String output;
        String nodeStr;
        String spacing;
        String rightConnector;
        
        output = prefix1 + ANSI_BOLD;
        switch (n.getColor()) {
        case BLACK:
            output += ANSI_BLUE;
            break;
        case RED:
            output += ANSI_RED;
            break;
        }
        
        nodeStr = n.toString();
        spacing = new String(new char[nodeStr.length()]).replace('\0', ' ');
        output += nodeStr;
        output += ANSI_NORMAL;
        
        if (! n.isLeaf()) {
            if (n.getLeft() == null) // Will never happen for red-black trees.
                rightConnector = "─";
            else
                rightConnector = "┬";
            output += toString(n.getRight(), rightConnector,
                                             prefix2 + spacing + "│") + "\n" +
                      toString(n.getLeft(), prefix2 + spacing + "└",
                                            prefix2 + spacing + " ");
        }

        return output;
    }
}
