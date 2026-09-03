package io.github.arrayv.sorts.quick;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
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
 * To use this algorithm in another, use {@link #quickSort(int[], int, int)} from a reference instance.
 *
 * @author Flanlaina
 * @author gooflang
 *
 */
@SortMeta(
    name = "More Ternary Intro",
    category = "Quick Sorts",
    authors = "Flanlaina, gooflang"
)
public class MoreTernaryIntroSort extends Sort {
    public MoreTernaryIntroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    static final int SIZE_THRESHOLD = 32;

    public static int log2(int n) {
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    public int ternarySearch(int[] array, int a, int b, int val, double sleep) {
        while (a < b) {
            int third = (b - a) / 3;
            int midA = a + third, midB = midA + third;
            Highlights.markArray(2, midA);
            Highlights.markArray(3, midB);
            Delays.sleep(sleep);
            if (Reads.compareValues(val, array[midA]) < 0)
                b = midA;
            else if (Reads.compareValues(val, array[midB]) >= 0)
                a = midB + 1;
            else {
                a = midA + 1;
                b = midB;
            }
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        return a;
    }

    public void ternaryInsert(int[] array, int a, int b, double rSleep, double wSleep, boolean aux) {
        for (int i = a + 1; i < b; i++) {
            int current = array[i];
            int dest = ternarySearch(array, a, i, current, rSleep);
            int pos = i;
            while (pos > dest) {
                Writes.write(array, pos, array[pos - 1], wSleep, true, aux);
                pos--;
            }
            if (pos < i) Writes.write(array, pos, current, wSleep, true, aux);
        }
    }

    private void siftDown(int[] array, int val, int i, int p, int n) {
        while (3 * i + 1 < n) {
            int max = val;
            int next = i, child = 3 * i + 1;
            for (int j = child; j < Math.min(child + 3, n); j++) {
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

    protected void ternaryHeap(int[] array, int a, int b) {
        int n = b - a;
        for (int i = (n - 1) / 3; i >= 0; i--)
            this.siftDown(array, array[a + i], i, a, n);
        for (int i = n - 1; i > 0; i--) {
            Highlights.markArray(2, a + i);
            int t = array[a + i];
            Writes.write(array, a + i, array[a], 1, false, false);
            this.siftDown(array, t, 0, a, i);
        }
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

    // partition -> [a][E < piv][i][E == piv][j][E > piv][b]
    // returns -> i, j ^
    public int[] partition(int[] array, int a, int b, int piv) {
        // determines which elements do not need to be moved
        for (; a < b; a++) {
            Highlights.markArray(1, a);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[a], piv) >= 0) break;
        }
        for (; b > a; b--) {
            Highlights.markArray(1, b - 1);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[b - 1], piv) <= 0) break;
        }
        // int i1 = a, i = a - 1, j = b, j1 = b;
        int pa = a, pb = a, pc = b - 1, pd = b;
        int cmp;
        while (true) {
            while (pb <= pc) {
                cmp = Reads.compareIndexValue(array, pb, piv, 0.5, true);
                if (cmp > 0) break;
                if (cmp == 0) Writes.swap(array, pa++, pb, 1, true, false);
                pb++;
            }
            while (pb <= pc) {
                cmp = Reads.compareIndexValue(array, pc, piv, 0.5, true);
                if (cmp < 0) break;
                if (cmp == 0) Writes.swap(array, --pd, pc, 1, true, false);
                pc--;
            }
            //if (pb == pc) pc--;
            if (pb >= pc) {
                // pc = pb - 1
                if (pb > pc) pc++;
                // [ E == piv ][    E < piv    ][    E > piv    ][ E == piv ]
                // a           pa               pb, pc           pd          b
                if (pa - a > pb - pa) {
                    int i2 = pb;
                    pb = a;
                    while (pa < i2) Writes.swap(array, pb++, pa++, 1, true, false);
                } else while (pa > a) Writes.swap(array, --pb, --pa, 1, true, false);
                if (b - pd > pd - pc) {
                    int j2 = pc;
                    pc = b;
                    while (pd > j2) Writes.swap(array, --pc, --pd, 1, true, false);
                } else while (pd < b) Writes.swap(array, pc++, pd++, 1, true, false);
                return new int[] { pb, pc };
            }
            Writes.swap(array, pb++, pc--, 1, true, false);
        }
    }

    private void introsortLoop(int[] a, int lo, int hi, int depthLimit) {
        while (hi - lo > SIZE_THRESHOLD) {
            if (depthLimit == 0) {
                Highlights.clearAllMarks();
                ternaryHeap(a, lo, hi);
                return;
            }
            depthLimit--;
            int[] p = partition(a, lo, hi, a[medOf3(a, lo, lo + ((hi - lo) / 2), hi - 1)]);
            if (hi - p[1] < p[0] - lo) {
                introsortLoop(a, p[1], hi, depthLimit);
                hi = p[0];
            } else {
                introsortLoop(a, lo, p[0], depthLimit);
                lo = p[1];
            }
        }
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using More Ternary Introsort.
     * 
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void quickSort(int[] array, int a, int b) {
        int z = 0, e = 0;
        for (int i = a; i < b - 1; i++) {
            int cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            z += cmp > 0 ? 1 : 0;
            e += cmp == 0 ? 1 : 0;
        }
        if (z == 0) return;
        if (z + e == b - a - 1) {
            if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        introsortLoop(array, a, b, 2 * log2(b - a));
        Highlights.clearAllMarks();
        ternaryInsert(array, a, b, 1, 0.05, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        quickSort(array, 0, sortLength);
    }
}
