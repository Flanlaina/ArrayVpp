package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author aphitorite
 * @author Flanlaina
 * @author Potassium
 *
 */
public final class PopShellSort extends Sort {

    public PopShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pop Shell");
        this.setRunAllSortsName("Pop Shell Sort");
        this.setRunSortName("Pop Shellsort");
        this.setCategory("Insertion Sorts");
        this.setAuthors("aphitorite, Flanlaina, Potassium");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    void shellPass(int[] array, int a, int b, int gap, boolean fw) {
        int cmp = fw ? 1 : -1;
        for (int i = a + gap; i < b; i++) {
            int tmp = array[i];
            int j = i;
            while (j >= a + gap && Reads.compareValues(array[j - gap], tmp) * cmp > 0) {
                Highlights.markArray(2, j - gap);
                Writes.write(array, j, array[j - gap], 0.7, true, false);
                j -= gap;
            }
            if (j - gap >= a) Highlights.markArray(2, j - gap);
            else Highlights.clearMark(2);
            if (j != i) Writes.write(array, j, tmp, 0.7, true, false);
        }
    }
    
    /**
     * Sorts the range {@code [start, end)} of {@code array} using Shellsort.
     * 
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end   the end of the range, exclusive
     * @param fw    {@code true} to sort ascending, {@code false} to sort descending
     */
    public void shellSort(int[] array, int start, int end, boolean fw) {
        int gap = 1;
        while (gap < end - start) gap = (int) Math.ceil(gap * 2.36);
        gap /= 2.36;
        for (; gap >= 2; gap /= 2.36) shellPass(array, start, end, gap, fw);
        shellPass(array, start, end, 1, fw);
    }
    
    /**
     * Sorts the range {@code [start, end)} of {@code array} using Pop Shellsort.
     * 
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end   the end of the range, exclusive
     */
    public void popSort(int[] array, int start, int end) {
        int half = (end - start) / 2, quarter = half / 2;
        shellSort(array, start, start + quarter, false);
        shellSort(array, start + quarter, start + half, true);
        shellSort(array, start + half, start + half + quarter, false);
        shellSort(array, start + half + quarter, end, true);
        shellSort(array, start, start + half, false);
        shellSort(array, start + half, end, true);
        shellSort(array, start, end, true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        popSort(array, 0, sortLength);

    }

}
