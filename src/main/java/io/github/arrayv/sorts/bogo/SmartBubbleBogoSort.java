package io.github.arrayv.sorts.bogo;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

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
 * Smart Bubble Bogosort is Unoptimized Bubble Sort, but the order of
 * comparators within a pass is random for every pass.
 * 
 * @author Flanlaina
 */
public class SmartBubbleBogoSort extends BogoSorting {
    public SmartBubbleBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Smart Bubble Bogo");
        this.setRunAllSortsName("Smart Bubble Bogo Sort");
        this.setRunSortName("Smart Bubble Bogosort");
        this.setCategory("Bogo Sorts");
        this.setAuthors("Flanlaina");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void sort(int[] array, int a, int b) {
        int length = b - a;
        if (length < 2) return;
        this.delay = 0.0625;
        if (length == 2) {
            if (Reads.compareIndices(array, a, a + 1, delay, true) > 0)
                Writes.swap(array, a, a + 1, delay, true, false);
            return;
        }
        int[] indices = Writes.createExternalArray(length - 1);
        boolean change = true;
        while (change) {
            change = false;
            for (int i = 0; i < length - 1; i++) {
                int j = BogoSorting.randInt(0, i + 1);
                if (i == j) Writes.write(indices, i, i, delay, true, true);
                else {
                    Writes.write(indices, i, indices[j], delay, true, true);
                    Writes.write(indices, j, i, delay, true, true);
                }
            }
            for (int i = 0; i < length - 1; i++) {
                int idx = a + indices[i];
                if (Reads.compareIndices(array, idx, idx + 1, delay, true) > 0)
                    Writes.swap(array, idx, idx + 1, delay, change = true, false);
            }
            Highlights.clearMark(2);
        }
        Writes.deleteExternalArray(indices);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        sort(array, 0, sortLength);
    }
}
