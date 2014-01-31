package com.yakovshklarov.branchy;

public class TestBranchy {
    public static void main (String[] args) {
        System.out.println("Testing branchy....");
        //Node n = new Node(Color.RED, 42);
        //n.setRight(new Node(Color.BLACK, 0));
        System.out.print("Creating a new tree. ");
        RedBlackTree testTree = new RedBlackTree(new Integer[]{5, 3, 1, 4, 7, 6, 8, 2});
        System.out.println("Displaying the tree: ");
        System.out.println(testTree);
        
        int size = testTree.size();
        System.out.printf("The tree has %d node", testTree.size());
        if (size != 1) System.out.printf("s");
        System.out.println(".");
        
        System.out.print("Converting to array: ");
        printArray(testTree.toArray());
        
        RedBlackTree testTreeClone = testTree.clone();
        Integer[] toRemove = new Integer[] {7,6,1,3,8,2,4,5};
        
        for (Integer n: toRemove) {
            System.out.println("Removing " + n + ": ");
            testTree.remove(n);
            System.out.println(testTree);
        }
        
        System.out.println("Original (cloned) tree: ");
        System.out.println(testTreeClone);
    }
    
    private static void printArray(Integer[] arr) {
        System.out.print("[ ");
        for (Integer elem: arr)
            System.out.print(elem + " ");
        System.out.println("]");
    }

}
