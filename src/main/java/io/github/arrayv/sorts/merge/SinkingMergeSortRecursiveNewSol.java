package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

Coded for ArrayV by Flanlaina

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 *
 */
public class SinkingMergeSortRecursiveNewSol extends Sort {
    public SinkingMergeSortRecursiveNewSol(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Sinking Merge (Recursive, New Solution)");
        this.setRunAllSortsName("Recursive Sinking Merge Sort");
        this.setRunSortName("Recursive Sinking Mergesort");
        this.setCategory("Merge Sorts");
        this.setAuthors("Flanlaina");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void bubbleMerge(int[] array, int start, int mid, int end) {
        int swap = end;
        while (swap > start && mid > start) {
            int lastSwap = start;
            for (int i = mid; i < swap; i++) {
                if (Reads.compareIndices(array, i - 1, i, 0.025, true) > 0) {
                    Writes.swap(array, i, i - 1, 0.025, true, false);
                    lastSwap = i;
                } else break;
            }
            swap = lastSwap;
            mid--;
        }
    }

    public void sort(int[] array, int a, int b, int depth) {
        Writes.recordDepth(depth);
        if (b - a < 2) return;
        int m = a + (b - a) / 2;
        Writes.recursion();
        sort(array, a, m, depth + 1);
        Writes.recursion();
        sort(array, m, b, depth + 1);
        bubbleMerge(array, a, m, b);
    }

    public void sort(int[] array, int a, int b) {
        sort(array, a, b, 0);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
