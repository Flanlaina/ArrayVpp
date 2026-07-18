package io.github.arrayv.sorts.hybrid;

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

An improved version of Gooflang's Nitro Sort.

 */

/**
 * @author Flanlaina
 * @author gooflang
 * 
 */
public class NitroSort extends Sort {
    public NitroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Nitro");
        this.setRunAllSortsName("Nitro Sort");
        this.setRunSortName("Nitrosort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter shrink factor (input/100):", 130);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 110) return 130;
        return answer;
    }

    int incs[] = { 48, 21, 7, 3, 1 };

    public void shellSort(int[] array, int lo, int hi) {
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

    public void combSort(int[] array, int a, int b, double shrink) {
        boolean swapped = false;
        int len = b - a, gap = len;

        while ((gap > 1) || swapped) {
            if (gap > 1) gap = (int) (gap / shrink);
            swapped = false;
            for (int i = a; (gap + i) < b; i++) {
                if (gap <= Math.min(64, len * 0.03125)) {
                    gap = 0;
                    shellSort(array, a, b);
                    break;
                }
                if (Reads.compareValues(array[i], array[i + gap]) == 1) {
                    Writes.swap(array, i, i + gap, 0.75, true, false);
                    swapped = true;
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        combSort(array, 0, currentLength, bucketCount/100D);
    }
}
