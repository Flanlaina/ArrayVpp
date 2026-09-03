package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/**
 * @author Flanlaina
 * @author yuji
 *
 */
public final class MiyskiyuSort extends Sort {

    public MiyskiyuSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Miyskiyu");
        setRunAllSortsName("Miyskiyu Sort");
        setRunSortName("Miyskiyusort");
        setCategory("Hybrid Sorts");
        setAuthors("Flanlaina, yuji");
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    protected boolean compSwap(int[] array, int a, int b) {
        if (Reads.compareIndices(array, a, b, 0.25, true) == 1) {
            Writes.swap(array, a, b, 0.25, false, false);
            return true;
        }
        return false;
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
        for (int i = a; i < b; i++) {
            int j = i, g = 1;
            while (j + g < b) {
                if (compSwap(array, j, j + g) && j > a)
                    j--;
                j++;
                g++;
            }
        }
        insertSort(array, a, b, 0.5);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
