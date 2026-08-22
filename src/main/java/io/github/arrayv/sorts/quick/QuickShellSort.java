package io.github.arrayv.sorts.quick;

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
 *
 */
public class QuickShellSort extends Sort {

    public QuickShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Quick Shell");
        this.setRunAllSortsName("Quick Shell Sort");
        this.setRunSortName("Quick Shellsort");
        this.setCategory("Quick Sorts");
        this.setAuthors("Flanlaina");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public static int log2(int n) {
        return 31 - Integer.numberOfLeadingZeros(n);
    }
    
    static int incs[] = {48, 21, 7, 3, 1};
    
    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b) Writes.swap(array, a, b, pause, mark, aux);
    }

    protected void shellSort(int[] array, int lo, int hi) {
        Highlights.clearAllMarks();
        for (int k = 0; k < incs.length; k++) {
            for (int h = incs[k], i = h + lo; i < hi; i++) {
                int v = array[i];
                int j = i;
                while (j >= h + lo && Reads.compareValues(array[j - h], v) == 1) {
                    Highlights.markArray(1, j);
                    Writes.write(array, j, array[j - h], 0.75, true, false);
                    j -= h;
                }
                if (j != i) Writes.write(array, j, v, 0.75, true, false);
            }
        }
        Highlights.clearAllMarks();
    }
    
    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if(Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if(Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if(Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }

    public int ninther(int[] array, int a, int b) {
        if (b - a <= 9) return a + (b - a) / 2;
        int len = b - a, half = len / 2, quart = len / 4, eight = len / 8;
        int c = medOf3(array, a, a + eight, a + quart);
        int d = medOf3(array, a + quart + eight, a + half, a + half + eight);
        int e = medOf3(array, b - quart, b - eight, b - 1);
        return medOf3(array, c, d, e);
    }

    // Median of 3 ninthers
    public int pseudomo27(int[] array, int a, int b) {
        if (b - a < 64) return this.ninther(array, a, b);
        int d = (b - a + 1) / 8;
        int m0 = this.ninther(array, a, a + 2 * d);
        int m1 = this.ninther(array, a + 3 * d, a + 5 * d);
        int m2 = this.ninther(array, a + 6 * d, b);
        return this.medOf3(array, m0, m1, m2);
    }

    // Ninther of 9 ninthers
    public int pseudomo81(int[] array, int a, int b) {
        if (b - a < 256) return this.pseudomo27(array, a, b);
        int d = (b - a + 1) / 24;
        int m0 = this.ninther(array, a, a + 2 * d);
        int m1 = this.ninther(array, a + 3 * d, a + 5 * d);
        int m2 = this.ninther(array, a + 6 * d, a + 8 * d);
        int m3 = this.ninther(array, a + 9 * d, a + 11 * d);
        int m4 = this.ninther(array, a + 12 * d, a + 14 * d);
        int m5 = this.ninther(array, a + 15 * d, a + 17 * d);
        int m6 = this.ninther(array, a + 18 * d, a + 20 * d);
        int m7 = this.ninther(array, a + 19 * d, a + 21 * d);
        int m8 = this.ninther(array, a + 22 * d, b);
        return this.medOf3(array, this.medOf3(array, m0, m1, m2), this.medOf3(array, m3, m4, m5),
                this.medOf3(array, m6, m7, m8));
    }

    // Ninther of 9 medians of 3 ninthers
    public int pseudomo243(int[] array, int a, int b) {
        if (b - a < 16384) return this.pseudomo81(array, a, b);
        int d = (b - a + 1) / 24;
        int m0 = this.pseudomo27(array, a, a + 2 * d);
        int m1 = this.pseudomo27(array, a + 3 * d, a + 5 * d);
        int m2 = this.pseudomo27(array, a + 6 * d, a + 8 * d);
        int m3 = this.pseudomo27(array, a + 9 * d, a + 11 * d);
        int m4 = this.pseudomo27(array, a + 12 * d, a + 14 * d);
        int m5 = this.pseudomo27(array, a + 15 * d, a + 17 * d);
        int m6 = this.pseudomo27(array, a + 18 * d, a + 20 * d);
        int m7 = this.pseudomo27(array, a + 19 * d, a + 21 * d);
        int m8 = this.pseudomo27(array, a + 22 * d, b);
        return this.medOf3(array, this.medOf3(array, m0, m1, m2), this.medOf3(array, m3, m4, m5),
                this.medOf3(array, m6, m7, m8));
    }

    private void siftDown(int[] array, int val, int i, int p, int n) {
        while (4 * i + 1 < n) {
            int max = val;
            int next = i, child = 4 * i + 1;
            for (int j = child; j < Math.min(child + 4, n); j++) {
                if (Reads.compareValues(array[p + j], max) > 0) {
                    max = array[p + j];
                    next = j;
                }
            }
            if (next == i) break;
            Writes.write(array, p + i, max, 1, true, false);
            i = next;
        }
        Writes.write(array, p + i, val, 1, true, false);
    }

    protected void heapSort(int[] array, int a, int b) {
        int n = b - a;
        for (int i = (n - 1) / 4; i >= 0; i--)
            this.siftDown(array, array[a + i], i, a, n);
        for (int i = n - 1; i > 0; i--) {
            Highlights.markArray(2, a + i);
            int t = array[a + i];
            Writes.write(array, a + i, array[a], 1, false, false);
            this.siftDown(array, t, 0, a, i);
        }
    }

    public int partition(int[] array, int a, int b, int pivIdx) {
        swap(array, a, pivIdx, 1, true, false);
        int i = a, j = b;
        Highlights.markArray(3, a);
        do {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            } while(i < j && Reads.compareValues(array[i], array[a]) < 0);
            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            } while(j >= i && Reads.compareValues(array[j], array[a]) > 0);
            if (i < j) Writes.swap(array, i, j, 1, false, false);
            else {
                Highlights.clearMark(3);
                swap(array, a, j, 1, true, false);
                return j;
            }
        } while(true);
    }
    
    void sortHelper(int[] array, int a, int b, int depthLim) {
        while (b - a > 64) {
            if (depthLim == 0) {
                heapSort(array, a, b);
                return;
            }
            depthLim--;
            int p = partition(array, a, b, pseudomo243(array, a, b));
            int lSize = p - a, rSize = b - (p + 1);
            if (rSize < lSize) {
                sortHelper(array, p + 1, b, depthLim);
                b = p;
            } else {
                sortHelper(array, a, p, depthLim);
                a = p + 1;
            }
        }
    }
    
    public void quickShellSort(int[] array, int a, int b) {
        sortHelper(array, a, b, 2 * log2(b - a));
        shellSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickShellSort(array, 0, sortLength);
    }
}
