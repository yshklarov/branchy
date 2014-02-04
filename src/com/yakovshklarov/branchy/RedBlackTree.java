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
    }
    
    public RedBlackTree(Integer[] values) {
        this();
        insert(values);
    }
    
    public void insert(Integer[] values) {
        for (int v: values) insert(v);
    }
    
    public void insert(Integer value) {
        int comp;
        Node where, parent;
        where = root;
        
        // Replace one of the leaves with the node.
        while (!where.isLeaf()) {
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
        
        // Adjust the tree to remain red-black.
        insertCase1(where);
    }
    
    private void insertCase1(Node n) {
        if (n == root)
            n.setColor(Color.BLACK);
        else
            insertCase2(n);
    }
    
    private void insertCase2(Node n) {
        if (n.getParent().getColor() == Color.BLACK)
            return;
        else
            insertCase3(n);
    }
    
    private void insertCase3(Node n) {
        Node u, g;
        u = n.getUncle();
        
        if (u != null && u.getColor() == Color.RED) {
            n.getParent().setColor(Color.BLACK);
            u.setColor(Color.BLACK);
            g = n.getGrandparent();
            g.setColor(Color.RED);
            insertCase1(g);
        } else {
            insertCase4(n);
        }
    }
    
    private void insertCase4(Node n) {
        Node g = n.getGrandparent();
        
        if (n == n.getParent().getRight() && n.getParent() == g.getLeft()) {
            rotateLeft(n.getParent());
            n = n.getLeft();
        } else if (n == n.getParent().getLeft() && n.getParent() == g.getRight()) {
            rotateRight(n.getParent());
            n = n.getRight();
        }
        
        insertCase5(n);
    }

    private void insertCase5(Node n) {
        Node g = n.getGrandparent();
        n.getParent().setColor(Color.BLACK);
        g.setColor(Color.RED);
        if (n == n.getParent().getLeft()) {
            rotateRight(g);
        } else {
            rotateLeft(g);
        }
    }
    
    private void rotateLeft(Node n) {
        Node pivot, oldP;
        pivot = n.getRight();
        oldP = n.getParent();
        
        n.setRight(pivot.getLeft());
        pivot.setLeft(n);
        if (oldP != null) {
            if (oldP.getLeft() == n)
                oldP.setLeft(pivot);
            else if (oldP.getRight() == n)
                oldP.setRight(pivot);
        } else { // n was the root node
            root = pivot;
            root.setParent(null);
        }
    }
    
    private void rotateRight(Node n){
        Node pivot, oldP;
        pivot = n.getLeft();
        oldP = n.getParent();
        
        n.setLeft(pivot.getRight());
        pivot.setRight(n);
        if (oldP != null) {
            if (oldP.getLeft() == n)
                oldP.setLeft(pivot);
            else if (oldP.getRight() == n)
                oldP.setRight(pivot);
        } else { // n was the root node
            root = pivot;
            root.setParent(null);
        }
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
            }
        } else if (!toRemove.getRight().isLeaf()) {
            replacement = toRemove.getRight().getLeftmost();
            if (replacement == toRemove.getRight()) {
                oldRight = replacement.getRight();
            } else {
                replacement.getParent().setLeft(replacement.getRight());
            }
        } else {
            replacement = new Node(); // leaf
        }
        
        if (!replacement.isLeaf()) {
            replacement.setLeft(oldLeft);
            replacement.setRight(oldRight);
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
        return toArray(root, false);
    }
    
    private Integer[] toArray(Node n, boolean cloningOrder) {
        ArrayList<Integer> list = new ArrayList<>();
        Integer[] array;
        Iterator<Integer> iter;

        if (!n.isLeaf()) {
            if (cloningOrder) list.add(n.getValue());
            if (!n.getLeft().isLeaf())
                list.addAll(Arrays.asList(toArray(n.getLeft(), cloningOrder)));
            if (!cloningOrder) list.add(n.getValue());
            if (!n.getRight().isLeaf())
                list.addAll(Arrays.asList(toArray(n.getRight(), cloningOrder)));
        }
        
        array = new Integer[list.size()];
        iter = list.iterator();
        
        for (int i = 0; i < list.size(); i++) {
            array[i] = iter.next();
        }
        
        return array;
    }
    
    public RedBlackTree clone() {
        return new RedBlackTree(toArray(root, true));
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
