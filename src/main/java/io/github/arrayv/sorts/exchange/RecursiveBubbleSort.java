package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * @author Flanlaina
 *
 */
public class RecursiveBubbleSort extends Sort {
    public RecursiveBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Recursive Bubble");
        this.setRunAllSortsName("Recursive Bubble Sort");
        this.setRunSortName("Recursive Bubblesort");
        this.setCategory("Exchange Sorts");
        this.setAuthors("Flanlaina");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public boolean bubblePass(int[] array, int a, int b, double sleep, int depth) {
        Writes.recordDepth(depth++);
        if (b - a < 2) return false;
        int m = a + (b - a) / 2;
        Writes.recursion();
        boolean l = bubblePass(array, a, m, sleep, depth);
        Writes.recursion();
        boolean r = bubblePass(array, m, b, sleep, depth);
        boolean c = false;
        if (Reads.compareIndices(array, m - 1, m, sleep, true) > 0)
            Writes.swap(array, m - 1, m, sleep, c = true, false);
        return l || r || c;
    }

    public void bubbleSort(int[] array, int a, int b) {
        while (bubblePass(array, a, b, 0.125, 0));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        bubbleSort(array, 0, sortLength);
    }
}
