package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;

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
 * @author PCBoy
 *
 */
public class CircleOptimizedWeaveMergeSortFlanlaina extends Sort {
    public CircleOptimizedWeaveMergeSortFlanlaina(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Circle Optimized Weave Merge (Flanlaina)");
        this.setRunAllSortsName("Flanlaina's Circle Optimized Weave Merge Sort");
        this.setRunSortName("Flanlaina's Circle Optimized Weave Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setAuthors("Flanlaina, PCBoy");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public static int log2(int n) {
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    // pow of 2 only (O(n))
    private void bitReversal(int[] array, int a, int b) {
        int len = b - a, m = 0;
        int d1 = len >> 1, d2 = d1 + (d1 >> 1);

        for (int i = 1; i < len - 1; i++) {
            int j = d1;

            for (int k = i, n = d2; (k & 1) == 0; j -= n, k >>= 1, n >>= 1);

            m += j;
            if (m > i) Writes.swap(array, a + i, a + m, 1, true, false);
        }
    }

    void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    void weave(int[] array, int a, int b) { // precondition: b-a is even
        for (int e = b, f; e - a > 2; e = f) {
            int m = a + (e - a) / 2;
            int p = 1 << log2(m - a);

            this.rotate(array, m - p, m, e - p);
            m = e - p;
            f = m - p;

            this.bitReversal(array, f, m);
            this.bitReversal(array, m, e);
            this.bitReversal(array, f, e);
        }
    }

    protected boolean circlePass(int[] array, int a, int b) {
        boolean anySwaps = false;
        int n = 1 << (log2(b - a - 1) + 1);
        for (int g = n / 2; g > 0; g /= 2) {
            for (int s = a; s + g < b; s += 2 * g) {
                int i = s, j = s + 2 * g - 1;
                while (i < j) {
                    if (j < b && Reads.compareIndices(array, i, j, 0.25, true) > 0) {
                        Writes.swap(array, i, j, 0.25, true, false);
                        anySwaps = true;
                    }
                    i++;
                    j--;
                }
            }
        }
        return anySwaps;
    }

    //000111 -> 010101 T
    //00011  -> 01010  T
    //00111  -> 10101  F
    private void weaveMerge(int[] array, int a, int m, int b) {
        if (b - a < 2) return;
        if (Reads.compareIndices(array, m - 1, m, 0, false) <= 0) return;
        int a1 = a, b1 = b;

        if ((b - a) % 2 == 1) {
            if (m - a < b - m) a1--;
            else b1++;
        }
        weave(array, a1, b1);
        circlePass(array, a, b);
    }

    public void mergeSort(int[] array, int a, int b) {
        int n = b - a, d = 1 << (log2(n - 1) + 1);

        while (d > 1) {
            int i = a, dec = 0;

            while (i < b) {
                int j = i;
                j += (dec + n) / d;
                dec = (dec + n) % d;
                int k = j;
                k += (dec + n) / d;
                dec = (dec + n) % d;
                this.weaveMerge(array, i, j, k);
                i = k;
            }
            d /= 2;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        mergeSort(array, 0, sortLength);
    }
}
