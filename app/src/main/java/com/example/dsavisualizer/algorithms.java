package com.example.dsavisualizer;

import java.util.ArrayList;

public final class algorithms {
    private final ArrayList<helper> searchAlg = new ArrayList<>();
    private final ArrayList<helper> sortAlg = new ArrayList<>();
    private final ArrayList<helper> pathFindingAlg = new ArrayList<>();
    private final ArrayList<helper> graphAlg = new ArrayList<>();

    public algorithms() {

        //searching algorithms
        helper helper = new helper("Linear Search", "O(n)", "O(1)", "O(n)", "A linear search or sequential search is a method for finding an element within a list. It sequentially checks each element of the list until a match is found or the whole list has been searched.");
        helper helper1 = new helper("Binary Search", "O(log n)", "O(1)", "O(log n)", "A  binary search, also known as half-interval search, logarithmic search, or binary chop, is a search algorithm that finds the position of a target value within a sorted array. Binary search compares the target value to the middle element of the array.");
        helper helper2 = new helper("Jump Search", "O(n)", "O(√n)", "O(√n)", "Jump search technique also works for ordered lists. It creates a block and tries to find the element in that block. If the item is not in the block, it shifts the entire block. The block size is based on the size of the list. If the size of the list is n then block size will be √n. After finding a correct block it finds the item using a linear search technique. The jump search lies between linear search and binary search according to its performance.");
        helper helper3 = new helper("Interpolation Search", "O(n)", "O(1)", "O(log log n)", "Interpolation search is an improved variant of binary search. This search algorithm works on the probing position of the required value. For this algorithm to work properly, the data collection should be in a sorted form and equally distributed.");
        helper helper4 = new helper("Exponential Search", "O(log n)", "O(1)", "O(log n)", "Exponential search is also known as doubling or galloping search. This mechanism is used to find the range where the search key may present. If L and U are the upper and lower bound of the list, then L and U both are the power of 2. For the last section, the U is the last position of the list. For that reason, it is known as exponential.\n" +
                "\n" +
                "After finding the specific range, it uses the binary search technique to find the exact location of the search key.");
        searchAlg.add(helper);
        searchAlg.add(helper1);
        searchAlg.add(helper2);
        searchAlg.add(helper3);
        searchAlg.add(helper4);

        //sorting algorithms
        helper helper5 = new helper("Selection sort", "O(n²)", "O(n²)", "O(n²)", "selection sort is an in-place comparison sorting algorithm. It has an O(n²) time complexity, which makes it inefficient on large lists, and generally performs worse than the similar insertion sort.");
        helper helper6 = new helper("Bubble sort", "O(n²)", "O(n)", "O(n²)", "Bubble sort, sometimes referred to as sinking sort, is a simple sorting algorithm that repeatedly steps through the input list element by element, comparing the current element with the one after it, swapping their values if needed.");
        helper helper7 = new helper("Insertion sort", "O(n²)", "O(n)", "O(n²)", "Insertion sort is a simple sorting algorithm that builds the final sorted array one item at a time by comparisons. It is much less efficient on large lists than more advanced algorithms such as quicksort, heapsort, or merge sort");
        helper helper8 = new helper("Merge sort", "O(n log n)", "O(n log n)", "O(n log n)", "Merge sort is a sorting algorithm that works by dividing an array into smaller subarrays, sorting each subarray, and then merging the sorted subarrays back together to form the final sorted array.");
        helper helper9 = new helper("Quick sort", "O(n²)", "O(n log n)", "O(n log n)", "Quicksort picks an element as pivot, and then it partitions the given array around the picked pivot element. In quick sort, a large array is divided into two arrays in which one holds values that are smaller than the specified value (Pivot), and another array holds the values that are greater than the pivot.\n" +
                "\n" +
                "After that, left and right sub-arrays are also partitioned using the same approach. It will continue until the single element remains in the sub-array.");
        sortAlg.add(helper5);
        sortAlg.add(helper6);
        sortAlg.add(helper7);
        sortAlg.add(helper8);
        sortAlg.add(helper9);

        //Path finding algorithms

        helper helper10 = new helper("Breadth-First Search", "O(n²)", "O(n²)", "O(n²)", "");
        helper helper11 = new helper("Depth-First Search", "O(n²)", "O(n)", "O(n²)", "");
        helper helper12 = new helper("Greedy", "O(n²)", "O(n)", "O(n²)", "");
        helper helper13 = new helper("Shortest Path", "O(n²)", "O(n²)", "O(n²)", "");
        helper helper14 = new helper("Dijkstra's Algorithm", "O(n²)", "O(n)", "O(n²)", "");
        helper helper15 = new helper("A*", "O(n²)", "O(n)", "O(n²)", "");
        pathFindingAlg.add(helper10);
        pathFindingAlg.add(helper11);
        pathFindingAlg.add(helper12);
        pathFindingAlg.add(helper13);
        pathFindingAlg.add(helper14);
        pathFindingAlg.add(helper15);

    }



    public ArrayList<helper> getSearchAlg() {
        return searchAlg;
    }

    public ArrayList<helper> getSortAlg() {
        return sortAlg;
    }

    public ArrayList<helper> getPathFindingAlg() {
        return pathFindingAlg;
    }

    public ArrayList<helper> getGraphAlg() {
        return graphAlg;
    }
}
