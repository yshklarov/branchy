package com.yakovshklarov.branchy;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class TestBranchy {
    public static void main (String[] args) {
        System.out.println("Testing branchy....");
        System.out.print("Creating a new tree. ");
        
        String[] strArr = new String[] {"aardvark", "barn owl", "cheetah",
                                        "dingo", "earwig", "falcon", "gerbil",
                                        "heron", "jaguar", "koala", "loris",
                                        "manatee", "narwhal", "opossum",
                                        "pelican", "quail", "raccoon", "shrew",
                                        "trout", "uakari", "viper", "walrus",
                                        "x-ray tetra", "yak", "zebra"};
        List<String> contents = Arrays.asList(strArr);
        Collections.shuffle(contents);
        
        RedBlackTree<String> testTree = new RedBlackTree<>
            (contents.toArray(new String[0]));
        System.out.println("Displaying the tree: ");
        System.out.println(testTree);
        
        int size = testTree.size();
        System.out.printf("The tree has %d node", testTree.size());
        if (size != 1) System.out.printf("s");
        System.out.println(".");
        
        System.out.printf("Converting to ArrayList. ArrayList has %d elements:\n",
                          testTree.toArrayList().size());
        System.out.println(testTree.toArrayList());
        
        Collections.shuffle(contents);
        String[] toRemove = contents.toArray(new String[0]);
        
        for (String n: toRemove) {
            System.out.println("Removing " + n + ": ");
            Node<String> nNode = testTree.search(n);
            if (nNode == null)
                System.err.println("ERROR: " + n + " is not in the tree!");
            else
                testTree.remove(nNode);
            if (!testTree.verify())
                System.err.println("ERROR: corrupt tree.");
            System.out.println(testTree);
        }
    }
    
    private static void printArray(Object[] arr) {
        System.out.print("[ ");
        for (Object elem: arr)
            System.out.print(elem + " ");
        System.out.println("]");
    }

}
