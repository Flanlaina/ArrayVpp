package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author Flanlaina
 * @author yuji
 *
 */
public class CursedWeaveSort extends Sort {

    public CursedWeaveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cursed Weave");
        this.setRunAllSortsName("Cursed Weave Sort");
        this.setRunSortName("Cursed Weave Sort");
        this.setCategory("Exchange Sorts");
        this.setAuthors("Flanlaina, yuji");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private void deakPass(int[] array, int a, int b, int depth) {
        if (a >= b) return;
        if (Reads.compareIndices(array, a, b, 0.1, true) > 0) Writes.swap(array, a, b, 0.1, true, false);
        int m = a + (b - a) / 2;
        Writes.recordDepth(depth);
        Writes.recursion();
        deakPass(array, a, m, depth+1);
        Writes.recursion();
        deakPass(array, m + 1, b, depth+1);
    }
    
    void inverseDeak(int[] array, int a, int b, int depth) {
        depth++;
        for (int i = a; i < b; i++) deakPass(array, i, b, depth);
    }
    
    protected void cursedMerge(int[] array, int a, int b, int depth) {
        if (a >= b) return;
        Writes.recordDepth(depth);
        int m = a + (b - a) / 2;
        Writes.recursion();
        cursedMerge(array, a, m, depth + 1);
        Writes.recursion();
        cursedMerge(array, m + 1, b, depth + 1);
        inverseDeak(array, a, b, depth);
    }

    public void insertSort(int[] array, int a, int b, double delay) {
        for (int i = a + 1; i < b; i++) {
            int j = i;
            int t = array[i];
            while (j > a && Reads.compareValueIndex(array, t, j - 1, delay, true) < 0) {
                Writes.write(array, j, array[j - 1], delay, true, false);
                j--;
            }
            if (j != i)Writes.write(array, j, t, delay, true, false);
        }
    }
    
    public void sort(int[] array, int a, int b) {
        cursedMerge(array, a, b - 1, 0);
        Highlights.clearAllMarks();
        insertSort(array, a, b, 0.25);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
