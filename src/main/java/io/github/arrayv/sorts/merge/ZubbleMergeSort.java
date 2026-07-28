package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
    name = "Zubble Merge",
    category = "Merge Sorts"
)
public class ZubbleMergeSort extends Sort {
    public ZubbleMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void zubbleMerge(int[] array, int start, int mid, int end) {
        int swap = end, first = mid;
        while (swap > start) {
            int lastSwap = start;
            boolean firsts = false;
            for (int i = Math.max(first, start + 1); i < swap; i++) {
                int k = i - 1;
                while (i < swap && Reads.compareIndices(array, k, i, 0.1, true) > 0) i++;
                if (k != i - 1) {
                    Writes.swap(array, k, i - 1, 1, true, false);
                    if (!firsts) {
                        first=k;
                        firsts=true;
                    }
                    lastSwap = i;
                    Highlights.markArray(3, lastSwap - 1);
                }
            }
            swap = lastSwap;
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        if(b - a < 2) return;
        int m = a + (b - a) / 2;
        mergeSort(array, a, m);
        mergeSort(array, m, b);
        zubbleMerge(array, a, m, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        mergeSort(array, 0, sortLength);

    }
}
