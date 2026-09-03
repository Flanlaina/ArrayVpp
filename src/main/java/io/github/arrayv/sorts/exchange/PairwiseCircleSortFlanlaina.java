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
 * @author aphitorite
 *
 */
public class PairwiseCircleSortFlanlaina extends Sort {
    public PairwiseCircleSortFlanlaina(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pairwise-Circle (Flanlaina)");
        this.setRunAllSortsName("Flanlaina's Pairwise-Circle Sort");
        this.setRunSortName("Flanlaina's Pairwise-Circle Sort");
        this.setCategory("Exchange Sorts");
        this.setAuthors("aphitorite, Flanlaina");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public boolean pairwisePass(int[] array, int start, int end, double sleep) {
        if (end - start < 2) return false;
        int b = start + 1;
        boolean anySwap = false;
        while (b < end){
            if(Reads.compareIndices(array, b - 1, b, sleep, true) > 0)
                Writes.swap(array, b - 1, b, sleep, anySwap = true, false);
            b += 2;
        }
        int a = 1;
        while (a < (end - start)) a = (a * 2) + 1;
        b = start + 1;
        while (b + 1 < end){
            int c = a;
            while (c > 1){
                c /= 2;
                if (b + c < end){
                    if(Reads.compareIndices(array, b, b + c, sleep, true) > 0)
                        Writes.swap(array, b, b + c, sleep, anySwap = true, false);
                }
            }
            b += 2;
        }
        return anySwap;
    }

    protected boolean circlePass(int[] array, int a, int b, double sleep) {
        int sortLength = b - a;
        boolean anySwaps = false;
        int n = 1 << (32 - Integer.numberOfLeadingZeros(sortLength - 1));
        for (int g = n / 2; g > 0; g /= 2) {
            for (int s = a; s + g < b; s += 2 * g) {
                int i = s, j = s + 2 * g - 1;
                while (i < j) {
                    if (j < b && Reads.compareIndices(array, i, j, sleep, true) > 0) {
                        Writes.swap(array, i, j, sleep, true, false);
                        anySwaps = true;
                    }
                    i++;
                    j--;
                }
            }
        }
        return anySwaps;
    }

    public void sort(int[] array, int a, int b) {
        while (pairwisePass(array, a, b, 0.5)) {
            if (!circlePass(array, a, b, 0.5)) break;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
