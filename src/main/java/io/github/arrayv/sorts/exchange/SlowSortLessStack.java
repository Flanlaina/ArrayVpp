package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

// Code refactored from Python: http://wiki.c2.com/?SlowSort

public final class SlowSortLessStack extends Sort {
    public SlowSortLessStack(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Slow (Less Stack)");
        this.setRunAllSortsName("Slow Sort");
        this.setRunSortName("Slowsort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(150);
        this.setBogoSort(false);
    }

    private void slowSort(int[] A, int i, int j) {
        while (i < j) {
            int m = i + ((j - i) / 2);

            this.slowSort(A, i, m);
            this.slowSort(A, m + 1, j);
            
            //if (Reads.compareIndices(A, m, m + 1, 0, true) <= 0) break;

            if (Reads.compareIndices(A, m, j, 0, true) > 0) {
                Writes.swap(A, m, j, 1, true, false);
            }

            j--;
            // this.slowSort(A, i, j - 1);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.slowSort(array, 0, currentLength - 1);
    }
}
