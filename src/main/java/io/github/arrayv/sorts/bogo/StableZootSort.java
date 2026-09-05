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
 * Stable Zootsort randomly shuffles the array and reverses the array, all
 * without changing the equal elements' order, until the array is sorted.
 * 
 * @author gooflang
 * @author Flanlaina
 */
public class StableZootSort extends BogoSorting {
    public StableZootSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stable Zoot");
        this.setRunAllSortsName("Stable Zoot Sort");
        this.setRunSortName("Stable Zootsort");
        this.setCategory("Bogo Sorts");
        this.setAuthors("Flanlaina, gooflang");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }

    public void stableShuffle(int[] array, int a, int b) {
        for (int j = a + 1; j < b; j++)
            for (int i = j, rIdx = randInt(a, j + 1); i > rIdx; i--)
                if (Reads.compareIndices(array, i - 1, i, this.delay, true) != 0)
                    Writes.swap(array, i - 1, i, this.delay, false, false);
    }

    public void stableReversal(int[] array, int a, int b) {
        for (int j = a + 1; j < b; j++)
            for (int i = j; i > a; i--)
                if (Reads.compareIndices(array, i - 1, i, this.delay, true) != 0)
                    Writes.swap(array, i - 1, i, this.delay, false, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        while (!isArraySorted(array, sortLength)) {
            stableShuffle(array, 0, sortLength);
            stableReversal(array, 0, sortLength);
        }
    }
}
