package com.yakovshklarov.branchy;

import java.util.Objects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

// TODO add docstrings for everything
// TODO this should extend a standard binary search tree...
public class RedBlackTree <T extends Comparable<T>> {
    private Node<T> root;

    private static final String ANSI_NORMAL = "\u001B[0m";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";
    
    public RedBlackTree() {
        root = new Node<T>();
    }
    
    public RedBlackTree(T[] values) {
        this();
        insert(values);
    }
    
    public void insert(T[] values) {
        for (T v: values) insert(v);
    }
    
    public void insert(T value) {
        int comp;
        Node<T> where, parent;
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
        where.makeRed();
        
        // Adjust the tree to remain red-black.
        insertCase1(where);
    }
    
    private void insertCase1(Node<T> n) {
        if (n == root)
            n.makeBlack();
        else
            insertCase2(n);
    }
    
    private void insertCase2(Node<T> n) {
        if (n.getParent().isBlack())
            return;
        else
            insertCase3(n);
    }
    
    private void insertCase3(Node<T> n) {
        Node<T> u, g;
        u = n.getUncle();
        
        if (u != null && u.isRed()) {
            n.getParent().makeBlack();
            u.makeBlack();
            g = n.getGrandparent();
            g.makeRed();
            insertCase1(g);
        } else {
            insertCase4(n);
        }
    }
    
    private void insertCase4(Node<T> n) {
        Node<T> g = n.getGrandparent();
        
        if (n == n.getParent().getRight() && n.getParent() == g.getLeft()) {
            rotateLeft(n.getParent());
            n = n.getLeft();
        } else if (n == n.getParent().getLeft() && n.getParent() == g.getRight()) {
            rotateRight(n.getParent());
            n = n.getRight();
        }
        
        insertCase5(n);
    }

    private void insertCase5(Node<T> n) {
        Node<T> g = n.getGrandparent();
        n.getParent().makeBlack();
        g.makeRed();
        if (n == n.getParent().getLeft()) {
            rotateRight(g);
        } else {
            rotateLeft(g);
        }
    }
    
    private void rotateLeft(Node<T> n) {
        Node<T> pivot, oldP;
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
    
    private void rotateRight(Node<T> n){
        Node<T> pivot, oldP;
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
    
    public void remove(Node<T> n) {
        Node<T> l, r, p, toRemove;
        
        if (n == null) return;

        l = n.getLeft();
        r = n.getRight();
        p = n.getParent();
        
        if (!l.isLeaf() && !r.isLeaf()) {
            toRemove = l.getRightmost();
            n.setValue(toRemove.getValue());
            removeOneChild(toRemove);
        } else {
            removeOneChild(n);
        }
    }
    
    // Call if n has no more than one non-leaf child.
    private void removeOneChild(Node<T> n) {
        Node<T> c;
        if (n.getRight().isLeaf())
            c = n.getLeft();
        else
            c = n.getRight();
        
        replaceWithChild(n, c);
        if (n.isBlack()) {
            if (c.isRed())
                c.makeBlack();
            else
                removeCase1(c);
        }
    }
    
    private void removeCase1(Node<T> n) {
        if (n != root)
            removeCase2(n);
    }

    private void removeCase2(Node<T> n) {
        Node<T> s = n.getSibling();
        Node<T> p = n.getParent();
 
        if (s.isRed()) {
            p.makeRed();
            s.makeBlack();
            if (n.isLeftChild())
                rotateLeft(p);
            else
                rotateRight(p);
        }
        removeCase3(n);
    }

    private void removeCase3(Node<T> n) {
        Node<T> s = n.getSibling();
        Node<T> p = n.getParent();
        
        if (p.isBlack() && s.isBlack() &&
            s.getLeft().isBlack() && s.getRight().isBlack()) {
            s.makeRed();
            removeCase1(p);
        } else {
            removeCase4(n);
        }
    }
    
    private void removeCase4(Node<T> n) {
        Node<T> s = n.getSibling();
        Node<T> p = n.getParent();
        
        if (p.isRed() && s.isBlack() &&
            s.getLeft().isBlack() && s.getRight().isBlack()) {
            s.makeRed();
            p.makeBlack();
        } else {
            removeCase5(n);
        }
    }

    private void removeCase5(Node<T> n) {
        Node<T> s = n.getSibling();
        Node<T> p = n.getParent();
        
        if (n.isLeftChild() && s.getRight().isBlack()) {
            s.makeRed();
            s.getLeft().makeBlack();
            rotateRight(s);
        } else if (n.isRightChild() && s.getLeft().isBlack()) {
            s.makeRed();
            s.getRight().makeBlack();
            rotateLeft(s);
        }
        removeCase6(n);
    }

    private void removeCase6(Node<T> n) {
        Node<T> s = n.getSibling();
        Node<T> p = n.getParent();
        
        s.setColor(p.getColor());
        p.makeBlack();
 
        if (n.isLeftChild()) {
            s.getRight().makeBlack();
            rotateLeft(p);
        } else {
            s.getLeft().makeBlack();
            rotateRight(p);
        }
    }
    
    private void replaceWithChild(Node<T> n, Node<T> c) {
        if (n.isRightChild()) {
            n.getParent().setRight(c);
        } else if (n.isLeftChild()) {
            n.getParent().setLeft(c);
        } else {  // (n == root)
            c.setParent(null);
            root = c;
        }
    }
    
    public Node<T> search(T value) {
        int comp;
        Node<T> where = root;
        
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
    
    public ArrayList<T> toArrayList() {
        return toArrayList(root);
    }
    
    private ArrayList<T> toArrayList(Node<T> n) {
        ArrayList<T> list = new ArrayList<>();

        if (!n.isLeaf()) {
            if (!n.getLeft().isLeaf())
                list.addAll(toArrayList(n.getLeft()));
            list.add(n.getValue());
            if (!n.getRight().isLeaf())
                list.addAll(toArrayList(n.getRight()));
        }
        
        return list;
    }
    
    // Return true if the tree's structure obeys the red-black rules and all
    // parent-child links are sane..
    public boolean verify() {
        return (root.getParent() == null)
            &&  root.isBlack() // Rule 2
            && (verify(root) >= 0);  // Check rules 1,3,4,5
    }
    
    // Returns -1 if tree is corrupt, otherwise returns the number of black
    // nodes in the path from n to any of its descendant leaves (inclusive).
    private int verify(Node<T> n) {
        Node<T> l, r;
        int lBlacks, rBlacks;
        
        if (n.isLeaf()) return n.isBlack() ? 1 : -1; // Rule 3
        
        l = n.getLeft();
        r = n.getRight();
        
        if (n.getColor() == null) return -1; // Rule 1
        
        if (n.isRed() && (l.isRed() || r.isRed())) return -1; // Rule 4
        
        // Check links
        if (n != l.getParent() || n != r.getParent()) return -1;
        
        lBlacks = verify(l);
        if (lBlacks == -1) return -1;
        rBlacks = verify(r);
        if (lBlacks != rBlacks) return -1;
        
        return lBlacks + (n.isBlack() ? 1 : 0);
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
    
    public String toString(Node<T> n, String prefix1, String prefix2) {
        String output;
        String nodeStr;
        String spacing;
        String rightConnector;
        
        output = prefix1 + ANSI_BOLD;
        if (n.isRed()) output += ANSI_RED;
        
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
