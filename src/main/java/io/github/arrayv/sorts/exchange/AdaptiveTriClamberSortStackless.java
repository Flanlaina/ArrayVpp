package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

Coded for ArrayV by Flanlaina
extending code by PCBoy and Amari (thatsOven)

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author PCBoy
 * @author Amari (thatsOven)
 * 
 */
public class AdaptiveTriClamberSortStackless extends Sort {
    public AdaptiveTriClamberSortStackless(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive TriSearch Clamber (Stackless)");
        this.setRunAllSortsName("Stackless Adaptive TriSearch Clamber Sort");
        this.setRunSortName("Stackless Adaptive TriSearch Clambersort");
        this.setCategory("Exchange Sorts");
        this.setAuthors("Flanlaina, PCBoy, thatsOven");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int triSearch(int[] array, int a, int b, int val, double sleep) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(1, a);
            Highlights.markArray(2, m);
            Highlights.markArray(3, b - 1);
            Delays.sleep(sleep);
            if (Reads.compareValues(val, array[a]) < 0) break;
            if (Reads.compareValues(val, array[b - 1]) >= 0) {
                Highlights.clearAllMarks();
                return b;
            }
            if (Reads.compareValues(val, array[m]) < 0) {
                a = a + 1;
                b = m;
            } else {
                a = m + 1;
                b = b - 1;
            }
        }
        Highlights.clearAllMarks();
        return a;
    }

    public void sort(int[] array, int a, int b, double sleep) {
        for (int right = a + 1; right < b; right++) {
            if (Reads.compareIndices(array, right - 1, right, sleep, true) > 0) {
                int left = triSearch(array, a, right - 1, array[right], sleep);
                while (left < right) Writes.swap(array, left++, right, sleep * 0.2, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        sort(array, 0, currentLength, 1);
    }
}
