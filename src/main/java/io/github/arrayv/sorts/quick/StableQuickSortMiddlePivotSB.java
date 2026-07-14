package io.github.arrayv.sorts.quick;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2024-2026 Flanlaina, Sorting Algorithm Scarlet

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

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
public class StableQuickSortMiddlePivotSB extends Sort {
    public StableQuickSortMiddlePivotSB(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stable Quick (Middle Pivot, Single Buffer)");
        this.setRunAllSortsName("Stable Quick Sort (Middle Pivot, Single Buffer)");
        this.setRunSortName("Stable Quicksort (Middle Pivot, Single Buffer)");
        this.setCategory("Quick Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int partition(int[] array, int[] buf, int left, int right, int pivIdx) {
        Highlights.clearMark(2);
        int a = left, b = right;
        int piv = array[pivIdx];
        // determines which elements do not need to be moved
        for (; a < pivIdx; a++) {
            Highlights.markArray(1, a);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[a], piv) > 0) break;
        }
        for (; b > pivIdx + 1; b--) {
            Highlights.markArray(1, b - 1);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[b - 1], piv) < 0) break;
        }
        // partitions the list stably
        int j = a, k = 0;
        for (int i = a; i < pivIdx; i++) {
            Highlights.markArray(2, j);
            if (Reads.compareIndexValue(array, i, piv, 0, true) <= 0)
                Writes.write(array, j++, array[i], 0.5, false, false);
            else Writes.write(buf, k++, array[i], 0.5, false, true);
        }
        for (int i = pivIdx + 1; i < b; i++) {
            Highlights.markArray(2, j);
            if (Reads.compareIndexValue(array, i, piv, 0, true) < 0)
                Writes.write(array, j++, array[i], 0.5, false, false);
            else Writes.write(buf, k++, array[i], 0.5, false, true);
        }
        // write the pivot at its correct location
        Writes.write(array, j, piv, 0.5, true, false);
        Writes.arraycopy(buf, 0, array, j + 1, k, 0.5, true, false);
        return j;
    }

    private void sortHelper(int[] array, int[] buf, int start, int end) {
        while (end - start > 1) {
            int mid = partition(array, buf, start, end, start + (end - start) / 2);
            // use tail recursion elimination to avoid stack overflow errors
            if (end - (mid + 1) < mid - start) {
                sortHelper(array, buf, mid + 1, end);
                end = mid;
            } else {
                sortHelper(array, buf, start, mid);
                start = mid + 1;
            }
        }
    }

    public void quickSort(int[] array, int left, int right) {
        int[] buf = Writes.createExternalArray(right - left);
        sortHelper(array, buf, left, right);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);
    }
}
