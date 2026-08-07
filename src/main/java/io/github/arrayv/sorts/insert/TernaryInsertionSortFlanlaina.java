package io.github.arrayv.sorts.insert;

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
 * @author gooflang
 *
 */
public class TernaryInsertionSortFlanlaina extends Sort {

    public TernaryInsertionSortFlanlaina(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ternary Insert 2");
        this.setRunAllSortsName("Flanlaina's Ternary Insertion Sort");
        this.setRunSortName("Flanlaina's Ternary Insertsort");
        this.setCategory("Insertion Sorts");
        this.setAuthors("Flanlaina, gooflang");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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

    public void insertionSort(int[] array, int a, int b, double rSleep, double wSleep, boolean aux) {
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

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        insertionSort(array, 0, sortLength, 1, 0.05, false);

    }

}
