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
 * @author Flanlaina
 *
 */
public class PancakeBovoSort extends BogoSorting {
    public PancakeBovoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pancake Bovo");
        this.setRunAllSortsName("Pancake Bovo Sort");
        this.setRunSortName("Pancake Bovosort");
        this.setCategory("Bogo Sorts");
        this.setAuthors("Flanlaina, PCBoy");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        while (!isArraySorted(array, sortLength)) {
            int i = randInt(1, sortLength);
            Writes.reversal(array, 0, i-1, delay, true, false);
            Writes.reversal(array, 0, i, delay, true, false);
        }
    }
}
