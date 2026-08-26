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
public class SemiDeterministicPancakeBovoSort extends BogoSorting {
    public SemiDeterministicPancakeBovoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Semi-Deterministic Pancake Bovo");
        this.setRunAllSortsName("Semi-Deterministic Pancake Bovo Sort");
        this.setRunSortName("Semi-Deterministic Pancake Bovosort");
        this.setCategory("Bogo Sorts");
        this.setAuthors("Flanlaina, PCBoy");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(13);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        for (int i = 1; i < sortLength; i++) {
            if (Reads.compareIndices(array, i - 1, i, delay, true) > 0) {
                int j = randInt(i, sortLength);
                Writes.reversal(array, 0, j-1, delay, true, false);
                Writes.reversal(array, 0, j, delay, true, false);
                i = 0;
            }
        }
    }
}
