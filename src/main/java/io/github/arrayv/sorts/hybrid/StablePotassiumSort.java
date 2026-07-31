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
 * @author Ayako-chan
 * @author Potassium
 *
 */
public final class StablePotassiumSort extends Sort {

    public StablePotassiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Potassium");
        this.setRunAllSortsName("Stable Potassium Sort");
        this.setRunSortName("Stable Potassiumsort");
        this.setCategory("Hybrid Sorts");
        this.setAuthors("Flanlaina, Potassium");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0))
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected int expSearch(int[] array, int a, int b, int val, boolean dir, boolean left) {
        int i = 1;
        int a1, b1;
        if (dir) {
            if (left)
                while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0)
                    i *= 2;
            else
                while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
                    i *= 2;
            a1 = a + i / 2;
            b1 = Math.min(b, a - 1 + i);
        } else {
            if (left)
                while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
                    i *= 2;
            else
                while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
                    i *= 2;
            a1 = Math.max(a, b - i + 1);
            b1 = b - i / 2;
        }
        return binSearch(array, a1, b1, val, left);
    }

    protected void insertSort(int[] array, int a, int b) {
        int i = a + 1;
        if (i >= b) return;
        if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
            Writes.reversal(array, a, i - 1, 1.0, true, false);
        } else while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        for (; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i], false, false));
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = expSearch(array, m, b, array[a], true, true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m >= b) break;
            a = expSearch(array, a, m, array[m], true, false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = expSearch(array, a, m, array[b - 1], false, false);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = expSearch(array, m, b, array[m - 1], false, true);
        }
    }

    protected boolean merge(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return false;
        a = expSearch(array, a, m, array[m], true, false);
        b = expSearch(array, m, b, array[m - 1], false, true);
        if (m - a > b - m)
            inPlaceMergeBW(array, a, m, b);
        else
            inPlaceMergeFW(array, a, m, b);
        return true;
    }

    protected boolean diamondMerge(int[] array, int a, int m, int b, int block) {
        if (a >= m || m >= b) return true; // ???
        if (Math.min(m - a, b - m) <= block) return merge(array, a, m, b);
        int q = (Math.min(m - a, b - m) - 1) / 2 + 1;
        if (diamondMerge(array, m - q, m, m + q, block)) {
            diamondMerge(array, a, m - q, m, block);
            diamondMerge(array, m, m + q, b, block);
            diamondMerge(array, a + q, m, b - q, block);
            return true;
        }
        return false;
    }

    protected void stoogeSort(int[] array, int a, int b, int block) {
        if (b - a <= block) {
            insertSort(array, a, b);
            return;
        }
        int m = a + (b - a) / 2;
        stoogeSort(array, a, m, block);
        stoogeSort(array, m, b, block);
        diamondMerge(array, a, m, b, block);
    }

    protected int[] partition(int[] array, int a, int b, int piv) {
        if (b - a < 2) {
            int[] court = new int[] { a, a };
            int cmp = Reads.compareValues(array[a], piv);
            if (cmp < 0) {
                court[0]++;
                court[1]++;
            } else if (cmp == 0) court[1]++;
            return court;
        }
        int m = a + (b - a) / 2;
        int[] l = partition(array, a, m, piv);
        int[] r = partition(array, m, b, piv);
        int l1 = l[0] - a, l2 = l[1] - l[0];
        int r1 = r[0] - m, r2 = r[1] - r[0];
        rotate(array, a + l1, m, m + r1);
        rotate(array, a + l1 + l2 + r1, m + r1, m + r1 + r2);
        return new int[] { a + l1 + r1, a + l1 + r1 + l2 + r2 };
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if(Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else
            tmp = i0;
        if(Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if(Reads.compareIndices(array, tmp, i2, 1, true) > 0)
                return tmp;
            return i2;
        }
        return i1;
    }
    
    public int medP3(int[] array, int a, int b) {
        if (b - a == 3) return medOf3(array, a, a + (b - a) / 2, b - 1);
        if (b - a < 3) return a + (b - a) / 2;

        int t = (b - a) / 3;
        int l = medP3(array, a, a + t),
            c = medP3(array, a + t, b - t),
            r = medP3(array, b - t, b);
        // median
        return medOf3(array, l, c, r);
    }

    public int medOfMed(int[] array, int a, int b) {
        if (b - a <= 6) return a + (b - a) / 2;

        int p = 1;
        while (6 * p < b - a) p *= 3;
        int l = medP3(array, a, a + p),
            c = medOfMed(array, a + p, b - p),
            r = medP3(array, b - p, b);
        // median
        return medOf3(array, l, c, r);
    }

    public boolean getSortedRuns(int[] array, int a, int b) {
        Highlights.clearAllMarks();
        boolean reverseSorted = true;
        boolean sorted = true;
        int comp;

        for (int i = a; i < b - 1; i++) {
            comp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            if (comp > 0)
                sorted = false;
            else
                reverseSorted = false;
            if ((!reverseSorted) && (!sorted))
                return false;
        }

        if (reverseSorted && !sorted) {
            Writes.reversal(array, a, b - 1, 1, true, false);
            sorted = true;
        }

        return sorted;
    }

    protected void sortHelper(int[] array, int a, int b, int threshold, int cbrt) {
        while (b - a > threshold) {
            if (getSortedRuns(array, a, b))
                return;
            int pIdx = medOfMed(array, a, b);
            int[] p = partition(array, a, b, array[pIdx]);
            if (p[1] - p[0] == b - a)
                return;
            if (b - p[1] < p[0] - a) {
                sortHelper(array, p[1], b, threshold, cbrt);
                b = p[0];
            } else {
                sortHelper(array, a, p[0], threshold, cbrt);
                a = p[1];
            }
        }
        stoogeSort(array, a, b, cbrt);
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using Potassium Sort.
     * 
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void sort(int[] array, int a, int b) {
        int s = 1;
        while (s * s * s < b - a) s++;
        sortHelper(array, a, b, s*s, s);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
