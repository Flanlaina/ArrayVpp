package io.github.arrayv.sorts.merge;

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
 * A variant of Rotate Merge Sort that uses O(1) stack space and has O(n) best
 * case.
 * <p>
 * To use this algorithm in another, use {@link #mergeSort(int[], int, int)}
 * from a reference instance.
 * 
 * @author Flanlaina
 * @author Distray
 *
 */
public class AdaptiveStacklessRotateMergeSort extends Sort {
    public AdaptiveStacklessRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Adaptive Stackless Rotate Merge");
        this.setRunAllSortsName("Adaptive Stackless Rotate Merge Sort");
        this.setRunSortName("Adaptive Stackless Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setAuthors("Flanlaina, Distray");
        this.setConstant("n log^2 n");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    /**
     * Tuning parameter: size of the smaller partition for rotate merging at or
     * below which Lazy Stable merging will be used in preference to Rotate Merge
     * merging.
     */
    private static final int SMALL_MERGE_THRESHOLD = 4;

    public void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    protected int binSearch(int[] array, int a, int b, int k, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            int c = Reads.compareIndices(array, k, m, 0.25, true);
            if (c < 0 || (left && c == 0)) b = m;
            else a = m + 1;
        }
        return a;
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = binSearch(array, m, b, a, true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m >= b) break;
            a = binSearch(array, a, m, m, false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = binSearch(array, a, m, b - 1, false);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = binSearch(array, m, b, m - 1, true);
        }
    }

    /**
     * Does a single round of the partitioning in Rotate Partition Merge Sort.
     * 
     * @param array the array
     * @param a     the start of the first range, inclusive
     * @param m     the start of the second range, inclusive. Since the ranges are
     *              consecutive, it is also the end of the first range, exclusive.
     * @param b     the end of the second range, exclusive
     * @return An {@code int[]} with two values:
     *         <ul>
     *         <li>{@code [0]}: the end point of the first segment after the
     *         partition, exclusive.</li>
     *         <li>{@code [1]}: {@code 0} if we have to continue partitioning, {@code 1} otherwise.</li>
     *         </ul>
     *         </li>
     *         </ul>
     */
    public int[] partitionMerge(int[] array, int a, int m, int b) {
        int lenA = m - a, lenB = b - m;
        if (lenA < 1 || lenB < 1) return new int[] { a, 1 };
        if (Math.min(lenA, lenB) <= SMALL_MERGE_THRESHOLD) {
            if (lenB < lenA) inPlaceMergeBW(array, a, m, b);
            else inPlaceMergeFW(array, a, m, b);
            return new int[] { a, 1 };
        }
        int c = (lenA + lenB) / 2;
        int first;
        if (lenB < lenA) { // partitions c largest elements
            int r1 = 0, r2 = lenB;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareIndices(array, m - (c - ml), b - ml - 1, 0.25, true) > 0) r2 = ml;
                else r1 = ml + 1;
            }
            // [lenA-(c-r1)][c-r1][lenB-r1][r1]
            // [lenA-(c-r1)][lenB-r1][c-r1][r1]
            this.rotate(array, m - (c - r1), m, b - r1);
            first = m - (c - r1);
        } else { // partitions c smallest elements
            int r1 = 0, r2 = lenA;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareIndices(array, a + ml, m + (c - ml) - 1, 0.25, true) > 0) r2 = ml;
                else r1 = ml + 1;
            }
            // [r1][lenA-r1][c-r1][lenB-(c-r1)]
            // [r1][c-r1][lenA-r1][lenB-(c-r1)]
            this.rotate(array, a + r1, m, m + (c - r1));
            first = a + r1;
        }
        return new int[] { first, 0 };
    }

    /**
     * Merges the sorted range {@code [a, m)} with the sorted range {@code [m, b)},
     * putting the result into the combined sorted range {@code [a, b)}. This method
     * is stable (i.e. it preserves the relative order of equal elements).
     * 
     * @param array the array
     * @param a     the start of the first range, inclusive
     * @param m     the start of the second range, inclusive. Since the ranges are
     *              consecutive, it is also the end of the first range, exclusive.
     * @param b     the end of the second range, exclusive
     */
    public void merge(int[] array, int a, int m, int b) {
        if (a == m || m == b) return;
        if (Reads.compareIndices(array, m-1, m, 0.5, true) <= 0) return;
        if (Math.min(m - a, b - m) <= SMALL_MERGE_THRESHOLD) {
            if (b - m < m - a) inPlaceMergeBW(array, a, m, b);
            else inPlaceMergeFW(array, a, m, b);
            return;
        }
        int j, k, l, /* s, */ q = b - a, r, c;
        boolean done = false;
        while (!done) {
            done = true;
            //s = 0;
            r = 0;
            j = k = a;
            c = m == a ? 1 : 0;
            for (int i = m + c; i < b; i++) {
                if (c == 0 || Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) {
                    l = i;
                    k = binSearch(array, l, l + Math.min(q, b - l), l - 1, true);
                    j = binSearch(array, j, l, l, false);
                    int lenA = l-j, lenB = k-l, t = Math.max(lenA, lenB);
                    int o;
                    if (r < t) r = t;
                    //int h = Math.min(lenA, lenB);
                    //if (h > s) s = h;
                    int[] f = partitionMerge(array, j, l, k);
                    if (f[1] == 0) done = false;
                    o = f[0];
                    j = k;
                    i = k - 1;
                    if (c++ == 0) m = o;
                }
            }
            q = r;
        }
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using Adaptive Stackless Rotate Merge Sort.
     * 
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void mergeSort(int[] array, int a, int b) {
        for (int j = 1; j < b - a; j *= 2)
            for (int i = a; i + j < b; i += 2 * j)
                merge(array, i, i + j, Math.min(i + 2 * j, b));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);
    }
}
