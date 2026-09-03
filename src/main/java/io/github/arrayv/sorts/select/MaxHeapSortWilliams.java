package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
    name = "Max Heap (Williams' original construction)",
    runName = "Max Heapsort (Williams' original construction)",
    showcaseName = "Max Heap Sort (Williams' original construction)",
    category = "Selection Sorts"
)
public class MaxHeapSortWilliams extends Sort {
    public MaxHeapSortWilliams(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    private void siftDown(int[] array, int val, int i, int p, int n) {
        while (2 * i + 1 < n) {
            int max = val;
            int next = i, child = 2 * i + 1;
            for (int j = child; j < Math.min(child + 2, n); j++) {
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

    void siftUp(int[] array, int a, int n) {
        int t = array[a + n];
        while (n > 0) {
            int p = (n - 1) / 2;
            if (Reads.compareValues(array[a + p], t) < 0) {
                Writes.write(array, a + n, array[a + p], 1, true, false);
                n = p;
            } else break;
        }
        Writes.write(array, a + n, t, 1, true, false);
    }

    protected void heapSort(int[] array, int a, int b) {
        int n = b - a;
        // for (int i = (n - 1) / 2; i >= 0; i--)
        //     this.siftDown(array, array[a + i], i, a, n);
        for (int i = 1; i < n; i++)
            siftUp(array, a, i);
        for (int i = n - 1; i > 0; i--) {
            Highlights.markArray(2, a + i);
            int t = array[a + i];
            Writes.write(array, a + i, array[a], 1, false, false);
            this.siftDown(array, t, 0, a, i);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        heapSort(array, 0, sortLength);
    }
}
