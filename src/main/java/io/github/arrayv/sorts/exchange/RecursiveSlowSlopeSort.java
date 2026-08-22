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
public class RecursiveSlowSlopeSort extends Sort {
    public RecursiveSlowSlopeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Recursive Slow Slope");
        this.setRunAllSortsName("Recursive Slow Slope Sort");
        this.setRunSortName("Recursive Slow Slopesort");
        this.setCategory("Impractical Sorts");
        this.setAuthors("Flanlaina");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32);
        this.setBogoSort(false);
    }

    public void sort(int[] array, int a, int b, int depth) {
        Writes.recordDepth(depth++);
        if (b - a < 2) return;
        Writes.recursion();
        sort(array, a, b - 1, depth);
        if (Reads.compareIndices(array, b - 2, b - 1, 0.5, true) > 0) {
            Writes.swap(array, b - 2, b - 1, 0.5, true, false);
        }
        Writes.recursion();
        sort(array, a, b - 1, depth);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        sort(array, 0, sortLength, 0);
    }
}
