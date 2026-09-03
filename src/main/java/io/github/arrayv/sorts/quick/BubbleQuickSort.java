package io.github.arrayv.sorts.quick;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

Coded for ArrayV by Flanlaina
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author PCBoy
 *
 */
public class BubbleQuickSort extends Sort {

    public BubbleQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bubble Quick");
        this.setRunAllSortsName("Bubble Quick Sort");
        this.setRunSortName("Bubble Quicksort");
        this.setCategory("Quick Sorts");
        this.setAuthors("Flanlaina, PCBoy");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int t;
        if (Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            t = i1;
            i1 = i0;
        } else t = i0;
        if (Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if (Reads.compareIndices(array, t, i2, 1, true) > 0) return t;
            return i2;
        }
        return i1;
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
    
    protected int pivCmpHelper(int v, int piv) {
        int c = Reads.compareValues(v, piv);
        return c > 0 ? 1 : (c < 0 ? -1 : 0);
    }

    protected int pivCmp(int[] array, int a, int b, int piv, boolean eqLower) {
        Highlights.markArray(1, a);
        Highlights.markArray(2, b);
        Delays.sleep(0.125);
        int c1 = pivCmpHelper(array[a], piv);
        int c2 = pivCmpHelper(array[b], piv);
        int biasType = eqLower ? 1 : 0;
        return (c1 >= biasType && c2 < biasType ? 1 : c1 < biasType && c2 >= biasType ? -1 : 0);
    }

    protected int partition(int[] array, int a, int b, int piv, boolean eqLower) {
        for (int i = b - 1, c = 1, s, f = a; i > a; i -= c) {
            c = 1;
            s = Math.max(f - 1, a);
            boolean fChange = false;
            for (int j = s; j < i; j++) {
                if (pivCmp(array, j, j + 1, piv, eqLower) > 0) {
                    if (!fChange) f = j;
                    Writes.swap(array, j, j + 1, 0.125, fChange = true, false);
                    c = 1;
                } else c++;
            }
        }
        return binSearch(array, a, b, piv, !eqLower);
    }
    
    protected void sortHelper(int[] array, int a, int b) {
        while (b - a > 2) {
            int pivIdx = medOf3(array, a, a + (b - a) / 2, b - 1);
            int m = partition(array, a, b, array[pivIdx], false);
            if (m == a) {
                a = partition(array, a, b, array[pivIdx], true);
                continue;
            }
            if (b - m < m - a) {
                sortHelper(array, m, b);
                b = m;
            } else {
                sortHelper(array, a, m);
                a = m;
            }
        }
        if (b - a == 2) {
            if (Reads.compareIndices(array, a, a + 1, 1, true) > 0)
                Writes.swap(array, a, a + 1, 1, true, false);
        }
    }
    
    public void quickSort(int[] array, int a, int b) {
        sortHelper(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
