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
        int comp;
        Node toRemove, replacement;
        
        toRemove = search(value);
        if (toRemove == null) return null;

        // TODO: fix ugly.
        if (!toRemove.getLeft().isLeaf()) {
            replacement = toRemove.getLeft().getRightmost();
            if (toRemove.getLeft().getRight().isLeaf())
                replacement.getParent().setLeft(new Node());
            else
                replacement.getParent().setRight(new Node());
        } else if (!toRemove.getRight().isLeaf()) {
            replacement = toRemove.getRight().getLeftmost();
            if (toRemove.getRight().getLeft().isLeaf())
                replacement.getParent().setRight(new Node());
            else
                replacement.getParent().setLeft(new Node());
        } else {
            replacement = new Node(); // leaf
        }
        
        if (!replacement.isLeaf()) {
            replacement.setLeft(toRemove.getLeft());
            replacement.setRight(toRemove.getRight());
            replacement.getLeft().setParent(replacement);
            replacement.getRight().setParent(replacement);
        }
        replacement.setParent(toRemove.getParent());
        
        if (toRemove.getParent() != null) {
            comp = toRemove.getParent().compareTo(toRemove);
            if (comp < 0) {
                toRemove.getParent().setRight(replacement);
            } else {
                toRemove.getParent().setLeft(replacement);
            }
        }

        toRemove.setLeft(null);
        toRemove.setRight(null);
        toRemove.setParent(null);
        if (toRemove == root)
            root = replacement;
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
        // TODO properly space non-single-character contents.
        String output = prefix1;
        
        output += ANSI_BOLD;
        switch (n.getColor()) {
        case BLACK:
            output += ANSI_BLUE;
            break;
        case RED:
            output += ANSI_RED;
            break;
        }
        
        output += n;
        output += ANSI_NORMAL;
        
        if (! n.isLeaf()) {
            output += toString(n.getRight(), "┬", prefix2 + " |") +
                "\n" + toString(n.getLeft(), prefix2 + " └", prefix2 + "  ");
        }

        return output;
    }
}
