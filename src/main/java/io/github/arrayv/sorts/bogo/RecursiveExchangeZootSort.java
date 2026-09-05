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
 * @author gooflang
 * 
 */
public class RecursiveExchangeZootSort extends BogoSorting {
    public RecursiveExchangeZootSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Recursive Exchange Zoot");
        this.setRunAllSortsName("Recursive Exchange Zoot Sort");
        this.setRunSortName("Recursive Exchange Zootsort");
        this.setCategory("Bogo Sorts");
        this.setAuthors("Flanlaina, gooflang");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public boolean recIsSorted(int[] array, int a, int b, int depth) {
        Writes.recordDepth(depth++);
        if (b - a < 2) return true;
        int m = a + (b - a) / 2;
        boolean c = Reads.compareIndices(array, m - 1, m, this.delay, true) <= 0;
        Writes.recursion();
        c &= recIsSorted(array, a, m, depth);
        Writes.recursion();
        c &= recIsSorted(array, m, b, depth);
        return c;
    }

    protected boolean circle(int[] array, int left, int right) {
        int a = left;
        int b = right;
        boolean anyswap = false;
        while (a < b) {
            if (Reads.compareIndices(array, a, b, this.delay, true) > 0) {
                Writes.swap(array, a, b, this.delay, true, false);
                anyswap = true;
            }
            a++;
            b--;
        }
        return anyswap;
    }

    public void recShuffle(int[] array, int a, int b, int d) {
        if (b - a < 2) return;
        Writes.recordDepth(d++);
        int r1 = randInt(a, b);
        int r2 = randInt(a, b);
        if (r1 > r2) {
            int t = r1; r1 = r2; r2 = t;
        }
        if (Reads.compareIndices(array, r1, r2, this.delay, true) > 0) Writes.swap(array, r1, r2, this.delay, true, false);
        int m = (b - a) / 2;
        Writes.recursion();
        recShuffle(array, a, a+m, d);
        Writes.recursion();
        recShuffle(array, a + m, b, d);
    }

    public void recRev(int[] array, int a, int b, int d) {
        if (a >= b) return;
        Writes.recordDepth(d++);
        circle(array, a, b);
        int m = (b - a) / 2;
        Writes.recursion();
        recRev(array, a, a+m, d);
        Writes.recursion();
        recRev(array, b-m, b, d);
    }

    public void recZoot(int[] array, int a, int b) {
        while (!recIsSorted(array, a, b, 0)) {
            recShuffle(array, a, b, 0);
            recRev(array, a, b-1, 0);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        recZoot(array, 0, sortLength);
    }
}
