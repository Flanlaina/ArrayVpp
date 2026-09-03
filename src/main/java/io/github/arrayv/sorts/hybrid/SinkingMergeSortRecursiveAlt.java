package io.github.arrayv.sorts.hybrid;

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
public class SinkingMergeSortRecursiveAlt extends Sort {

    public SinkingMergeSortRecursiveAlt(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Sinking Merge (Recursive, Alt)");
        this.setRunAllSortsName("Recursive Sinking Merge Sort");
        this.setRunSortName("Recursive Sinking Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setAuthors("Flanlaina");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void bubbleSort(int[] array, int start, int end, double sleep) {
        int consecSorted = 1;
        for (int i = end - 1; i > start; i -= consecSorted) {
            consecSorted = 1;
            for (int j = start; j < i; j++) {
                if (Reads.compareIndices(array, j, j + 1, sleep / 2.0, true) > 0) {
                    Writes.swap(array, j, j + 1, sleep, true, false);
                    consecSorted = 1;
                } else
                    consecSorted++;
            }
        }
    }

    public void sort(int[] array, int start, int end, double sleep) {
        if (end - start > 1) {
            int mid = start + (end - start) / 2;
            sort(array, start, mid, sleep);
            sort(array, mid, end, sleep);
            bubbleSort(array, start, end, sleep);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength, 0.25);

    }

}
