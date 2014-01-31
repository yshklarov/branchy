package com.yakovshklarov.branchy;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class TestBranchy {
    public static void main (String[] args) {
        System.out.println("Testing branchy....");

        System.out.print("Creating a new tree. ");
        
        List<Integer> contents = new ArrayList<Integer>();
        for (int i = 1; i <= 15; i++) {
            contents.add(i);
        }
        Collections.shuffle(contents);
        
        RedBlackTree testTree = new RedBlackTree(contents.toArray(new Integer[0]));
        System.out.println("Displaying the tree: ");
        System.out.println(testTree);
        
        int size = testTree.size();
        System.out.printf("The tree has %d node", testTree.size());
        if (size != 1) System.out.printf("s");
        System.out.println(".");
        
        System.out.print("Converting to array: ");
        printArray(testTree.toArray());
        
        RedBlackTree testTreeClone = testTree.clone();
        
        Collections.shuffle(contents);
        Integer[] toRemove = contents.toArray(new Integer[0]);
        
        for (Integer n: toRemove) {
            System.out.println("Removing " + n + ": ");
            if (testTree.remove(n) == null)
                System.out.println("ERROR: " + n + " is not in the tree!");
            System.out.println(testTree);
        }
        
        System.out.println("Original (cloned) tree: ");
        System.out.println(testTreeClone);
    }
    
    private static void printArray(Object[] arr) {
        System.out.print("[ ");
        for (Object elem: arr)
            System.out.print(elem + " ");
        System.out.println("]");
    }

}
